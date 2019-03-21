package com.appoets.basemodule.utils;

import android.Manifest;
import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

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
            if (task.isSuccessful() && task.getResult() != null) {
                mCallBack.onSuccess(task.getResult());
            }
        }).addOnFailureListener(e -> mCallBack.onFailure(e.getLocalizedMessage()));
    }
}
