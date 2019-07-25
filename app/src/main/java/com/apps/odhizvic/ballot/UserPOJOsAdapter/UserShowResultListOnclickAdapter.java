package com.apps.odhizvic.ballot.UserPOJOsAdapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.odhizvic.ballot.AdminPOJOs.RegisteredCandidatesData;
import com.apps.odhizvic.ballot.R;
import com.apps.odhizvic.ballot.UserFragments.UserShowResultsList;
import com.apps.odhizvic.ballot.UserFragments.UserShowResultsListOnclickFrag;
import com.apps.odhizvic.ballot.UserPOJOs.UserShowResults;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.apps.odhizvic.ballot.UserFragments.UserShowResultsListOnclickFrag.candidatePosition;


public class UserShowResultListOnclickAdapter extends ArrayAdapter {
    private Activity activity;
    private List<RegisteredCandidatesData> showResults;
    public static String votes;

    public UserShowResultListOnclickAdapter(Activity activity, List<RegisteredCandidatesData> showResults){
        super(activity, R.layout.user_show_result_onclick_adapter, showResults);

        this.activity = activity;
        this.showResults = showResults;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(activity)
                    .inflate(R.layout.user_show_result_onclick_adapter, null);
        }

        TextView vote = (TextView) convertView.findViewById(R.id.vote);
        CircleImageView leader_dp = (CircleImageView) convertView.findViewById(R.id.admin_leader_dp);
        TextView leader_name = (TextView) convertView.findViewById(R.id.admin_leader_name);
        TextView leader_position = (TextView) convertView.findViewById(R.id.admin_leader_position);
        TextView leader_regno = (TextView) convertView.findViewById(R.id.admin_leader_regno);
        TextView leader_year = (TextView) convertView.findViewById(R.id.admin_leader_year);

        final RegisteredCandidatesData registeredCandidatesData = showResults.get(position);
//        getVoteCounts(registeredCandidatesData.getLeader_regNo());


        Picasso.with(activity).load(registeredCandidatesData.getLeader_profile())
                .into(leader_dp);
        vote.setText(registeredCandidatesData.getVoteCount() + " Vote(s)");
        leader_name.setText(registeredCandidatesData.getLeader_name());
        leader_position.setText(registeredCandidatesData.getLeader_position());
        leader_regno.setText(registeredCandidatesData.getLeader_regNo());
        leader_year.setText(registeredCandidatesData.getLeader_year());



        return convertView;
    }

    private  void getVoteCounts(String candidateReg){
        //Get the candidate reg and find their votes
        String unique_reg = "";
        if (candidateReg.contains("/")) {
            unique_reg = candidateReg.replace('/', '_');
        }
        DatabaseReference singleResult = FirebaseDatabase.getInstance()
                .getReference("Vote_counts").child(candidatePosition);
        singleResult.child(unique_reg).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot count : dataSnapshot.getChildren()) {
                     votes = count.getValue().toString();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
