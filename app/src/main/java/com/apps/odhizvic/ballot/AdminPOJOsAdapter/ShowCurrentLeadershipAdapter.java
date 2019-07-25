package com.apps.odhizvic.ballot.AdminPOJOsAdapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.odhizvic.ballot.AdminPOJOs.RegisteredCandidatesData;
import com.apps.odhizvic.ballot.AdminPOJOs.ShowCurrentLeadership;
import com.apps.odhizvic.ballot.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShowCurrentLeadershipAdapter extends ArrayAdapter {
    private Activity activity;
    private List<RegisteredCandidatesData> currentLeadershipList;

    public ShowCurrentLeadershipAdapter(Activity activity,
                                        List<RegisteredCandidatesData> currentLeadershipList){
        super(activity, R.layout.admin_show_current_leadership_template, currentLeadershipList);

        this.activity = activity;
        this.currentLeadershipList = currentLeadershipList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(activity)
                    .inflate(R.layout.admin_show_current_leadership_template, null, false);
        }


        ImageView leader_dp = (ImageView) convertView.findViewById(R.id.admin_leader_dp);
        TextView leader_name = (TextView) convertView.findViewById(R.id.admin_leader_name);
        TextView leader_position = (TextView) convertView.findViewById(R.id.admin_leader_position);
        TextView leader_regno = (TextView) convertView.findViewById(R.id.admin_leader_regno);
        TextView leader_year = (TextView) convertView.findViewById(R.id.admin_leader_year);
        TextView leader_course = (TextView) convertView.findViewById(R.id.admin_leader_course);

        RegisteredCandidatesData registeredCandidatesData = currentLeadershipList.get(position);

        Picasso.with(activity).load(registeredCandidatesData.getLeader_profile())
                .into(leader_dp);

        leader_name.setText(registeredCandidatesData.getLeader_name());
        leader_position.setText(registeredCandidatesData.getLeader_position());
        leader_regno.setText(registeredCandidatesData.getLeader_regNo());
        leader_year.setText(registeredCandidatesData.getLeader_year());
        leader_course.setText("");

        
        return convertView;
    }
}
