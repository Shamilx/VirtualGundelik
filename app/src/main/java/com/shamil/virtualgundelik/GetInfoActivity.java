package com.shamil.virtualgundelik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.QueryProto;
import com.google.type.DateTime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GetInfoActivity extends AppCompatActivity {

    Button button;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_info);

        init();
    }

    private void init() {
        EditText firstName = findViewById(R.id.registerUserNameEditText);
        EditText lastName = findViewById(R.id.registerUserLastnameEditText);
        DatePicker time = findViewById(R.id.registerDatePicker);
        MaterialButton button = findViewById(R.id.SaveMaterialButton);
        progressBar = findViewById(R.id.progress_circular);
        time.setMaxDate(new Date().getTime());

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(ProgressBar.VISIBLE);
                if (TextUtils.isEmpty(firstName.getText().toString())) {
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    firstName.setError(getString(R.string.java6));
                    return;
                }

                if (TextUtils.isEmpty(lastName.getText().toString())) {
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    lastName.setError(getString(R.string.java6));
                    return;
                }

                Task<QuerySnapshot> dr = firestore.collection("Users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentReference documentReference = firestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("id", task.getResult().size() + 100000);
                                    map.put("firstName", firstName.getText().toString());
                                    map.put("lastName", lastName.getText().toString());
                                    map.put("birthDate", toStringTime(time.getDayOfMonth(), time.getMonth(), time.getYear()));

                                    documentReference.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Intent intent = new Intent(GetInfoActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                                                new MaterialAlertDialogBuilder(GetInfoActivity.this)
                                                        .setTitle(getString(R.string.java4))
                                                        .setMessage(getString(R.string.java5))
                                                        .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss()).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });
            }
        });
    }

    private String toStringTime(int day, int month, int year) {
        return new String(day + ":" + month + 1 + ":" + year);
    }
}