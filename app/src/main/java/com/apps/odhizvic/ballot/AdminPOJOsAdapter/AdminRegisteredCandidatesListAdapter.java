package com.apps.odhizvic.ballot.AdminPOJOsAdapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.apps.odhizvic.ballot.AdminPOJOs.RegisteredCandidatesList;
import com.apps.odhizvic.ballot.R;

import java.util.List;

public class AdminRegisteredCandidatesListAdapter extends ArrayAdapter {
    private Activity activity;
    private List<RegisteredCandidatesList> candidatesList;

    public AdminRegisteredCandidatesListAdapter(Activity activity,
                    List<RegisteredCandidatesList> candidatesList){
        super(activity, R.layout.registered_candidates_template, candidatesList);
        this.activity = activity;
        this.candidatesList = candidatesList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(activity)
                    .inflate(R.layout.registered_candidates_template, null, false);
        }

        TextView candidatePosition = (TextView)convertView.findViewById(R.id.registered_candidate_position);

        RegisteredCandidatesList registeredCandidatesList = candidatesList.get(position);

        candidatePosition.setText(registeredCandidatesList.getCandidatePosition());

        return convertView;
    }
}
