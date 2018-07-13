package ory.ofir.beerz;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ory.ofir.beerz.Model.Beer;
import ory.ofir.beerz.Model.Model;
import ory.ofir.beerz.View.BeersListFragment;
import ory.ofir.beerz.View.LoginFragment;


//import ory.ofir.beerz.View.BeersListFragment;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_WRITE_STORAGE = 112;

    private TextView mTextMessage;
    //final int REQUEST_WRITE_STORAGE = 1;
    private FirebaseAuth mAuth;
    public static Context contextCompat;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText("My Beers");
                    onHomeSelected();
                    return true;
                case R.id.navigation_all_beers:
                    mTextMessage.setText("All Beers");
                    return true;
                case R.id.navigation_top_beers:
                    mTextMessage.setText("Top Beers");
                    return true;
            }
            return false;
        }
    };

    private void onHomeSelected(){
        BeersListFragment fragment = new BeersListFragment();
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        tran.replace(R.id.main_container, fragment);
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(view.getContext(),AddBeerActivity.class));
            }
        });

        contextCompat = getBaseContext();

        //BEGIN LOGIN/REGISTER TESTING
        //FirebaseAuth.getInstance().signOut();
        //END TESTING

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        Log.d("tag", "currentUser is :" +currentUser);
        if(currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
//            this.finish();
        }

        if (savedInstanceState == null) {
            BeersListFragment fragment = new BeersListFragment();
            FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
            tran.replace(R.id.main_container, fragment);
            tran.addToBackStack("");
            tran.commit();
        }/*


        */

        String[] permissions = { android.Manifest.permission.WRITE_EXTERNAL_STORAGE };
        int check = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (check != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this,permissions,REQUEST_WRITE_STORAGE);
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
