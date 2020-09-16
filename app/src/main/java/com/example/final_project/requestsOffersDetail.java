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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class requestsOffersDetail extends AppCompatActivity {

    ImageView productImageView, publisherImageView;
    TextView publisherUserNameView, descriptionView, categoryView, publishDateView, priceView, warrantyView, conditionView, publisherPhoneView, publisherEmailView;


    String publisherUserName;
    String publisherEmail;
    String requestKey;
    String productImage;
    String description ;
    String category;
    String publishDate;
    String warranty;
    String condition;
    //PUBLISHERINFORMATION
    String publisherImage;
    String publisherPhone;
    String publisherLatitude;
    String publisherLongitude;
    SweetAlertDialog pDialog;
    Button messageNowButton, callNowButton;
    CircularProgressButton putOfferButton, putCommentButton;
    TextView noOffersYet;

    private RecyclerView recyclerViewLatestOffers;
    private LinearLayoutManager linearLayoutManagerLatestOffers;
    private FirebaseRecyclerAdapter adapterLatestOffers;

    private RecyclerView recyclerViewComments;
    private LinearLayoutManager linearLayoutManagerComments;
    private FirebaseRecyclerAdapter adapterComments;

    boolean userNameCheck = false, imageCheck = false;

    offerComments offerComment;

    private BottomSheetDialog dialog;

    ImageView getDirectionButtonDetailRequests;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_offers_detail);


        callNowButton = findViewById(R.id.callNowButtonOffersDetail);
        messageNowButton = findViewById(R.id.messageNowButtonOffersDetail);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        getPublisherInformation();
        descriptionView = findViewById(R.id.descriptionDetailRequestsOffers);
        categoryView = findViewById(R.id.categoryDetailRequestsOffers);
        publishDateView = findViewById(R.id.dateDetailRequestsOffers);
        warrantyView = findViewById(R.id.warrantyDetailRequestsOffers);
        conditionView = findViewById(R.id.conditionDetailRequestsOffers);
        putOfferButton = findViewById(R.id.putOffersButton);
        publisherImageView = findViewById(R.id.publisherImageDetailOffers);
        publisherPhoneView = findViewById(R.id.publisherPhoneDetailOffers);
        publisherEmailView = findViewById(R.id.publisherEmailDetailOffers);
        productImageView = findViewById(R.id.productImageRequestOffers);
        publisherImageView = findViewById(R.id.publisherImageDetailOffers);
        publisherUserNameView = findViewById(R.id.publisherUsernameDetailOffers);

        noOffersYet = findViewById(R.id.nolatestOffersDetailTv);
        putCommentButton = findViewById(R.id.addCommentButton);

        getDirectionButtonDetailRequests = findViewById(R.id.getDirectionButtonDetailRequests);


        pDialog = new SweetAlertDialog(requestsOffersDetail.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setCancelable(false);


        Intent intent = this.getIntent();

        productImage = intent.getExtras().getString("PRODUCT_IMAGE");
        description = intent.getExtras().getString("DESCRIPTION");
        category = intent.getExtras().getString("CATEGORY");
        publishDate = intent.getExtras().getString("PUBLISHDATE");
        warranty = intent.getExtras().getString("WARRANTY");
        condition = intent.getExtras().getString("CONDITION");
        //PUBLISHERINFORMATION
        publisherImage = intent.getExtras().getString("PUBLISHERIMAGE");
        publisherUserName = intent.getExtras().getString("PUBLISHERUSERNAME");
        publisherEmail = intent.getExtras().getString("PUBLSIHEREMAIL");
        publisherPhone = intent.getExtras().getString("PUBLISHERPHONE");
        requestKey = intent.getExtras().getString("REQUESTKEY");
        publisherLatitude = intent.getExtras().getString("PUBLISHERLATITUDE");
        publisherLongitude = intent.getExtras().getString("PUBLISHERLONGITUDE");


        descriptionView.setText(description);
        categoryView.setText(category);
        publishDateView.setText(publishDate);
        warrantyView.setText(warranty);
        conditionView.setText(condition);
        publisherUserNameView.setText(publisherUserName);
        publisherEmailView.setText(publisherEmail);
        publisherPhoneView.setText(publisherPhone);

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

        getDirectionButtonDetailRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse("geo:"+publisherLatitude+","+publisherLongitude);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, (uri));
                mapIntent.setPackage("com.google.android.apps.maps");


                if (mapIntent.resolveActivity(getPackageManager()) != null){

                    startActivity(mapIntent);
                }
            }
        });

        putCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final View v = getLayoutInflater().inflate(R.layout.offers_comments_bottomsheet, null);
                dialog = new BottomSheetDialog(requestsOffersDetail.this);
                dialog.setContentView(v);

                final TextInputLayout commentLayout = v.findViewById(R.id.offersCommentLayout);
                final TextInputEditText commentEditText = v.findViewById(R.id.offersCommentEt);

                commentEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                        String comment = commentEditText.getText().toString().trim();
                        isValidComment(comment, commentLayout);
                    }
                });
                dialog.show();

                CircularProgressButton putCommentButton = v.findViewById(R.id.OffersPutCommentBottomSheet);


                putCommentButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String commentText = commentEditText.getText().toString().trim();

                        offerComment = new offerComments();

                        if (!isValidComment(commentText, commentLayout)){

                            return;
                        }

                        showDialog("Putting comment, just a second!");




                        String date = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault()).format(new Date());
                        String comment = commentEditText.getText().toString().trim();

                        offerComment.setComment(comment);
                        offerComment.setDate(date);
                        offerComment.setCommenterImage(" ");
                        offerComment.setUsername(" ");
                        offerComment.setRequestKey(requestKey);

                        Toast.makeText(requestsOffersDetail.this, offerComment.getRequestKey(), Toast.LENGTH_SHORT).show();

                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = firebaseDatabase.getReference().child("offerComments").push();
                        String key = databaseReference.getKey();

                        offerComment.setKey(key);

                        databaseReference.setValue(offerComment);
                        getPublisherInformation2();
                        getPublisherImage();



                    }
                });



            }

            public boolean isValidComment( String comment, TextInputLayout textInputLayout){

                 if (comment.isEmpty()){

                     textInputLayout.setError("Comment cannot be empty");
                     return false;

                 }
                 else {
                     textInputLayout.setErrorEnabled(false);
                 }
                 return true;


            }

        });

        putOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(requestsOffersDetail.this, publishOffer.class);

                intent1.putExtra("REQUESTKEY", requestKey);

                startActivity(intent1);
            }
        });

        recyclerViewLatestOffers = findViewById(R.id.latestOffersDetailRecyclerView);
        linearLayoutManagerLatestOffers = new LinearLayoutManager(this);
        recyclerViewLatestOffers.setLayoutManager(linearLayoutManagerLatestOffers);

        fetchLatestOffers();

        recyclerViewComments = findViewById(R.id.commentsRecyclerviewOffersDetail);
        linearLayoutManagerComments = new LinearLayoutManager(requestsOffersDetail.this);
        recyclerViewComments.setLayoutManager(linearLayoutManagerComments);
        fetchComments();
    }


    public class LatestOffersViewHolder extends RecyclerView.ViewHolder {

        public TextView title, price, date, publisherName, category;
        public ImageView productimage, publisherImage;
        public RelativeLayout rev_layout;


        public LatestOffersViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.commenterName);
            price = itemView.findViewById(R.id.price);
            productimage = itemView.findViewById(R.id.productImage_adsFragment);
            publisherImage = itemView.findViewById(R.id.publisherImage);
            rev_layout = itemView.findViewById(R.id.relativeLayout);
            publisherName = itemView.findViewById(R.id.publisherName);
            date = itemView.findViewById(R.id.date);
            category = itemView.findViewById(R.id.category);
        }


    }



    private void fetchLatestOffers(){


        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Offers").orderByChild("requestKey").equalTo(requestKey);


        FirebaseRecyclerOptions<offers> options =
                new FirebaseRecyclerOptions.Builder<offers>()
                        .setQuery(query, new SnapshotParser<offers>() {
                            @NonNull
                            @Override
                            public offers parseSnapshot(@NonNull DataSnapshot snapshot) {
//

                                showDialog("Fetching Request and Offers, just a second");
                                offers offer = snapshot.getValue(offers.class);





                                return offer;
                            }
                        }).build();


        //adapter settings



        adapterLatestOffers = new FirebaseRecyclerAdapter<offers, LatestOffersViewHolder>(options) {


            @NonNull
            @Override
            public LatestOffersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.generic_ads_layout, parent, false);

                return new LatestOffersViewHolder(itemview);

            }

            @Override
            protected void onBindViewHolder(@NonNull final LatestOffersViewHolder holder, int position, @NonNull final offers model) {

                holder.publisherImage.setAnimation(AnimationUtils.loadAnimation(requestsOffersDetail.this, R.anim.rv_fade_transition));
                holder.rev_layout.setAnimation(AnimationUtils.loadAnimation(requestsOffersDetail.this, R.anim.rv_scale_animation));

                holder.title.setText(model.getTitle());
                holder.date.setText(model.getPublishDate());
                holder.publisherName.setText(model.getPublisherUsername());
                holder.category.setText(model.getCategory());
                final Uri productImage = Uri.parse(model.getProductImage());
                Uri publisherImage = Uri.parse(model.getPublisherImage());

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

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.ad_placeholder)
                        .error(R.drawable.ad_error);

                Glide.with(holder.productimage.getContext())
                        .load(productImage)
                        .apply(options).centerCrop().override(300, 300).into(holder.productimage);

                holder.productimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse(model.getProductImage())));

                    }
                });


                Glide.with(holder.publisherImage.getContext())
                        .load(publisherImage)
                        .apply(options).override(40, 40).into(holder.publisherImage);

                dismissDialog();

                holder.rev_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        openDetailActivity(model.getProductImage() , model.getDescription(), model.getCategory()
                                , model.getPublishDate() , model.getPrice() , model.getCurrency() , model.getPriceType()
                                , model.getWarranty() , model.getCondition() , model.getPublisherImage()
                                , model.getPublisherUsername() , model.getPublisherEmail(), model.getPublisherPhoneNumber(), model.getOfferKey(), model.getTitle(), model.getPublisherLatitude(), model.getPublisherLongitude(), model.getPublisherState());

                    }
                });
            }
        };


        recyclerViewLatestOffers.setAdapter(adapterLatestOffers);


    }

    public void openDetailActivity(String... detail){

        Intent intent = new Intent(requestsOffersDetail.this, detail_activity.class);

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
        intent.putExtra("PUBLISHERLATITUDE", detail[15]);
        intent.putExtra("PUBLISHERLONGITUDE", detail[16]);
        intent.putExtra("PUBLISHERSTATE", detail[17]);

        startActivity(intent);



    }
    public class offerCommentsViewHolder extends RecyclerView.ViewHolder {

        public TextView commenterName, comment, date;
        public ImageView commenterImage;
        public CardView cardView;
        public RatingBar commentRatingBar;


        public offerCommentsViewHolder(@NonNull View itemView) {
            super(itemView);

            commenterName = itemView.findViewById(R.id.commentername);
            comment  = itemView.findViewById(R.id.comment_tv);
            date = itemView.findViewById(R.id.ratingDate);
            commenterImage = itemView.findViewById(R.id.commenterImage);
            cardView = itemView.findViewById(R.id.constraint);
            commentRatingBar = itemView.findViewById(R.id.ratignBar_comment);
            commentRatingBar.setVisibility(View.GONE);






        }


    }



    private void fetchComments() {

        final TextView noComments = findViewById(R.id.noCommentsLatestOffersDetailTv);

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("offerComments").orderByChild("requestKey").equalTo(requestKey);


        FirebaseRecyclerOptions<offerComments> options =
                new FirebaseRecyclerOptions.Builder<offerComments>()
                        .setQuery(query, new SnapshotParser<offerComments>() {
                            @NonNull
                            @Override
                            public offerComments parseSnapshot(@NonNull DataSnapshot snapshot) {
//




                                offerComments comment = snapshot.getValue(offerComments.class);
                                noComments.setVisibility(View.GONE);

                                return comment;


                            }
                        }).build();


        //adapter settings



        adapterComments = new FirebaseRecyclerAdapter<offerComments, offerCommentsViewHolder>(options) {


            @NonNull
            @Override
            public offerCommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.rating_layout, parent, false);

                return new offerCommentsViewHolder(itemview);

            }

            @Override
            protected void onBindViewHolder(@NonNull final offerCommentsViewHolder holder, int position, @NonNull final offerComments model) {

                holder.commenterImage.setAnimation(AnimationUtils.loadAnimation(requestsOffersDetail.this, R.anim.rv_fade_transition));
                holder.cardView.setAnimation(AnimationUtils.loadAnimation(requestsOffersDetail.this, R.anim.rv_scale_animation));


                holder.commenterName.setText(model.getUsername());
                holder.comment.setText(model.getComment());
                holder.date.setText(model.getDate());

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



        recyclerViewComments.setAdapter(adapterComments);


    }

    public void getPublisherInformation(){

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        databaseReference.child("userInformation").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                showDialog("just a second");
                Userinformation userProfile = dataSnapshot.getValue(Userinformation.class);

                if (userProfile.getUserType().equals("customer")){

                    putOfferButton.setVisibility(View.GONE);
                }
                dismissDialog();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {



            }
        });


    }

    public void getPublisherImage() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        StorageReference publisher_reference = firebaseStorage.getReference(firebaseAuth.getUid()).child("images").child("Profile Pic");

        publisher_reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                String commenterImage = uri.toString();

                databaseReference.child("offerComments").child(offerComment.getKey()).child("commenterImage").setValue(commenterImage);

                imageCheck = true;
                Toast.makeText(requestsOffersDetail.this, "commenterImage acquired", Toast.LENGTH_SHORT).show();

                check(userNameCheck, imageCheck);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                imageCheck = false;
                Toast.makeText(requestsOffersDetail.this, "commenterImage not acquired", Toast.LENGTH_SHORT).show();

                check(userNameCheck, imageCheck);
            }
        });
    }
    public void getPublisherInformation2(){

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        databaseReference.child("userInformation").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Userinformation userProfile = dataSnapshot.getValue(Userinformation.class);

                String commenterUserName = userProfile.getUserName();

                DatabaseReference databaseReference1 = firebaseDatabase.getReference();
                databaseReference1.child("offerComments").child(offerComment.getKey()).child("username").setValue(commenterUserName);
                userNameCheck = true;
                Toast.makeText(requestsOffersDetail.this, "publisher Information acquired"+commenterUserName, Toast.LENGTH_SHORT).show();
                check(userNameCheck, imageCheck);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                userNameCheck = false;
                Toast.makeText(requestsOffersDetail.this, "publisher Infromation failed", Toast.LENGTH_SHORT).show();
                check(userNameCheck, imageCheck);
            }
        });


    }

    public void check(boolean username, boolean image){

        if (username && image){

            dismissDialog();
            Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
            dismissDialog();
            new SweetAlertDialog(requestsOffersDetail.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Awesome!")
                    .setContentText("Comment added!")
                    .show();
        }


        else{
            Toast.makeText(this, "not yet", Toast.LENGTH_SHORT).show();
        }

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
    @Override
    public void onStart() {
        super.onStart();


        adapterLatestOffers.startListening();
        adapterComments.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapterLatestOffers.stopListening();
        adapterComments.stopListening();
    }
}
