package com.shamil.virtualgundelik;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class ChangeBirthdayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_birthday);

        init();
    }

    private void init() {
        DatePicker datePicker = findViewById(R.id.changeBirthdayDatePicker);
        MaterialButton button = findViewById(R.id.changeBirthdaySave);
        ImageButton button2 = findViewById(R.id.changeBirthdayGoBack);

        datePicker.setMaxDate(new Date().getTime());

        button.setOnClickListener(view -> {
            String birthDate = toStringTime(datePicker.getDayOfMonth(), datePicker.getMonth(), datePicker.getYear());
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            DocumentReference documentReference = firestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            documentReference.update("birthDate",birthDate);
            // TODO: Update Settings
            finish();
        });

        button2.setOnClickListener(view -> {
            finish();
        });
    }

    private String toStringTime(int day, int month, int year) {
        return new String(day + ":" + month + ":" + year);
    }

}