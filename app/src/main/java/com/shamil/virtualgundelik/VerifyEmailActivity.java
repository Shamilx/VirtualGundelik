package com.shamil.virtualgundelik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class VerifyEmailActivity extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    FirebaseAuth Auth = FirebaseAuth.getInstance();





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
        TextView textView = findViewById(R.id.textView3);
        textView.setText(Auth.getCurrentUser().getEmail().toString());
        Auth = FirebaseAuth.getInstance();

        Auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                        @Override
                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                            if(Auth.getCurrentUser().isEmailVerified()) {
                                new MaterialAlertDialogBuilder(VerifyEmailActivity.this)
                                        .setTitle("Done")
                                        .setMessage("You have registered")
                                        .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss()).show();
                            }
                        }
                    });
                } else{
                    new MaterialAlertDialogBuilder(VerifyEmailActivity.this)
                            .setTitle("Error")
                            .setMessage("Something went wrong,please try again later,or contact to Developers.")
                            .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss()).show();
                }
            }
        });
    }
}