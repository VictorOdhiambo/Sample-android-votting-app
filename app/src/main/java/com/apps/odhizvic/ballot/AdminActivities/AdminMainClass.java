package com.apps.odhizvic.ballot.AdminActivities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.apps.odhizvic.ballot.AdminFragments.RegisterNewCandidate;
import com.apps.odhizvic.ballot.AdminFragments.RegisteredCandidatesListOnclickFrag;
import com.apps.odhizvic.ballot.AdminFragments.ShowCurrentLeadershipFrag;
import com.apps.odhizvic.ballot.AdminFragments.ShowRegisteredLeadershipFrag;
import com.apps.odhizvic.ballot.AdminFragments.VotingSession;
import com.apps.odhizvic.ballot.R;

public class AdminMainClass extends AppCompatActivity {

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private String title;
    private String fragment_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_class);

        Intent intent = getIntent();
        if(intent != null){
            title = getIntent().getExtras().getString("title");
            fragment_title = getIntent().getExtras().getString("fragment_title");
        }

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentManager = getSupportFragmentManager();

            displayFragment(fragment_title);

    }

    private void displayFragment(String title){
        switch (title){
            case "show_leadership":
                fragment = new ShowCurrentLeadershipFrag();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.admin_fragment_container, fragment);
                fragmentTransaction.commit();
                break;
            case "register_candidate":
                fragment = new RegisterNewCandidate();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.admin_fragment_container, fragment);
                fragmentTransaction.commit();

                break;
            case "candidates_registered":
                fragment = new ShowRegisteredLeadershipFrag();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.admin_fragment_container, fragment);
                fragmentTransaction.commit();

                break;
            case "add_student":

                break;
            case "voting_process":
                fragment = new VotingSession();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.admin_fragment_container, fragment);
                fragmentTransaction.commit();
                break;
            case "registered_list_onclick":
                fragment = new RegisteredCandidatesListOnclickFrag();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.admin_fragment_container, fragment);
                fragmentTransaction.commit();
                break;
                default:
                    break;
        }
    }
}
