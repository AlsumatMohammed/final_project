package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.pedant.SweetAlert.SweetAlertDialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.ArrayList;

public class detail_activity extends AppCompatActivity {

    //    public FirebaseAuth firebaseAuth;
//    public FirebaseUser firebaseUser;
//    DatabaseReference db;
//    FirebaseAdsHelper helper;
//    GenericAdsAdapter adapter;
//    RecyclerView rv;
    SweetAlertDialog pDialog;

    ImageView productImageView, publisherImageView;
    TextView descriptionView, categoryView, publishDateView, priceView, warrantyView, conditionView, publisherUserNameView, publisherPhoneView, publisherEmailView;


    ArrayList<generalAds> generalAdsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_activity);


        pDialog = new SweetAlertDialog(detail_activity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading ...");
        pDialog.setCancelable(false);

        productImageView = findViewById(R.id.productImage);
        publisherImageView = findViewById(R.id.publisherImage_detail);
        descriptionView = findViewById(R.id.description_detail);
        categoryView = findViewById(R.id.cateogryDetail);
        publishDateView = findViewById(R.id.date_detail);
        priceView = findViewById(R.id.price_detail);
        warrantyView = findViewById(R.id.warranty_detail);
        conditionView = findViewById(R.id.condition_detail);
        publisherUserNameView = findViewById(R.id.publisherUsername_detail);
        publisherPhoneView = findViewById(R.id.publisherPhone_detail);
        publisherEmailView = findViewById(R.id.publisherEmail_detail);

        Intent intent = this.getIntent();

        String productImage = intent.getExtras().getString("PRODUCT_IMAGE");
        String description = intent.getExtras().getString("DESCRIPTION");
        String category = intent.getExtras().getString("CATEGORY");
        String publishDate = intent.getExtras().getString("PUBLISHDATE");
        String price = intent.getExtras().getString("PRICE");
        String currency = intent.getExtras().getString("CURRENCY");
        String priceType = intent.getExtras().getString("PRICETYPE");
        String warranty = intent.getExtras().getString("WARRANTY");
        String condition = intent.getExtras().getString("CONDITION");
        //PUBLISHERINFORMATION
        String publisherImage = intent.getExtras().getString("PUBLISHERIMAGE");
        String publisherUserName = intent.getExtras().getString("PUBLISHERUSERNAME");
        String publisherEmail = intent.getExtras().getString("PUBLSIHEREMAIL");
        String publisherPhone = intent.getExtras().getString("PUBLISHERPHONE");

        Uri productImagePath = Uri.parse(productImage);
        Uri publisherImagePath = Uri.parse(publisherImage);




        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.drawable.ads_icon);


        Glide.with(getApplicationContext())
                .load(productImage)
                .apply(options).into(productImageView);

        Glide.with(getApplicationContext())
                .load(publisherImage)
                .apply(options).override(90, 90).into(publisherImageView);


        if (currency.equals("YER")){
            priceView.setText(price+" "+currency+" "+"("+priceType+")");
        }
        else if (currency.equals("$")){
            priceView.setText(currency+" "+price+"("+priceType+")");
        }


        if (price.isEmpty()){
            priceView.setText("FREE");
        }

        descriptionView.setText(description);
        categoryView.setText(category);
        publishDateView.setText(publishDate);

        warrantyView.setText(warranty);
        conditionView.setText(condition);
        //PUBLISHER

        publisherUserNameView.setText(publisherUserName);
        publisherEmailView.setText(publisherEmail);
        publisherPhoneView.setText(publisherPhone);

    }



    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

}













