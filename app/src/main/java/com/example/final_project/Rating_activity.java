package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import cn.pedant.SweetAlert.SweetAlertDialog;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Rating_activity extends AppCompatActivity {

    CircularProgressButton submitRating;
    TextInputLayout commentLayout;
    TextInputEditText commentEdittext;
    RatingBar ratingBar;

    SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_activity);

        pDialog = new SweetAlertDialog(Rating_activity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setCancelable(false);

        submitRating = findViewById(R.id.submitRating_button);
        commentLayout = findViewById(R.id.rating_comment_layout);
        commentEdittext = findViewById(R.id.rating_comment_et);
        ratingBar = findViewById(R.id.ratingBar);



        submitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float rating = ratingBar.getRating();

                Toast.makeText(Rating_activity.this, "Rating is "+rating, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void showDialog(String message){

        pDialog.setTitleText(message+" ...");
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
