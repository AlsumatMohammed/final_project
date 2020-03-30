package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class getting_you extends AppCompatActivity {


    ImageView loading_animation;
    TextView get_you;
    Animation top, bottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getting_you);

        loading_animation = findViewById(R.id.getting_you_loading);
        get_you = findViewById(R.id.getting_you_tv);

        top = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        loading_animation.setAnimation(top);
        get_you.setAnimation(bottom);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                Intent intent = new Intent(getting_you.this, edit_profile.class);
                startActivity(intent);
                finish();



            }
        }, 10000);
    }
}
