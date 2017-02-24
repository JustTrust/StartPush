package com.belichenko.a.startpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String mLongitude;
    String mLatitude;
    String mMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mLongitude = getIntent().getStringExtra("longitude");
        mLatitude = getIntent().getStringExtra("latitude");
        MessageReceiver messageReceiver = new MessageReceiver();
        registerReceiver(messageReceiver, new IntentFilter("com.belichenko.a.PUSH_CAME"));
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        updateMapPoint();
    }

    private void updateMapPoint() {
        if (mMap == null) return;
        if (mLatitude != null && mLongitude != null) {
            // Add a marker and move the camera
            LatLng newPoint = new LatLng(Double.parseDouble(mLatitude), Double.parseDouble(mLongitude));
            mMap.addMarker(new MarkerOptions().position(newPoint).title(mMessage));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPoint, 15));
        } else {
            Toast.makeText(this, "Can't get coordinates", Toast.LENGTH_SHORT).show();
        }
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mLongitude = intent.getStringExtra("longitude");
            mLatitude = intent.getStringExtra("latitude");
            mMessage = intent.getStringExtra("body");
            updateMapPoint();
        }
    }
}
