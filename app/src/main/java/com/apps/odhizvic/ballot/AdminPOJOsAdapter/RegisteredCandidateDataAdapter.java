package com.apps.odhizvic.ballot.AdminPOJOsAdapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.odhizvic.ballot.AdminFragments.RegisteredCandidatesListOnclickFrag;
import com.apps.odhizvic.ballot.AdminPOJOs.RegisteredCandidatesData;
import com.apps.odhizvic.ballot.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RegisteredCandidateDataAdapter extends ArrayAdapter {
    private Activity activity;
    private List<RegisteredCandidatesData> candidatesData;
    private String candidateReg;

    public RegisteredCandidateDataAdapter(Activity activity,
                                          List<RegisteredCandidatesData> candidatesData){
        super(activity, R.layout.registered_candidates_list_onclick_template, candidatesData);

        this.activity = activity;
        this.candidatesData = candidatesData;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(activity)
                    .inflate(R.layout.registered_candidates_list_onclick_template, null, false);
        }

        final ImageView menu_options = (ImageView) convertView.findViewById(R.id.register_options);

        ImageView leader_dp = (ImageView) convertView.findViewById(R.id.admin_leader_dp);
        TextView leader_name = (TextView) convertView.findViewById(R.id.admin_leader_name);
        TextView leader_position = (TextView) convertView.findViewById(R.id.admin_leader_position);
        TextView leader_regno = (TextView) convertView.findViewById(R.id.admin_leader_regno);
        TextView leader_year = (TextView) convertView.findViewById(R.id.admin_leader_year);
        TextView leader_course = (TextView) convertView.findViewById(R.id.admin_leader_course);

        RegisteredCandidatesData registeredCandidatesData = candidatesData.get(position);

        Picasso.with(activity).load(registeredCandidatesData.getLeader_profile())
                .into(leader_dp);

        leader_name.setText(registeredCandidatesData.getLeader_name());
        leader_position.setText(registeredCandidatesData.getLeader_position());
        leader_regno.setText(registeredCandidatesData.getLeader_regNo());
        leader_year.setText(registeredCandidatesData.getLeader_year());
        leader_course.setText("");

        candidateReg = registeredCandidatesData.getLeader_regNo();
        menu_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(activity, "You clicked list at index: " + position, Toast.LENGTH_SHORT)
//                        .show();
                ;
                callPopUp(menu_options);

            }
        });

        return convertView;
    }

    public void callPopUp(View view) {
        PopupMenu popup = new PopupMenu(activity, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popupmenu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int item = menuItem.getItemId();
                 if (item == R.id.delete_candidate){
//                    Toast.makeText(activity, "Delete candidate", Toast.LENGTH_SHORT).show();
                    RegisteredCandidatesListOnclickFrag.deleteCandidate(candidateReg);
                }
                return true;
            }
        });
        popup.show();

    }
}
