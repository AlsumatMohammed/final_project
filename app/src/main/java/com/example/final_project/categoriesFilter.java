package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import cn.pedant.SweetAlert.SweetAlertDialog;

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
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class categoriesFilter extends AppCompatActivity {


    private RecyclerView recyclerViewCategoriesFilter;
    private LinearLayoutManager linearLayoutManagerCategoriesFilter;
    private FirebaseRecyclerAdapter adapterCategoriesFilter;


    TextView categoryTv;

    SweetAlertDialog pDialog;
    CircularProgressButton addRequestsOffersButton;
    String category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_filter);


        pDialog = new SweetAlertDialog(categoriesFilter.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setCancelable(false);

        categoryTv = findViewById(R.id.categoryFilterTv);


        Intent intent = this.getIntent();

        category = intent.getExtras().getString("CATEGORY");

        categoryTv.setText(category);
        recyclerViewCategoriesFilter = findViewById(R.id.categoriesFilterRecyclerView);
        linearLayoutManagerCategoriesFilter = new LinearLayoutManager(categoriesFilter.this);
        recyclerViewCategoriesFilter.setLayoutManager(linearLayoutManagerCategoriesFilter);

        Toast.makeText(this, category, Toast.LENGTH_SHORT).show();


        fetchCategories();







    }
    public class CategoriesFilterViewHolder extends RecyclerView.ViewHolder {

        public TextView title, price, date, publisherName, category;
        public ImageView productimage, publisherImage;
        public RelativeLayout rev_layout;


        public CategoriesFilterViewHolder(@NonNull View itemView) {
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



    private void fetchCategories() {


        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("GeneralAds").orderByChild("category").equalTo(category);




        FirebaseRecyclerOptions<generalAds> options =
                new FirebaseRecyclerOptions.Builder<generalAds>()
                        .setQuery(query, new SnapshotParser<generalAds>() {
                            @NonNull
                            @Override
                            public generalAds parseSnapshot(@NonNull DataSnapshot snapshot) {
                                   showDialog("Loading");
                                generalAds generalAd = snapshot.getValue(generalAds.class);



                                return generalAd;
                            }
                        }).build();


        //adapter settings



        adapterCategoriesFilter = new FirebaseRecyclerAdapter<generalAds, CategoriesFilterViewHolder>(options) {


            @NonNull
            @Override
            public CategoriesFilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.generic_ads_layout, parent, false);

                return new CategoriesFilterViewHolder(itemview);

            }

            @Override
            protected void onBindViewHolder(@NonNull final CategoriesFilterViewHolder holder, int position, @NonNull final generalAds model) {

                holder.publisherImage.setAnimation(AnimationUtils.loadAnimation(categoriesFilter.this, R.anim.rv_fade_transition));
                holder.rev_layout.setAnimation(AnimationUtils.loadAnimation(categoriesFilter.this, R.anim.rv_scale_animation));

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
                                , model.getPublisherUsername() , model.getPublisherEmail(), model.getPublisherphoneNumber(), model.getKey(), model.getTitle());

                    }
                });
            }
        };


        recyclerViewCategoriesFilter.setAdapter(adapterCategoriesFilter);


    }

    public void openDetailActivity(String... detail){

        Intent intent = new Intent(this, detail_activity.class);

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


        startActivity(intent);



    }

    @Override
    public void onStart() {
        super.onStart();
        adapterCategoriesFilter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapterCategoriesFilter.stopListening();
    }




    public void showDialog(String s ){
        pDialog.setTitleText(s);

        pDialog.show();

        //editprofileButton.startAnimation();

    }

    public void dismissDialog(){
        pDialog.dismiss();

        //editprofileButton.revertAnimation();
    }
}
