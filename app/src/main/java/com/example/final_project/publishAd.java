package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tapadoo.alerter.Alerter;

public class publishAd extends AppCompatActivity {

    private static int PICK_IMAGE_GALLERY = 123;
    public Uri imagepath;
    String ProductImage;
    String publisherImage;
    DatabaseReference db;
    generalAds generalAd;

    public String publisher_name;
    public String publisher_phone;
    public String publisher_email;

    //categories
    public Spinner categories_spinner;
    String category = "";

    public Spinner priceType_spinner;
    //condition
    public Spinner condition_spinner;
    String condition = "";

    //radioButtons
    RadioButton warrantyNo, warrantyYes;
    String warranty;

    public RelativeLayout price_layout;

    //title
    TextInputLayout title_layout;
    TextInputEditText title_et;

    //description
    TextInputLayout description_layout;
    TextInputEditText description_et;

    //imageButton
    Button addImage_but;
    ImageView check_image;
    ImageView loadedImage;

    //ad button
    CircularProgressButton publishad_but;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_ad);

        //title
        title_layout = findViewById(R.id.title_et_layout);
        title_et = findViewById(R.id.title_et);

        title_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                isValidTitle();
            }
        });

        //description

        description_layout = findViewById(R.id.description_layout);
        description_et = findViewById(R.id.description_et);

        description_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                isValidDescription();
            }
        });
        price_layout = findViewById(R.id.price_relative_layout);
        categories_spinner = findViewById(R.id.categories_spinner);
        priceType_spinner = findViewById(R.id.priceType_spinner);
        condition_spinner = findViewById(R.id.condition_spinner);
        //categories Spinner
        final String[] categories_array = getResources().getStringArray(R.array.categories_array);
        ArrayAdapter<String> categoriesAdapter =
                new ArrayAdapter<String> (getApplicationContext(),
                        R.layout.spinner_layout, R.id.textView,
                        categories_array);


        categories_spinner.setAdapter(categoriesAdapter);

        categories_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String category_selected = categories_spinner.getSelectedItem().toString().trim();

                category = category_selected;


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //RadioButtons

        warrantyNo = findViewById(R.id.radio_nowarranty);
        warrantyYes = findViewById(R.id.radio_yeswarranty);


        //priceType spinner

        final String[] priceType_array = getResources().getStringArray(R.array.priceType_array);

        ArrayAdapter<String> priceAdapter =
                new ArrayAdapter<String> (getApplicationContext(),
                        R.layout.spinner_layout, R.id.textView,
                        priceType_array);


        priceType_spinner.setAdapter(priceAdapter);

        priceType_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (priceType_spinner.getSelectedItem() == "Free"){

                    price_layout.setVisibility(View.INVISIBLE);
                }

                else{
                    price_layout.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final String[] condition_array = getResources().getStringArray(R.array.condition_array);
        ArrayAdapter<String> condition_Adapter =
                new ArrayAdapter<String> (getApplicationContext(),
                        R.layout.spinner_layout, R.id.textView,
                        condition_array);


        condition_spinner.setAdapter(condition_Adapter);

        condition_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String condition_selected = condition_spinner.getSelectedItem().toString().trim();
                condition = condition_selected;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addImage_but = findViewById(R.id.add_image_button);
        check_image = findViewById(R.id.image_loaded_check);
        check_image.setVisibility(View.INVISIBLE);
        loadedImage = findViewById(R.id.image_selected);

        addImage_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFormGallery();
            }
        });

        publishad_but = findViewById(R.id.publish_ad_button);

        publishad_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        publishad_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getPublisherInfromation();

                if (!isValidTitle()) {
                    return;
                }

                if (category.isEmpty() || category.equals("Select an Option")) {

                    Alerter.create(publishAd.this)
                            .setTitle("Y-parts")
                            .setText("You must select a category. It will help in the search for your ADS !")
                            .enableSwipeToDismiss()
                            .setDuration(3000)
                            .setBackgroundColorRes(R.color.text_color_orange)
                            .show();
                    return;
                }

                if (condition.isEmpty() || condition.equals("Select an Option")) {

                    Alerter.create(publishAd.this)
                            .setTitle("Y-parts")
                            .setText("You must select a category. It will help in the search for your ADS !")
                            .enableSwipeToDismiss()
                            .setDuration(3000)
                            .setBackgroundColorRes(R.color.text_color_orange)
                            .show();
                    return;

                }

                if (warrantyNo.isChecked()){
                    warranty = "No";
                }
                if (warrantyYes.isChecked()){
                    warranty = "Yes";
                }

                if (!addPicture()){
                    return;
                }

                Toast.makeText(publishAd.this, category+" "+condition+" "+warranty+" "+imagepath, Toast.LENGTH_SHORT).show();
            }
        });
//        Button button = findViewById(R.id.button2);
//        Button button1 = findViewById(R.id.button3);
//
//
//        getPublisherInfromation();
//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                pickFormGallery();
//            }
//        });
//
//
//
//
//
//
//
//         db = FirebaseDatabase.getInstance().getReference();
//        //final FirebaseAdsHelper firebaseAdsHelper = new FirebaseAdsHelper(db);
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                 generalAd = new generalAds();
//
//                if (!addPicture()){
//
//                    return;
//                }
//
//
//                if (publisher_name.isEmpty() || publisher_phone.isEmpty() || publisher_email.isEmpty()){
//
//                    Toast.makeText(publishAd.this, "publisher information not acquired", Toast.LENGTH_SHORT).show();
//                    getPublisherInfromation();
//
//                }
//
//                getPublisherImage();
//
//                generalAd.setPublisherEmail(publisher_email);
//                generalAd.setPublisherphoneNumber(publisher_phone);
//                generalAd.setPublisherUsername(publisher_name);
//                generalAd.setPrice("$200");
//                generalAd.setTitle("Brake system");
//                generalAd.setProductImage(" ");
//                generalAd.setPublisherImage(" ");
//
//                DatabaseReference databaseReference = db.child("GeneralAds").push();
//                databaseReference.setValue(generalAd);
//                String key = databaseReference.getKey();
//                generalAd.setKey(key);
//
//                sendPicture();
//
//
//            }
//        });
    }

    public void sendPicture(){

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        final StorageReference imageRef = firebaseStorage.getReference().child("genericAds/"+imagepath.getLastPathSegment());

        UploadTask uploadTask = imageRef.putFile(imagepath);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(publishAd.this, "can't upload image", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(publishAd.this, "image uploaded", Toast.LENGTH_SHORT).show();

                StorageMetadata storageMetadata = taskSnapshot.getMetadata();
                Task<Uri> downloadUrl = imageRef.getDownloadUrl();
                downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        ProductImage = uri.toString();
                        db.child("GeneralAds").child(generalAd.getKey()).child("productImage").setValue(ProductImage);


                    }
                });
            }
        });





    }

    public void getPublisherImage(){
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


        StorageReference publisher_reference = firebaseStorage.getReference(firebaseAuth.getUid()).child("images").child("Profile Pic");

        publisher_reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                publisherImage = uri.toString();
                db.child("GeneralAds").child(generalAd.getKey()).child("publisherImage").setValue(publisherImage);

                Toast.makeText(publishAd.this, "publisher image updated", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(publishAd.this, "couldn't get publisher image", Toast.LENGTH_SHORT).show();
            }
        });






    }

    public void getPublisherInfromation(){

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        databaseReference.child("userInfromation").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Userinformation userProfile = dataSnapshot.getValue(Userinformation.class);

                publisher_name = userProfile.getUserName();
                publisher_email = firebaseUser.getEmail();
                publisher_phone = userProfile.getPhoneNumber();
                Toast.makeText(publishAd.this, "publisher Information acquired", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(publishAd.this, "publisher Information was not acquired", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void pickFormGallery(){

        Intent profileIntent = new Intent();
        profileIntent.setType("image/*");


        profileIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(profileIntent, "select image."), PICK_IMAGE_GALLERY);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_GALLERY && resultCode == RESULT_OK && data.getData() != null) {


            imagepath = data.getData();
            //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);

            check_image.setVisibility(View.VISIBLE);
            Toast.makeText(this, "imagePath updated ", Toast.LENGTH_SHORT).show();

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.avatar)
                    .error(R.drawable.ads_icon);

            Glide.with(loadedImage.getContext())
                    .load(imagepath)
                    .apply(options).override(100, 100).centerCrop().into(loadedImage);

        }
    }

        public boolean addPicture () {

            if (imagepath == null) {

                Alerter.create(publishAd.this)
                        .setTitle("Y-parts")
                        .setText("You must add a picture of the product")
                        .enableSwipeToDismiss()
                        .setDuration(3000)
                        .setBackgroundColorRes(R.color.text_color_orange)
                        .show();

                return false;

            }

            return true;
        }

        public boolean isValidTitle(){

        String title = title_et.getText().toString().trim();

        if (title.isEmpty()){
            title_layout.setError("Title field cannot be empty");
            return false;
        }

        else{
            title_layout.setErrorEnabled(false);
        }

        return true;
        }

        public boolean isValidDescription(){

        String description = description_et.getText().toString().trim();

        if (description.isEmpty()){

            description_layout.setError("Description field cannot be empty!");
            return false;
        }

        else {

            description_layout.setErrorEnabled(false);

        }
        return true;
        }


    }
