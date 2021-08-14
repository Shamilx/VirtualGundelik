package com.shamil.virtualgundelik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText EmailEditText;
    private TextInputEditText PasswordEditText;
    private TextInputEditText PasswordRepeatEditText;
    private MaterialButton MaterialButton;

    private FirebaseAuth Auth;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
    }



    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    private void RegisterUser(String email,String password) {
        Auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                //if (!validate(EmailEditText.getText().toString())){ Toast.makeText(RegisterActivity.this, "No Correct Email Address!", Toast.LENGTH_LONG).show(); return; }
                if (PasswordEditText.getText().toString().length() < 6){ Toast.makeText(RegisterActivity.this, "No Correct Password1!", Toast.LENGTH_LONG).show(); return; }
                else if (PasswordRepeatEditText.getText().toString().length() < 6){ Toast.makeText(RegisterActivity.this, "No Correct Password2!", Toast.LENGTH_LONG).show();}
                else if (PasswordRepeatEditText.getText().toString().length() != PasswordEditText.getText().toString().length()){ Toast.makeText(RegisterActivity.this, "Password repetition is not correct", Toast.LENGTH_LONG).show();}
                else if (!checkPass(PasswordEditText.getText().length())){ Toast.makeText(RegisterActivity.this, "Password repetition is not correct", Toast.LENGTH_LONG).show();}
                else {
                    Toast.makeText(RegisterActivity.this,"Registered!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    /*public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }
*/
    private boolean checkPass(int passLength)
    {
        String pass = PasswordEditText.getText().toString();
        String repeatPass = PasswordRepeatEditText.getText().toString();

        for (int i = 0; i < passLength; i++)
        {
            if (repeatPass.charAt(i) != pass.charAt(i)) {return false;}
        }

        return true;
    }



    private void init() {
        EmailEditText = findViewById(R.id.registerEmailInput);
        PasswordEditText = findViewById(R.id.registerPasswordInput);
        PasswordRepeatEditText = findViewById(R.id.registerPasswordInputRepeat);
        MaterialButton = findViewById(R.id.materialButton);
        Auth = FirebaseAuth.getInstance();



        MaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = EmailEditText.getText().toString();
                String txt_password = PasswordEditText.getText().toString();
                String txt_password_repeat = PasswordRepeatEditText.getText().toString();

                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_password_repeat)) {
                    Toast.makeText(RegisterActivity.this,"Please fill the inputs correctly!",Toast.LENGTH_LONG).show();
                } else {
                    RegisterUser(txt_email,txt_password);
                }

            }
        });
    }
}