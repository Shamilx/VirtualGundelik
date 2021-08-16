package com.shamil.virtualgundelik;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GetInfoActivity extends AppCompatActivity {

    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_info);

        init();
    }

    private void init()
    {
        EditText name = findViewById(R.id.registerUserNameEditText);
        EditText lastname = findViewById(R.id.registerUserLastnameEditText);
        EditText phone = findViewById(R.id.registerUserPhoneEditText);
        DatePicker time = findViewById(R.id.registerDatePicker);
        MaterialButton button = findViewById(R.id.SaveMaterialButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText() == null && lastname.getText() == null && phone.getText() == null){
                    name.setError("Please fill here");
                    lastname.setError("Please fill here");
                    phone.setError("Please fill here");
                }else if (name.getText() == null){
                    name.setError("Please enter your name");
                }else if (lastname.getText() == null){
                    name.setError("Please enter your lastname");
                }else if (phone.getText() == null){
                    name.setError("Please enter your phone number");
                }


                Toast.makeText(GetInfoActivity.this, "Yes", Toast.LENGTH_LONG).show();
            }
        });
    }
}