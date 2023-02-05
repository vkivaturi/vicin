package com.vicin.utils;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.vicin.model.LocationData;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationUtil {
    public static void getLocationCoords(FusedLocationProviderClient fusedLocationClient, FragmentActivity activity, LocationData locationData) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.i("###", "No permissions by user....");
            //return TODO;
        }

        //Create a basic Current Location Request object. No parameters are set
        CurrentLocationRequest currentLocationRequest = new CurrentLocationRequest.Builder().build();
        fusedLocationClient.getCurrentLocation(currentLocationRequest,null)
        //fusedLocationClient.getLastLocation()
                .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        captureLocationDetails(location, locationData, activity);
                    }
                });
    }

    private static void captureLocationDetails(Location location, LocationData locationData, FragmentActivity activity) {
        if (location != null) {
            // Logic to handle location object
            Log.i("### latitude", String.valueOf(location.getLatitude()));
            Log.i("### longitude", String.valueOf(location.getLongitude()));
            locationData.setLatitude(String.valueOf(location.getLatitude()));
            locationData.setLongitude(String.valueOf(location.getLongitude()));

            Geocoder gcd = new Geocoder(activity, Locale.getDefault());
            List<Address> geoAddresses = null;
            try {
                geoAddresses = geoAddresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (geoAddresses.size() > 0) {
                String mUserLocation = "";
                mUserLocation = mUserLocation + geoAddresses.get(0).getAddressLine(0).replace(",", "") + ", ";
                Log.i("### mUserLocation", mUserLocation);
                locationData.setLocationLine1(mUserLocation);
            }
        }

    }
}
