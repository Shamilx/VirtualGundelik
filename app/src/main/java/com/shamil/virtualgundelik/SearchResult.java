package com.shamil.virtualgundelik;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchResult#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchResult extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public SearchResult() {}
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

        try {
            init();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void init() throws InterruptedException {
        TextView loading = getView().findViewById(R.id.loading);
        TextView user_name_result = getView().findViewById(R.id.user_name_result);
        TextView user_lastname_result = getView().findViewById(R.id.user_lastname_result);
        TextView user_email_result = getView().findViewById(R.id.user_email_result);
        TextView user_birth_day_result = getView().findViewById(R.id.user_birth_day_result);

        //int count = 0;
        //while (count <= 10)
        //{
        //    loading.setText("Loading");
        //    Thread.sleep(500);
        //    loading.setText("Loading.");
        //    Thread.sleep(500);
        //    loading.setText("Loading..");
        //    Thread.sleep(500);
        //    loading.setText("Loading...");
        //    Thread.sleep(500);
        //}
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_result, container, false);
    }
}