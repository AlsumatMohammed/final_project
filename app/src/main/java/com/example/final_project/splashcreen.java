package com.example.final_project;

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

public class splashcreen extends AppCompatActivity {

    Animation top, bottom;
    ImageView logo;
    TextView tv1, tv2;

    public static int SPLASHTIME = 5000;

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


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splashcreen.this, login_screen.class);

                Pair[] pairs = new Pair[2];
                pairs [0] = new Pair<View, String>(logo, "logo_image");
                pairs [1] = new Pair<View, String>(tv1, "logo_text");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(splashcreen.this, pairs);

                startActivity(intent, options.toBundle());
                finish();

            }
        }, SPLASHTIME);
    }
}
