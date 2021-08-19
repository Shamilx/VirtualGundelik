package com.shamil.virtualgundelik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
public class MainActivity extends AppCompatActivity {

    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = findViewById(R.id.flFragment);
    }

    public void Profile_OnClick(MenuItem item) {
        if (frameLayout.findViewById(R.id.profileFragment) == null) {
            FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
            fr.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left);
            fr.replace(R.id.flFragment, new ProfileFragment());
            fr.commit();
        }

        item.setChecked(true);
    }

    public void Search_OnClick(MenuItem item) {
        if (frameLayout.findViewById(R.id.searchFragment) == null) {
            FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
            fr.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            fr.replace(R.id.flFragment, new SearchFragment());
            fr.commit();

        }

        item.setChecked(true);
    }
}