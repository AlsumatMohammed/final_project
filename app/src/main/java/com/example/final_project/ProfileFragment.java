package com.example.final_project;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import cn.pedant.SweetAlert.SweetAlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    public CircularProgressButton logoutButton;
    public CircularProgressButton editprofileButton;

    public ImageView profileimageView;


    SweetAlertDialog pDialog;
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
        editprofileButton = view.findViewById(R.id.edit_profile_button__fragmentprofile);


        DatabaseReference databaseReference = firebaseDatabase.getReference();
        StorageReference storageReference = firebaseStorage.getReference();






        storageReference.child(firebaseAuth.getUid()).child("images").child("Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {


                Glide.with(getActivity()).load(uri).centerCrop().into(profileimageView);




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


                dismissDialog();


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



        return view;

    }

    public void showDialog(){
        pDialog.show();

        //editprofileButton.startAnimation();

    }

    public void dismissDialog(){
        pDialog.dismiss();

        //editprofileButton.revertAnimation();
    }

    public void signout(){

        firebaseAuth.signOut();

        Intent intent = new Intent(getActivity(), login_screen.class);
        startActivity(intent);

        getActivity().finish();


    }

}
