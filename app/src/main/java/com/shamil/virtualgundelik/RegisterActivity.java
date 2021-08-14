package com.shamil.virtualgundelik;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText EmailEditText;
    private TextInputEditText PasswordEditText;
    private TextInputEditText PasswordRepeatEditText;

    private FirebaseAuth Auth;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
    private void RegisterUser(String email,String password) {
        Auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this, this::onComplete);
    }
    private boolean checkPass(int passLength) {
        String pass = PasswordEditText.getText().toString();
        String repeatPass = PasswordRepeatEditText.getText().toString();

        if(pass.equals(repeatPass)) // gelirem
            return true;
    }
    private void init() {
        EmailEditText = findViewById(R.id.registerEmailInput);
        PasswordEditText = findViewById(R.id.registerPasswordInput);
        PasswordRepeatEditText = findViewById(R.id.registerPasswordInputRepeat);
        MaterialButton registerMatieralButton = findViewById(R.id.materialButton);
        Auth = FirebaseAuth.getInstance();

        registerMatieralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = EmailEditText.getText().toString();
                String txt_password = PasswordEditText.getText().toString();
                String txt_password_repeat = PasswordRepeatEditText.getText().toString();

                if(checkForEmptyInputAndWarnUser()) { Toast.makeText(RegisterActivity.this,"Please fill the inputs correctly!",Toast.LENGTH_LONG).show(); }
                else if (txt_password.length() < 6){  PasswordEditText.setError("Please use password with at least 6 symbols!");  }
                else if (txt_password_repeat.length() < 6){ PasswordRepeatEditText.setError("Please use password with at least 6 symbols!"); }
                else if (!checkPass(txt_password.length())){ PasswordRepeatEditText.setError("Passwords are not same!");}
                else {
                    RegisterUser(txt_email,txt_password);
                }
            }
        });
    }


    private boolean checkForEmptyInputAndWarnUser() {
        boolean thereIsError = false;

        if(TextUtils.isEmpty(EmailEditText.getText().toString())) {
            EmailEditText.setError("Please fill that field!");
            thereIsError = true;
        }

        if(TextUtils.isEmpty(PasswordEditText.getText().toString())) {
            PasswordEditText.setError("Please fill that field!");
            thereIsError = true;
        }

        if(TextUtils.isEmpty(PasswordRepeatEditText.getText().toString())) {
            PasswordRepeatEditText.setError("Please fill that field!");
            thereIsError = true;
        }

        return thereIsError;
    }

    private void onComplete(Task<AuthResult> task) {
        Toast.makeText(RegisterActivity.this, "Registered!", Toast.LENGTH_LONG).show();
    }
}