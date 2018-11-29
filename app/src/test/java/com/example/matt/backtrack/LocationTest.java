package com.example.matt.backtrack;

import java.lang.Object;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.filters.LargeTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import android.location.LocationManager;
import android.location.Location;


import android.os.Bundle;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LocationTest {
    private FirebaseAuth mAuth;
    final double TEST_LONGITUDE = -1.084095;
    final double TEST_LATITUDE = 3.4006;
    final String TEST_PROVIDER = "test";
    final Location mLocation_instance;
    final LocationManager LocationManager_instance;



    @Test
    public void testLocationReceived() throws Exception {
        Location fakeLocation = new Location(LocationManager.NETWORK_PROVIDER);
        fakeLocation.setLongitude(100);
        fakeLocation.setLatitude(-80);
        onLocationChanged(fakeLocation);
    }

    public void onLocationChanged(Location location) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.child(mAuth.getCurrentUser().getUid()).child("latitude").setValue(location.getLatitude());
        myRef.child(mAuth.getCurrentUser().getUid()).child("longitude").setValue(location.getLongitude());
    }


    LocationManager_instance = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
        if (LocationManager_instance.getProvider(TEST_PROVIDER) != null) {
        LocationManager_instance.removeTestProvider(TEST_PROVIDER);
    }
        if (LocationManager_instance.getProvider(TEST_PROVIDER) == null) {
        LocationManager_instance.addTestProvider(TEST_PROVIDER,
                false, //requiresNetwork,
                false, // requiresSatellite,
                false, // requiresCell,
                false, // hasMonetaryCost,
                false, // supportsAltitude,
                false, // supportsSpeed,
                false, // supportsBearing,
                android.location.Criteria.POWER_MEDIUM, // powerRequirement
                android.location.Criteria.ACCURACY_FINE); // accuracy
    }
    mLocation_instance = new Location(TEST_PROVIDER);
        mLocation_instance.setLatitude(TEST_LATITUDE);
        mLocation_instance.setLongitude(TEST_LONGITUDE);
        mLocation_instance.setTime(System.currentTimeMillis());
        mLocation_instance.setAccuracy(25);
        mLocation_instance.setTestProviderEnabled(TEST_PROVIDER, true);
        mLocation_instance.setTestProviderStatus(TEST_PROVIDER, LocationProvider.AVAILABLE, null, System.currentTimeMillis());
        mLocation_instance.setTestProviderLocation(TEST_PROVIDER, mLocation);

    LocationListener ll = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {   }
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onProviderDisabled(String provider) {}
        @Override
        public void onLocationChanged(Location location) {
            locations.add(location);
            Log.d("TEST", "lat: " + location.getLatitude() + ", lon: " + location.getLongitude());
        }
    };



    static void sendLocation(double location_latitude, double location_longitude) {
        try {
            String str = "geo_location " + location_longitude + " " + location_latitude ;
            Writer w = new OutputStreamWriter(getOutputStream());
            w.flush();
        }
        catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    }