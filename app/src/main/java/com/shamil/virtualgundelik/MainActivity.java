package com.shamil.virtualgundelik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import Models.VirtualGundelikUser;

public class MainActivity extends AppCompatActivity {

    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = findViewById(R.id.flFragment);

        DocumentReference ref = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    SharedPreferences mPrefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    VirtualGundelikUser virtualGundelikUser = new VirtualGundelikUser();
                    FirebaseAuth.getInstance().getCurrentUser().reload();

                    virtualGundelikUser.Email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    virtualGundelikUser.FirstName = snapshot.get("firstName").toString();
                    virtualGundelikUser.LastName = snapshot.get("lastName").toString();
                    virtualGundelikUser.BirthDate = snapshot.get("birthDate").toString();
                    virtualGundelikUser.ID = Integer.parseInt(snapshot.get("id").toString());

                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    prefsEditor.clear();
                    Gson gson = new Gson();
                    String json = gson.toJson(virtualGundelikUser);
                    prefsEditor.putString("User", json);
                    prefsEditor.apply();
                }
            }
        });

        FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.flFragment, new GroupsFragment());
        fr.commit();
    }

    public void Profile_OnClick(MenuItem item) {
        if (frameLayout.findViewById(R.id.profileFragment) == null) {
            FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
            fr.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fr.replace(R.id.flFragment, new ProfileFragment());
            fr.commit();
        }

        item.setChecked(true);
    }

    public void Search_OnClick(MenuItem item) {
        if (frameLayout.findViewById(R.id.searchFragment) == null) {
            FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
            fr.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fr.replace(R.id.flFragment, new SearchFragment());
            fr.commit();
        }

        item.setChecked(true);
    }

    public void Test_Method(DocumentSnapshot data) {
        FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
        fr.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fr.replace(R.id.flFragment, new SearchResult(data));
        fr.commit();
    }


    public void Groups_OnClick(MenuItem item) {
        if(frameLayout.findViewById(R.id.groupFragment) == null) {
            FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
            fr.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fr.replace(R.id.flFragment, new GroupsFragment());
            fr.commit();
        }

        item.setChecked(true);
    }
}