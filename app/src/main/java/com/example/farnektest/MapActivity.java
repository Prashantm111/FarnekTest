package com.example.farnektest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import database.DataBaseHelper;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    LocationBroadcastReceiver locationBroadcastReceiver = new LocationBroadcastReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initMaps();
    }




    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter("GPSLocationUpdates");
        registerReceiver(locationBroadcastReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(locationBroadcastReceiver);
    }


    private void initMaps() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    private void setMarkerOnMap(Location location){
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mMap.clear();
        mMap.addMarker(markerOptions);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                latLng, 16));
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public class LocationBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
                         Bundle b = intent.getBundleExtra("Location");
                       Location newLocation = (Location) b.getParcelable("Location");
                      setMarkerOnMap(newLocation);
            DataBaseHelper.getInstance(getApplicationContext()).saveLocation(newLocation);
            Toast.makeText(MapActivity.this, "Sending to server" , Toast.LENGTH_LONG).show();
        }
    }
}