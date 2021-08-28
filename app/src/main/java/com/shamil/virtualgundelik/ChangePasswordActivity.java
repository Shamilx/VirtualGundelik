package com.shamil.virtualgundelik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {
    TextInputEditText oldPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        init();
    }

    private void init() {
        ImageButton goBack = findViewById(R.id.ChangePasswordGoBack);

        oldPassword = findViewById(R.id.ChangePasswordOldPass);
        TextInputEditText newPassword1 = findViewById(R.id.ChangePAsswordNewPass1);
        TextInputEditText newPassword2 = findViewById(R.id.ChangePAsswordNewPass2);
        MaterialButton saveButton = findViewById(R.id.CHangePasswordSave);

        saveButton.setOnClickListener(view -> {
            if(oldPassword.getText().toString().isEmpty()) {
                oldPassword.setError(getString(R.string.java6));
                return;
            }

            if(newPassword1.getText().toString().isEmpty()) {
                newPassword1.setError(getString(R.string.java6));
                return;
            }

            if(newPassword2.getText().toString().isEmpty()) {
                newPassword2.setError(getString(R.string.not_same_pass));
                return;
            }

            if(!checkIfSame(newPassword1,newPassword2)) {
                newPassword2.setError(getString(R.string.not_same_pass));
                return;
            }

            if(checkIfSame(oldPassword,newPassword1)) {
                newPassword1.setError(getString(R.string.password_cant_be_same));
            }

            UpdatePassword(oldPassword.getText().toString(),newPassword1.getText().toString());

        });

        goBack.setOnClickListener(view -> finish());
    }

    private void UpdatePassword(String oldPass, String newPass) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPass);

        user.reauthenticate(credential).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                user.updatePassword(newPass).addOnCompleteListener(task1 -> {
                   if(task1.isSuccessful()) {
                       Toast.makeText(ChangePasswordActivity.this,getString(R.string.change_saved),Toast.LENGTH_LONG).show();
                        finish();
                   } else {
                       Toast.makeText(ChangePasswordActivity.this, getString(R.string.failed),Toast.LENGTH_LONG).show();
                   }
                });
            } else {
                oldPassword.setError(getString(R.string.wrong_pass));
            }
        });
    }

    private boolean checkIfSame(TextInputEditText newPassword1, TextInputEditText newPassword2) {
        String pass1 = newPassword1.getText().toString();
        String pass2 = newPassword2.getText().toString();

        return pass1.equals(pass2);
    }
}