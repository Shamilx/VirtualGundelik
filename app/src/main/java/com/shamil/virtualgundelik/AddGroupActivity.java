package com.shamil.virtualgundelik;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.button.MaterialButton;

public class AddGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        init();
    }

    private void init() {
        ImageButton button = findViewById(R.id.AddGroupGoBack);

        button.setOnClickListener(view -> {
            finish();
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

        });

    }
}