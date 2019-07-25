package com.apps.odhizvic.ballot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apps.odhizvic.ballot.UserActivities.UserMainAccount;
import com.apps.odhizvic.ballot.UserPOJOs.CreateUserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
    EditText user, pass;
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        Button btn3 = (Button) findViewById(R.id.sign_up);
        Button btn4 = (Button) findViewById(R.id.signIn);
         user = (EditText) findViewById(R.id.username);
         pass = (EditText) findViewById(R.id.password);

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, SignUp.class));
//                finish();
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyUserLogin();
            }
        });
    }

    private void verifyUserLogin(){
        final String name = user.getText().toString().trim();
        final String pwd = pass.getText().toString().trim();

        if(checkInputField(name)){//is admin
            signInAdmin(name, pwd);
        }else {
            progressDialog.setMessage("Signing in...");
            progressDialog.show();
            DatabaseReference databaseReference = FirebaseDatabase
                    .getInstance()
                    .getReference("Student_DB");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        CreateUserAccount userAccount = user.getValue(CreateUserAccount.class);
                        if (userAccount.getStudentRegNo().equalsIgnoreCase(name)
                                && userAccount.getStudentPassword().equals(pwd)) {
                            startActivity(new Intent(Login.this, UserMainAccount.class)
                                    .putExtra("currentUserReg", name.toUpperCase())
                                    .putExtra("currentUserFaculty", userAccount.getStudentFaculty())
                            );
                            finish();
                        } else {
                            Toast.makeText(Login.this,
                                    "Incorrect Username or password",
                                    Toast.LENGTH_SHORT)
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

    private void signInAdmin(String email, String password){
        progressDialog.setMessage("Signing in...");
        progressDialog.show();
        if (email.equals("admin@ballot.app") && password.equals("admin1234")){
            startActivity(new Intent(Login.this, AdminMenuTemp.class));
            finish();
            } else {
            Toast.makeText(Login.this,
                    "Incorrect Username or password",
                    Toast.LENGTH_SHORT)
                    .show();
        }

//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()){
//                            startActivity(new Intent(Login.this, AdminMenuTemp.class));
//                            finish();
//                        } else {
//                            Toast.makeText(Login.this,
//                                    "Incorrect Username or password",
//                                    Toast.LENGTH_SHORT)
//                                    .show();
//                        }
//                        progressDialog.dismiss();
//
//                    }
//                });
        progressDialog.dismiss();


    }

    private boolean checkInputField(String username){
        String emailFormat = "^(.+)@(.+)$";
        String regFormat = "^[A-Za-z0-9]\\ /[0-9]\\ /[0-9]";

        Pattern pattern = Pattern.compile(emailFormat);
        Matcher matcher = pattern.matcher(username);

        if(matcher.matches()){
            return true;
        }

        return false;
    }
}
