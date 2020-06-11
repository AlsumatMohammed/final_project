package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import cn.pedant.SweetAlert.SweetAlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
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
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.facebook.appevents.AppEventsLogger;
import com.facebook.FacebookSdk;
import com.google.firebase.auth.GoogleAuthProvider;
import com.tapadoo.alerter.Alerter;

public class sign_up extends AppCompatActivity {

    SweetAlertDialog pDialog;
    public TextInputLayout email_layout, password_layout;
    public TextView back_arrow;
    public CircularProgressButton sign_up_button;
    public TextInputEditText email_sign_up_et, password_sign_up_et;
    public FirebaseAuth mAuth;
    //private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }


        setContentView(R.layout.activity_sign_up);


//        progressDialog = new ProgressDialog(sign_up.this);
//        progressDialog.setMessage("please wait!");
//        progressDialog.setCancelable(false);

        changeStatusBarColor();
        pDialog = new SweetAlertDialog(sign_up.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setCancelable(false);




        email_layout = findViewById(R.id.textInputLayoutEmail_signup);
        password_layout = findViewById(R.id.textInputLayoutPassword_signup);
        email_sign_up_et = findViewById(R.id.email_et_signup);
        password_sign_up_et = findViewById(R.id.password_et_signup);
        sign_up_button = findViewById(R.id.signup_button);
        back_arrow =findViewById(R.id.back_arrow);

        mAuth = FirebaseAuth.getInstance();

        //facebook Authentication





        email_sign_up_et.addTextChangedListener(new TextWatcher() {
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

        password_sign_up_et.addTextChangedListener(new TextWatcher() {
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
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), login_screen.class);
                startActivity(intent);
                finish();
            }
        });

        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {


                if (!validateEmail()){
                    //email_layout.setError("Enter a valid email");
                    return;
                }
                if (!validatePassword()){
                    return;
                }
//                String email_signup_string = email_sign_up_et.getText().toString().trim();
//                String password_singup_string = password_sign_up_et.getText().toString().trim();

                showDialog("Signing Up!, Just a Moment");
                spinButton();



                mAuth.createUserWithEmailAndPassword(email_sign_up_et.getText().toString().trim(), password_sign_up_et.getText().toString().trim())
                        .addOnCompleteListener(sign_up.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    stopButton();
                                    dismissDialog();

                                    new SweetAlertDialog(sign_up.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Awesome!")
                                            .setContentText("Registered successfully!")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    Intent intent = new Intent(sign_up.this, getting_you.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            })
                                            .show();


//



                                }

                                else {
                                    // If sign in fails, display a message to the user.
                                    //Log.w(TAG, "createUserWithEmail:failure", task.getException());

                                    dismissDialog();

                                    stopButton();

                                    Alerter.create(sign_up.this)
                                            .setTitle("Y-parts")
                                            .setText("Registration failed or Account is already registered. Try again !")
                                            .enableSwipeToDismiss()
                                            .setDuration(3000)
                                            .setBackgroundColorRes(R.color.text_color_orange)
                                            .show();



                                }


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
    public void handleFacebookToken(AccessToken token){

        showDialog("Just a second");

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(sign_up.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                dismissDialog();

                    Intent intent = new Intent(sign_up.this, getting_you.class);
                    startActivity(intent);
                    finish();

                    Toast.makeText(sign_up.this, "cool", Toast.LENGTH_SHORT).show();

                }

                else{
                    dismissDialog();
                    Toast.makeText(sign_up.this, "Authentication failed", Toast.LENGTH_SHORT).show();

                }


            }
        });


    }








    private boolean validateEmail(){

        String email = email_sign_up_et.getText().toString().trim();
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
        String password = password_sign_up_et.getText().toString().trim();

        if (password.length()<8){

            password_layout.setError("Password must be at least 8 digits");
            return false;
        }
        else {
            password_layout.setErrorEnabled(false);
        }
        return true;
    }



    public void spinButton(){

        sign_up_button.startAnimation();

    }

    public void stopButton(){

        sign_up_button.revertAnimation();
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
