package com.xjek.base.utils;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.xjek.base.base.BaseActivity;
import com.xjek.base.data.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    public static boolean hasPemission(BaseActivity activity) {
        if (activity.getPermissionUtil().hasPermission(activity, Constants.RequestPermission.INSTANCE.getPERMISSIONS_LOCATION())) {
            return true;
        } else {
            return false;
        }
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

    public static String getCountryCode(@NonNull Context context, LatLng currentLocation) throws IOException {
        List<Address> addresses;
        Geocoder geocoder;
        String countryCode = "";
        if (Geocoder.isPresent()) {
            geocoder = new Geocoder(context, Locale.getDefault());
            addresses = geocoder.getFromLocation(currentLocation.latitude, currentLocation.longitude, 1);
            if (addresses.size() > 0) countryCode = addresses.get(0).getCountryCode();
        }
        countryCode = TextUtils.isEmpty(countryCode) ? "US" : countryCode;
        return countryCode;
    }

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }




}
