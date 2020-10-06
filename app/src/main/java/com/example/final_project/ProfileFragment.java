package com.example.final_project;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.pedant.SweetAlert.SweetAlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.login.LoginManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tapadoo.alerter.Alerter;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;

import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    //firebase
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    //firebaseend

    public TextView username;
    public TextView phoneNumber;
    public TextView emailAddress;
    public TextView userType;
    public ImageView logoutButton;
    public ImageView userState;


    public Switch notificationSwitch;
    public ImageView profileimageView;
    public LinearLayout helpAndFeedbackLayout;
    public LinearLayout aboutTheAppLayout;



    SweetAlertDialog pDialog;
    public ConstraintLayout previousCardProfile;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;

    String publisherEmail;

    private RecyclerView previousAdsRecyclerView;
    private LinearLayoutManager linearLayoutManagerPrevious;
    private FirebaseRecyclerAdapter previousAdsAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading ...");
        pDialog.setCancelable(false);







    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        showDialog();

        View view =  inflater.inflate(R.layout.fragment_profile, container, false);


        notificationSwitch = view.findViewById(R.id.notificationSwitch);

        SharedPreferences settings = (getActivity()).getSharedPreferences("settings", Context.MODE_PRIVATE);
        boolean silent = settings.getBoolean("switchkey", false);
        notificationSwitch.setChecked(silent);

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (notificationSwitch.isChecked()) {

                    Toast.makeText(getActivity(), "notification is on", Toast.LENGTH_SHORT).show();
                    subscribeToNotifications();

                }

                else{
                    Toast.makeText(getActivity(), "notification is off", Toast.LENGTH_SHORT).show();
                    unsubscribeToNotification();

                }

                SharedPreferences settings =  (getActivity()).getSharedPreferences("settings", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("switchkey", isChecked);
                editor.commit();

            }
        });

        username = view.findViewById(R.id.username_fragmentprofile);
        phoneNumber = view.findViewById(R.id.phone_fragmentprofile);
        emailAddress = view.findViewById(R.id.email_fragmentprofile);
        userType = view.findViewById(R.id.usertype_fragmentprofile);
        profileimageView = view.findViewById(R.id.profile_imageprofile_fragment);
        logoutButton = view.findViewById(R.id.logout_fragmentprofile);
        previousCardProfile = view.findViewById(R.id.previousCardProfile);
        helpAndFeedbackLayout = view.findViewById(R.id.helpLinearLayout);

        helpAndFeedbackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new LovelyInfoDialog(getActivity())
                        .setTopColorRes(R.color.cardColor)
                        .setIcon(R.drawable.ic_feedback)
                        //This will add Don't show again checkbox to the dialog. You can pass any ID as argument
//                        .setNotShowAgainOptionEnabled(0)
//                        .setNotShowAgainOptionChecked(true)
                        .setTitle("Help And Feedback:")
                        .setMessage("For any information or inquiry, feel free to contact: yparts@info.com.\n The administration will reply to you as soon as possible")
                        .show();
            }
        });

        aboutTheAppLayout = view.findViewById(R.id.aboutTheAppLinearLayout);
        aboutTheAppLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LovelyInfoDialog(getActivity())
               .setTopColorRes(R.color.cardColor)
                        .setIcon(R.drawable.ic_about)
                        //This will add Don't show again checkbox to the dialog. You can pass any ID as argument
//                        .setNotShowAgainOptionEnabled(0)
//                        .setNotShowAgainOptionChecked(true)
                        .setTitle("About the App")
                        .setMessage("Y-Parts is a mobile application that provides an online Auto-parts service\nto ease the process of buying used and new Auto-Parts in Yemen\nby offering a variety of services to both buyers and sellers\ngiving the buyer many different offerings from sellers to choose from,\nextending the selling capability for sellers,\nand opening the used Auto-Parts marketspace without boundaries for all.")
                        .show();


            }
        });




        userState = view.findViewById(R.id.userStateProfileFragment);


        publisherEmail = firebaseAuth.getCurrentUser().getEmail();
        Toast.makeText(getActivity(), publisherEmail, Toast.LENGTH_SHORT).show();

        DatabaseReference databaseReference = firebaseDatabase.getReference();
        StorageReference storageReference = firebaseStorage.getReference();





        storageReference.child(firebaseAuth.getUid()).child("images").child("Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getActivity()).load(uri).circleCrop().into(profileimageView);
                dismissDialog();



            }
        });


        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


        databaseReference.child("userInformation").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Userinformation userProfile = dataSnapshot.getValue(Userinformation.class);

                //Toast.makeText(getActivity(), userProfile.getUserName(), Toast.LENGTH_SHORT).show();
                username.setText(userProfile.getUserName());
                phoneNumber.setText(userProfile.getPhoneNumber());
                emailAddress.setText(firebaseUser.getEmail());
                userType.setText(userProfile.getUserType());

                userState.setVisibility(View.GONE);
                if (userProfile.getUserType().equals("customer")){

                    previousCardProfile.setVisibility(View.GONE);
                    userState.setVisibility(View.GONE);

                }

                if (userProfile.getUserType().equals("supplier")&& userProfile.getPublisherState().equals("verified")){
                    userState.setVisibility(View.VISIBLE);
                }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Alerter.create(getActivity())
                        .setTitle("Y-parts")
                        .setText("Something went wrong, Could not retrieve profile information, Try again !")
                        .enableSwipeToDismiss()
                        .setDuration(3000)
                        .setBackgroundColorRes(R.color.text_color_orange)
                        .show();
                dismissDialog();
            }
        });



        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signOut();
            }
        });


        recyclerView = view.findViewById(R.id.favouritesAdsRecyclerview);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        fetch();

        previousAdsRecyclerView = view.findViewById(R.id.previousAdsProfileRecyclerView);
        linearLayoutManagerPrevious = new LinearLayoutManager(getActivity());
        linearLayoutManagerPrevious.setReverseLayout(true);
        linearLayoutManagerPrevious.setStackFromEnd(true);
        previousAdsRecyclerView.setLayoutManager(linearLayoutManagerPrevious);
        fetchPreviousAds();
        return view;

    }

    public class favouritesViewHolder extends RecyclerView.ViewHolder {

        public TextView title, price, date, publisherName, category;
        public ImageView productimage, publisherImage;
        public RelativeLayout rev_layout;


        public favouritesViewHolder(@NonNull View itemView) {
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



    private void fetch() {

        //Toast.makeText(getActivity(), firebaseAuth.getUid(), Toast.LENGTH_SHORT).show();
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("favouritesAds").child(firebaseAuth.getUid());



        FirebaseRecyclerOptions<generalAds> options =
                new FirebaseRecyclerOptions.Builder<generalAds>()
                        .setQuery(query, new SnapshotParser<generalAds>() {
                            @NonNull
                            @Override
                            public generalAds parseSnapshot(@NonNull DataSnapshot snapshot) {
//
                               // showDialog();

                                generalAds generalAd = snapshot.getValue(generalAds.class);



                                return generalAd;
                            }
                        }).build();


        //adapter settings



        adapter = new FirebaseRecyclerAdapter<generalAds, favouritesViewHolder>(options) {


            @NonNull
            @Override
            public favouritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.generic_ads_layout, parent, false);

                return new favouritesViewHolder(itemview);

            }

            @Override
            protected void onBindViewHolder(@NonNull final favouritesViewHolder holder, int position, @NonNull final generalAds model) {

                holder.publisherImage.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.rv_fade_transition));
                holder.rev_layout.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.rv_scale_animation));

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
                holder.date.setText(model.getPublishDate());
                holder.publisherName.setText(model.getPublisherUsername());
                holder.category.setText(model.getCategory());
                final Uri productImage = Uri.parse(model.getProductImage());
                Uri publisherImage = Uri.parse(model.getPublisherImage());

                Toast.makeText(getActivity(), holder.category.getText(), Toast.LENGTH_SHORT).show();

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.avatar)
                        .error(R.drawable.ads_icon);

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



                holder.rev_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        openDetailActivity(model.getProductImage() , model.getDescription(), model.getCategory()
                                , model.getPublishDate() , model.getPrice() , model.getCurrency() , model.getPriceType()
                                , model.getWarranty() , model.getCondition() , model.getPublisherImage()
                                , model.getPublisherUsername() , model.getPublisherEmail(), model.getPublisherPhoneNumber(), model.getKey(), model.getTitle(),model.getPublisherLatitude(), model.getPublisherLongitude(), model.getPublisherState());

                    }
                });
            }
        };


        recyclerView.setAdapter(adapter);


    }

    public class previousAdsProfileHolder extends RecyclerView.ViewHolder {

        public TextView title, price, date, publisherName, category;
        public ImageView productimage, publisherImage;
        public RelativeLayout rev_layout;


        public previousAdsProfileHolder(@NonNull View itemView) {
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



    private void fetchPreviousAds() {

        //Toast.makeText(getActivity(), firebaseAuth.getUid(), Toast.LENGTH_SHORT).show();

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
                                //showDialog();
                                generalAds generalAd = snapshot.getValue(generalAds.class);



                                return generalAd;
                            }
                        }).build();


        //adapter settings



        previousAdsAdapter = new FirebaseRecyclerAdapter<generalAds, previousAdsProfileHolder>(options) {


            @NonNull
            @Override
            public previousAdsProfileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.generic_ads_layout, parent, false);

                return new previousAdsProfileHolder(itemview);

            }

            @Override
            protected void onBindViewHolder(@NonNull final previousAdsProfileHolder holder, int position, @NonNull final generalAds model) {

                holder.publisherImage.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.rv_fade_transition));
                holder.rev_layout.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.rv_scale_animation));

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
                holder.date.setText(model.getPublishDate());
                holder.publisherName.setText(model.getPublisherUsername());
                holder.category.setText(model.getCategory());
                final Uri productImage = Uri.parse(model.getProductImage());
                Uri publisherImage = Uri.parse(model.getPublisherImage());

                Toast.makeText(getActivity(), holder.category.getText(), Toast.LENGTH_SHORT).show();

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.avatar)
                        .error(R.drawable.ads_icon);

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



                holder.rev_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        openDetailActivity(model.getProductImage() , model.getDescription(), model.getCategory()
                                , model.getPublishDate() , model.getPrice() , model.getCurrency() , model.getPriceType()
                                , model.getWarranty() , model.getCondition() , model.getPublisherImage()
                                , model.getPublisherUsername() , model.getPublisherEmail(), model.getPublisherPhoneNumber(), model.getKey(), model.getTitle(),model.getPublisherLatitude(), model.getPublisherLongitude(), model.getPublisherState());

                    }
                });
            }
        };


        previousAdsRecyclerView.setAdapter(previousAdsAdapter);


    }

    public void openDetailActivity(String... detail){

        Intent intent = new Intent(getActivity(), detail_activity.class);

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


        getActivity().startActivity(intent);



    }



    public void showDialog(){
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
        adapter.startListening();
        previousAdsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
        previousAdsAdapter.stopListening();
    }


    public void signOut(){

        firebaseAuth.signOut();
        LoginManager.getInstance().logOut();


        Intent intent = new Intent(getActivity(), login_screen.class);
        startActivity(intent);

        getActivity().finish();


    }
    public void subscribeToNotifications(){


        showDialog();
        FirebaseMessaging.getInstance().subscribeToTopic("general_notification")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (!task.isSuccessful()){
                            Toast.makeText(getActivity(), " Failed to subscribe to notifications", Toast.LENGTH_SHORT).show();
                            dismissDialog();
                        }
                        else{
                            Toast.makeText(getActivity(), " Successfully Subscribed to notifications", Toast.LENGTH_SHORT).show();
                        dismissDialog();
                        }
                    }
                });
    }

    public void unsubscribeToNotification(){


        FirebaseMessaging.getInstance().unsubscribeFromTopic("general_notification")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (!task.isSuccessful()){
                            Toast.makeText(getActivity(), " Failed to unsubscribe to notifications", Toast.LENGTH_SHORT).show();
                            dismissDialog();
                        }
                        else{
                            Toast.makeText(getActivity(), " Successfully unSubscribed to notifications", Toast.LENGTH_SHORT).show();
                            dismissDialog();
                        }
                    }
                });
    }

    public void shownotification(String message){


        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "com.example.final_project"; //your app package name

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setDescription("Techrush Channel");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getActivity(), NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.tire)
                .setContentTitle(message)
                .setContentText(message)
                .setContentInfo("Info");

        notificationManager.notify(new Random().nextInt(),notificationBuilder.build());

    }

}
