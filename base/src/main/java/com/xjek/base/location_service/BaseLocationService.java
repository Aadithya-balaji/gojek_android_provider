package com.xjek.base.location_service;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.xjek.base.R;

import java.text.DateFormat;
import java.util.Date;

public class BaseLocationService extends Service {

    public static String BROADCAST = "BASE_BROADCAST";
    public static final String EXTRA_LOCATION = BROADCAST + ".LOCATION";
    public static final String NOTIFICATION = BROADCAST + ".NOTIFICATION";

    private final String CHANNEL_ID = "channel_01";

    private final int CHECK_INTERVAL = 10000;

    private Runnable r;
    private Handler h;

    private NotificationManager mNotificationManager;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    private Location mLocation;

    public BaseLocationService() {
        System.out.println("RRRR:: BaseLocationService.BaseLocationService");
    }

    @Override
    public void onCreate() {
        System.out.println("RRRR:: BaseLocationService.onCreate");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        h = new Handler();
        r = () -> {
            h.postDelayed(r, CHECK_INTERVAL);
            streamLocation();
        };

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                onNewLocation(locationResult.getLastLocation());
                if (h != null) {
                    h.removeCallbacks(r);
                    h.postDelayed(r, CHECK_INTERVAL);
                }
            }
        };

        long DISPLACEMENT = 10;
        long UPDATE_INTERVAL = 6000;
        long FASTEST_UPDATE_INTERVAL = UPDATE_INTERVAL / 2;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission missing...", Toast.LENGTH_SHORT).show();
                return;
            }

        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());

        streamLocation();

        HandlerThread thread = new HandlerThread("BaseLocationService");
        thread.start();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            NotificationChannel mChannel = new NotificationChannel
                    (CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(mChannel);
        }

        requestLocationUpdates();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("RRRR:: BaseLocationService.onStartCommand");
        boolean startedFromNotification = intent.getBooleanExtra(NOTIFICATION, false);

        if (startedFromNotification) {
            removeLocationUpdates();
            stopSelf();
        }

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        System.out.println("RRRR:: BaseLocationService.onDestroy");
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private void streamLocation() {
        System.out.println("RRRR:: BaseLocationService.streamLocation");
        try {
            mFusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    mLocation = task.getResult();
                    onNewLocation(mLocation);
                } else System.out.println("RRRR:: Failed to get location.");
                System.out.println("RRRR:: task = " + task);
            });
        } catch (SecurityException unlikely) {
            System.out.println("RRRR:: Lost location permission." + unlikely);
            unlikely.printStackTrace();
        }
    }

    private void onNewLocation(Location location) {
        System.out.println("RRRR:: BaseLocationService.onNewLocation :: BROADCAST :: " + BROADCAST);

        mLocation = location;

        Intent intent = new Intent(BROADCAST);
        intent.putExtra(EXTRA_LOCATION, location);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

        int NOTIFICATION_ID = 12345678;
        if (serviceIsRunningInForeground(this))
            mNotificationManager.notify(NOTIFICATION_ID, getNotification());
    }

    private boolean serviceIsRunningInForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
            if (getClass().getName().equals(service.service.getClassName()))
                if (service.foreground) return true;
        return false;
    }

    private Notification getNotification() {
        Intent intent = new Intent(this, BaseLocationService.class);
        intent.putExtra(NOTIFICATION, true);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "123456")
                .addAction(R.drawable.star_blue, getString(R.string.app_name), null)
                .setContentText(mLocation.toString())
                .setContentTitle(DateFormat.getDateTimeInstance().format(new Date()))
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.star_blue)
                .setTicker(mLocation.toString())
                .setWhen(System.currentTimeMillis());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            builder.setChannelId(CHANNEL_ID);

        return builder.build();
    }

    public void requestLocationUpdates() {
        System.out.println("RRRR:: BaseLocationService.requestLocationUpdates");
        startService(new Intent(getApplicationContext(), BaseLocationService.class));
        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        } catch (SecurityException unlikely) {
            System.out.println("RRRR:: Lost location permission. Could not request updates. " + unlikely);
        }
    }

    public void removeLocationUpdates() {
        System.out.println("RRRR:: BaseLocationService.removeLocationUpdates");
        try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            stopSelf();
        } catch (SecurityException unlikely) {
            System.out.println("RRRR:: Lost location permission. Could not remove updates. " + unlikely);
        }
    }
}