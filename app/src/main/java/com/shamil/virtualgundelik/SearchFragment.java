package com.shamil.virtualgundelik;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LayoutInflater inflater;
    private ViewGroup container;
    private Bundle savedInstanceState;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CollectionReference usersRef = FirebaseFirestore.getInstance().collection("Users");
        MaterialButton button = getActivity().findViewById(R.id.searchButton);
        TextInputEditText editText = getActivity().findViewById(R.id.searchWithid);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String givenId = editText.getText().toString();


                if(givenId.length() < 6 &&  Integer.parseInt(givenId) != 0 && Integer.parseInt(givenId) != 118) {
                    editText.setError(getString(R.string.java2));
                    return;
                }

                FirebaseFirestore.getInstance().collection("Users")
                        .whereEqualTo("id",Integer.parseInt(givenId))
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (view.getId() == R.id.searchButton)
                        {
                            // ToDo: burda problem var (Cox ehtimal )
                            FragmentManager fm = getChildFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.replace(R.layout.fragment_search_result, new SearchResult());
                            ft.commit();
                        }

                                          }
                });
            }
        });

    }
}