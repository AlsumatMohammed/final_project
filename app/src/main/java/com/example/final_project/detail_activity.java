package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import cn.pedant.SweetAlert.SweetAlertDialog;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.List;

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


    RecyclerView.LayoutManager RecyclerViewLayoutManager;


    ImageView productImageView, publisherImageView;
    TextView publisherUserNameViewPrevious, descriptionView, categoryView, publishDateView, priceView, warrantyView, conditionView, publisherUserNameView, publisherPhoneView, publisherEmailView;

    private RecyclerView recyclerViewRatings;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapterRatings;



    Button favouriteButton, callNowButton, messageNowButton;
    CircularProgressButton addRating;

    public RatingBar supplierRatingBar;

    private FirebaseAuth firebaseAuth;


    String publisherUserName;
    String publisherEmail;
    String adKey;
    String adtitle;
    String productImage;
    String description ;
    String category;
    String publishDate;
    String price;
    String currency;
    String priceType;
    String warranty;
    String condition;
    //PUBLISHERINFORMATION
    String publisherImage;
    String publisherPhone;

    boolean checkAd = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_activity);

        firebaseAuth = FirebaseAuth.getInstance();





        supplierRatingBar = findViewById(R.id.ratingBar_detail);
        callNowButton = findViewById(R.id.callNow_button);
        messageNowButton = findViewById(R.id.messageNow_button);

        favouriteButton = findViewById(R.id.favouriteButton);
        addRating = findViewById(R.id.addRatingButton);
        pDialog = new SweetAlertDialog(detail_activity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
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

        productImage = intent.getExtras().getString("PRODUCT_IMAGE");
        description = intent.getExtras().getString("DESCRIPTION");
        category = intent.getExtras().getString("CATEGORY");
        publishDate = intent.getExtras().getString("PUBLISHDATE");
        price = intent.getExtras().getString("PRICE");
        currency = intent.getExtras().getString("CURRENCY");
        priceType = intent.getExtras().getString("PRICETYPE");
        warranty = intent.getExtras().getString("WARRANTY");
        condition = intent.getExtras().getString("CONDITION");
        //PUBLISHERINFORMATION
        publisherImage = intent.getExtras().getString("PUBLISHERIMAGE");
        publisherUserName = intent.getExtras().getString("PUBLISHERUSERNAME");
        publisherEmail = intent.getExtras().getString("PUBLSIHEREMAIL");
        publisherPhone = intent.getExtras().getString("PUBLISHERPHONE");
        adKey = intent.getExtras().getString("ADKEY");
        adtitle = intent.getExtras().getString("ADTITLE");

        Uri productImagePath = Uri.parse(productImage);
        Uri publisherImagePath = Uri.parse(publisherImage);

        publisherUserNameViewPrevious.setText(publisherUserName);

        addRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent1 = new Intent(detail_activity.this, Rating_activity.class);

                intent1.putExtra("key", adKey);
                intent1.putExtra("PUBLISHEREMAIL", publisherEmail);
                startActivity(intent1);

            }
        });

        callNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                //PHONE INTENT
                Uri number = Uri.parse("tel:"+publisherPhone);
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);

                PackageManager packageManager = getPackageManager();
                List<ResolveInfo> activities = packageManager.queryIntentActivities(callIntent, 0);
                boolean isIntentSafe = activities.size() > 0;

                //PHONE END

                if (isIntentSafe){
                    startActivity(callIntent);
                }

            }
        });



        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ad_placeholder)
                .error(R.drawable.ad_error);


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

        getSupplierRating();

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

        recyclerViewRatings = findViewById(R.id.ratingsRecyclerview);
        linearLayoutManager = new LinearLayoutManager(detail_activity.this);
        recyclerViewRatings.setLayoutManager(linearLayoutManager);
        fetchRatings();

        addedAd();

        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkAd){
                    Alerter.create(detail_activity.this)
                            .setTitle("Y-parts")
                            .setText("This is already in favourites!")
                            .enableSwipeToDismiss()
                            .setDuration(3000)
                            .setBackgroundColorRes(R.color.text_color_orange)
                            .show();
                    return;

                }



                showDialog("Adding to favourites");

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference();

                DatabaseReference favouriteAdsReference = databaseReference.child("favouritesAds").child(firebaseAuth.getUid()).push();

                generalAds generalAd = new generalAds();

                generalAd.setPublisherEmail(publisherEmail);
                generalAd.setPublisherphoneNumber(publisherPhone);
                generalAd.setPublisherUsername(publisherUserName);
                generalAd.setTitle(adtitle);
                generalAd.setCategory(category);
                generalAd.setCondition(condition);
                generalAd.setWarranty(warranty);
                generalAd.setDescription(description);
                generalAd.setPriceType(priceType);
                generalAd.setPrice(price);
                generalAd.setCurrency(currency);
                generalAd.setPublishDate(publishDate);
                generalAd.setProductImage(productImage);
                generalAd.setPublisherImage(publisherImage);
                generalAd.setKey(adKey);
                favouriteAdsReference.setValue(generalAd);


                dismissDialog();
                Toast.makeText(detail_activity.this, "added ", Toast.LENGTH_SHORT).show();
                new SweetAlertDialog(detail_activity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Awesome!")
                        .setContentText("Added to favourites!")
                        .show();
                favouriteButton.setBackground(getResources().getDrawable(R.drawable.star));

            }
        });

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
                .child("GeneralAds").orderByChild("publisherEmail").equalTo(publisherEmail);


        FirebaseRecyclerOptions<generalAds> options =
                new FirebaseRecyclerOptions.Builder<generalAds>()
                        .setQuery(query, new SnapshotParser<generalAds>() {
                            @NonNull
                            @Override
                            public generalAds parseSnapshot(@NonNull DataSnapshot snapshot) {
//
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
                        .placeholder(R.drawable.ad_placeholder)
                        .error(R.drawable.ad_error);

                Glide.with(holder.productimage.getContext())
                        .load(productImage)
                        .apply(options).override(200, 200).into(holder.productimage);




                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        openDetailActivity(model.getProductImage() , model.getDescription(), model.getCategory()
                                , model.getPublishDate() , model.getPrice() , model.getCurrency() , model.getPriceType()
                                , model.getWarranty() , model.getCondition() , model.getPublisherImage()
                                , model.getPublisherUsername() , model.getPublisherEmail(), model.getPublisherphoneNumber(), model.getKey(), model.getTitle());

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
        intent.putExtra("ADKEY" , detail[13]);
        intent.putExtra("ADTITLE", detail[14]);


        detail_activity.this.startActivity(intent);



    }




    public class ratingsViewHolder extends RecyclerView.ViewHolder {

        public TextView commenterName, comment, date;
        public ImageView commenterImage;
        public CardView cardView;
        public RatingBar commentRatingBar;


        public ratingsViewHolder(@NonNull View itemView) {
            super(itemView);

            commenterName = itemView.findViewById(R.id.commentername);
            comment  = itemView.findViewById(R.id.comment_tv);
            date = itemView.findViewById(R.id.ratingDate);
            commenterImage = itemView.findViewById(R.id.commenterImage);
            cardView = itemView.findViewById(R.id.constraint);
            commentRatingBar = itemView.findViewById(R.id.ratignBar_comment);





        }


    }



    private void fetchRatings() {


        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("comments").orderByChild("adkey").equalTo(adKey);


        FirebaseRecyclerOptions<comment> options =
                new FirebaseRecyclerOptions.Builder<comment>()
                        .setQuery(query, new SnapshotParser<comment>() {
                            @NonNull
                            @Override
                            public comment parseSnapshot(@NonNull DataSnapshot snapshot) {
//


                                comment comment = snapshot.getValue(comment.class);


                                return comment;
                            }
                        }).build();


        //adapter settings



        adapterRatings = new FirebaseRecyclerAdapter<comment, ratingsViewHolder>(options) {


            @NonNull
            @Override
            public ratingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.rating_layout, parent, false);

                return new ratingsViewHolder(itemview);

            }

            @Override
            protected void onBindViewHolder(@NonNull final ratingsViewHolder holder, int position, @NonNull final comment model) {

                holder.commenterImage.setAnimation(AnimationUtils.loadAnimation(detail_activity.this, R.anim.rv_fade_transition));
                holder.cardView.setAnimation(AnimationUtils.loadAnimation(detail_activity.this, R.anim.rv_scale_animation));


                holder.commenterName.setText(model.getUsername());
                holder.comment.setText(model.getComment());
                holder.date.setText(model.getDate());
                holder.commentRatingBar.setRating(model.getRating());

                Toast.makeText(detail_activity.this, model.getAdkey(), Toast.LENGTH_SHORT).show();
                Uri commenterImage = Uri.parse(model.getCommenterImage());



                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.ad_placeholder)
                        .error(R.drawable.ad_error);

                Glide.with(holder.commenterImage.getContext())
                        .load(commenterImage)
                        .apply(options).override(60, 60).into(holder.commenterImage);



            }
        };



        recyclerViewRatings.setAdapter(adapterRatings);


    }

    private void getSupplierRating(){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        databaseReference.child("comments").orderByChild("publisherEmail").equalTo(publisherEmail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                float total = 0;
                float count = 0;

                for (DataSnapshot child: dataSnapshot.getChildren() ){

                    comment comment = child.getValue(comment.class);

                    total = total+comment.getRating();
                    count = count+1;


                }


                float finalRating = total/count;
                supplierRatingBar.setRating(finalRating);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addedAd(){

        showDialog("Loading");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        DatabaseReference favouriteAdsReference = databaseReference.child("favouritesAds").child(firebaseAuth.getUid());

        favouriteAdsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot data: dataSnapshot.getChildren()){

                    generalAds generalAd = data.getValue(generalAds.class);

                    if (generalAd.getKey().equals(adKey)){

                        checkAd = true;
                        favouriteButton.setBackground(getResources().getDrawable(R.drawable.star));
                        break;
                    }

                }

                dismissDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }





    @Override
    protected void onStart() {
        super.onStart();

        adapter.startListening();
        adapterRatings.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        adapter.stopListening();
        adapterRatings.stopListening();
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
}













