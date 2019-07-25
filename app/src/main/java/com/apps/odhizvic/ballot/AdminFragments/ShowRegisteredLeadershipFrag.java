package com.apps.odhizvic.ballot.AdminFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.apps.odhizvic.ballot.AdminActivities.AdminMainClass;
import com.apps.odhizvic.ballot.AdminPOJOs.RegisteredCandidatesList;
import com.apps.odhizvic.ballot.AdminPOJOsAdapter.AdminRegisteredCandidatesListAdapter;
import com.apps.odhizvic.ballot.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowRegisteredLeadershipFrag extends Fragment {
    private ListView listView;
    public static String candidatePosition;
    private List<RegisteredCandidatesList> showRegisteredCandidatesList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_registered_leadership_frag, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showRegisteredCandidatesList = new ArrayList<>();

        listView = (ListView) view.findViewById(R.id.admin_show_registered_candidates_listview);

        showRegisteredCandidatesList.add(new RegisteredCandidatesList("Presidential"));
        showRegisteredCandidatesList.add(new RegisteredCandidatesList("Organizing_Secretary"));
        showRegisteredCandidatesList.add(new RegisteredCandidatesList("Academics"));
        showRegisteredCandidatesList.add(new RegisteredCandidatesList("Treasury"));
        showRegisteredCandidatesList.add(new RegisteredCandidatesList("Sports_and_Entertainment"));
        showRegisteredCandidatesList.add(new RegisteredCandidatesList("Welfare"));
        showRegisteredCandidatesList.add(new RegisteredCandidatesList("FIST"));
        showRegisteredCandidatesList.add(new RegisteredCandidatesList("SOBE"));
        showRegisteredCandidatesList.add(new RegisteredCandidatesList("SPASS"));
        showRegisteredCandidatesList.add(new RegisteredCandidatesList("FASS"));
        showRegisteredCandidatesList.add(new RegisteredCandidatesList("Law"));
        showRegisteredCandidatesList.add(new RegisteredCandidatesList("Education"));

        listView.setAdapter(new AdminRegisteredCandidatesListAdapter(getActivity(),showRegisteredCandidatesList));


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RegisteredCandidatesList registeredCandidatesList = showRegisteredCandidatesList
                        .get(position);
                candidatePosition = registeredCandidatesList.getCandidatePosition();

                startActivity(new Intent(getActivity(), AdminMainClass.class)
                .putExtra("title", candidatePosition)
                .putExtra("fragment_title", "registered_list_onclick"));
            }
        });

    }

}
