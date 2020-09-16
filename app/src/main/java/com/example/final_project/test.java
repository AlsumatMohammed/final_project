package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;


public class test extends AppCompatActivity {

    private RecyclerView recyclerViewRatings;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapterRatings;
    String adKey;
    //Merlin merlin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

//


//        new LovelyInfoDialog(this)
//                .setTopColorRes(R.color.toolbar)
//                .setIcon(R.drawable.ic_verified_dialog)
//                .setNotShowAgainOptionChecked(true)
//                .setTitle("Unverified Account")
//                .setMessage("Your account hasn't been verified.\nFor speeding up the verification process,\ncontact y-parts@gmail.com")
//                .show();



//
//
//        }
//
//        else{
//
//            Toast.makeText(this, "valid ", Toast.LENGTH_SHORT).show();
//        }



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

                            return;
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

//    public boolean isConnected() {
//        boolean connected = false;
//        try {
//            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo nInfo = cm.getActiveNetworkInfo();
//            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
//            return connected;
//        } catch (Exception e) {
//            Log.e("Connectivity Exception", e.getMessage());
//        }
//
//
//            new LovelyStandardDialog(this, LovelyStandardDialog.ButtonLayout.VERTICAL)
//                    .setTopColorRes(R.color.toolbar)
//                    .setButtonsColorRes(R.color.toolbar)
//                    .setIcon(R.drawable.ic_verified_dialog)
//                    .setTitle("No network connection")
//                    .setMessage("There's seems to be a problem with your internet connection")
//                    .setPositiveButton("Retry", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            isConnected();
//
//                        }
//                    })
//                    .setNegativeButton("Exit", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//
//                        }
//                    })
//                    .show();
//
//
//
//
//        return connected;
//    }




    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();


    }

    @Override
    protected void onResume() {
        super.onResume();
        //merlin.bind();
    }

    @Override
    protected void onPause() {
        //merlin.unbind();
        super.onPause();
    }
}
