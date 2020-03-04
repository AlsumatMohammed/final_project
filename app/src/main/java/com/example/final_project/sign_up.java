package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.facebook.appevents.AppEventsLogger;
import com.facebook.FacebookSdk;
import com.tapadoo.alerter.Alerter;

public class sign_up extends AppCompatActivity {

    public TextInputLayout email_layout, password_layout;
    public TextView back_arrow;
    public CircularProgressButton sign_up_button;
    public TextInputEditText email_sign_up_et, password_sign_up_et;
    public FirebaseAuth mAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        progressDialog = new ProgressDialog(sign_up.this);
        progressDialog.setMessage("please wait!");
        progressDialog.setCancelable(false);
        //progressDialog.setProgressStyle(android.R.style.Widget_Material_Light_ProgressBar);

        progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);

        email_layout = findViewById(R.id.textInputLayoutEmail_signup);
        password_layout = findViewById(R.id.textInputLayoutPassword_signup);
        email_sign_up_et = findViewById(R.id.email_et_signup);
        password_sign_up_et = findViewById(R.id.password_et_signup);
        sign_up_button = findViewById(R.id.signup_button);
        back_arrow =findViewById(R.id.back_arrow);

        mAuth = FirebaseAuth.getInstance();

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

                //showProgress();
                spinButton();



                mAuth.createUserWithEmailAndPassword(email_sign_up_et.getText().toString().trim(), password_sign_up_et.getText().toString().trim())
                        .addOnCompleteListener(sign_up.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    //Log.d(TAG, "createUserWithEmail:success");
                                    //FirebaseUser user = mAuth.getCurrentUser();
                                    //updateUI(user);
//                                    Toast.makeText(sign_up.this, "done",
//                                            Toast.LENGTH_SHORT).show();
                                    //hideProgress();

                                    stopButton();

                                    Alerter.create(sign_up.this)
                                            .setTitle("Y-parts")
                                            .setText("Registered successfully. Try again !")
                                            .enableSwipeToDismiss()
                                            .setDuration(3000)
                                            .setBackgroundColorRes(R.color.text_color_orange)
                                            .show();

//                                    Snackbar.make(view, "Registered successfully!", Snackbar.LENGTH_LONG)
//                                            .setAction("Action", null).show();

                                    Intent intent = new Intent(sign_up.this, getting_you.class);
                                    startActivity(intent);
                                    finish();

                                }

                                else {
                                    // If sign in fails, display a message to the user.
                                    //Log.w(TAG, "createUserWithEmail:failure", task.getException());

                                    //hideProgress();

                                    stopButton();

                                    Alerter.create(sign_up.this)
                                            .setTitle("Y-parts")
                                            .setText("Registration failed or Account is already registered. Try again !")
                                            .enableSwipeToDismiss()
                                            .setDuration(3000)
                                            .setBackgroundColorRes(R.color.text_color_orange)
                                            .show();


//                                    Snackbar.make(view, "Registration failed or Account is already registered ", Snackbar.LENGTH_LONG)
//                                            .setAction("Action", null).show();
                                    //updateUI(null);
                                }

                                // ...
                            }
                        });

            }
        });




    }

    private boolean     validateEmail(){

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

    public void showProgress(){

        if (!progressDialog.isShowing()){
            progressDialog.show();
        }

    }

    public void hideProgress(){

        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    public void spinButton(){

        sign_up_button.startAnimation();

    }

    public void stopButton(){

        sign_up_button.revertAnimation();
    }


}
