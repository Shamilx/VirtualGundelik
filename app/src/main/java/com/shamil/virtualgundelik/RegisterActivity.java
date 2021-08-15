package com.shamil.virtualgundelik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private TextInputEditText PasswordRepeatEditText;
    private TextInputEditText PasswordEditText;
    private TextInputEditText EmailEditText;
    private FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void RegisterUser(String email, String password) {
        Auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((task -> {
            if (task.isSuccessful()){
                Auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(RegisterActivity.this,VerifyEmailActivity.class);
                            startActivity(intent);
                            finish();
                        } else{
                            new MaterialAlertDialogBuilder(RegisterActivity.this)
                                    .setTitle("Error")
                                    .setMessage("Something went wrong,please try again later,or contact to Developers.")
                                    .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss()).show();
                        }
                    }
                });
            }else{
                Toast.makeText(RegisterActivity.this, "No", Toast.LENGTH_LONG).show();
            }
        }));
    }
    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
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

        if (TextUtils.isEmpty(PasswordRepeatEditText.getText().toString())) {
            PasswordRepeatEditText.setError("Please fill that field!");
            thereIsError = true;
        }

        return thereIsError;
    }
    private boolean checkPass() {
        String pass = PasswordEditText.getText().toString();
        String repeatPass = PasswordRepeatEditText.getText().toString();

        return pass.equals(repeatPass);

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

                if (checkForEmptyInputAndWarnUser()) {
                    Toast.makeText(RegisterActivity.this, "Please fill the inputs correctly!", Toast.LENGTH_LONG).show();
                } else if (!validate(txt_email)) {
                    EmailEditText.setError("Please use correct Email!");
                } else if (txt_password.length() < 6) {
                    PasswordEditText.setError("Please use password with at least 6 symbols!");
                } else if (txt_password_repeat.length() < 6) {
                    PasswordRepeatEditText.setError("Please use password with at least 6 symbols!");
                } else if (!checkPass()) {
                    PasswordRepeatEditText.setError("Passwords are not same!");
                } else {
                    Auth.fetchSignInMethodsForEmail(txt_email).addOnCompleteListener(task -> {
                        boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                        if (isNewUser) {
                            RegisterUser(txt_email, txt_password);
                        } else {
                            new MaterialAlertDialogBuilder(RegisterActivity.this)
                                    .setTitle("Error")
                                    .setMessage("The Email that you used to register is already registered, please fix it and try again.")
                                    .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss()).show();
                            EmailEditText.setError("This Email already registired!");
                        }

                    });

                }
            }
        });
    }
}