package com.apps.odhizvic.ballot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apps.odhizvic.ballot.AdminActivities.AdminMainClass;
import com.apps.odhizvic.ballot.AdminPOJOs.AddNewStudent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminMenuTemp extends AppCompatActivity {
    private CardView show_leadership;
    private CardView show_registered_candidates;
    private CardView register_candidates;
    private CardView register_students;
    private CardView voting_session;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu_temp);

        getSupportActionBar().setTitle("Dashboard");

        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        show_leadership = (CardView) findViewById(R.id.admin_show_current_leadership);
        show_registered_candidates = (CardView) findViewById(R.id.admin_show_registered_candidates);
        register_candidates = (CardView) findViewById(R.id.admin_register_candidate);
        register_students = (CardView) findViewById(R.id.admin_add_student);
        voting_session = (CardView) findViewById(R.id.admin_voting_process);

        show_leadership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMenuTemp.this, AdminMainClass.class)
                .putExtra("title", "Current Union Leaders")
                .putExtra("fragment_title", "show_leadership"));

            }
        });

        show_registered_candidates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMenuTemp.this, AdminMainClass.class)
                        .putExtra("title", "Registered Candidate List")
                        .putExtra("fragment_title", "candidates_registered"));

            }
        });

        register_candidates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMenuTemp.this, AdminMainClass.class)
                        .putExtra("title", "Register New Candidate")
                        .putExtra("fragment_title", "register_candidate"));

            }
        });

        register_students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View mView = getLayoutInflater().inflate(R.layout.admin_add_new_student, null);
                final Button addStudent = (Button) mView.findViewById(R.id.add_new_student);
                final EditText student_reg = (EditText) mView.findViewById(R.id.new_student_regNo);
                addStudent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String reg = student_reg.getText().toString().trim();
                        if(TextUtils.isEmpty(reg)){
                            student_reg.setError("Student Registration required!");
                            return;
                        }else{
                            addStudent(reg);
                            student_reg.setText("");
                        }
                    }
                });
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminMenuTemp.this);
                builder.setTitle("Add Student Registration Number");
                builder.setView(mView);
                builder.setCancelable(true);
                builder.show();
            }
        });


        voting_session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMenuTemp.this, AdminMainClass.class)
                        .putExtra("title", "Voting Process")
                        .putExtra("fragment_title", "voting_process"));
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.admin_logout){
            mAuth.signOut();
            startActivity(new Intent(AdminMenuTemp.this, Login.class));
            finish();
        }else if(id == R.id.admin_reset){
            View mView = getLayoutInflater().inflate(R.layout.admin_reset, null);
            final Button delete = (Button) mView.findViewById(R.id.admin_delete);
            final EditText password = (EditText) mView.findViewById(R.id.admin_password);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String pwd = password.getText().toString().trim();
                    if(TextUtils.isEmpty(pwd)){
                        password.setError("password required!");
                        return;
                    }
                    if(pwd.equals("admin1234")){
                         clearEverything();
                        }else{
                            Toast.makeText(getApplicationContext(), "Incorrect password", Toast.LENGTH_SHORT)
                                    .show();
                        }
                        password.setText("");
                    }

            });
            AlertDialog.Builder builder = new AlertDialog.Builder(AdminMenuTemp.this);
            builder.setTitle("Clear Current Union and Students");
            builder.setView(mView);
            builder.setCancelable(true);
            builder.show();
        }

        return true;
    }

    private void addStudent(String regNo){
        progressDialog.setMessage("Adding Student...");
        progressDialog.show();
        DatabaseReference all_students_db_ref = FirebaseDatabase
                .getInstance()
                .getReference("All_students");
        Pattern m = Pattern.compile("[A-Za-z0-9]//[0-9]//[0-9]");
        Matcher matcher = m.matcher(regNo);
        if(matcher.matches()){
            Log.d("REGEX:", "IT MATCHES!");
        }else {
            Log.d("REGEX:", "IT DOES NOT MATCH!");
        }
        AddNewStudent student = new AddNewStudent(regNo);
        all_students_db_ref
                .push()
                .setValue(student)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(AdminMenuTemp.this,
                            "Student's Registration was successfully added", Toast.LENGTH_SHORT)
                            .show();
                }else{
                    Toast.makeText(AdminMenuTemp.this,
                            "FAILED TO ADD STUDENT!" + task
                                    .getException()
                                    .getMessage()
                                    .toString(), Toast.LENGTH_SHORT)
                            .show();
                }
                progressDialog.dismiss();
            }
        });
    }


    void clearEverything(){
        progressDialog.setMessage("Clearing Everything...");
        progressDialog.show();
        DatabaseReference union = FirebaseDatabase.getInstance()
                .getReference("Student_union");
        union.removeValue();

        DatabaseReference registeredCandidates = FirebaseDatabase.getInstance()
                .getReference("Registered_candidates_data");
        registeredCandidates.removeValue();

        DatabaseReference voteStatus = FirebaseDatabase.getInstance()
                .getReference("StudentVoteStatus");
        voteStatus.removeValue();

        DatabaseReference student_db = FirebaseDatabase.getInstance()
                .getReference("Student_DB");
        student_db.removeValue();

        DatabaseReference voteCounts = FirebaseDatabase.getInstance()
                .getReference("Vote_counts");
        voteCounts.removeValue();

        DatabaseReference status = FirebaseDatabase.getInstance()
                .getReference("Position_posted_status");
        status.removeValue();

        DatabaseReference allStudents = FirebaseDatabase.getInstance()
                .getReference("All_students");
        allStudents.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "All data is cleared", Toast.LENGTH_SHORT)
                            .show();
                }else{
                    Toast.makeText(getApplicationContext(), "Error clearing the data", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }
}
