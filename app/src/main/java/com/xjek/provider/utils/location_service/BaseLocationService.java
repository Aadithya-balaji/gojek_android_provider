package com.xjek.provider.utils.location_service;

import android.Manifest;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.xjek.provider.R;
import com.xjek.provider.views.dashboard.DashBoardActivity;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class BaseLocationService extends Service {

    public static String BROADCAST = "BASE_BROADCAST";
    public static final String EXTRA_LOCATION = BROADCAST + ".LOCATION";
    public static final String NOTIFICATION = BROADCAST + ".NOTIFICATION";

    private final String CHANNEL_ID = "channel_01";

    private final long DISPLACEMENT = 10;
    private final long UPDATE_INTERVAL = 6000;
    private final long FASTEST_UPDATE_INTERVAL = UPDATE_INTERVAL / 2;
    private final int NOTIFICATION_ID = 12345678;

    private final int CHECK_INTERVAL = 15000;
    private Runnable r;
    private Handler h;

    private NotificationManager mNotificationManager;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    private Location mLocation;
    private boolean mChangingConfiguration = false;

    public BaseLocationService() {
    }

    @Override
    public void onCreate() {
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

        HandlerThread thread = new HandlerThread("RRR BaseLocationService");
        thread.start();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            NotificationChannel mChannel = new NotificationChannel
                    (CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("RRR BaseLocationService.onStartCommand");
        boolean startedFromNotification = intent.getBooleanExtra(NOTIFICATION, false);

        if (startedFromNotification) {
            removeLocationUpdates();
            stopSelf();
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mChangingConfiguration = true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        /*System.out.println("RRR BaseLocationService.onBind");
        stopForeground(true);*/
        mChangingConfiguration = false;
        return null;
    }

    @Override
    public void onRebind(Intent intent) {
        /*System.out.println("RRR BaseLocationService.onRebind");
        stopForeground(true);
        mChangingConfiguration = false;*/
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        System.out.println("RRR BaseLocationService.onUnbind");
        /*if (!mChangingConfiguration) {
            System.out.println("RRR Starting foreground service");
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
                startService(new Intent(this, BaseLocationService.class));
//                mNotificationManager.startServiceInForeground(new Intent(this,
//                        BaseLocationService.class), NOTIFICATION_ID, getNotification());
            } else startForeground(NOTIFICATION_ID, getNotification());
        }*/
        return true;
    }

    @Override
    public void onDestroy() {
        System.out.println("" +
                " BaseLocationService.onDestroy");
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private void streamLocation() {
        try {
            mFusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    mLocation = task.getResult();
                    onNewLocation(mLocation);
                } else System.out.println("RRR Failed to get location.");
                System.out.println("RRR task = " + task);
            });
        } catch (SecurityException unlikely) {
            System.out.println("RRR Lost location permission." + unlikely);
            unlikely.printStackTrace();
        }
    }

    private void onNewLocation(Location location) {
        System.out.println("RRR BaseLocationService.onNewLocation");
        System.out.println("RRR :: BROADCAST = " + BROADCAST);

        mLocation = location;

        System.out.println("RRR location = " + location);

        Intent intent = new Intent(BROADCAST);
        intent.putExtra(EXTRA_LOCATION, location);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

        if (serviceIsRunningInForeground(this))
            mNotificationManager.notify(NOTIFICATION_ID, getNotification());

//        getLocationResultText(location, "onLocationChanged");
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

        PendingIntent service = PendingIntent.getService(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent activity = PendingIntent.getActivity(this, 0,
                new Intent(this, DashBoardActivity.class), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "123456")
                .addAction(R.mipmap.ic_launcher_round, getString(R.string.app_name), activity)
                .addAction(R.mipmap.ic_launcher_round, "Remove Location Updates", service)
                .setContentText(mLocation.toString())
                .setContentTitle(DateFormat.getDateTimeInstance().format(new Date()))
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(mLocation.toString())
                .setWhen(System.currentTimeMillis());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            builder.setChannelId(CHANNEL_ID);

        return builder.build();
    }

    public void requestLocationUpdates() {
        System.out.println("RRR BaseLocationService.requestLocationUpdates");
        startService(new Intent(getApplicationContext(), BaseLocationService.class));
        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
//            getLocationResultText(null, "requestLocationUpdates");
        } catch (SecurityException unlikely) {
            System.out.println("RRR Lost location permission. Could not request updates. " + unlikely);
        }
    }

    public void removeLocationUpdates() {
        System.out.println("RRR BaseLocationService.removeLocationUpdates");
        try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
//            getLocationResultText(null, "removeLocationUpdates");
            stopSelf();
        } catch (SecurityException unlikely) {
            System.out.println("RRR Lost location permission. Could not remove updates. " + unlikely);
        }
    }

    private void getLocationResultText(Location l, String notes) {
        List<LocationPoint> points = new ArrayList<>();
        LocationPoint point;
        if (l != null) {
            point = new LocationPoint();
            point.setLat(l.getLatitude() + "");
            point.setLng(l.getLongitude() + "");
            point.setDistance(" : isNetworkAvailable : " + isNetworkAvailable()
                    + " : isBackground : " + isBack(BaseLocationService.this)
                    + " : isLocked : " + isLocked());
            point.setMobtime(DateFormat.getDateTimeInstance().format(new Date()));
        } else {
            point = new LocationPoint();
            point.setLat("00");
            point.setLng("00");
            point.setDistance(" : isNetworkAvailable : " + isNetworkAvailable()
                    + " : isBackground : " + isBack(BaseLocationService.this)
                    + " : isLocked : " + isLocked());
            point.setMobtime(DateFormat.getDateTimeInstance().format(new Date()));
        }
        points.add(point);
        new Gson().toJson(points);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    private boolean isLocked() {
        KeyguardManager kgMgr = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        return kgMgr.inKeyguardRestrictedInputMode();
    }

    private boolean isBack(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses)
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
                for (String activeProcess : processInfo.pkgList)
                    if (activeProcess.equals(context.getPackageName())) isInBackground = false;
        return isInBackground;
    }

    public class LocalBinder extends Binder {
        public BaseLocationService locationService() {
            return BaseLocationService.this;
        }
    }

}