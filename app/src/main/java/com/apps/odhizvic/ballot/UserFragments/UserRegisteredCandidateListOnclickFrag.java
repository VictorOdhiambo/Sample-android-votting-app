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
import com.apps.odhizvic.ballot.UserPOJOs.VotingData;
import com.apps.odhizvic.ballot.UserPOJOsAdapter.UserRegisteredCandidateAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserRegisteredCandidateListOnclickFrag extends Fragment {
    private ListView listView;
    private List<RegisteredCandidatesData> candidatesData;
    private static String candidatePosition;
    private ProgressBar progressBar;

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

        candidatesData = new ArrayList<>();
        progressBar = (ProgressBar) view.findViewById(R.id.myprogressbar);

        candidatePosition = UserShowRegisteredLeadershipFrag.candidatePosition;

        activity = getActivity();

        listView = (ListView) view.findViewById(R.id.onclick_registered_candidates);

        displayRegisteredCandidates();


    }

    private void displayRegisteredCandidates(){
        DatabaseReference registered_db_ref = FirebaseDatabase.getInstance()
                .getReference("Registered_candidates_data").child(candidatePosition);

        registered_db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                candidatesData.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot candidate : dataSnapshot.getChildren()) {
                        RegisteredCandidatesData candidateData = candidate
                                .getValue(RegisteredCandidatesData.class);
                        candidatesData.add(candidateData);
                        progressBar.setVisibility(View.GONE);
                    }
                }else {
                    Toast.makeText(activity, "No data to display", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
                UserRegisteredCandidateAdapter candidateDataAdapter = new UserRegisteredCandidateAdapter(getActivity(),
                        candidatesData);

                listView.setAdapter(candidateDataAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void vote(String studentReg, String candidatePosition, String candidateReg){
        String unique_reg = "";
        if (studentReg.contains("/")) {
            unique_reg = studentReg.replace('/', '_');
        }
        DatabaseReference status = FirebaseDatabase.getInstance()
                .getReference("StudentVoteStatus").child(candidatePosition);

        VotingData votingData = new VotingData(unique_reg, "True");
        status.child(unique_reg).setValue(votingData);

        //getVotes
        getVoteCounts(candidateReg, candidatePosition);
    }

    private static void getVoteCounts(final String candidateReg, final String candidatePosition){
        String unique_reg = "";
        if (candidateReg.contains("/")) {
            unique_reg = candidateReg.replace('/', '_');
        }
        DatabaseReference countsDbRef = FirebaseDatabase.getInstance()
                .getReference("Vote_counts").child(candidatePosition);

        countsDbRef.child(unique_reg).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot count : dataSnapshot.getChildren()) {

                    String numberOfVotes = count.getValue().toString();

//                    Toast.makeText(activity, numberOfVotes, Toast.LENGTH_SHORT).show();

                    int newVoteCount = Integer.parseInt(numberOfVotes) + 1;
                    String updatedVoteCount = String.valueOf(newVoteCount);
                    updateVoteCount(candidatePosition, candidateReg, updatedVoteCount);
                    updateCandidateData(candidatePosition, candidateReg, newVoteCount);


                    Toast.makeText(activity, "You have voted!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static void updateVoteCount(String candidatePosition, String candidateReg, String newVoteCount){
        String unique_reg = "";
        if (candidateReg.contains("/")) {
            unique_reg = candidateReg.replace('/', '_');
        }
        DatabaseReference voteCountsDbRef = FirebaseDatabase.getInstance()
                .getReference("Vote_counts").child(candidatePosition);

        VoteCounts voteCounts = new VoteCounts(newVoteCount);
        voteCountsDbRef.child(unique_reg).setValue(voteCounts);
    }


    public static void studentHasVoted(final String studentReg,
                                       final String candidatePosition, final String candidateReg) {
        String unique_reg = "";
        if (studentReg.contains("/")) {
            unique_reg = studentReg.replace('/', '_');
        }
        DatabaseReference status = FirebaseDatabase.getInstance()
                .getReference("StudentVoteStatus").child(candidatePosition);

//        vote(studentReg, candidatePosition, candidateReg);

        final String finalUnique_reg = unique_reg;

//
        status.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(finalUnique_reg)){
                    Toast.makeText(activity, "You cannot vote twice", Toast.LENGTH_SHORT).show();
                }else{
                    vote(studentReg, candidatePosition, candidateReg);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private static void updateCandidateData(String candidatePosition, String candidateReg, int voteCount){
        DatabaseReference candidateData = FirebaseDatabase.getInstance()
                .getReference("Registered_candidates_data").child(candidatePosition);

        String unique_reg = "";
        if (candidateReg.contains("/")) {
            unique_reg = candidateReg.replace('/', '_');
        }

        candidateData.child(unique_reg).child("voteCount").setValue(voteCount);
    }


}
