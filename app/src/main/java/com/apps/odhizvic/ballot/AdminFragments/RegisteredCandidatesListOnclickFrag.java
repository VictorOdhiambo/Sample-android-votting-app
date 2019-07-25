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
import com.apps.odhizvic.ballot.AdminPOJOs.ShowCurrentLeadership;
import com.apps.odhizvic.ballot.AdminPOJOsAdapter.RegisteredCandidateDataAdapter;
import com.apps.odhizvic.ballot.AdminPOJOsAdapter.ShowCurrentLeadershipAdapter;
import com.apps.odhizvic.ballot.R;
import com.apps.odhizvic.ballot.UserPOJOsAdapter.UserRegisteredCandidateAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RegisteredCandidatesListOnclickFrag extends Fragment {
    private ListView listView;
    private List<RegisteredCandidatesData> candidatesData;
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

        activity = getActivity();
        progressBar = (ProgressBar) view.findViewById(R.id.myprogressbar);

        listView = (ListView) view.findViewById(R.id.onclick_registered_candidates);

        displayRegisteredCandidates();

    }


    private void displayRegisteredCandidates(){//admin
        DatabaseReference registered_db_ref = FirebaseDatabase.getInstance()
                .getReference("Registered_candidates_data").child(ShowRegisteredLeadershipFrag.candidatePosition);

        registered_db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                candidatesData.clear();
                if(dataSnapshot.exists()) {
                    for (DataSnapshot candidate : dataSnapshot.getChildren()) {
                        RegisteredCandidatesData candidateData = candidate.getValue(RegisteredCandidatesData.class);
                        candidatesData.add(candidateData);
                        progressBar.setVisibility(View.GONE);
                    }
                }else{
                    Toast.makeText(activity, "No data to display", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
                RegisteredCandidateDataAdapter candidateDataAdapter = new RegisteredCandidateDataAdapter(getActivity(),
                        candidatesData);

                listView.setAdapter(candidateDataAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void deleteCandidate(final String candidateReg){
        DatabaseReference registered_db_ref = FirebaseDatabase.getInstance()
                .getReference("Registered_candidates_data").child(ShowRegisteredLeadershipFrag.candidatePosition);
        String unique_reg = "";
        if(candidateReg.contains("/")){
            unique_reg = candidateReg.replace('/','_');
        }

        registered_db_ref.child(unique_reg)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(activity, "Candidate with Registration: " + candidateReg +
                    " is removed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
