package com.example.matt.backtrack;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.HashMap;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    private FirebaseAuth mAuth;
    private double  latitude = 0;
    private double longitude = 0;
    private GoogleMap mMap;
    private static boolean finish_activity = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle extras = getIntent().getExtras();
        String groupName=null;

        if (extras != null) {
            groupName = extras.getString("EXTRA_GROUP_ID");
            System.out.println("aaxxoo the group Id: " + groupName);
        }

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

        final String finalGroupName = groupName;
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //each ds represents a user!!
                    String a = ds.getKey();
                    if (a.equals("users")) {
                        for (DataSnapshot ds2 : ds.getChildren()) {

                            HashMap<Object, Object> users = (HashMap<Object, Object>) ds2.getValue();
                            HashMap<Object, Object> groups = (HashMap<Object, Object>) users.get("groups");

                            System.out.println("name123: "+ users);
                            System.out.println("name123: "+ groups);

                            if(groups!=null)
                            {
                                if(groups.containsValue(finalGroupName))
                                {
                                    if(users.containsKey("visibility") && users.containsKey("name") && users.containsKey("latitude") && users.containsKey("longitude") && users.containsValue("true"))
                                    {
                                        Object lat = users.get("latitude");
                                        lat = Double.parseDouble(lat.toString());

                                        Object longit = users.get("longitude");
                                        longit = Double.parseDouble(longit.toString());

                                        Object username = users.get("name");

                                        MarkerOptions mp = new MarkerOptions();
                                        mp.position(new LatLng((double)lat,(double)longit));
                                        mp.title((String) username);
                                        mMap.addMarker(mp);

                                    }
                                    else
                                    {
                                        continue;
                                    }
                                }
                                else
                                    {
                                    continue;
                                }
                            }
                            else
                            {
                                continue;
                            }

                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

}


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


/*
                mAuth = FirebaseAuth.getInstance();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("users");
                    myRef.child(mAuth.getCurrentUser().getUid()).child("visibility").setValue("true");
*/
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




        // Add a marker in Sydney and move the camera
        if(latitude != 0 && longitude != 0) {
            LatLng sydney = new LatLng(latitude, longitude);

            mMap.addMarker(new MarkerOptions().position(sydney).title("Your Current Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        Log.w("Location", "Location" + location.getLatitude());
        myRef.child(mAuth.getCurrentUser().getUid()).child("latitude").setValue(location.getLatitude());
        myRef.child(mAuth.getCurrentUser().getUid()).child("longitude").setValue(location.getLongitude());
//        TextView text_lat = (TextView) findViewById(R.id.text_view_latitude);
//        TextView text_long = (TextView) findViewById(R.id.text_view_longitude);
//        text_lat.setText("" + location.getLatitude() + ",   ");
//        text_long.setText("" + location.getLongitude());
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();

//        mMap.clear();



        MarkerOptions mp = new MarkerOptions();

        mp.position(new LatLng(location.getLatitude(), location.getLongitude()));

        mp.title("my position");

        mMap.addMarker(mp);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 16));


//        return location;
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

    public void setBoolean(Boolean bool)
    {
        finish_activity  = bool;
    }
}
