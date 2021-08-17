package com.shamil.virtualgundelik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private TextInputEditText PasswordRepeatEditText;
    private TextInputEditText PasswordEditText;
    private TextInputEditText EmailEditText;
    private MaterialButton registerMatieralButton;
    private FirebaseAuth Auth;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterActivity.this,LogInActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void RegisterUser(String email, String password) {
        Auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((task -> {
            if (task.isSuccessful()){
                TextInputLayout textInputLayout1 = findViewById(R.id.outlinedTextField);
                TextInputLayout textInputLayout2 = findViewById(R.id.outlinedTextField2);
                TextInputLayout textInputLayout3 = findViewById(R.id.outlinedTextField3);
                ProgressBar progressBar = findViewById(R.id.progress_circular);

                textInputLayout1.setVisibility(TextInputLayout.INVISIBLE);
                textInputLayout2.setVisibility(TextInputLayout.INVISIBLE);
                textInputLayout3.setVisibility(TextInputLayout.INVISIBLE);
                registerMatieralButton.setVisibility(MaterialButton.INVISIBLE);
                progressBar.setVisibility(ProgressBar.VISIBLE);

                startActivity(new Intent(RegisterActivity.this,VerifyEmailActivity.class));
                finish();
            } else {
                new MaterialAlertDialogBuilder(RegisterActivity.this)
                        .setTitle(getString(R.string.java4))
                        .setMessage(getString(R.string.java5))
                        .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss()).show();
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
            EmailEditText.setError(getString(R.string.java6));
            thereIsError = true;
        }

        if (TextUtils.isEmpty(PasswordEditText.getText().toString())) {
            PasswordEditText.setError(getString(R.string.java6));
            thereIsError = true;
        }

        if (TextUtils.isEmpty(PasswordRepeatEditText.getText().toString())) {
            PasswordRepeatEditText.setError(getString(R.string.java6));
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
        registerMatieralButton = findViewById(R.id.materialButton);
        Auth = FirebaseAuth.getInstance();

        registerMatieralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = EmailEditText.getText().toString();
                String txt_password = PasswordEditText.getText().toString();
                String txt_password_repeat = PasswordRepeatEditText.getText().toString();

                if (checkForEmptyInputAndWarnUser()) {
                    Toast.makeText(RegisterActivity.this, getString(R.string.java6), Toast.LENGTH_LONG).show();
                } else if (!validate(txt_email)) {
                    EmailEditText.setError(getString(R.string.java3));
                } else if (txt_password.length() < 6) {
                    PasswordEditText.setError(getString(R.string.java2));
                } else if (txt_password_repeat.length() < 6) {
                    PasswordRepeatEditText.setError(getString(R.string.java2));
                } else if (!checkPass()) {
                    PasswordRepeatEditText.setError(getString(R.string.java7));
                } else {
                    Auth.fetchSignInMethodsForEmail(txt_email).addOnCompleteListener(task -> {
                        boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                        if (isNewUser) {
                            RegisterUser(txt_email, txt_password);
                        } else {
                            new MaterialAlertDialogBuilder(RegisterActivity.this)
                                    .setTitle(getString(R.string.java4))
                                    .setMessage(getString(R.string.java8))
                                    .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss()).show();

                            EmailEditText.setError(getString(R.string.java9));
                        }

                    });
                }
            }
        });
    }
}