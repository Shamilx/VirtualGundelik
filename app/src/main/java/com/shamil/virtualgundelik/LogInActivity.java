package com.shamil.virtualgundelik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {
    private TextInputEditText EmailEditText;
    private TextInputEditText PasswordEditText;
    private MaterialButton MaterialButton;

    private FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        EmailEditText = findViewById(R.id.LoginEmailEditText);
        PasswordEditText = findViewById(R.id.LogInPasswordEditText);
        MaterialButton = findViewById(R.id.LogInMaterialButton);

        Auth = FirebaseAuth.getInstance();

        MaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = EmailEditText.getText().toString();
                String txt_password = PasswordEditText.getText().toString();

                if (checkForEmptyInputAndWarnUser()) {
                    Toast.makeText(LogInActivity.this, "Please fill the inputs correctly!", Toast.LENGTH_LONG).show();
                } else if (txt_password.length() < 6) {
                    PasswordEditText.setError("Please use password with at least 6 symbols!");
                } else {
                    Auth.signInWithEmailAndPassword(txt_email,txt_password);
                    Toast.makeText(LogInActivity.this, "Log in succses!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean checkForEmptyInputAndWarnUser() {
        boolean thereIsError = false;

        if (TextUtils.isEmpty(EmailEditText.getText().toString())) {
            EmailEditText.setError("Please fill that field!");
            thereIsError = true;
        }

        if (TextUtils.isEmpty(PasswordEditText.getText().toString())) {
            PasswordEditText.setError("Please fill that field!");
            thereIsError = true;
        }

        return thereIsError;
    }

    public void Register_OnClick(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}