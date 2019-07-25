package com.apps.odhizvic.ballot.UserActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.apps.odhizvic.ballot.AdminFragments.ShowCurrentLeadershipFrag;
import com.apps.odhizvic.ballot.Login;
import com.apps.odhizvic.ballot.R;
import com.apps.odhizvic.ballot.UserFragments.UserShowRegisteredLeadershipFrag;
import com.apps.odhizvic.ballot.UserFragments.UserShowResultsList;


public class UserMainAccount extends AppCompatActivity {

    public Fragment fragment;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;

    public static String currentUserReg;
    public static String currentUserFaculty;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    displayFragment("show_leadership");
                    return true;
                case R.id.navigation_dashboard:
                    displayFragment("candidates_registered");
                    return true;
                case R.id.navigation_notifications:
                    displayFragment("candidates_results");
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main_account);
        getSupportActionBar().setTitle("Current Union Leaders");

        Intent intent = getIntent();
        if(intent != null){
            currentUserReg = intent.getExtras().getString("currentUserReg");
            currentUserFaculty = intent.getExtras().getString("currentUserFaculty");
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentManager = getSupportFragmentManager();

        if(fragment == null){
            fragment = new ShowCurrentLeadershipFrag();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.user_fragment_container, fragment);
            fragmentTransaction.commit();
        }

    }

    public void displayFragment(String title){
        switch (title){
            case "show_leadership":
                getSupportActionBar().setTitle("Current Union Leaders");
                fragment = new ShowCurrentLeadershipFrag();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.user_fragment_container, fragment);
                fragmentTransaction.commit();
                break;
            case "candidates_registered":
                getSupportActionBar().setTitle("Registered Candidates");
                fragment = new UserShowRegisteredLeadershipFrag();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.user_fragment_container, fragment);
                fragmentTransaction.commit();
                break;
            case "candidates_results":
                getSupportActionBar().setTitle("Voting Results");
                fragment = new UserShowResultsList();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.user_fragment_container, fragment);
                fragmentTransaction.commit();
                break;
                default:
                    break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.user_logout:
                startActivity(new Intent(UserMainAccount.this, Login.class));
                finish();
        }

        return true;
    }
}
