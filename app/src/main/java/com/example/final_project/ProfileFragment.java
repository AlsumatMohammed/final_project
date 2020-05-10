package com.example.final_project;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.pedant.SweetAlert.SweetAlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.tapadoo.alerter.Alerter;


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


    public ImageView profileimageView;


    SweetAlertDialog pDialog;
    public CardView previousCardProfile;

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


        username = view.findViewById(R.id.username_fragmentprofile);
        phoneNumber = view.findViewById(R.id.phone_fragmentprofile);
        emailAddress = view.findViewById(R.id.email_fragmentprofile);
        userType = view.findViewById(R.id.usertype_fragmentprofile);
        profileimageView = view.findViewById(R.id.profile_imageprofile_fragment);
        logoutButton = view.findViewById(R.id.logout_fragmentprofile);
        previousCardProfile = view.findViewById(R.id.previousCardProfile);



        publisherEmail = firebaseAuth.getCurrentUser().getEmail();
        Toast.makeText(getActivity(), publisherEmail, Toast.LENGTH_SHORT).show();

        DatabaseReference databaseReference = firebaseDatabase.getReference();
        StorageReference storageReference = firebaseStorage.getReference();





        storageReference.child(firebaseAuth.getUid()).child("images").child("Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getActivity()).load(uri).centerCrop().into(profileimageView);
                dismissDialog();



            }
        });


        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


        databaseReference.child("userInfromation").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Userinformation userProfile = dataSnapshot.getValue(Userinformation.class);

                //Toast.makeText(getActivity(), userProfile.getUserName(), Toast.LENGTH_SHORT).show();
                username.setText(userProfile.getUserName());
                phoneNumber.setText(userProfile.getPhoneNumber());
                emailAddress.setText(firebaseUser.getEmail());
                userType.setText(userProfile.getUserType());

                if (userProfile.getUserType().equals("customer")){

                    previousCardProfile.setVisibility(View.GONE);
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

                signout();
            }
        });


        recyclerView = view.findViewById(R.id.favouritesAdsRecyclerview);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);


        fetch();

        previousAdsRecyclerView = view.findViewById(R.id.previousAdsProfileRecyclerView);
        linearLayoutManagerPrevious = new LinearLayoutManager(getActivity());
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
                                showDialog();

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
                                , model.getPublisherUsername() , model.getPublisherEmail(), model.getPublisherPhoneNumber(), model.getKey(), model.getTitle());

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
                                showDialog();
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
                                , model.getPublisherUsername() , model.getPublisherEmail(), model.getPublisherPhoneNumber(), model.getKey(), model.getTitle());

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


    public void signout(){

        firebaseAuth.signOut();

        Intent intent = new Intent(getActivity(), login_screen.class);
        startActivity(intent);

        getActivity().finish();


    }

}
