package com.shamil.virtualgundelik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.DateTime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class GetInfoActivity extends AppCompatActivity {

    Button button;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_info);

        init();
    }

    private void init()
    {
        EditText firstName = findViewById(R.id.registerUserNameEditText);
        EditText lastName = findViewById(R.id.registerUserLastnameEditText);
        DatePicker time = findViewById(R.id.registerDatePicker);
        MaterialButton button = findViewById(R.id.SaveMaterialButton);
        Calendar cal = Calendar.getInstance();
        firestore = FirebaseFirestore.getInstance();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(firstName.getText().toString())) {
                    firstName.setError("Fill here!");
                    return;
                }

                if(TextUtils.isEmpty(lastName.getText().toString())) {
                    lastName.setError("Fill here!");
                    return;
                }

                DocumentReference documentReference = firestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

                Map<String,Object> map = new HashMap<String,Object>();

                map.put("firstName",firstName.getText().toString());
                map.put("lastName",lastName.getText().toString());
                map.put("birthDate",time.getYear() + time.getMonth() + time.getDayOfMonth());

                documentReference.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(GetInfoActivity.this,"WORKS",Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(GetInfoActivity.this,"No WORKS",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}