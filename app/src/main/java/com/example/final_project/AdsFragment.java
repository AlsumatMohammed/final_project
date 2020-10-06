package com.example.final_project;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import cn.pedant.SweetAlert.SweetAlertDialog;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.yarolegovich.lovelydialog.LovelyInfoDialog;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdsFragment extends Fragment {

    public FirebaseAuth firebaseAuth;
    public FirebaseUser firebaseUser;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;



    SweetAlertDialog pDialog;
    ImageView publishButton;

    LinearLayout brakeLayout;
    LinearLayout engineLayout;
    LinearLayout lightLayout;
    LinearLayout ignitionLayout;
    LinearLayout suspensionLayout;
    LinearLayout exhaustLayout;

    CircularProgressButton viewAllButton;

    public EditText searchBar;
    ImageView searchIcon;
    ImageView searchIconToolBar;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdsFragment newInstance(String param1, String param2) {
        AdsFragment fragment = new AdsFragment();
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
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseAuth.getCurrentUser() == null){

            Intent intent = new Intent(getActivity(), login_screen.class);

            startActivity(intent);
            getActivity().finish();

        }

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        databaseReference.child("userInformation").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                showDialog();

                if (!dataSnapshot.exists()){

                    Intent intent = new Intent(getActivity(), edit_profile.class);

                    startActivity(intent);
                    getActivity().finish();


                }

                dismissDialog();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading ...");
        pDialog.setCancelable(false);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        View view =  inflater.inflate(R.layout.fragment_ads, container, false);

        viewAllButton = view.findViewById(R.id.viewAllCategoriesButton);
        brakeLayout = view.findViewById(R.id.brakeLayout);
        engineLayout = view.findViewById(R.id.engineLayout);
        lightLayout = view.findViewById(R.id.lightLayout);
        suspensionLayout = view.findViewById(R.id.suspensionLayout);
        exhaustLayout = view.findViewById(R.id.exhaustLayout);
        ignitionLayout = view.findViewById(R.id.ignitionLayout);
        publishButton = view.findViewById(R.id.add_ad);
        searchBar = view.findViewById(R.id.searchBar);
        searchIcon = view.findViewById(R.id.searchIcon);
        searchIconToolBar = view.findViewById(R.id.searchIconToolBar);

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (searchBar.getText().toString().trim().isEmpty()){

                    Alerter.create(getActivity())
                            .setTitle("Y-parts")
                            .setText("The Search Filed Cannot be empty!")
                            .enableSwipeToDismiss()
                            .setDuration(3000)
                            .setBackgroundColorRes(R.color.text_color_orange)
                            .show();
                }

                else{

                    String searchText = searchBar.getText().toString().trim();

                    Intent i  = new Intent(getActivity(), searchFilter.class);

                    i.putExtra("SEARCHTEXT", searchText);

                    getActivity().startActivity(i);
                }
            }
        });

        searchIconToolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String searchText = searchBar.getText().toString().trim();

                Intent i  = new Intent(getActivity(), searchFilter.class);

                i.putExtra("SEARCHTEXT", searchText);

                getActivity().startActivity(i);
            }
        });




        brakeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), categoriesFilter.class);

                intent.putExtra("CATEGORY", "Brake Systems");

                getActivity().startActivity(intent);
            }
        });

        engineLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), categoriesFilter.class);

                intent.putExtra("CATEGORY", "Engines & Components");

                getActivity().startActivity(intent);
            }
        });
        lightLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), categoriesFilter.class);

                intent.putExtra("CATEGORY", "Lights & Lightning");

                getActivity().startActivity(intent);
            }
        });

        ignitionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), categoriesFilter.class);

                intent.putExtra("CATEGORY", "Ignition & Electrical");

                getActivity().startActivity(intent);
            }
        });

        exhaustLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), categoriesFilter.class);

                intent.putExtra("CATEGORY", "Exhaust");

                getActivity().startActivity(intent);
            }
        });

        suspensionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), categoriesFilter.class);

                intent.putExtra("CATEGORY", "Chassis & Suspension");

                getActivity().startActivity(intent);
            }
        });

        publishButton.setVisibility(View.GONE);

        viewAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), allCategories.class);

                getActivity().startActivity(intent);
            }
        });

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        databaseReference.child("userInformation").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Userinformation userProfile = dataSnapshot.getValue(Userinformation.class);
//
//                if (!dataSnapshot.exists()){
//
//                    Toast.makeText(getActivity(), "you must edit your profile", Toast.LENGTH_SHORT).show();
//                }

                if (userProfile.getPublisherState().equals("unverified")&& userProfile.getUserType().equals("supplier")){
//                    Alerter.create(getActivity())
//                            .setTitle("Y-parts")
//                            .setText("Your Account has not yet been verified!")
//                            .enableSwipeToDismiss()
//                            .setDuration(3000)
//                            .setBackgroundColorRes(R.color.text_color_orange)
//                            .show();

                    new LovelyInfoDialog(getActivity())
                            .setTopColorRes(R.color.toolbar)
                            .setIcon(R.drawable.ic_verified_dialog)
                            .setNotShowAgainOptionChecked(true)
                            .setTitle("Unverified Account")
                            .setMessage("Your account hasn't been verified.\nFor speeding up the verification process,\ncontact y-parts@gmail.com")
                            .show();
                }

                if (userProfile.getUserType().equals("supplier")){

                    publishButton.setVisibility(View.VISIBLE);
                }
                else{
                    publishButton.setVisibility(View.GONE);
                }


            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity() , publishAd.class);
                startActivity(intent);
//                getActivity().finish();
            }
        });

        recyclerView = view.findViewById(R.id.recycler_view1);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        fetch();
        return view;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, price, date, publisherName, category;
        public ImageView productimage, publisherImage;
        public RelativeLayout rev_layout;


        public MyViewHolder(@NonNull View itemView) {
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

        //showDialog();
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("GeneralAds");



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



        adapter = new FirebaseRecyclerAdapter<generalAds, MyViewHolder>(options) {


            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.generic_ads_layout, parent, false);

                return new MyViewHolder(itemview);

            }

            @Override
            protected void onBindViewHolder(@NonNull final MyViewHolder holder, int position, @NonNull final generalAds model) {

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
                                , model.getPublisherUsername() , model.getPublisherEmail(), model.getPublisherPhoneNumber(), model.getKey(), model.getTitle(), model.getPublisherLatitude(), model.getPublisherLongitude(), model.getPublisherState());


                    }
                });
            }
        };


        recyclerView.setAdapter(adapter);


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

        @Override
        public void onStart() {
            super.onStart();
            adapter.startListening();

        }

        @Override
        public void onStop() {
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
