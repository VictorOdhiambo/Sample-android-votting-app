package com.apps.odhizvic.ballot.UserFragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.apps.odhizvic.ballot.AdminPOJOs.RegisteredCandidatesData;
import com.apps.odhizvic.ballot.AdminPOJOs.VoteCounts;
import com.apps.odhizvic.ballot.R;
import com.apps.odhizvic.ballot.UserPOJOs.UserShowResults;
import com.apps.odhizvic.ballot.UserPOJOsAdapter.UserRegisteredCandidateAdapter;
import com.apps.odhizvic.ballot.UserPOJOsAdapter.UserShowResultListOnclickAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserShowResultsListOnclickFrag extends Fragment {
    private static ListView listView;
    public static String candidatePosition;
    private static String candidateReg;
    public  static String voteCount;
    private List<UserShowResults> candidateResults;
    private static List<RegisteredCandidatesData> registeredCandidatesData;
    private static ProgressBar progressBar;

    static Activity activity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registered_candidates_list_onclick_frag, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        candidateResults = new ArrayList<>();
        registeredCandidatesData = new ArrayList<>();

        progressBar = (ProgressBar) view.findViewById(R.id.myprogressbar);

        candidatePosition = UserShowResultsList.candidatePosition;

        activity = getActivity();

        listView = (ListView) view.findViewById(R.id.onclick_registered_candidates);

        showFinalResult(candidatePosition);
    }



    private static void showFinalResult(final String candidatePosition){
        DatabaseReference finalResults = FirebaseDatabase.getInstance()
                .getReference("Registered_candidates_data").child(candidatePosition);

        finalResults.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                registeredCandidatesData.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot candidate : dataSnapshot.getChildren()) {
                        RegisteredCandidatesData candidatesData = candidate.getValue(RegisteredCandidatesData.class);
                        registeredCandidatesData.add(candidatesData);
                        progressBar.setVisibility(View.GONE);
                    }
                }else {
                    Toast.makeText(activity, "No data to display", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
                UserShowResultListOnclickAdapter candidateDataAdapter = new UserShowResultListOnclickAdapter(activity,
                        registeredCandidatesData);
                listView.setAdapter(candidateDataAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
