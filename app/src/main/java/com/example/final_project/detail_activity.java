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
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

    private RecyclerView recyclerView;
    private LinearLayoutManager HorizontalLayout;
    private FirebaseRecyclerAdapter adapter;

    String publisherUserName;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;


    ImageView productImageView, publisherImageView;
    TextView publisherUserNameViewPrevious, descriptionView, categoryView, publishDateView, priceView, warrantyView, conditionView, publisherUserNameView, publisherPhoneView, publisherEmailView;



    ArrayList<generalAds> generalAdsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_activity);


        pDialog = new SweetAlertDialog(detail_activity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading ...");
        pDialog.setCancelable(false);
        publisherUserNameViewPrevious = findViewById(R.id.publisherName_previous);
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
        publisherUserName = intent.getExtras().getString("PUBLISHERUSERNAME");
        String publisherEmail = intent.getExtras().getString("PUBLSIHEREMAIL");
        String publisherPhone = intent.getExtras().getString("PUBLISHERPHONE");

        Uri productImagePath = Uri.parse(productImage);
        Uri publisherImagePath = Uri.parse(publisherImage);

        publisherUserNameViewPrevious.setText(publisherUserName);



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


        recyclerView = findViewById(R.id.previousAdsRecyclerview);
        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(RecyclerViewLayoutManager);

        HorizontalLayout = new LinearLayoutManager(detail_activity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(HorizontalLayout);

        fetch();
    }

    public class detailViewHolder extends RecyclerView.ViewHolder {

        public TextView title, price;
        public ImageView productimage;
        public CardView cardView;


        public detailViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title_ad_previous);
            price = itemView.findViewById(R.id.ad_price_previous);
            productimage = itemView.findViewById(R.id.productImage_previous);
            cardView = itemView.findViewById(R.id.previous_card);



        }


    }



    private void fetch() {


        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("GeneralAds").orderByChild("publisherUsername").equalTo(publisherUserName);


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



        adapter = new FirebaseRecyclerAdapter<generalAds, detailViewHolder>(options) {


            @NonNull
            @Override
            public detailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.previousads_layout, parent, false);

                return new detailViewHolder(itemview);

            }

            @Override
            protected void onBindViewHolder(@NonNull final detailViewHolder holder, int position, @NonNull final generalAds model) {

                holder.cardView.setAnimation(AnimationUtils.loadAnimation(detail_activity.this, R.anim.rv_fade_transition));
                //holder.cardView.setAnimation(AnimationUtils.loadAnimation(detail_activity.this, R.anim.rv_scale_animation));

                String currency = model.getCurrency();
                String price = model.getPrice();


                if (currency.equals("YER")){
                    holder.price.setText(model.getPrice()+" "+currency);
                }
                else if (currency.equals("$")){
                    holder.price.setText(currency+" "+model.getPrice());
                }


                if (price.isEmpty()){
                    holder.price.setText("FREE");
                }
//
                holder.title.setText(model.getTitle());
                Uri productImage = Uri.parse(model.getProductImage());



                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.avatar)
                        .error(R.drawable.ads_icon);

                Glide.with(holder.productimage.getContext())
                        .load(productImage)
                        .apply(options).override(200, 200).into(holder.productimage);




                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        openDetailActivity(model.getProductImage() , model.getDescription(), model.getCategory()
                                , model.getPublishDate() , model.getPrice() , model.getCurrency() , model.getPriceType()
                                , model.getWarranty() , model.getCondition() , model.getPublisherImage()
                                , model.getPublisherUsername() , model.getPublisherEmail(), model.getPublisherphoneNumber());

                    }
                });
            }
        };


        recyclerView.setAdapter(adapter);


    }

    public void openDetailActivity(String... detail){

        Intent intent = new Intent(detail_activity.this, detail_activity.class);

        intent.putExtra("PRODUCT_IMAGE" , detail[0]);
        intent.putExtra("DESCRIPTION" , detail[1]);
        intent.putExtra("CATEGORY" , detail[2]);
        intent.putExtra("PUBLISHDATE" , detail[3]);
        intent.putExtra("PRICE" , detail[4]);
        intent.putExtra("CURRENCY" , detail[5]);
        intent.putExtra("PRICETYPE" , detail[6]);
        intent.putExtra("WARRANTY" , detail[7]);
        intent.putExtra("CONDITION" , detail[8]);

        //PUBLISHER
        intent.putExtra("PUBLISHERIMAGE" , detail[9]);
        intent.putExtra("PUBLISHERUSERNAME" , detail[10]);
        intent.putExtra("PUBLSIHEREMAIL" , detail[11]);
        intent.putExtra("PUBLISHERPHONE" , detail[12]);


        detail_activity.this.startActivity(intent);



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

    public void showDialog(){
        pDialog.show();

        //editprofileButton.startAnimation();

    }

    public void dismissDialog(){
        pDialog.dismiss();

        //editprofileButton.revertAnimation();
    }
}













