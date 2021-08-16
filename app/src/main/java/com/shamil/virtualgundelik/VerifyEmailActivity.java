package com.shamil.virtualgundelik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;

public class VerifyEmailActivity extends AppCompatActivity {
    private boolean doubleBackToExitPressedOnce = false;
    private FirebaseAuth Auth;
    private Thread thread;
    private Intent intent;

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
        Button button = findViewById(R.id.VerifiedButton);

        Auth = FirebaseAuth.getInstance();
        intent = new Intent();

        Auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    StartThread();
                } else {
                    new MaterialAlertDialogBuilder(VerifyEmailActivity.this)
                            .setTitle("Error")
                            .setMessage("Something went wrong,please try again later,or contact to Developers.")
                            .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss()).show();
                }
            }
        });
    }

    private void StartThread() {
        new Thread(new Runnable() {
            boolean threadRunning = true;
            int count = 0;

            @Override
            public void run() {
                if(threadRunning) {
                    if (Auth.getCurrentUser().isEmailVerified()) {

                        threadRunning = false;
                        StopThread();
                    } else {
                        count++;
                        if(count == 30) {
                            new MaterialAlertDialogBuilder(VerifyEmailActivity.this)
                                    .setTitle("Error")
                                    .setMessage("Something went wrong,please try again later,or contact to Developers.")
                                    .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss()).show();
                        }
                    }
            public void onClick(View view) {
                Auth.getCurrentUser().reload();

                if(Auth.getCurrentUser().isEmailVerified()) {
                    new MaterialAlertDialogBuilder(VerifyEmailActivity.this)
                            .setTitle("YES WE DID IT")
                            .setMessage("YESSSS.")
                            .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss()).show();

                    Intent intent = new Intent(VerifyEmailActivity.this, GetInfoActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    new MaterialAlertDialogBuilder(VerifyEmailActivity.this)
                            .setTitle("Error")
                            .setMessage("Something went wrong,please try again later,or contact to Developers.")
                            .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss()).show();
                }
            }
        }).start();
    }

    private void StopThread() {
        thread.stop();
    }
}