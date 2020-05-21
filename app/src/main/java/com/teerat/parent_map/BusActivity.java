package com.teerat.parent_map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class BusActivity extends AppCompatActivity implements View.OnClickListener {

    private DocumentReference mDocRef = FirebaseFirestore.getInstance().document("Bus/001");

    private TextView name_textView;
    private TextView numberPlate_textView;
    private TextView speed_textView;
    private TextView distance_textView;
    private ImageView busIcon_imageView;
    private ImageView driverIcon_imageView;

    private String driverName;
    private String numberPlate;
    private String speed;
    private String distancetoDes;

    private Button notificationButton;
    private Button scheduleButton;
    private Button mapButton;


    @Override
    protected void onStart() {
        super.onStart();
        Log.w("test", "2");

        mDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    driverName = documentSnapshot.getString("name");
                    numberPlate = documentSnapshot.getString("numberPlate");
                    speed = documentSnapshot.getString("speed");
                    speed = speed + " Km";

                    name_textView.setText(driverName);
                    numberPlate_textView.setText(numberPlate);
                    speed_textView.setText(speed);

                } else if (e != null) {
                    Log.w("fail", "Got an exception!", e);
                }
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);
        name_textView = (TextView) findViewById(R.id.drivername);
        numberPlate_textView = (TextView) findViewById(R.id.numberplate);
        speed_textView = (TextView) findViewById(R.id.speed);
        distance_textView = (TextView) findViewById(R.id.distance);
        busIcon_imageView = (ImageView) findViewById(R.id.busLogo);
        driverIcon_imageView = (ImageView) findViewById(R.id.driverLogo);

        mapButton = (Button) findViewById(R.id.mapButton);
        notificationButton = (Button) findViewById(R.id.notificationButton);
        scheduleButton = (Button) findViewById(R.id.scheduleButton);
        mapButton.setOnClickListener(this);
        notificationButton.setOnClickListener(this);
        scheduleButton.setOnClickListener(this);

        busIcon_imageView.setImageBitmap(resizeIcons("bus_logo", 300, 300));
        driverIcon_imageView.setImageBitmap(resizeIcons("driver_logo", 300, 300));


    }

    public Bitmap resizeIcons(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    @Override
    public void onClick(View v) {
        if(v == notificationButton){
            Intent intent = new Intent(BusActivity.this,NotificationActivity.class);
            startActivity(intent);
        }
        if(v == mapButton){
            Intent intent = new Intent(BusActivity.this,MapsActivity.class);
            startActivity(intent);
        }
        if(v == scheduleButton){
            Intent intent = new Intent(BusActivity.this,ScheduleActivity.class);
            startActivity(intent);
        }
    }
}
