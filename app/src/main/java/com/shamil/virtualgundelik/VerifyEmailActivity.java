package com.shamil.virtualgundelik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;

public class VerifyEmailActivity extends AppCompatActivity {
    private boolean doubleBackToExitPressedOnce = false;
    private FirebaseAuth Auth;
    private MaterialButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        init();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    private void init() {
        Auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.VerifyButton);

        Auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    button.setClickable(true);
                } else {
                    new MaterialAlertDialogBuilder(VerifyEmailActivity.this)
                            .setTitle("Error")
                            .setMessage("Something went wrong,please try again later,or contact to Developers.")
                            .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss()).show();
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(CheckIfEmailVerified()) {
                    startActivity(new Intent(VerifyEmailActivity.this,GetInfoActivity.class));
                    finish();
                } else {
                    new MaterialAlertDialogBuilder(VerifyEmailActivity.this)
                            .setTitle("You are not Verified!")
                            .setMessage("We have detected that you are not verified try again!")
                            .setPositiveButton("Try Again", (dialogInterface, i) ->{  dialogInterface.dismiss(); button.performClick();}).show();
                }
            }
        });
    }

    public boolean CheckIfEmailVerified() {
        Auth.getCurrentUser().reload();

        if(Auth.getCurrentUser().isEmailVerified()) {
            return true;
        } else {
            return false;
        }

    }
}