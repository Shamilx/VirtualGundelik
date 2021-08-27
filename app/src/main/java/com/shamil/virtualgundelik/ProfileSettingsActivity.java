package com.shamil.virtualgundelik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Verify;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Models.CustomAdapter;
import Models.ListViewLine;
import Models.VirtualGundelikUser;

public class ProfileSettingsActivity extends AppCompatActivity {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        init();
    }

    private void init() {
        ImageButton button = findViewById(R.id.backProfileSettingsButton);
        UpdateListView();
        button.setOnClickListener(view -> finish());
    }

    private VirtualGundelikUser GetUserInfo() {
        SharedPreferences mPrefs = getSharedPreferences("MyPrefs",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("User","");
        return gson.fromJson(json,VirtualGundelikUser.class);
    }

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    private void UpdateListView() {
        ListView listView = findViewById(R.id.profileSettingsList);
        VirtualGundelikUser myUser = GetUserInfo();
        List<ListViewLine> lines = new ArrayList<>();

        lines.add(new ListViewLine(getString(R.string.edit_text_hint1), myUser.Email, view -> {

        }));


        lines.add(new ListViewLine(getString(R.string.edit_text_hint2), "*******", view -> {

        }));

        lines.add(new ListViewLine(getString(R.string.edit_text_hint3), myUser.FirstName, view -> {
            Intent intent = new Intent(ProfileSettingsActivity.this,ChangeNameActivity.class);
            startActivity(intent);
        }));

        lines.add(new ListViewLine(getString(R.string.edit_text_hint4), myUser.LastName, view -> {
            Intent intent = new Intent(ProfileSettingsActivity.this,ChangeNameActivity.class);
            startActivity(intent);
        }));

        lines.add(new ListViewLine(getString(R.string.edit_text_hint5), myUser.BirthDate, view -> {
            Intent intent = new Intent(ProfileSettingsActivity.this,ChangeBirthdayActivity.class);
            startActivity(intent);
        }));

        CustomAdapter adapter = new CustomAdapter(this,lines);

        listView.setAdapter(adapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        UpdatePrefs();
        UpdateListView();
    }

    public void UpdatePrefs() {
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
    }
}