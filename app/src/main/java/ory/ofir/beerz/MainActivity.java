package ory.ofir.beerz;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ory.ofir.beerz.View.BeersListFragment;
import ory.ofir.beerz.View.LoginFragment;


//import ory.ofir.beerz.View.BeersListFragment;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    final int REQUEST_WRITE_STORAGE = 1;
    private FirebaseAuth mAuth;
    Context contextCompat;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText("My Beers");
                    onHomeSelected();
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText("Top Rated Beers");
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    private void onHomeSelected(){
        BeersListFragment fragment = new BeersListFragment();
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        tran.replace(R.id.main_container, fragment);
        tran.addToBackStack("");
        tran.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        onHomeSelected();
        mAuth = FirebaseAuth.getInstance();

        contextCompat = getBaseContext();
/*
        if (savedInstanceState == null) {
            BeersListFragment fragment = new BeersListFragment();
            FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
            tran.replace(R.id.main_container, fragment);
            tran.addToBackStack("");
            tran.commit();
        }/*

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
            }
        }
        */
    }

    @Override
    public void onStart() {
        super.onStart();
        //BEGIN TESTING
        FirebaseAuth.getInstance().signOut();
        //FINISH TESTING

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d("tag", "currentUser is :" +currentUser);
        if(currentUser == null) {
            LoginFragment fragment = new LoginFragment();
            FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
            tran.replace(R.id.main_container, fragment);
            tran.commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

}
