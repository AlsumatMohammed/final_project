package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import cn.pedant.SweetAlert.SweetAlertDialog;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tapadoo.alerter.Alerter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class edit_profile extends AppCompatActivity {

    private static  final int REQUEST_LOCATION=2;
    SweetAlertDialog pDialog;
    private static int PICK_IMAGE_GALLERY = 123;
    private static int PICK_IMAGE_CAMERA = 1;
    public ImageView profileImageView;
    public Uri imagePath;

    public TextView email;
    public TextInputLayout username_layout, phone_layout;
    public TextInputEditText username_ed, phone_et;
    public RadioButton customer_rd, supplier_rd;
    //public String user_type;
    //public String phone;

    public CircularProgressButton edit_butt;

    //firebase

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;


    File f;


    // image activity for results
    String imageFilePath;

    boolean imageCheck= false, informationCheck = false;
    String latitude = "", longitude = "";
    LocationManager locationManager;
    ImageView getLocationButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        ActivityCompat.requestPermissions(this,new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        pDialog = new SweetAlertDialog(edit_profile.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setCancelable(false);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        getLocationButton = findViewById(R.id.locationButton);
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        //userInformationNull();

        profileImageView = findViewById(R.id.profile_image_editprofile);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callbottomsheet();

            }
        });


        if (firebaseAuth.getCurrentUser() == null) {

            startActivity(new Intent(edit_profile.this, login_screen.class));
        }

        email = findViewById(R.id.userEmail_et);
        email.setText(firebaseUser.getEmail());
        username_layout = findViewById(R.id.customer_supplier_name_inputlayout);
        username_ed = findViewById(R.id.customer_supplier_name_et);

        username_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                isValidName();

            }
        });
        phone_layout = findViewById(R.id.customer_supplier_phone_inputlayout);
        phone_et = findViewById(R.id.customer_supplier_phone_et);

        phone_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                isValidPhone();

            }
        });
        edit_butt = findViewById(R.id.edit_profile_button);
        customer_rd = findViewById(R.id.radio_customer);
        supplier_rd = findViewById(R.id.radio_supplier);


        //firebase


        //end firebase

        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);

                //Check gps is enable or not

                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                {
                    //Write Function To enable gps

                    OnGPS();
                }
                else
                {
                    //GPS is already On then

                    getLocation();
                }
            }
        });

        edit_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isValidName()) {
                    return;
                }

                if (!isValidPhone()) {
                    return;
                }

                if (!addPicture()) {
                    return;
                }

                if (latitude.isEmpty() || longitude.isEmpty()){
                    Alerter.create(edit_profile.this)
                            .setTitle("Y-parts")
                            .setText("Couldn't Get Location, click on location icon!")
                            .enableSwipeToDismiss()
                            .setDuration(3000)
                            .setBackgroundColorRes(R.color.text_color_orange)
                            .show();
                    return;
                }

                spinButton();
                showDialog("Creating Profile, Just a Moment!");
                userInformation();
                sendUserData();
                Toast.makeText(edit_profile.this, "all done", Toast.LENGTH_SHORT).show();


            }
        });


    }

    //onActivityResult

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == PICK_IMAGE_GALLERY && resultCode == RESULT_OK && data.getData() != null) {


            imagePath = data.getData();
            //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);


            Glide.with(edit_profile.this).load(imagePath).centerCrop().into(profileImageView);


        } else if (requestCode == PICK_IMAGE_CAMERA && resultCode == RESULT_OK) {

            imagePath = Uri.fromFile(f);

            Glide.with(this).load(imagePath).centerCrop().into(profileImageView);

            //imagePath = Uri.parse("camera");

//            Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
//
//            profileImageView.setImageBitmap(selectedImage);

            //imagePath = getImageUri(edit_profile.this, v);

            //Glide.with(edit_profile.this).load(imagePath).centerCrop().into(profileImageView);

//            if (resultCode == Activity.RESULT_OK) {
//                Glide.with(this).load(imageFilePath).into(profileImageView);
//            }
//            else if(resultCode == Activity.RESULT_CANCELED) {
//                Toast.makeText(this, "image uplad canceled", Toast.LENGTH_SHORT).show();
//            }

        } else {

            Alerter.create(edit_profile.this)
                    .setTitle("Y-parts")
                    .setText("Could not load image, Try again !")
                    .enableSwipeToDismiss()
                    .setDuration(3000)
                    .setBackgroundColorRes(R.color.text_color_orange)
                    .show();
        }
        super.onActivityResult(requestCode, resultCode, data);

    }


    public void callbottomsheet() {

        View v = getLayoutInflater().inflate(R.layout.bottomsheet, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(edit_profile.this);
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

    public void pickFormGallery() {

        Intent profileIntent = new Intent();
        profileIntent.setType("image/*");


        profileIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(profileIntent, "select image."), PICK_IMAGE_GALLERY);

    }

    public void pickFormCamera() {

//        Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(takePicture, PICK_IMAGE_CAMERA);

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


    public boolean isValidName() {

        String userName = username_ed.getText().toString().trim();
        if (userName.isEmpty()) {

            username_layout.setError("Username cannot be empty");
            return false;
        } else {


            username_layout.setErrorEnabled(false);
        }

        return true;
    }

    public boolean isValidPhone() {

        String phoneNumber = phone_et.getText().toString().trim();
        if (phoneNumber.isEmpty()) {

            phone_layout.setError("Phone field cannot be empty");
            return false;
        }

        if (!((phoneNumber.length() == 9) || (phoneNumber.length() == 6) || (phoneNumber.length() == 8))) {

            phone_layout.setError("phone number can only be 9, 8, or 6 digits ");
            return false;

        } else {

            phone_layout.setErrorEnabled(false);
        }

        return true;
    }

    public boolean addPicture() {

        if (imagePath == null) {

            Alerter.create(edit_profile.this)
                    .setTitle("Y-parts")
                    .setText("Click on avatar to add image")
                    .enableSwipeToDismiss()
                    .setDuration(3000)
                    .setBackgroundColorRes(R.color.text_color_orange)
                    .show();

            return false;

        }

        return true;
    }

    private void userInformation() {

        String userName = username_ed.getText().toString().trim();
        String user_type = null;
        String phone = phone_et.getText().toString().trim();

        if (customer_rd.isChecked()) {
            user_type = "customer";
        }
        if (supplier_rd.isChecked()) {
            user_type = "supplier";
        }

        String publisherState = "unverified";

        Userinformation userinformation = new Userinformation(userName, user_type, phone, latitude, longitude, publisherState);


        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference.child("userInformation").child(firebaseUser.getUid()).setValue(userinformation);
        informationCheck = true;
        Toast.makeText(this, "user information updated", Toast.LENGTH_SHORT).show();
        confirm(imageCheck, informationCheck);

    }



    private void sendUserData() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        StorageReference imageReference = storageReference.child(firebaseAuth.getUid()).child("images").child("Profile Pic");
        UploadTask uploadTask = imageReference.putFile(imagePath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {



                Alerter.create(edit_profile.this)
                        .setTitle("Y-parts")
                        .setText(" Something went wrong, Could not upload image, Try again !")
                        .enableSwipeToDismiss()
                        .setDuration(3000)
                        .setBackgroundColorRes(R.color.text_color_orange)
                        .show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {



                imageCheck = true;
                Toast.makeText(edit_profile.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                confirm(imageCheck, informationCheck);
            }
        });
    }

    public void confirm(boolean publisherInformationConfirm, boolean publisherImageConfirm ){

        if (publisherInformationConfirm && publisherImageConfirm ){

            Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
            dismissDialog();
            new SweetAlertDialog(edit_profile.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Awesome!")
                    .setContentText("Profile Created, Enjoy!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            Intent intent = new Intent(edit_profile.this, MainActivity.class);
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

    private void getLocation() {

        //Check Permissions again

        showDialog("Getting location...");
        if (ActivityCompat.checkSelfPermission(edit_profile.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(edit_profile.this,

                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            Location LocationGps= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps !=null)
            {
                double lat=LocationGps.getLatitude();
                double longi=LocationGps.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);
                dismissDialog();
                Toast.makeText(this, "Location acquired", Toast.LENGTH_SHORT).show();

            }
            else if (LocationNetwork !=null)
            {
                double lat=LocationNetwork.getLatitude();
                double longi=LocationNetwork.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);
                dismissDialog();
                Toast.makeText(this, "Location acquired", Toast.LENGTH_SHORT).show();


            }
            else if (LocationPassive !=null)
            {
                double lat=LocationPassive.getLatitude();
                double longi=LocationPassive.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);
                dismissDialog();
                Toast.makeText(this, "Location acquired", Toast.LENGTH_SHORT).show();


            }
            else
            {
                dismissDialog();
                Alerter.create(edit_profile.this)
                        .setTitle("Y-parts")
                        .setText("Can't get your location, try again !")
                        .enableSwipeToDismiss()
                        .setDuration(3000)
                        .setBackgroundColorRes(R.color.text_color_orange)
                        .show();
            }



            //That's All Run Your App
        }



    }

    private void OnGPS() {

        final AlertDialog.Builder builder= new AlertDialog.Builder(this);

        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }


    public void spinButton() {

        edit_butt.startAnimation();

    }

    public void stopButton() {

        edit_butt.revertAnimation();
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
