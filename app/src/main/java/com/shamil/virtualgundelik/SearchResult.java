package com.shamil.virtualgundelik;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import Models.CustomAdapter;
import Models.ListViewLine;
import Models.VirtualGundelikUser;


public class SearchResult extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private DocumentSnapshot _data;
    private ImageButton _button;

    public SearchResult() {}
    public SearchResult(DocumentSnapshot data) {_data = data; }

    public static SearchResult newInstance(String param1, String param2) {
        SearchResult fragment = new SearchResult();
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
        ListView listView = getView().findViewById(R.id.searchResultList);
        List<ListViewLine> lines = new ArrayList<>();

        lines.add(new ListViewLine(getString(R.string.edit_text_hint3), _data.getString("firstName"), x -> {
        }));

        lines.add(new ListViewLine(getString(R.string.edit_text_hint4), _data.getString("lastName"), x -> {
        }));

        lines.add(new ListViewLine(getString(R.string.edit_text_hint5), _data.getString("birthDate"), x -> {
        }));

        CustomAdapter adapter = new CustomAdapter(getActivity(),lines);

        listView.setAdapter(adapter);

        _button = getView().findViewById(R.id.backSearchButton);
        _button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = getActivity().getSupportFragmentManager().beginTransaction();
                fr.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                fr.replace(R.id.flFragment, new SearchFragment());
                fr.commit();
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_result, container, false);
    }
}