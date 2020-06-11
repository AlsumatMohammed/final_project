package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import cn.pedant.SweetAlert.SweetAlertDialog;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class publishOffer extends AppCompatActivity {

    private static int PICK_IMAGE_CAMERA = 1;
    private static int PICK_IMAGE_GALLERY = 123;
    File f;

    SweetAlertDialog pDialog;

    // image activity for results
    String imageFilePath;
    public Uri imagePath;
    String ProductImage;
    String publisherImage;
    DatabaseReference db;
    offers offer;

    public String publisher_name = "";
    public String publisher_phone = "";
    public String publisher_email = "";
    public String publisherLatitude = "";
    public String publisherLongitude = "";

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
    String title = "";

    //description
    TextInputLayout description_layout;
    TextInputEditText description_et;
    String description ="";

    //imageButton
    Button addImage_but;
    ImageView check_image;
    ImageView loadedImage;

    //
    TextInputLayout price_etlayout;
    TextInputEditText price_et;
    String currency = "";
    String priceType = "";
    String price = "";



    String publishDate = "";

    RadioButton currencyReal, currencyDollar;

    //ad button
    CircularProgressButton publishOfferButton;
    String requestKey;
    boolean publisherImageConfirm = false, sendingImageConfirm = false, productImageReferenceConfirm = false, publiserInformationConfirm = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_offer);

        getPublisherInformation();

        Intent intent = this.getIntent();
        requestKey = intent.getExtras().getString("REQUESTKEY");
       // Toast.makeText(this, offerKey, Toast.LENGTH_SHORT).show();

        pDialog = new SweetAlertDialog(publishOffer.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setCancelable(false);

        title_layout = findViewById(R.id.titleLayoutPutOffer);
        title_et = findViewById(R.id.titleEtPutOffer);

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

        description_layout = findViewById(R.id.descriptionLayoutPutOffers);
        description_et = findViewById(R.id.descriptionEtPutOffers);

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

        price_layout = findViewById(R.id.priceRelativeLayoutPutOffers);
        categories_spinner = findViewById(R.id.categoriesSpinnerPutOffers);
        priceType_spinner = findViewById(R.id.priceTypeSpinnerPutOffers);
        condition_spinner = findViewById(R.id.conditionSpinnerPutOffers);
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

        warrantyNo = findViewById(R.id.radioNoWarrantyPutOffers);
        warrantyYes = findViewById(R.id.radioYesWarrantyPutOffers);


        final String[] priceType_array = getResources().getStringArray(R.array.priceType_array);

        ArrayAdapter<String> priceAdapter =
                new ArrayAdapter<String> (getApplicationContext(),
                        R.layout.spinner_layout, R.id.textView,
                        priceType_array);


        priceType_spinner.setAdapter(priceAdapter);

        priceType_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (priceType_spinner.getSelectedItem().toString().trim().equals("Free")){

                    priceType = "Free";
                    price_etlayout.setErrorEnabled(false);
                    price_layout.setVisibility(View.GONE);
                }

                else{
                    priceType = priceType_spinner.getSelectedItem().toString().trim();
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

        addImage_but = findViewById(R.id.addImageButtonPutOffers);
        check_image = findViewById(R.id.imageLoadedCheckPutOffers);
        check_image.setVisibility(View.INVISIBLE);
        loadedImage = findViewById(R.id.imageSelectedPutOffers);

        addImage_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callbottomsheet();
            }
        });

        publishOfferButton = findViewById(R.id.publishOfferButton);

        price_etlayout = findViewById(R.id.priceLayoutPutOffers);
        price_et = findViewById(R.id.priceEtPutOffers);
        price_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                isValidPrice();
            }
        });

        currencyReal = findViewById(R.id.radioRealPutOffers);
        currencyDollar = findViewById(R.id.radioDollarPutOffers);

        publishOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                offer = new offers();


                if (!isValidTitle()) {
                    return;
                }

                if (category.isEmpty() || category.equals("Select an Option")) {

                    Alerter.create(publishOffer.this)
                            .setTitle("Y-parts")
                            .setText("You must select a category. It will help in the search for your ADS !")
                            .enableSwipeToDismiss()
                            .setDuration(3000)
                            .setBackgroundColorRes(R.color.text_color_orange)
                            .show();
                    return;
                }

                if (condition.isEmpty() || condition.equals("Select an Option")) {

                    Alerter.create(publishOffer.this)
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

                if (!isValidDescription()){
                    return;
                }

                if (priceType.isEmpty() || priceType.equals("Select an Option")){

                    Alerter.create(publishOffer.this)
                            .setTitle("Y-parts")
                            .setText("You must select a price type.")
                            .enableSwipeToDismiss()
                            .setDuration(3000)
                            .setBackgroundColorRes(R.color.text_color_orange)
                            .show();

                    return;
                }

                if (!isValidPrice()){
                    return;
                }

                showDialog("Putting Offer, JUST A SECOND");
                spinButton();
                if (currencyReal.isChecked()){
                    currency = "YER";
                }
                if (currencyDollar.isChecked()){
                    currency = "$";
                }

                title = title_et.getText().toString().trim();
                description = description_et.getText().toString().trim();
                publishDate = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault()).format(new Date());

                if (priceType.equals("Free")){
                    price = "";
                    currency = "";
                }
                else {
                    price = price_et.getText().toString().trim();
                }

                db = FirebaseDatabase.getInstance().getReference();


                if (publisher_name.isEmpty() || publisher_phone.isEmpty() || publisher_email.isEmpty()){

                    Alerter.create(publishOffer.this)
                            .setTitle("Y-parts")
                            .setText("could not attain user's information, try again.")
                            .enableSwipeToDismiss()
                            .setDuration(3000)
                            .setBackgroundColorRes(R.color.text_color_orange)
                            .show();
                    Toast.makeText(publishOffer.this, "publisher information not acquired, try again", Toast.LENGTH_SHORT).show();
                    dismissDialog();
                    stopButton();
                    getPublisherInformation();
                    return;

                }



                getPublisherImage();


                offer.setPublisherEmail(publisher_email);
                offer.setPublisherPhoneNumber(publisher_phone);
                offer.setPublisherUsername(publisher_name);
                offer.setPublisherLatitude(publisherLatitude);
                offer.setPublisherLongitude(publisherLongitude);
                offer.setTitle(title);
                offer.setCategory(category);
                offer.setCondition(condition);
                offer.setWarranty(warranty);
                offer.setDescription(description);
                offer.setPriceType(priceType);
                offer.setPrice(price);
                offer.setCurrency(currency);
                offer.setPublishDate(publishDate);
                offer.setProductImage(" ");
                offer.setPublisherImage(" ");
                offer.setRequestKey(requestKey);

                DatabaseReference databaseReference = db.child("Offers").push();

                String key = databaseReference.getKey();
                offer.setOfferKey(key);
                databaseReference.setValue(offer);
                sendPicture();

            }
        });






    }

    public void sendPicture(){

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        final StorageReference imageRef = firebaseStorage.getReference().child("offers/"+ imagePath.getLastPathSegment());

        UploadTask uploadTask = imageRef.putFile(imagePath);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(publishOffer.this, "can't upload product image", Toast.LENGTH_SHORT).show();
                sendingImageConfirm = false;
                confirm(publiserInformationConfirm, publisherImageConfirm, sendingImageConfirm, productImageReferenceConfirm);

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(publishOffer.this, "image uploaded", Toast.LENGTH_SHORT).show();

                sendingImageConfirm = true;

                confirm(publiserInformationConfirm, publisherImageConfirm, sendingImageConfirm, productImageReferenceConfirm);

                StorageMetadata storageMetadata = taskSnapshot.getMetadata();
                Task<Uri> downloadUrl = imageRef.getDownloadUrl();
                downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        ProductImage = uri.toString();
                        db.child("Offers").child(offer.getOfferKey()).child("productImage").setValue(ProductImage);

                        productImageReferenceConfirm = true;
                        confirm(publiserInformationConfirm, publisherImageConfirm, sendingImageConfirm, productImageReferenceConfirm);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(publishOffer.this, "can't put image reference", Toast.LENGTH_SHORT).show();

                        productImageReferenceConfirm = false;
                        confirm(publiserInformationConfirm, publisherImageConfirm, sendingImageConfirm, productImageReferenceConfirm);

                    }
                });
            }
        });





    }

    public void getPublisherImage(){
        db = FirebaseDatabase.getInstance().getReference();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


        StorageReference publisher_reference = firebaseStorage.getReference(firebaseAuth.getUid()).child("images").child("Profile Pic");

        publisher_reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                publisherImage = uri.toString();
                db.child("Offers").child(offer.getOfferKey()).child("publisherImage").setValue(publisherImage);

                publisherImageConfirm = true;
                Toast.makeText(publishOffer.this, "publisher image updated", Toast.LENGTH_SHORT).show();
                confirm(publiserInformationConfirm, publisherImageConfirm, sendingImageConfirm, productImageReferenceConfirm);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                publisherImageConfirm = false;
                Toast.makeText(publishOffer.this, "couldn't get publisher image", Toast.LENGTH_SHORT).show();
                confirm(publiserInformationConfirm, publisherImageConfirm, sendingImageConfirm, productImageReferenceConfirm);


            }
        });






    }

    public void getPublisherInformation(){

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        databaseReference.child("userInformation").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Userinformation userProfile = dataSnapshot.getValue(Userinformation.class);

                publisher_name = userProfile.getUserName();
                publisher_email = firebaseUser.getEmail();
                publisher_phone = userProfile.getPhoneNumber();
                publisherLatitude = userProfile.getLatitude();
                publisherLongitude = userProfile.getLongitude();
                publiserInformationConfirm = true;
                Toast.makeText(publishOffer.this, "publisher Information acquired", Toast.LENGTH_SHORT).show();
                confirm(publiserInformationConfirm, publisherImageConfirm, sendingImageConfirm, productImageReferenceConfirm);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                publiserInformationConfirm = false;
                Toast.makeText(publishOffer.this, "publisher Information was not acquired", Toast.LENGTH_SHORT).show();

                confirm(publiserInformationConfirm, publisherImageConfirm, sendingImageConfirm, productImageReferenceConfirm);

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


            imagePath = data.getData();
            //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);

            check_image.setVisibility(View.VISIBLE);
            Toast.makeText(this, "imagePath updated ", Toast.LENGTH_SHORT).show();

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.ad_placeholder)
                    .error(R.drawable.ad_error);

            Glide.with(loadedImage.getContext())
                    .load(imagePath)
                    .apply(options).override(100, 100).centerCrop().into(loadedImage);

        }

        else if (requestCode == PICK_IMAGE_CAMERA && resultCode == RESULT_OK){
            imagePath = Uri.fromFile(f);

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.ad_placeholder)
                    .error(R.drawable.ad_error);

            Glide.with(loadedImage.getContext())
                    .load(imagePath)
                    .apply(options).override(100, 100).centerCrop().into(loadedImage);

        }
    }

    public boolean addPicture () {

        if (imagePath == null) {

            Alerter.create(publishOffer.this)
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

    private boolean  isValidPrice(){

        String price = price_et.getText().toString().trim();

        if (priceType.equals("Free")){
            return true;
        }

        else if (price.isEmpty()){
            price_etlayout.setError("Price field cannot be empty");
            return false;
        }


        else{
            price_etlayout.setErrorEnabled(false);
        }

        return true;

    }


    public void callbottomsheet() {

        View v = getLayoutInflater().inflate(R.layout.bottomsheet, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(publishOffer.this);
        dialog.setContentView(v);


        dialog.show();
        ImageView fromG = v.findViewById(R.id.add_from_gallery);

        ImageView fromC = v.findViewById(R.id.add_from_camera);

        fromG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pickFormGallery();
                dialog.hide();
            }
        });

        fromC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pickFormCamera();
                dialog.hide();
            }
        });

    }


    public void confirm(boolean publisherInformationConfirm, boolean publisherImageConfirm, boolean sendingImageConfirm, boolean productImageReferenceConfirm ){

        if (publisherInformationConfirm && publisherImageConfirm  && sendingImageConfirm && productImageReferenceConfirm ){

            Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
            dismissDialog();
            stopButton();
            new SweetAlertDialog(publishOffer.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Awesome!")
                    .setContentText("Offer published!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            Intent intent = new Intent(publishOffer.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .show();
        }
        else{
            Toast.makeText(this, "not yet", Toast.LENGTH_SHORT).show();
        }
    }

    public void pickFormCamera() {



        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.final_project", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoURI);
                startActivityForResult(pictureIntent,
                        PICK_IMAGE_CAMERA);
            }
        }

    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        f = new File(imageFilePath);
        return image;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void showDialog(String s ){
        pDialog.show();
        pDialog.setTitleText(s);

    }

    public void dismissDialog(){
        pDialog.dismiss();

    }

    public void spinButton() {

        publishOfferButton.startAnimation();

    }

    public void stopButton() {

        publishOfferButton.revertAnimation();
    }
}
