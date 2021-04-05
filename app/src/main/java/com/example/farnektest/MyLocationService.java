package com.example.farnektest;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import database.DataBaseHelper;

public class MyLocationService extends Service {


    protected LocationManager locationManager;
    Handler handler;
    Runnable runnable;
   // long timeDurationForServer = 15 * 1000;
     long timeDurationForServer = 3*60*1000;
    private Location mLocation;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        initLoc();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        handler = null;
    }


    void initLoc() {

        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, timeDurationForServer);
                if (null != mLocation) {
                    sendLocationToActivity(mLocation);
                    sendLocationToServer(mLocation);
                }
            }
        }, 5000);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                mLocation = location;

            }
        });
        mLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


    }


    private void sendLocationToServer(final Location location) {
      //  DataBaseHelper.getInstance().saveLocation(location);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void sendLocationToActivity(Location mLocation) {
        Intent intent = new Intent("GPSLocationUpdates");
        Bundle b = new Bundle();
        b.putParcelable("Location", mLocation);
        intent.putExtra("Location", b);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        sendBroadcast(intent);
    }


}
