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

        SharedPreferences myPrefs = getSharedPreferences("MyPrefs",MODE_PRIVATE);

        if(!myPrefs.contains("User")) {
            Toast.makeText(MainActivity.this,"We are initliazing your data!",Toast.LENGTH_LONG).show();
            DocumentReference ref = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

            ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        DocumentSnapshot snapshot = task.getResult();
                        if(snapshot.exists()) {
                            SharedPreferences mPrefs = getSharedPreferences("MyPrefs",MODE_PRIVATE);
                            VirtualGundelikUser virtualGundelikUser = new VirtualGundelikUser();

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
                }
            });
        }
    }

    public void Profile_OnClick(MenuItem item) {
        if (frameLayout.findViewById(R.id.profileFragment) == null) {
            FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
            fr.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left);
            fr.replace(R.id.flFragment, new ProfileFragment());
            fr.commit();
        }

        item.setChecked(true);
    }

    public void Search_OnClick(MenuItem item) {
        if (frameLayout.findViewById(R.id.searchFragment) == null) {
            FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
            fr.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            fr.replace(R.id.flFragment, new SearchFragment());
            fr.commit();

        }

        item.setChecked(true);
    }

    public void Test_Method(DocumentSnapshot data) {
        FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
        fr.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fr.replace(R.id.flFragment,new SearchResult());
        fr.commit();
    }
}