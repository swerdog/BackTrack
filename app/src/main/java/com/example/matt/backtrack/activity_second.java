package com.example.matt.backtrack;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class activity_second extends AppCompatActivity implements LocationListener {

    private FirebaseAuth mAuth;
    private boolean isAuthListenerSet = false;
    private boolean finish_activity = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button mEmailRegisterButton = (Button) findViewById(R.id.button_map);
        mEmailRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMap();
            }
        });
        Button viewGroupButton = (Button) findViewById(R.id.viewGroup);
        viewGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewGroups();
            }
        });
        Switch switchButton = (Switch) findViewById(R.id.switch_location);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mAuth = FirebaseAuth.getInstance();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("users");
                if (isChecked) {
                    myRef.child(mAuth.getCurrentUser().getUid()).child("visibility").setValue("true");
                } else {
                    myRef.child(mAuth.getCurrentUser().getUid()).child("visibility").setValue("false");
                }
            }
        });
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        Button buttonOne = (Button) findViewById(R.id.signout);
        buttonOne.setOnClickListener( new View.OnClickListener()
        {
            public void onClick (View v){
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();

            }
        });


    }

    private FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                //User is signed in
            } else {

                Intent intent = new Intent(activity_second.this, LoginActivity.class);
                startActivity(intent);
                finish_activity = false;

            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if (!isAuthListenerSet) {
            FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
            isAuthListenerSet = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
            isAuthListenerSet = false;
        }
    }


    private void openMap() {
        Intent i = new Intent(activity_second.this, MapsActivity.class);
        startActivity(i);
    }


    private void viewGroups(){
        Intent i = new Intent(activity_second.this, GroupViewActivity.class);
        startActivity(i);
    }

    @Override
    public void onLocationChanged(Location location) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        Log.w("Location", "Location" + location.getLatitude());
        if (finish_activity != false) {
            myRef.child(mAuth.getCurrentUser().getUid()).child("latitude").setValue(location.getLatitude());
            myRef.child(mAuth.getCurrentUser().getUid()).child("longitude").setValue(location.getLongitude());
            TextView text_lat = (TextView) findViewById(R.id.text_view_latitude);
            TextView text_long = (TextView) findViewById(R.id.text_view_longitude);
            text_lat.setText("Coordinates:  " + location.getLatitude() + ",   ");
            text_long.setText("" + location.getLongitude());
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}
