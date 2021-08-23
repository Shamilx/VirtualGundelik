package com.shamil.virtualgundelik;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
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

        lines.add(new ListViewLine(getString(R.string.edit_text_hint1), myUser.Email, view -> {
            Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
            animation1.setDuration(2000);
            view.startAnimation(animation1);

        }));

        /*

        lines.add(new ListViewLine(getString(R.string.edit_text_hint2),"*******"));

        lines.add(new ListViewLine(getString(R.string.edit_text_hint3),myUser.FirstName));

        lines.add(new ListViewLine(getString(R.string.edit_text_hint4),myUser.LastName));

        lines.add(new ListViewLine("ID", String.valueOf(myUser.ID)));

        */

        CustomAdapter adapter = new CustomAdapter(this,lines);

        listView.setAdapter(adapter);

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

}