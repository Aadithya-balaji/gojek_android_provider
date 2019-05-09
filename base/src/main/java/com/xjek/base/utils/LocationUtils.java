package com.xjek.base.utils;

import android.Manifest;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public final class LocationUtils {
    private LocationUtils() {
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    public static void getLastKnownLocation(@NonNull Context context, LocationCallBack.LastKnownLocation mCallBack) {
        FusedLocationProviderClient mFusedLocation = getFusedLocationProviderClient(context);
        Task<Location> locationResult = mFusedLocation.getLastLocation();
        locationResult.addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null)
                mCallBack.onSuccess(task.getResult());
        }).addOnFailureListener(e -> mCallBack.onFailure(e.getLocalizedMessage()));
    }

    public static List<Address> getCurrentAddress(@NonNull Context context, LatLng currentLocation) {
        List<Address> addresses = new ArrayList<>();
        Geocoder geocoder;
        try {
            if (Geocoder.isPresent()) {
                geocoder = new Geocoder(context, Locale.getDefault());
                addresses = geocoder.getFromLocation(currentLocation.latitude, currentLocation.longitude, 1);
                // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            }
        } catch (Exception e) {
            Log.d("EXception", "EXception" + e.getMessage());
        }
        return addresses;
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
