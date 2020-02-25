package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
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

public class splashcreen extends AppCompatActivity {

    Animation top, bottom;
    ImageView logo;
    TextView tv1, tv2;

    FirebaseAuth mAuth;
    public static int SPLASHTIME = 5000;
    public FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashcreen);

        logo = findViewById(R.id.y_parts_logo_splash);

        tv1 = findViewById(R.id.y_parts_tv_splash);
        tv2  = findViewById(R.id.y_parts_tv_splash_add);

        top = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);


        logo.setAnimation(top);
        tv1.setAnimation(bottom);
        tv2.setAnimation(bottom);

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
                final Intent intent_login = new Intent(splashcreen.this, login_screen.class);
                final Intent intent_home = new Intent (splashcreen.this, MainActivity.class);

                Pair[] pairs = new Pair[2];
                pairs [0] = new Pair<View, String>(logo, "logo_image");
                pairs [1] = new Pair<View, String>(tv1, "logo_text");

                final ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(splashcreen.this, pairs);


                 firebaseUser = mAuth.getCurrentUser();

                if (firebaseUser == null){

                    Toast.makeText(splashcreen.this, "null", Toast.LENGTH_SHORT).show();
                    startActivity(intent_login, options.toBundle());
                    finish();
                }

                if (firebaseUser != null){

                    Toast.makeText(splashcreen.this, "not null", Toast.LENGTH_SHORT).show();

                    startActivity(intent_home);
                    finish();
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
}
