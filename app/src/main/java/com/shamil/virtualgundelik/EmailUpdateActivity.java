package com.shamil.virtualgundelik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class EmailUpdateActivity extends AppCompatActivity {

    FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_update);

        FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.frame_layout_email_update, new UpdateEmailGetPasswordFragment());
        fr.commit();
    }

    /*
    private void init() {
        MaterialButton button = findViewById(R.id.email_update_next);
        TextInputEditText editText = findViewById(R.id.email_update_password);

        Auth = FirebaseAuth.getInstance();

        button.setOnClickListener(view -> {
            Auth.signInWithEmailAndPassword(Auth.getCurrentUser().getEmail(),editText.getText().toString()).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    Auth.getCurrentUser().updateEmail()
                }
            });
        });
    }
     */
}