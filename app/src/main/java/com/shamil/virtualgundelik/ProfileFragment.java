package com.shamil.virtualgundelik;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import Models.VirtualGundelikUser;

public class ProfileFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView userId = getView().findViewById(R.id.user_id);
        TextView userName = getView().findViewById(R.id.user_name);
        MaterialButton button1 = getView().findViewById(R.id.profileMaterialButton1);
        MaterialButton button2 = getView().findViewById(R.id.profileMaterialButton2);
        MaterialButton button3 = getView().findViewById(R.id.profileMaterialButton3);

        UpdateUser(userId, userName);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ProfileSettingsActivity.class);
                startActivity(intent);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("MyPrefs",getActivity().MODE_PRIVATE).edit();
                editor.remove("User");
                editor.commit();

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LogInActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private void UpdateUser(TextView userId, TextView userName) {
        SharedPreferences mPrefs = getActivity().getSharedPreferences("MyPrefs",getActivity().MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("User","");
        VirtualGundelikUser virtualGundelikUser = gson.fromJson(json,VirtualGundelikUser.class);

        userName.setText(virtualGundelikUser.FirstName);
        userId.setText("@" + String.valueOf(virtualGundelikUser.ID));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView userId = getView().findViewById(R.id.user_id);
        TextView userName = getView().findViewById(R.id.user_name);
        UpdateUser(userId,userName);
    }
}