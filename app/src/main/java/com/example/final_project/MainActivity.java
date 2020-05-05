package com.example.final_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import io.grpc.internal.SharedResourceHolder;

import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    BottomNavigationView navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(toolbar);

//        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
//        progressDialog.setMessage("please wait!");
//        progressDialog.setCancelable(false);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.show();


        loadFragment(new AdsFragment());

        navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.ads);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mAuth = FirebaseAuth.getInstance();

        firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser == null){
            Intent intent = new Intent(MainActivity.this, login_screen.class);
            startActivity(intent);
            finish();
        }

        String email = firebaseUser.getEmail();

//        TextView textView = findViewById(R.id.textView4);
//
//        textView.setText(email);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                mAuth.signOut();

                Intent intent = new Intent(MainActivity.this, login_screen.class);

                startActivity(intent);
                finish();
            }
        });
        fab.hide();
    }

    private void loadFragment(Fragment fragment){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();



    }

    @Override
    public void onBackPressed() {

        int selecteditem = navigation.getSelectedItemId();

        if (R.id.ads != selecteditem){
            navigation.setSelectedItemId(R.id.ads);

        }
//        else if (R.id.request_offers != selecteditem){
//            navigation.setSelectedItemId(R.id.request_offers);
//
//        }
//        else if (R.id.profile != selecteditem){
//            navigation.setSelectedItemId(R.id.profile);
//
//        }

        else{

            super.onBackPressed();
        }


    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    Fragment fragment;

                    switch (menuItem.getItemId()) {
                        case R.id.request_offers:

                            fragment = new RequestsOffersFragment();
                            loadFragment(fragment);
                            return true;

                        case R.id.ads:

                            fragment = new AdsFragment();
                            loadFragment(fragment);
                            return true;

                        case R.id.profile:

                            fragment = new ProfileFragment();
                            loadFragment(fragment);
                            return true;
                    }
                    return false;
                }
            };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


