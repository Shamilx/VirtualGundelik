package com.shamil.virtualgundelik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Models.VirtualGundelikUser;

public class LogInActivity extends AppCompatActivity {
    private TextInputEditText EmailEditText;
    private TextInputEditText PasswordEditText;
    private MaterialButton MaterialButton;
    private TextView openRegister;
    private ProgressBar progressBar;
    private FirebaseAuth Auth;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(LogInActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        init();
    }

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    private void init() {
        EmailEditText = findViewById(R.id.LoginEmailEditText);
        PasswordEditText = findViewById(R.id.LogInPasswordEditText);
        MaterialButton = findViewById(R.id.LogInMaterialButton);
        progressBar = findViewById(R.id.progress_circular);
        openRegister = findViewById(R.id.OpenRegister);
        Auth = FirebaseAuth.getInstance();


        MaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = EmailEditText.getText().toString();
                String txt_password = PasswordEditText.getText().toString();

                progressBar.setVisibility(ProgressBar.VISIBLE);
                MaterialButton.setVisibility(com.google.android.material.button.MaterialButton.INVISIBLE);
                openRegister.setVisibility(TextView.INVISIBLE);

                if (checkForEmptyInputAndWarnUser()) {
                    openRegister.setVisibility(TextView.VISIBLE);
                    MaterialButton.setVisibility(com.google.android.material.button.MaterialButton.VISIBLE);
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    Toast.makeText(LogInActivity.this, getString(R.string.java1), Toast.LENGTH_LONG).show();
                } else if (txt_password.length() < 6) {
                    openRegister.setVisibility(TextView.VISIBLE);
                    MaterialButton.setVisibility(com.google.android.material.button.MaterialButton.VISIBLE);
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    PasswordEditText.setError(getString(R.string.java2));
                } else if (!validate(txt_email)) {
                    openRegister.setVisibility(TextView.VISIBLE);
                    MaterialButton.setVisibility(com.google.android.material.button.MaterialButton.VISIBLE);
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    EmailEditText.setError(getString(R.string.java3));
                }else {
                    Auth.signInWithEmailAndPassword(txt_email,txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                if(!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                                    Intent intent = new Intent(LogInActivity.this,VerifyEmailActivity.class);
                                    startActivity(intent);
                                    finish();
                                    return;
                                }

                                FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                                DocumentReference docIdRef = rootRef.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                Intent intent = new Intent(LogInActivity.this,MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Intent intent = new Intent(LogInActivity.this,GetInfoActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        } else {
                                            openRegister.setVisibility(TextView.VISIBLE);
                                            MaterialButton.setVisibility(com.google.android.material.button.MaterialButton.VISIBLE);
                                            progressBar.setVisibility(ProgressBar.INVISIBLE);
                                            new MaterialAlertDialogBuilder(LogInActivity.this)
                                                    .setTitle(getString(R.string.java4))
                                                    .setMessage(getString(R.string.java5))
                                                    .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss()).show();
                                        }
                                    }
                                });
                            } else {
                                openRegister.setVisibility(TextView.VISIBLE);
                                MaterialButton.setVisibility(com.google.android.material.button.MaterialButton.VISIBLE);
                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                                PasswordEditText.setError(getString(R.string.java14));
                            }
                        }
                    });
                }
            }
        });
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

        return thereIsError;
    }
    public void Register_OnClick(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }


}