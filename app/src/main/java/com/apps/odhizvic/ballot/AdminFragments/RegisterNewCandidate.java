package com.apps.odhizvic.ballot.AdminFragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.apps.odhizvic.ballot.AdminMenuTemp;
import com.apps.odhizvic.ballot.AdminPOJOs.RegisteredCandidatesData;
import com.apps.odhizvic.ballot.AdminPOJOs.RegisteredCandidatesList;
import com.apps.odhizvic.ballot.AdminPOJOs.VoteCounts;
import com.apps.odhizvic.ballot.R;
import com.apps.odhizvic.ballot.UserPOJOs.UserShowResults;
import com.apps.odhizvic.ballot.UserPOJOs.VotingData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class RegisterNewCandidate extends Fragment {
    private static final int IMAGE_GALLERY_REQUEST = 334;
    private Spinner choosePosition;
    private Spinner chooseFaculty;
    private Spinner chooseYear;
    private EditText candidateName;
    private EditText candidateRegNo;
    private CircleImageView candidateProfile;
    private Button registerBtn;


    private ProgressDialog progressDialog;


    private FirebaseDatabase registered_db;
    private DatabaseReference registered_db_ref;
    private StorageReference imagesReference;

    private Uri uploadImageUri;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_new_candidate, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new ProgressDialog(getActivity());

        choosePosition = (Spinner) view.findViewById(R.id.choose_position);
        chooseFaculty = (Spinner) view.findViewById(R.id.choose_faculty);
        chooseYear = (Spinner) view.findViewById(R.id.choose_year);
        candidateName = (EditText) view.findViewById(R.id.candidate_name);
        candidateRegNo = (EditText) view.findViewById(R.id.candidate_regno);
        candidateProfile = (CircleImageView) view.findViewById(R.id.candidate_profile);
        registerBtn = (Button) view.findViewById(R.id.register_btn);

        registered_db = FirebaseDatabase.getInstance();
        registered_db_ref = registered_db.getReference("Registered_candidates_data");
        imagesReference = FirebaseStorage.getInstance().getReference("Candidate_profile_image");


        choosePosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item_selected = choosePosition.getSelectedItem().toString();
                if(item_selected.equals("Faculty Representative")){
                    chooseFaculty.setVisibility(View.VISIBLE);
                }else{
                    chooseFaculty.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                chooseFaculty.setVisibility(View.GONE);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item_selected = choosePosition.getSelectedItem().toString();
                if(item_selected.equals("Faculty Representative")){
                    registerFacultyRep();
                }else{
                    registerExec();
                }

            }
        });



        candidateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

    }

    private void registerExec(){
        String position = choosePosition.getSelectedItem().toString();
        String name = candidateName.getText().toString().trim();
        String reg = candidateRegNo.getText().toString().trim();
        String year = chooseYear.getSelectedItem().toString();
        if(TextUtils.isEmpty(name)){
            candidateName.setError("Candidate's name required!");
            return;
        }else if(TextUtils.isEmpty(reg)){
            candidateRegNo.setError("Registration required!");
            return;
        }else if(position.equals("---select position---")){
            Toast.makeText(getActivity(), "Please choose a position", Toast.LENGTH_SHORT)
                    .show();
        }else if(year.equals("---select year---")){
            Toast.makeText(getActivity(), "Please choose a position", Toast.LENGTH_SHORT)
                    .show();
        }else{

            uploadImage(uploadImageUri, position, reg, name, year);
//            candidateName.setText("");
//            candidateRegNo.setText("");
        }


    }

    private void registerFacultyRep(){
        String faculty = chooseFaculty.getSelectedItem().toString();
        String name = candidateName.getText().toString().trim();
        String reg = candidateRegNo.getText().toString().trim();
        String year = chooseYear.getSelectedItem().toString();
        if(TextUtils.isEmpty(name)){
            candidateName.setError("Candidate's name required!");
            return;
        }else if(TextUtils.isEmpty(reg)){
            candidateRegNo.setError("Registration required!");
            return;
        }else if(faculty.equals("---select faculty---")){
            Toast.makeText(getActivity(), "Please choose a faculty", Toast.LENGTH_SHORT)
                    .show();
        }else if(year.equals("---select year---")){
            Toast.makeText(getActivity(), "Please choose a position", Toast.LENGTH_SHORT)
                    .show();
        }else{
            String unique_regNo = "";
            if(reg.contains("/")){
                unique_regNo = reg.replace('/','_');
            }

            uploadImage(uploadImageUri, faculty, unique_regNo, name, year);
//            candidateName.setText("");
//            candidateRegNo.setText("");
        }

    }

    private void selectImage(){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK);

        File pictureDirectory = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String picturePath = pictureDirectory.getPath();

        Uri data = Uri.parse(picturePath);
        pickPhoto.setDataAndType(data, "image/*");

        startActivityForResult(pickPhoto, IMAGE_GALLERY_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_GALLERY_REQUEST){
                uploadImageUri = data.getData();

                InputStream inputStream;
                try {
                    inputStream = getContext().getContentResolver().openInputStream(uploadImageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    candidateProfile.setImageBitmap(bitmap);
                }catch (FileNotFoundException e){
                    Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }
    }

    private void uploadImage(Uri uri, final String candidatePosition, final String candidate_reg,
                             final String name, final String year){
        String unique_regNo = "";
        if(candidate_reg.contains("/")){
            unique_regNo = candidate_reg.replace('/','_');
        }
        final ProgressDialog p = new ProgressDialog(getActivity());
                p.setMessage("Uploading profile...");
                p.show();
        StorageReference profileRef = imagesReference.child(unique_regNo + ".jpg");

        final String finalUnique_regNo = unique_regNo;
        profileRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    String downloadUrl = task.getResult().getDownloadUrl().toString();
                    p.dismiss();

                    progressDialog.setMessage("Registering new candidate...");
                    progressDialog.show();
                    RegisteredCandidatesData registerNewCandidate = new RegisteredCandidatesData
                            (name, candidatePosition,downloadUrl, candidate_reg, year, 0);

                        registered_db_ref.child(candidatePosition)
                            .child(finalUnique_regNo)
                            .setValue(registerNewCandidate)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
//                                        createCandidateList(candidatePosition);
                                        addCandidateToPolls(candidatePosition, candidate_reg);
//                                        createVotingStatus();
                                Toast.makeText(getActivity(), "Candidate registered successfully", Toast.LENGTH_SHORT)
                                        .show();
                                    }
                                    progressDialog.dismiss();
                                }
                            });

                }
            }
        });
        progressDialog.dismiss();

    }


    private void addCandidateToPolls(String position, final String regNo){
        String candidateReg = "";
        if(regNo.contains("/")){
            candidateReg = regNo.replace('/','_');
        }
        DatabaseReference countsDbRef = FirebaseDatabase.getInstance()
                .getReference("Vote_counts").child(position);
        VoteCounts results = new VoteCounts("0");

        final String finalUnique_regNo = candidateReg;
        countsDbRef.child(candidateReg).setValue(results)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getActivity(), "Candidate Poll's ready" + finalUnique_regNo, Toast.LENGTH_SHORT)
                                    .show();
                        }else{
                            Toast.makeText(getActivity(), "Candidate Poll's NOT ready", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }
    private void createVotingStatus(){

        DatabaseReference votingStatusRef = FirebaseDatabase.getInstance()
                .getReference("StudentVoteStatus");

        VotingData votingData = new VotingData("");
        votingStatusRef.child("Presidential").setValue(votingData);
        votingStatusRef.child("Organizing_Sec").setValue(votingData);
        votingStatusRef.child("Academics").setValue(votingData);
        votingStatusRef.child("Treasury").setValue(votingData);
        votingStatusRef.child("Welfare").setValue(votingData);
        votingStatusRef.child("FIST").setValue(votingData);
        votingStatusRef.child("SOBE").setValue(votingData);
        votingStatusRef.child("SPASS").setValue(votingData);
        votingStatusRef.child("FASS").setValue(votingData);
        votingStatusRef.child("Education").setValue(votingData);
        votingStatusRef.child("Agriculture").setValue(votingData);
        votingStatusRef.child("Law").setValue(votingData);
    }
}
