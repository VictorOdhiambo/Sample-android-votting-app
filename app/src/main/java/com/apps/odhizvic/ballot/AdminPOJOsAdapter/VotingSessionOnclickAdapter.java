package com.apps.odhizvic.ballot.AdminPOJOsAdapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.apps.odhizvic.ballot.AdminFragments.VotingSessionListOnclick;
import com.apps.odhizvic.ballot.AdminPOJOs.RegisteredCandidatesData;
import com.apps.odhizvic.ballot.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class VotingSessionOnclickAdapter extends ArrayAdapter {
    private Activity activity;
    private List<RegisteredCandidatesData> showResults;
    public static String votes;

    public VotingSessionOnclickAdapter(Activity activity, List<RegisteredCandidatesData> showResults){
        super(activity, R.layout.voting_session_onclick_adapter, showResults);

        this.activity = activity;
        this.showResults = showResults;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(activity)
                    .inflate(R.layout.voting_session_onclick_adapter, null);
        }

        TextView vote = (TextView) convertView.findViewById(R.id.vote);
        CircleImageView leader_dp = (CircleImageView) convertView.findViewById(R.id.admin_leader_dp);
        TextView leader_name = (TextView) convertView.findViewById(R.id.admin_leader_name);
        TextView leader_position = (TextView) convertView.findViewById(R.id.admin_leader_position);
        TextView leader_regno = (TextView) convertView.findViewById(R.id.admin_leader_regno);
        TextView leader_year = (TextView) convertView.findViewById(R.id.admin_leader_year);
        MaterialButton postCandidate = (MaterialButton) convertView.findViewById(R.id.postCandidate);

        final RegisteredCandidatesData registeredCandidatesData = showResults.get(position);

        Picasso.with(activity).load(registeredCandidatesData.getLeader_profile())
                .into(leader_dp);
        vote.setText(registeredCandidatesData.getVoteCount() + " Vote(s)");
        leader_name.setText(registeredCandidatesData.getLeader_name());
        leader_position.setText(registeredCandidatesData.getLeader_position());
        leader_regno.setText(registeredCandidatesData.getLeader_regNo());
        leader_year.setText(registeredCandidatesData.getLeader_year());


        postCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisteredCandidatesData candidatesData = new RegisteredCandidatesData(
                        registeredCandidatesData.getLeader_name(),
                        registeredCandidatesData.getLeader_position(),
                        registeredCandidatesData.getLeader_profile(),
                        registeredCandidatesData.getLeader_regNo(),
                        registeredCandidatesData.getLeader_year(),
                        registeredCandidatesData.getVoteCount()
                );

                VotingSessionListOnclick.positionIsPosted(
                        VotingSessionListOnclick.candidatePosition,
                        registeredCandidatesData.getLeader_regNo(),
                        candidatesData
                        );
            }
        });

        return convertView;
    }

}
