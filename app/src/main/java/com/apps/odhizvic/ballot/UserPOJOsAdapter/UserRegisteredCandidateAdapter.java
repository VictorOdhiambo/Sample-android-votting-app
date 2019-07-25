package com.apps.odhizvic.ballot.UserPOJOsAdapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.apps.odhizvic.ballot.AdminPOJOs.RegisteredCandidatesData;
import com.apps.odhizvic.ballot.R;
import com.apps.odhizvic.ballot.UserActivities.UserMainAccount;
import com.apps.odhizvic.ballot.UserFragments.UserRegisteredCandidateListOnclickFrag;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserRegisteredCandidateAdapter extends ArrayAdapter {
    private Activity activity;
    private List<RegisteredCandidatesData> candidatesData;

    public UserRegisteredCandidateAdapter(Activity activity,
                                          List<RegisteredCandidatesData> candidatesData){
        super(activity, R.layout.user_registered_candidates_list_onclick_template, candidatesData);

        this.activity = activity;
        this.candidatesData = candidatesData;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(activity)
                    .inflate(R.layout.user_registered_candidates_list_onclick_template, null, false);
        }

        MaterialButton vote = (MaterialButton) convertView.findViewById(R.id.vote);

        CircleImageView leader_dp = (CircleImageView) convertView.findViewById(R.id.admin_leader_dp);
        TextView leader_name = (TextView) convertView.findViewById(R.id.admin_leader_name);
        TextView leader_position = (TextView) convertView.findViewById(R.id.admin_leader_position);
        TextView leader_regno = (TextView) convertView.findViewById(R.id.admin_leader_regno);
        TextView leader_year = (TextView) convertView.findViewById(R.id.admin_leader_year);

        final RegisteredCandidatesData registeredCandidatesData = candidatesData.get(position);

        Picasso.with(activity).load(registeredCandidatesData.getLeader_profile())
                .into(leader_dp);
        leader_name.setText(registeredCandidatesData.getLeader_name());
        leader_position.setText(registeredCandidatesData.getLeader_position());
        leader_regno.setText(registeredCandidatesData.getLeader_regNo());
        leader_year.setText(registeredCandidatesData.getLeader_year());

        vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRegisteredCandidateListOnclickFrag.studentHasVoted(
                        UserMainAccount.currentUserReg,
                        registeredCandidatesData.getLeader_position(),
                        registeredCandidatesData.getLeader_regNo()
                );
//                Toast.makeText(activity,"Test",Toast.LENGTH_SHORT).show();
            }
        });



        return convertView;
    }
}
