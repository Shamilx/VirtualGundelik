package com.shamil.virtualgundelik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private TextInputEditText PasswordRepeatEditText;
    private TextInputEditText PasswordEditText;
    private TextInputEditText EmailEditText;
    private FirebaseAuth Auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void RegisterUser(String email, String password) {
        Auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    // Auth.signInWithEmailAndPassword(email, password);
                    user = Auth.getCurrentUser();
                }
            }
        });

        if(user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });
        } else {
            Toast.makeText(RegisterActivity.this,"User is null!",Toast.LENGTH_LONG);
        }
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
    private boolean checkPass(int passLength) {
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
                } else if (!checkPass(txt_password.length())) {
                    PasswordRepeatEditText.setError("Passwords are not same!");
                } else {
                    Auth.fetchSignInMethodsForEmail(txt_email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                            boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                            if (isNewUser) {
                                RegisterUser(txt_email, txt_password);
                            } else {
                                new MaterialAlertDialogBuilder(RegisterActivity.this)
                                        .setTitle("Error")
                                        .setMessage("The Email that you used to register is already registered, please fix it and try again.")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        }).show();
                                EmailEditText.setError("This Email already registired!");
                            }

                        }
                    });

                }
            }
        });
    }
}