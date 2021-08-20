package com.shamil.virtualgundelik;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileSettingsActivity extends AppCompatActivity {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private int []counts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        init();
    }

    private void init(){

        counts = new int[4];

        // EditTexts inisalizasya.
        EditText emailEditTxt = findViewById(R.id.EmailEditText);
        EditText passwordEditTxt = findViewById(R.id.PasswordEditText);
        EditText firstNameEditTxt = findViewById(R.id.NameEditText);
        EditText lastNameEditTxt = findViewById(R.id.LastNameEditText);

        // Buttons

        Button change1 = findViewById(R.id.changeAndCanacelButton1);
        Button change2 = findViewById(R.id.changeAndCanacelButton2);
        Button change3 = findViewById(R.id.changeAndCanacelButton3);
        Button change4 = findViewById(R.id.changeAndCanacelButton4);

        Button check1 = findViewById(R.id.checkButton1);
        Button check2 = findViewById(R.id.checkButton2);
        Button check3 = findViewById(R.id.checkButton3);
        Button check4 = findViewById(R.id.checkButton4);

        Button backProfile = findViewById(R.id.backToProfileButton);

        FirebaseAuth auth =  FirebaseAuth.getInstance();
        DocumentReference ref = FirebaseFirestore.getInstance().collection("Users").document(auth.getCurrentUser().getUid());

        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot snap = task.getResult();
                    if(snap.exists()) {
                        emailEditTxt.setText(auth.getCurrentUser().getEmail());
                        firstNameEditTxt.setText(snap.get("firstName").toString());
                        lastNameEditTxt.setText(snap.get("lastName").toString());
                    }
                }
            }
        });


        change1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if (counts[0] == 0){
                    check1.setVisibility(Button.VISIBLE);
                    change1.setBackground(getResources().getDrawable(R.drawable.ic_clear_black_24dp));
                    change1.setBackgroundTintList(ContextCompat.getColorStateList(ProfileSettingsActivity.this, R.color.red));
                    emailEditTxt.setEnabled(true);
                    change2.setEnabled(false);
                    change3.setEnabled(false);
                    change4.setEnabled(false);
                    backProfile.setEnabled(false);
                    counts[0]++;
                } else if (counts[0] == 1){
                    check1.setVisibility(Button.INVISIBLE);
                    change1.setBackground(getResources().getDrawable(R.drawable.ic_change_circle_black_24dp));
                    change1.setBackgroundTintList(ContextCompat.getColorStateList(ProfileSettingsActivity.this, R.color.blue));
                    emailEditTxt.setEnabled(false);
                    counts[0] = 0;
                }

            }
        });

        check1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailEditTxt.getText().toString() == auth.getCurrentUser().getEmail().toString()) {
                    emailEditTxt.setEnabled(false);
                    check1.setVisibility(Button.INVISIBLE);
                    change1.setBackground(getResources().getDrawable(R.drawable.ic_change_circle_black_24dp));
                    change1.setBackgroundTintList(ContextCompat.getColorStateList(ProfileSettingsActivity.this, R.color.blue));
                    emailEditTxt.setEnabled(false);
                    return;
                }else if (validate(change1.getText().toString())){
                        new MaterialAlertDialogBuilder(ProfileSettingsActivity.this)
                                .setTitle(getString(R.string.java4))
                                .setMessage(getString(R.string.java15))
                                .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss()).show();
                    } else {
                        auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    emailEditTxt.setEnabled(false);
                                    check1.setVisibility(Button.INVISIBLE);
                                    change1.setBackground(getResources().getDrawable(R.drawable.ic_change_circle_black_24dp));
                                    change1.setBackgroundTintList(ContextCompat.getColorStateList(ProfileSettingsActivity.this, R.color.blue));
                                    emailEditTxt.setEnabled(false);
                                    counts[0] = 0;
                                } else {
                                    new MaterialAlertDialogBuilder(ProfileSettingsActivity.this)
                                            .setTitle(getString(R.string.java4))
                                            .setMessage(getString(R.string.java16))
                                            .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss()).show();
                                }
                            }
                        });
                    }
                }
        });
    }


    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

}