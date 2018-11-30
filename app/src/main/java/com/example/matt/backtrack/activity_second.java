package com.example.matt.backtrack;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class activity_second extends AppCompatActivity implements LocationListener {

    private FirebaseAuth mAuth;
    private boolean isAuthListenerSet = false;
    private static boolean finish_activity = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        boolean finish = getIntent().getBooleanExtra("finish", false);
        if (finish) {
//            startActivity(new Intent(activity_second.this, LoginActivity.class));

            Intent intent = new Intent(activity_second.this, LoginActivity.class);
            intent.putExtra("finish", true);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFF");
            finish();
            // To clean up all activities
            startActivity(intent);
        }

        Button openMapButton = (Button) findViewById(R.id.button_map);
        openMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMap();
            }
        });

        Button joinGroupButton = (Button) findViewById(R.id.joinGroup);
        joinGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinButtonMethod();
            }
        });


        Button createGroupButton = (Button) findViewById(R.id.createGroup);
        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGroupMethod();
              
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
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        FloatingActionButton buttonOne = (FloatingActionButton) findViewById(R.id.settings);
        buttonOne.setOnClickListener( new View.OnClickListener()
        {
            public void onClick (View v){
                Intent i = new Intent(activity_second.this, SetActivity.class);
                startActivity(i);

            }
        });

    }

    public void setBoolean(Boolean bool)
    {
        finish_activity  = bool;
    }

    @Override
    protected void onActivityResult(int requextCode, int resultCode, Intent data)
    {
        activity_second.this.finish();
    }


    private FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                activity_second.this.finish();
        }
    };
    private void openMap() {
        Intent i = new Intent(activity_second.this, MapsActivity.class);
        startActivity(i);
    }

    private void createGroupMethod() {

        Intent i = new Intent(activity_second.this, CreateGroupActivity.class);
        startActivity(i);
}

    private void viewGroups(){
        Intent i = new Intent(activity_second.this, GroupViewActivity.class);
        startActivity(i);
    }

    private void joinButtonMethod() {

        Intent i = new Intent(activity_second.this, JoinActivity.class);
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
