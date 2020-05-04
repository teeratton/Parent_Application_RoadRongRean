package com.teerat.parent_map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class BusActivity extends AppCompatActivity {

    private DocumentReference mDocRef = FirebaseFirestore.getInstance().document("Bus/001");

    TextView name_textView;
    TextView numberPlate_textView;
    TextView speed_textView;
    TextView distance_textView;
    ImageView busIcon_imageView;
    ImageView driverIcon_imageView;

    String driverName;
    String numberPlate;
    String speed;
    String distancetoDes;


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

        busIcon_imageView.setImageBitmap(resizeIcons("bus_logo", 300, 300));
        driverIcon_imageView.setImageBitmap(resizeIcons("driver_logo", 300, 300));


    }

    public Bitmap resizeIcons(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }
}
