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
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestsOffersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class RequestsOffersFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public FirebaseAuth firebaseAuth;
    public FirebaseUser firebaseUser;

    private RecyclerView recyclerViewMyRequests;
    private LinearLayoutManager linearLayoutManagerMyRequests;
    private FirebaseRecyclerAdapter adapterMyRequests;

    private RecyclerView recyclerViewLatestRequests;
    private LinearLayoutManager linearLayoutManagerLatestRequests;
    private FirebaseRecyclerAdapter adapterLatestRequests;


    String userType = "";

    SweetAlertDialog pDialog;
    ImageView addRequestsOffersButton;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RequestsOffersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RequestsOffersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RequestsOffersFragment newInstance(String param1, String param2) {
        RequestsOffersFragment fragment = new RequestsOffersFragment();
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

        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setCancelable(false);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

         View view = inflater.inflate(R.layout.fragment_requests_offers, container, false);


         addRequestsOffersButton = view.findViewById(R.id.addRequestButton);

         addRequestsOffersButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 Intent intent = new Intent(getActivity(), putRequest.class);

                 startActivity(intent);
             }
         });

        recyclerViewMyRequests= view.findViewById(R.id.myRequestsRecyclerView);
        linearLayoutManagerMyRequests = new LinearLayoutManager(getActivity());
        linearLayoutManagerMyRequests.setReverseLayout(true);
        linearLayoutManagerMyRequests.setStackFromEnd(true);
        recyclerViewMyRequests.setLayoutManager(linearLayoutManagerMyRequests);

        recyclerViewLatestRequests = view.findViewById(R.id.latestRequestsRecyclerView);
        linearLayoutManagerLatestRequests = new LinearLayoutManager(getActivity());
        linearLayoutManagerLatestRequests.setReverseLayout(true);
        linearLayoutManagerLatestRequests.setStackFromEnd(true);
        recyclerViewLatestRequests.setLayoutManager(linearLayoutManagerLatestRequests);






        fetchLatestRequest();
        fetchMyRequests();
        return view;


    }

    public class LatestRequestsViewHolder extends RecyclerView.ViewHolder {

        public TextView title, price, date, publisherName, category;
        public ImageView productimage, publisherImage;
        public RelativeLayout rev_layout;


        public LatestRequestsViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.commenterName);
            price = itemView.findViewById(R.id.price);
            price.setVisibility(View.GONE);
            productimage = itemView.findViewById(R.id.productImage_adsFragment);
            publisherImage = itemView.findViewById(R.id.publisherImage);
            rev_layout = itemView.findViewById(R.id.relativeLayout);
            publisherName = itemView.findViewById(R.id.publisherName);
            date = itemView.findViewById(R.id.date);
            category = itemView.findViewById(R.id.category);
        }


    }



    private void fetchLatestRequest() {


        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Requests");



        FirebaseRecyclerOptions<requests> options =
                new FirebaseRecyclerOptions.Builder<requests>()
                        .setQuery(query, new SnapshotParser<requests>() {
                            @NonNull
                            @Override
                            public requests parseSnapshot(@NonNull DataSnapshot snapshot) {
//
                                showDialog("Fetching Request and Offers, just a second");
                                requests request = snapshot.getValue(requests.class);



                                return request;
                            }
                        }).build();


        //adapter settings



        adapterLatestRequests = new FirebaseRecyclerAdapter<requests, LatestRequestsViewHolder>(options) {


            @NonNull
            @Override
            public LatestRequestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.generic_ads_layout, parent, false);

                return new LatestRequestsViewHolder(itemview);

            }

            @Override
            protected void onBindViewHolder(@NonNull final LatestRequestsViewHolder holder, int position, @NonNull final requests model) {

                holder.publisherImage.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.rv_fade_transition));
                holder.rev_layout.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.rv_scale_animation));


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
                                , model.getPublishDate(), model.getWarranty() , model.getCondition() , model.getPublisherImage()
                                , model.getPublisherUsername() , model.getPublisherEmail(), model.getPublisherPhoneNumber(), model.getKey(), model.getPublisherLatitude(), model.getPublisherLongitude());

                    }
                });
            }
        };


        recyclerViewLatestRequests.setAdapter(adapterLatestRequests);


    }

    public class myRequestsViewHolder extends RecyclerView.ViewHolder {

        public TextView title, price, date, publisherName, category;
        public ImageView productimage, publisherImage;
        public RelativeLayout rev_layout;


        public myRequestsViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.commenterName);
            price = itemView.findViewById(R.id.price);
            price.setVisibility(View.GONE);
            productimage = itemView.findViewById(R.id.productImage_adsFragment);
            publisherImage = itemView.findViewById(R.id.publisherImage);
            rev_layout = itemView.findViewById(R.id.relativeLayout);
            publisherName = itemView.findViewById(R.id.publisherName);
            date = itemView.findViewById(R.id.date);
            category = itemView.findViewById(R.id.category);
        }


    }



    private void fetchMyRequests() {



        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Requests").orderByChild("publisherEmail").equalTo(firebaseUser.getEmail());



        FirebaseRecyclerOptions<requests> options =
                new FirebaseRecyclerOptions.Builder<requests>()
                        .setQuery(query, new SnapshotParser<requests>() {
                            @NonNull
                            @Override
                            public requests parseSnapshot(@NonNull DataSnapshot snapshot) {
//
                                showDialog("Fetching Request and Offers, just a second");
                                requests request = snapshot.getValue(requests.class);



                                return request;
                            }
                        }).build();


        //adapter settings



        adapterMyRequests = new FirebaseRecyclerAdapter<requests, myRequestsViewHolder>(options) {


            @NonNull
            @Override
            public myRequestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.generic_ads_layout, parent, false);

                return new myRequestsViewHolder(itemview);

            }

            @Override
            protected void onBindViewHolder(@NonNull final myRequestsViewHolder holder, int position, @NonNull final requests model) {

                holder.publisherImage.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.rv_fade_transition));
                holder.rev_layout.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.rv_scale_animation));


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
                                , model.getPublishDate(), model.getWarranty() , model.getCondition() , model.getPublisherImage()
                                , model.getPublisherUsername() , model.getPublisherEmail(), model.getPublisherPhoneNumber(), model.getKey(), model.getPublisherLatitude(), model.getPublisherLongitude());

                    }
                });
            }
        };


        recyclerViewMyRequests.setAdapter(adapterMyRequests);


    }

    public void openDetailActivity(String... detail){

        Intent intent = new Intent(getActivity(), requestsOffersDetail.class);

        intent.putExtra("PRODUCT_IMAGE" , detail[0]);
        intent.putExtra("DESCRIPTION" , detail[1]);
        intent.putExtra("CATEGORY" , detail[2]);
        intent.putExtra("PUBLISHDATE" , detail[3]);
        intent.putExtra("WARRANTY" , detail[4]);
        intent.putExtra("CONDITION" , detail[5]);
        //PUBLISHER
        intent.putExtra("PUBLISHERIMAGE" , detail[6]);
        intent.putExtra("PUBLISHERUSERNAME" , detail[7]);
        intent.putExtra("PUBLSIHEREMAIL" , detail[8]);
        intent.putExtra("PUBLISHERPHONE" , detail[9]);
        intent.putExtra("REQUESTKEY" , detail[10]);
        intent.putExtra("PUBLISHERLATITUDE" , detail[11]);
        intent.putExtra("PUBLISHERLONGITUDE" , detail[12]);


        getActivity().startActivity(intent);



    }



    public void showDialog(String message){

        pDialog.setTitleText(message);
        pDialog.show();



    }

    public void dismissDialog(){
        pDialog.dismiss();


    }

    @Override
    public void onStart() {
        super.onStart();


        adapterLatestRequests.startListening();
        adapterMyRequests.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapterLatestRequests.stopListening();
        adapterMyRequests.startListening();
    }
}
