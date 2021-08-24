package com.shamil.virtualgundelik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import Models.VirtualGundelikUser;

public class ChangeNameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);

        init();
    }

    private void init() {
        TextInputEditText editText1 = findViewById(R.id.changeNameEdiText1);
        TextInputEditText editText2 = findViewById(R.id.changeNameEdiText2);
        MaterialButton button = findViewById(R.id.SaveMaterialButton);
        ImageButton button2 = findViewById(R.id.backProfileSettingsButton);

        VirtualGundelikUser virtualGundelikUser = getUser();

        editText1.setText(virtualGundelikUser.FirstName);
        editText2.setText(virtualGundelikUser.LastName);

        button.setOnClickListener(view -> {
            if(TextUtils.isEmpty(editText1.getText().toString())) {
                editText1.setError(getString(R.string.java6));
                return;
            }

            if(TextUtils.isEmpty(editText2.getText().toString())) {
                editText2.setError(getString(R.string.java6));
                return;
            }

            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            DocumentReference documentReference = firestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

            documentReference.update("firstName",editText1.getText().toString(),"lastName",editText2.getText().toString());
            // TODO: Update Settings
            finish();
        });

        button2.setOnClickListener(view -> {
            finish();
        });
    }

    private VirtualGundelikUser getUser() {
        SharedPreferences mPrefs = getSharedPreferences("MyPrefs",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("User","");
        return gson.fromJson(json,VirtualGundelikUser.class);
    }
}