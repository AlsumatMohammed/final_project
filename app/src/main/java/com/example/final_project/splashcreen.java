package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

public class splashcreen extends AppCompatActivity {

    Animation top, bottom;
    ImageView logo, loading, shape;
    TextView tv1, tv2, tv3_wait;
    AnimationDrawable anim;

    FirebaseAuth mAuth;
    public static int SPLASHTIME = 7000;
    public FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashcreen);


        logo = findViewById(R.id.y_parts_logo_splash);
        //shape = findViewById(R.id.shape_splash);

        //loading = findViewById(R.id.loading);
        //loading.setBackgroundResource(R.drawable.animation_loading);
        //anim = (AnimationDrawable) loading.getBackground();

        tv1 = findViewById(R.id.y_parts_tv_splash);
        tv2  = findViewById(R.id.y_parts_tv_splash_add);
        //tv3_wait = findViewById(R.id.internet_check_tv);

        top = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //shape.setAnimation(top);
        logo.setAnimation(top);
        tv1.setAnimation(bottom);
        tv2.setAnimation(bottom);
        //loading.setAnimation(bottom);

//        tv3_wait.setVisibility(View.VISIBLE);
//        tv3_wait.setAnimation(bottom);

        //loading.setAnimation(bottom);

        //anim.start();


        mAuth = FirebaseAuth.getInstance();

//        FirebaseUser firebaseUser = mAuth.getCurrentUser();
//
//        if (firebaseUser !=null){
//
//        }
//
//        else if (firebaseUser == null){
//
//            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
//        }




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent intent_welcome = new Intent(splashcreen.this, activity_welcome.class);
                final Intent intent_home = new Intent (splashcreen.this, MainActivity.class);

//                Pair[] pairs = new Pair[2];
//                pairs [0] = new Pair<View, String>(logo, "logo_image");
//                pairs [1] = new Pair<View, String>(tv1, "logo_text");
//                //pairs [1] = new Pair<View, String>(shape, "shape_image");
//
//                final ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(splashcreen.this, pairs);

                //checking Internet connection


                firebaseUser = mAuth.getCurrentUser();

                if (firebaseUser == null){

                    Toast.makeText(splashcreen.this, "null", Toast.LENGTH_SHORT).show();
                    startActivity(intent_welcome);
                    finish();
                }

                if (firebaseUser != null){

//                    Toast.makeText(splashcreen.this, "not null", Toast.LENGTH_SHORT).show();
//
//                    startActivity(intent_home);
//                    finish();
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference();

                    databaseReference.child("userInformation").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (!dataSnapshot.exists()) {

                                Intent intent = new Intent(splashcreen.this, edit_profile.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(splashcreen.this, "not null", Toast.LENGTH_SHORT).show();
//
                                startActivity(intent_home);
                                finish();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

//                FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
//                    @Override
//                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//
//
//
//                    }
//                };




            }
        }, SPLASHTIME);
    }

    public boolean checkInternet(){

        ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){

            new LovelyStandardDialog(this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                    .setTopColorRes(R.color.toolbar)
                    .setButtonsColorRes(R.color.toolbar)
                    .setIcon(R.drawable.ic_verified_dialog)
                    .setTitle("No network connection")
                    .setMessage("There's seems to be a problem with your internet connection")
                    .setPositiveButton("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setNegativeButton("Exit", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    })
                    .show();

            return false;
        }
        else{

        }
        return true;
    }
}
