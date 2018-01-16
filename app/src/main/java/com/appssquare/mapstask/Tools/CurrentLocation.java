package com.appssquare.mapstask.Tools;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by LAP_SHOP on 28/02/2017.
 */

public class CurrentLocation implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<Status> {
    protected static final String TAG = "MainActivity";


    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    Location mlocation;
    Context context;
    GoogleMap googleMap;

    public CurrentLocation(final Context context, GoogleMap googleMap) {
        this.context = context;
        this.googleMap = googleMap;
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000);
        locationRequest.setFastestInterval(900);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            googleApiClient.connect();
        }


    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.clear();
        Double lat = LocationServices.FusedLocationApi.getLastLocation(googleApiClient).getLatitude();
        Double log = LocationServices.FusedLocationApi.getLastLocation(googleApiClient).getLongitude();
        googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, log)).title("Current Place"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, log), 15));

//        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
//        System.out.println("Test" + LocationServices.FusedLocationApi.getLastLocation(googleApiClient) + "");


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(context, location + "jkljkhkj", Toast.LENGTH_LONG).show();
        Double lat = location.getLatitude();
        Double log = location.getLongitude();
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, log)).title("Current Place"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, log), 15));

    }


    @Override
    public void onResult(@NonNull Status status) {

        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:

                // All location settings are satisfied. The client can initialize location
                // requests here.
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                // Location settings are not satisfied. But could be fixed by showing the user
                // a dialog.
                try {

                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    status.startResolutionForResult((Activity) context, 1000);

                } catch (IntentSender.SendIntentException e) {
                    // Ignore the error.
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                // Location settings are not satisfied. However, we have no way to fix the
                // settings so we won't show the dialog.
                break;
        }

    }


}
