package com.shamil.virtualgundelik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Profile_OnClick(MenuItem item) {
        FrameLayout frameLayout = findViewById(R.id.flFragment);

        if (frameLayout.findViewById(R.id.profileFragment) == null) {
            FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
            fr.replace(R.id.flFragment, new ProfileFragment());
            fr.commit();
        }

        item.setChecked(true);
    }
}