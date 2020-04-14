package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import cn.pedant.SweetAlert.SweetAlertDialog;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Rating_activity extends AppCompatActivity {

    CircularProgressButton submitRating;
    TextInputLayout commentLayout;
    TextInputEditText commentEdittext;
    RatingBar ratingBar;
    public FirebaseUser firebaseUser;
    private FirebaseStorage firebaseStorage;
    String date;
    String adKey;
    String commenterImage = "";
    String commenterUserName = "";
    SweetAlertDialog pDialog;
    public FirebaseAuth firebaseAuth;

    comment comment;
    boolean userNameCheck = false, imageCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_activity);


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();


        pDialog = new SweetAlertDialog(Rating_activity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setCancelable(false);

        submitRating = findViewById(R.id.submitRating_button);
        commentLayout = findViewById(R.id.rating_comment_layout);
        commentEdittext = findViewById(R.id.rating_comment_et);
        ratingBar = findViewById(R.id.ratingBar);

        Intent intent = this.getIntent();


        adKey = intent.getExtras().getString("key");



        submitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                float rating = ratingBar.getRating();

                comment = new comment();

                if (!isValidComment()){
                    return;
                }



                showDialog("Submitting Rating...");

                date = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault()).format(new Date());
                Toast.makeText(Rating_activity.this, "Rating is "+rating+" "+adKey, Toast.LENGTH_SHORT).show();

                String commentText = commentEdittext.getText().toString().trim();

                comment.setComment(commentText);
                comment.setDate(date);
                comment.setRating(rating);
                comment.setCommenterImage(" ");
                comment.setUsername(" ");
                comment.setAdkey(adKey);

                DatabaseReference databaseReference1 = databaseReference.child("comments").push();

                String key = databaseReference1.getKey();
                comment.setKey(key);
                databaseReference1.setValue(comment);
                getPublisherInformation();
                getPublisherImage();




            }
        });

    }

    public void getPublisherInformation(){

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        databaseReference.child("userInfromation").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Userinformation userProfile = dataSnapshot.getValue(Userinformation.class);

                commenterUserName = userProfile.getUserName();

                DatabaseReference databaseReference1 = firebaseDatabase.getReference();
                databaseReference1.child("comments").child(comment.getKey()).child("username").setValue(commenterUserName);
                userNameCheck = true;
                Toast.makeText(Rating_activity.this, "publisher Information acquired"+commenterUserName, Toast.LENGTH_SHORT).show();
                check(userNameCheck, imageCheck);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                userNameCheck = false;
                Toast.makeText(Rating_activity.this, "publisher Infromation failed", Toast.LENGTH_SHORT).show();
                check(userNameCheck, imageCheck);
            }
        });


    }





    public void getPublisherImage() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        StorageReference publisher_reference = firebaseStorage.getReference(firebaseAuth.getUid()).child("images").child("Profile Pic");

        publisher_reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                commenterImage = uri.toString();

                databaseReference.child("comments").child(comment.getKey()).child("commenterImage").setValue(commenterImage);

                imageCheck = true;
                Toast.makeText(Rating_activity.this, "commenterImage acquired", Toast.LENGTH_SHORT).show();

                check(userNameCheck, imageCheck);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                imageCheck = false;
                Toast.makeText(Rating_activity.this, "commenterImage not acquired", Toast.LENGTH_SHORT).show();

                check(userNameCheck, imageCheck);
            }
        });
    }


    public void check(boolean username, boolean image){

        if (username && image){

            dismissDialog();
            Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
            dismissDialog();
            new SweetAlertDialog(Rating_activity.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Awesome!")
                    .setContentText("Review added!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            Intent intent = new Intent(Rating_activity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .show();
        }


        else{
            Toast.makeText(this, "not yet", Toast.LENGTH_SHORT).show();
        }

    }


    public boolean isValidComment() {

        String comment = commentEdittext.getText().toString().trim();
        if (comment.isEmpty()) {

            commentLayout.setError("You must add a comment");
            return false;

        }

        else {


            commentLayout.setErrorEnabled(false);
        }

        return true;
    }

    public void showDialog(String message){

        pDialog.setTitleText(message);
        pDialog.show();

        //editprofileButton.startAnimation();

    }

    public void dismissDialog(){
        pDialog.dismiss();

        //editprofileButton.revertAnimation();
    }

    @Override
    protected void onStart() {
        super.onStart();

//        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        //adapter.stopListening();
    }

}
