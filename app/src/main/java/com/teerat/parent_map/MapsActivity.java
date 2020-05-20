package com.teerat.parent_map;


import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;





public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private String name;
    private Double lat;
    private Double lng;
    private Button notificationButton;
    private Button busButton;
    private Button scheduleButton;
    private DocumentReference mDocRef = FirebaseFirestore.getInstance().document("bus/B111");

    @Override
    protected void onStart() {
        super.onStart();
        Log.w("test", "2");
        mDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    GeoPoint geo = documentSnapshot.getGeoPoint("location");
                    System.out.println(geo);
                    lat = geo.getLatitude();
                    lng = geo.getLongitude();

                    LatLng location = new LatLng(lat, lng);
                    mMap.clear();

                    mMap.addMarker(new MarkerOptions().position(location).title("my's location").icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("school_bus", 100, 100))));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
                } else if (e != null) {
                    Log.w("fail", "Got an exception!", e);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        notificationButton = (Button) findViewById(R.id.notificationButton);
        busButton = (Button) findViewById(R.id.busButton);
        scheduleButton = (Button) findViewById(R.id.scheduleButton);
        notificationButton.setOnClickListener(this);
        busButton.setOnClickListener(this);
        scheduleButton.setOnClickListener(this);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        this.getWindow().getDecorView().setSystemUiVisibility(

                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.w("test", "1");
        lat = 0.0;
        lng = 0.0;


        LatLng location = new LatLng(lat, lng);
        System.out.println(location);


        //LatLng location = new LatLng(51.51671, -0.26018);
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(location).title("my's location"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));


    }

    public Bitmap resizeMapIcons(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    @Override
    public void onClick(View v) {
        if(v == notificationButton){
            Intent intent = new Intent(MapsActivity.this,NotificationActivity.class);
            startActivity(intent);
        }
        if(v == busButton){
            Intent intent = new Intent(MapsActivity.this,BusActivity.class);
            startActivity(intent);
        }
        if(v == scheduleButton){
            Intent intent = new Intent(MapsActivity.this,ScheduleActivity.class);
            startActivity(intent);
        }
    }
}


