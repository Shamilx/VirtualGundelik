package com.shamil.virtualgundelik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText EmailEditText;
    private TextInputEditText PasswordEditText;
    private TextInputEditText PasswordRepeatEditText;
    private MaterialButton MaterialButton;

    private FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
    }

    private void RegisterUser(String email,String password) {
        Auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this,"Registered!",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RegisterActivity.this,"Failed!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void init() {
        EmailEditText = findViewById(R.id.registerEmailInput);
        PasswordEditText = findViewById(R.id.registerPasswordInput);
        PasswordRepeatEditText = findViewById(R.id.registerPasswordInputRepeat);
        MaterialButton = findViewById(R.id.materialButton);
        Auth = FirebaseAuth.getInstance();

        MaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = EmailEditText.getText().toString();
                String txt_password = PasswordEditText.getText().toString();
                String txt_password_repeat = PasswordRepeatEditText.getText().toString();

                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_password_repeat)) {
                    Toast.makeText(RegisterActivity.this,"Please fill the inputs correctly!",Toast.LENGTH_LONG).show();
                } else {
                    RegisterUser(txt_email,txt_password);
                }

            }
        });
    }
}