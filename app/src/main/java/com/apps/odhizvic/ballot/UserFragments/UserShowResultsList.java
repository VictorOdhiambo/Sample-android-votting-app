package com.apps.odhizvic.ballot.UserFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.apps.odhizvic.ballot.AdminPOJOs.RegisteredCandidatesList;
import com.apps.odhizvic.ballot.R;
import com.apps.odhizvic.ballot.UserActivities.UserMainAccount;
import com.apps.odhizvic.ballot.UserPOJOsAdapter.UserListShowResultsAdapter;

import java.util.ArrayList;
import java.util.List;

public class UserShowResultsList extends Fragment {
    private ListView listView;
    private List<RegisteredCandidatesList> showRegisteredCandidatesList;

    public static String candidatePosition;

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

        String myFaculty = UserMainAccount.currentUserFaculty;

        showRegisteredCandidatesList.add(new RegisteredCandidatesList("Presidential"));
        showRegisteredCandidatesList.add(new RegisteredCandidatesList("Organizing_Secretary"));
        showRegisteredCandidatesList.add(new RegisteredCandidatesList("Academics"));
        showRegisteredCandidatesList.add(new RegisteredCandidatesList("Treasury"));
        showRegisteredCandidatesList.add(new RegisteredCandidatesList("Sports_and_Entertainment"));
        showRegisteredCandidatesList.add(new RegisteredCandidatesList("Welfare"));
        showRegisteredCandidatesList.add(new RegisteredCandidatesList(myFaculty));

        UserListShowResultsAdapter listAdapter = new UserListShowResultsAdapter(getActivity(),
                showRegisteredCandidatesList);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RegisteredCandidatesList registeredCandidatesList = showRegisteredCandidatesList
                        .get(position);
                candidatePosition = registeredCandidatesList.getCandidatePosition();

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.user_fragment_container,
                                new UserShowResultsListOnclickFrag())
                        .commit();

            }
        });

    }

    }

