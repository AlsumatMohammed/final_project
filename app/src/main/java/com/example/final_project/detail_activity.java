package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.pedant.SweetAlert.SweetAlertDialog;

import android.content.Context;
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

    //firebase rec
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;

    ArrayList<generalAds> generalAdsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_activity);


        pDialog = new SweetAlertDialog(detail_activity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading ...");
        pDialog.setCancelable(false);

////        firebaseDatabase = FirebaseDatabase.getInstance();
////        databaseReference = FirebaseDatabase.getInstance().getReference();
//
//        generalAdsArrayList = new ArrayList<generalAds>();
//        rv = (RecyclerView) findViewById(R.id.recycler_view2);
//        rv.setLayoutManager(new LinearLayoutManager(this));
//
//        db= FirebaseDatabase.getInstance().getReference();
//        helper=new FirebaseAdsHelper(db);
//
//        //atabaseReference databaseReference = firebaseDatabase.getReference();
//        adapter=new GenericAdsAdapter(helper.retrieve(),this);
//        rv.setAdapter(adapter);

        recyclerView = findViewById(R.id.recycler_view2);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        fetch();

        //afsdddddddddddddddddddddddddddddddddddddddddd


    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, price;
        public CardView cardView;
        public ImageView productimage, publisherImage;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title_ad);
            price = itemView.findViewById(R.id.price);
            //productimage = itemView.findViewById(R.id.prodcutImage);
            publisherImage = itemView.findViewById(R.id.publisherImage);
        }

        public void setTxtTitle(String string) {
            title.setText(string);
        }


        public void setTxtPrice(String string) {
            price.setText(string);
        }

    }



        private void fetch() {
            Query query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("GeneralAds");


            FirebaseRecyclerOptions<generalAds> options =
                    new FirebaseRecyclerOptions.Builder<generalAds>()
                            .setQuery(query, new SnapshotParser<generalAds>() {
                                @NonNull
                                @Override
                                public generalAds parseSnapshot(@NonNull DataSnapshot snapshot) {
//                                    return new generalAds(snapshot.child("price").getValue().toString(),
//                                            snapshot.child("productImage").getValue().toString(),
//                                            snapshot.child("publisherImage").getValue().toString(),
//                                            snapshot.child("title").getValue().toString());

                                    generalAds generalAd = snapshot.getValue(generalAds.class);

                                    return generalAd;
                                }
                            }).build();


            //adapter settings



            adapter = new FirebaseRecyclerAdapter<generalAds, MyViewHolder>(options) {

                @NonNull
                @Override
                public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                    View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.generic_ads_layout, parent, false);

                    return new MyViewHolder(itemview);
                }

                @Override
                protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull generalAds model) {

                    holder.setTxtPrice(model.getPrice());
                    holder.setTxtTitle(model.getTitle());

                    Uri productImage = Uri.parse(model.getProductImage());
                    Uri publisherImage = Uri.parse(model.getPublisherImage());


                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.mipmap.ic_launcher_round)
                            .error(R.drawable.ads_icon);

                    Glide.with(holder.productimage.getContext())
                            .load(model.getProductImage())
                            .apply(options).override(400, 400).into(holder.productimage);

                    Glide.with(holder.publisherImage.getContext())
                            .load(model.getPublisherImage())
                            .apply(options).override(400, 400).into(holder.publisherImage);


                }
            };


            recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }






}








