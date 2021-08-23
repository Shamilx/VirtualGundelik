package com.shamil.virtualgundelik;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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
        ListView listView = findViewById(R.id.profileSettingsList);

        VirtualGundelikUser myUser = GetUserInfo();
        List<ListViewLine> lines = new ArrayList<>();

        lines.add(new ListViewLine(getString(R.string.edit_text_hint1),myUser.Email));
        lines.add(new ListViewLine(getString(R.string.edit_text_hint2),"*******"));
        lines.add(new ListViewLine(getString(R.string.edit_text_hint3),myUser.FirstName));
        lines.add(new ListViewLine(getString(R.string.edit_text_hint4),myUser.LastName));
        lines.add(new ListViewLine("ID", String.valueOf(myUser.ID)));

        CustomAdapter adapter = new CustomAdapter(this,lines);

        listView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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

}