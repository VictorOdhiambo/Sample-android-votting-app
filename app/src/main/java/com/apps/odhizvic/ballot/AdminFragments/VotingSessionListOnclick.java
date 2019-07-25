package com.apps.odhizvic.ballot.AdminFragments;

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
import com.apps.odhizvic.ballot.AdminPOJOsAdapter.VotingSessionOnclickAdapter;
import com.apps.odhizvic.ballot.R;
import com.apps.odhizvic.ballot.UserFragments.UserShowResultsList;
import com.apps.odhizvic.ballot.UserPOJOs.UserShowResults;
import com.apps.odhizvic.ballot.UserPOJOsAdapter.UserShowResultListOnclickAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VotingSessionListOnclick extends Fragment {
    private static ListView listView;
    public static String candidatePosition;

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

        registeredCandidatesData = new ArrayList<>();

        progressBar = (ProgressBar) view.findViewById(R.id.myprogressbar);

        candidatePosition = VotingSession.candidatePosition;

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
                if(dataSnapshot.exists()) {
                    for (DataSnapshot candidate : dataSnapshot.getChildren()) {
                        RegisteredCandidatesData candidatesData = candidate.getValue(RegisteredCandidatesData.class);
                        registeredCandidatesData.add(candidatesData);
                        progressBar.setVisibility(View.GONE);
                    }
                }else{
                    Toast.makeText(activity, "No data to display", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
                VotingSessionOnclickAdapter candidateDataAdapter = new VotingSessionOnclickAdapter(activity,
                        registeredCandidatesData);
                listView.setAdapter(candidateDataAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public static void postFinalResults(String candidateReg, RegisteredCandidatesData candidatesData){
        DatabaseReference studentUnion = FirebaseDatabase.getInstance()
                .getReference("Student_union");
        String unique_reg = "";
        if(candidateReg.contains("/")){
            unique_reg = candidateReg.replace('/','_');
        }
        studentUnion.child(unique_reg)
                .setValue(candidatesData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(activity, "Candidate posted successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(activity, "Error posting the candidate", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void positionIsPosted(final String candidatePosition, final String candidateReg, final RegisteredCandidatesData candidatesData){
        DatabaseReference positionPosted = FirebaseDatabase.getInstance()
                .getReference("Position_posted_status");

        positionPosted.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(candidatePosition)){
                    Toast.makeText(activity, "You cannot post twice", Toast.LENGTH_SHORT).show();
                }else {
                    postFinalResults(candidateReg, candidatesData);
                    postStatus(candidatePosition);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    static void postStatus(String candidatePosition){
        DatabaseReference positionPosted = FirebaseDatabase.getInstance()
                .getReference("Position_posted_status");


        positionPosted
                .child(candidatePosition)
                .setValue("Posted")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(activity, "True", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(activity, "False", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
