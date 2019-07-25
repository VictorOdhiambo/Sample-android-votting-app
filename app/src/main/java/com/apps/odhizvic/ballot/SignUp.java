package com.apps.odhizvic.ballot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.apps.odhizvic.ballot.AdminPOJOs.AddNewStudent;
import com.apps.odhizvic.ballot.UserActivities.UserMainAccount;
import com.apps.odhizvic.ballot.UserPOJOs.CreateUserAccount;
import com.apps.odhizvic.ballot.UserPOJOs.VotingData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity {
    private CircleImageView user_profile;
    private EditText student_name;
    private EditText student_regNo;
    private EditText password_one;
    private EditText password_two;
    private Spinner choose_faculty;
    private Button signUp;

    private List studentInfo;

    private ProgressDialog progressDialog;

    private FirebaseDatabase student_db;
    private DatabaseReference student_db_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();

        progressDialog = new ProgressDialog(this);

        studentInfo = new ArrayList();

        student_db = FirebaseDatabase.getInstance();

        user_profile = (CircleImageView) findViewById(R.id.student_profile);
        student_name = (EditText) findViewById(R.id.student_name);
        student_regNo = (EditText) findViewById(R.id.student_regno);
        password_one = (EditText) findViewById(R.id.student_password);
        password_two = (EditText) findViewById(R.id.student_password2);
        choose_faculty = (Spinner) findViewById(R.id.choose_faculty);
        signUp = (Button) findViewById(R.id.sign_up);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isAStudent();
            }
        });
    }

    private void isAStudent(){
        final String regNo = student_regNo.getText().toString().trim();
        if(TextUtils.isEmpty(regNo)){
            student_regNo.setError("Student registration number required!");
            return;
        }else{
            progressDialog.setMessage("Verifying Student Registration...");
            progressDialog.show();
            DatabaseReference all_students_db_ref = FirebaseDatabase
                    .getInstance()
                    .getReference("All_students");

            all_students_db_ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot student : dataSnapshot.getChildren()){
                        AddNewStudent newStudent = student.getValue(AddNewStudent.class);
                        if(newStudent.getRegistration_number().equalsIgnoreCase(regNo)){
                            signUpStudent();
                            break;
                        }else{
                            Toast.makeText(SignUp.this, "Registration Not Found!"
                                    , Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                    progressDialog.dismiss();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    private void signUpStudent(){
        String name = student_name.getText().toString().trim();
        final String regNo = student_regNo.getText().toString().trim();
        String pass1 = password_one.getText().toString().trim();
        String pass2 = password_two.getText().toString().trim();
        final String faculty = choose_faculty.getSelectedItem().toString();

        if(TextUtils.isEmpty(name)){
            student_name.setError("Student name required!");
            return;
        }else if(TextUtils.isEmpty(regNo)){
            student_regNo.setError("Student registration number required!");
            return;
        }else if(TextUtils.isEmpty(pass1)){
            password_one.setError("Password is required!");
            return;
        }else if(TextUtils.isEmpty(pass2)){
            password_two.setError("Kindly, re-enter the password!");
            return;
        }else if(faculty.equals("---select faculty---")){
            Toast.makeText(SignUp.this, "You have not selected a faculty",Toast.LENGTH_SHORT)
                    .show();
        }else{
            progressDialog.setMessage("Creating Student Account...");
            progressDialog.show();
            if(pass1.equals(pass2)){
                String unique_reg = "";
                if(regNo.contains("/")){
                    unique_reg = regNo.replace('/','_').toUpperCase();
                }
                CreateUserAccount studentData = new CreateUserAccount(faculty, name, pass1, regNo.toUpperCase());
                student_db_ref = student_db.getReference("Student_DB");
                student_db_ref.child(unique_reg)
                        .setValue(studentData)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
//                            createVotingStatus(regNo, faculty);
                            startActivity(new Intent(SignUp.this, UserMainAccount.class)
                            .putExtra("currentUserReg", regNo)
                            .putExtra("currentUserFaculty", faculty));
                            Toast.makeText(SignUp.this,
                                    "Account Creation was successfully!", Toast.LENGTH_SHORT)
                                    .show();
                            finish();
                        }else{
                            Toast.makeText(SignUp.this,
                                    "FAILED TO CREATE ACCOUNT!" + task.getException().getMessage().toString(),
                                     Toast.LENGTH_SHORT)
                                    .show();
                        }
                        progressDialog.dismiss();
                    }
                });
            }else{
                Toast.makeText(SignUp.this,
                        "Passwords did not match!", Toast.LENGTH_SHORT)
                        .show();
            }
        }

    }

    private void createVotingStatus(String studentReg, String faculty){
        String unique_reg = "";
        if(studentReg.contains("/")){
            unique_reg = studentReg.replace('/','_');
        }
        DatabaseReference votingStatusRef = FirebaseDatabase.getInstance()
                .getReference("StudentVoteStatus").child(unique_reg);

        VotingData votingData = new VotingData("False");
        votingStatusRef.child("Presidential").setValue(votingData);
        votingStatusRef.child("Organizing_Sec").setValue(votingData);
        votingStatusRef.child("Academics").setValue(votingData);
        votingStatusRef.child("Treasury").setValue(votingData);
        votingStatusRef.child("Welfare").setValue(votingData);
        votingStatusRef.child(faculty).setValue(votingData);
    }
}
