package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import cn.pedant.SweetAlert.SweetAlertDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.SigningInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.tapadoo.alerter.Alerter;

public class login_screen extends AppCompatActivity implements View.OnClickListener {

//    Button send_data;
//
//    EditText key;
//    EditText value;
//
//    TextView textchange;

    public TextInputLayout email_layout;
    public TextInputEditText email_editText;
    public TextInputLayout password_layout;
    public TextInputEditText password_editText;
    public CircularProgressButton sign_in_button;
    public TextView sign_up_textview;

    SweetAlertDialog pDialog;


    private FirebaseAuth mAuth;

    //facebook variables
    private CallbackManager callbackManager;
    private LoginButton loginButton;


    //google variables
    private SignInButton Google_signInButton;

    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 9001;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_login_screen);

        mAuth = FirebaseAuth.getInstance();

        pDialog = new SweetAlertDialog(login_screen.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Just a second...");
        pDialog.setCancelable(false);

        changeStatusBarColor();
//        FacebookSdk.sdkInitialize(login_screen.this);
//        AppEventsLogger.activateApp(login_screen.this);


        //facebook Authentication



        loginButton = findViewById(R.id.facebook_icon);
        callbackManager = CallbackManager.Factory.create();



        loginButton.setPermissions("email", "public_profile");


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

              handleFacebookToken(loginResult.getAccessToken());
                //Toast.makeText(login_screen.this, "okay", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancel() {

                Toast.makeText(login_screen.this, "cancel", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {

                //Log.d(TAG, "facebook:onError", error);
                Toast.makeText(login_screen.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        //end facebook


        //google authentication

        Google_signInButton = findViewById(R.id.google_button);

        GoogleSignInOptions gso  = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(login_screen.this, gso);

        Google_signInButton.setOnClickListener(this);


        // end google



        email_layout = findViewById(R.id.textInputLayoutEmail);
        email_editText = findViewById(R.id.email_et_signin);
        password_layout = findViewById(R.id.textInputLayoutPassword);
        password_editText = findViewById(R.id.password_et_signin);
        sign_in_button = findViewById(R.id.signin_button);
        sign_up_textview = findViewById(R.id.signup_tv);

        final Context c = getApplicationContext();

        sign_up_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), sign_up.class);
                startActivity(intent);
            }
        });

        email_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {


                validateEmail();
            }
        });
        password_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                validatePassword();
            }
        });

        sign_in_button.setOnClickListener(new View.OnClickListener() {




            String email_signin_string = email_editText.getText().toString().trim();
            String password_singin_string = password_editText.getText().toString().trim();
            @Override
            public void onClick( final View view) {



                if (!validateEmail()){
                    return;
                }

                if (!validatePassword()){
                    return;
                }


               //showProgress();
                spinButton();

                mAuth.signInWithEmailAndPassword(email_editText.getText().toString().trim(), password_editText.getText().toString().trim())
                        .addOnCompleteListener(login_screen.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    //hideProgress();
                                    stopButton();

                                    Snackbar.make(view, " okaaay!", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();

                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(i);
                                    finish();
                                }

                                else {

                                    //hideProgress();
                                    stopButton();


                                    Alerter.create(login_screen.this)
                                            .setTitle("Y-parts")
                                            .setText("something went wrong. Try again !")
                                            .enableSwipeToDismiss()
                                            .setDuration(3000)
                                            .setBackgroundColorRes(R.color.text_color_orange)

                                            .show();

                                }

                                // ...
                            }
                        });

            }
        });



    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN){

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try{

                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithgoogle(account);
            }



            catch (ApiException e){

                Toast.makeText(this, "could not sign in with google", Toast.LENGTH_SHORT).show();

            }


        }
    }



    public void handleFacebookToken(AccessToken token){

        showProgress();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(login_screen.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    hideProgress();

                    new SweetAlertDialog(login_screen.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Welcome!")
                            .setContentText("Continue to app!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            })
                            .show();

                }

                else{
                    hideProgress();
                    Toast.makeText(login_screen.this, "Authentication failed", Toast.LENGTH_SHORT).show();

                }


            }
        });


    }

    private void firebaseAuthWithgoogle(GoogleSignInAccount acct){

        showProgress();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(login_screen.this, "task successful", Toast.LENGTH_SHORT).show();

                    new SweetAlertDialog(login_screen.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Welcome!")
                            .setContentText("Continue to app!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            })
                            .show();
                }

                else{
                    //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                    Toast.makeText(login_screen.this, "Authentication failed", Toast.LENGTH_SHORT).show();

                }

                hideProgress();
            }
        });


    }

    private void signIn(){

        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }



    public void signout(){
        mAuth.signOut();
        LoginManager.getInstance().logOut();

        googleSignInClient.signOut().addOnCompleteListener(login_screen.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(login_screen.this, "Signed out successfully ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void revokeAccess(){

        mAuth.signOut();

        googleSignInClient.revokeAccess().addOnCompleteListener(login_screen.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(login_screen.this, "revoked Access Successfully", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View view) {

        int i = view.getId();


        if (i == R.id.google_button){

            signIn();
        }

//        else if (i == R.id.sign_out){
//            signout();
//        }
//
//        else if (i == R.id.revoke_access){
//            revokeAccess();
//
//        }

    }



        @Override
    protected void onStart() {
            super.onStart();

            TextView t = findViewById(R.id.welcome);
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {


                String s = user.getEmail();

                if (s != null) {

                    t.setText(s);
                } else {
                    t.setText("welcome");
                }
            } else {
                t.setText("welcome");
            }
        }

    //                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//
//                            startActivity(intent);





    private boolean validateEmail(){

        String email = email_editText.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)){

            email_layout.setError("Enter a vaild Email address");


            return false;

        }

        else{
            email_layout.setErrorEnabled(false);
        }

        return true;
    }


    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validatePassword(){
        String password = password_editText.getText().toString().trim();

        if (password.length()<8){

            password_layout.setError("Password must be at least 8 digits");
            return false;
        }
        else {
            password_layout.setErrorEnabled(false);
        }
        return true;
    }


    public void showProgress(){
        if (!pDialog.isShowing()){
            pDialog.show();
        }

        //editprofileButton.startAnimation();

    }

    public void hideProgress(){
        if (pDialog.isShowing()){
            pDialog.dismiss();
        }
    }

        //editprofileButton.revertAnimation();






    public void spinButton(){

       sign_in_button.startAnimation();

    }

    public void stopButton(){

        sign_in_button.revertAnimation();
    }




//        key = findViewById(R.id.email_et);
//        value = findViewById(R.id.password_et);
//        textchange = findViewById(R.id.textView);
//
//        send_data = findViewById(R.id.send_data);
//        send_data.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                try {
//
//                    FirebaseDatabase database = FirebaseDatabase.getInstance();
//                    DatabaseReference myRef = database.getReference();
//
//
//
//                    //writing data to the database
//
////                    String keyy = key.getText().toString();
////                    String valuee = value.getText().toString();
////
////
////                    //myRef.child("Users").child(key.getText().toString()).setValue(value.getText().toString());
////
////                    int i = 0;
////                    myRef.child("myUsers").push().child(Integer.toString(i++)).child(keyy).setValue(valuee);
////                    //myRef.getchsetValue("mohammed Alsumat!");
//
//
////
////                    ///////////////////////////////////////////////
////
////                    //retrieving from the database
//
////                    myRef.child("Users").child("user_name").addValueEventListener(new ValueEventListener() {
////                        @Override
////                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////
////                            String value = dataSnapshot.getValue(String.class);
////
////                            textchange.setText(value);
////                        }
////
////                        @Override
////                        public void onCancelled(@NonNull DatabaseError databaseError) {
////
////                        }
////                    });
//                        /////////////////////////////////////////////////
//
//
//                }
//                catch (Exception e){
//
//                    Toast.makeText(getApplicationContext(), "nooo", Toast.LENGTH_LONG).show();
//                }
//
//
//            }
//        });
//









}
