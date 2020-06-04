package com.teerat.parent_map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class BusActivity extends AppCompatActivity implements View.OnClickListener {

    private DocumentReference bDocRef;
    private DocumentReference dDocRef;

    private TextView name_textView;
    private TextView numberPlate_textView;
    private TextView speed_textView;
    private TextView driver_Gender_textView;
    private TextView driver_Age_textView;
    private TextView driver_ContactNo_textView;
    private TextView bus_id_textView;
    private TextView distance_textView;
    private ImageView busIcon_imageView;
    private ImageView driverIcon_imageView;

    private String driverName;
    private String numberPlate;
    private Double speed;
    private String driverGender;
    private String driverContactNo;
    private String driverAge;

    private Button notificationButton;
    private Button scheduleButton;
    private Button mapButton;
    private Button profileButton;
    private String busId;
    private String driverId;
    private Parent parent;
    private String studentId;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference imageRef;

    protected void showBusInfo() {
        name_textView.setText(driverName);
        driver_Gender_textView.setText(driverGender);
        driver_Age_textView.setText(driverAge);
        driver_ContactNo_textView.setText(driverContactNo);
        SpannableString content = new SpannableString(driverContactNo);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        driver_ContactNo_textView.setText(content);

        driver_ContactNo_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel://"+driverContactNo));
                startActivity(intent);
            }
        });


        bDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    numberPlate = documentSnapshot.getString("license");
                    speed = documentSnapshot.getDouble("speed");
                    Integer speedInt = speed.intValue();
                    String speedString = speedInt.toString() + " Km";

                    numberPlate_textView.setText(numberPlate);
                    //speed_textView.setText(speedString);

                } else if (e != null) {
                    Log.w("fail", "Got an exception!", e);
                }
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_2);
        name_textView = (TextView) findViewById(R.id.drivername);
        numberPlate_textView = (TextView) findViewById(R.id.numberplate);
        speed_textView = (TextView) findViewById(R.id.speed);
        driver_Age_textView = (TextView) findViewById(R.id.driverAge);
        driver_Gender_textView = (TextView) findViewById(R.id.driverGender);
        driver_ContactNo_textView = (TextView) findViewById(R.id.driverContactNo);
        bus_id_textView = (TextView) findViewById(R.id.busIdTextView);
        busIcon_imageView = (ImageView) findViewById(R.id.busLogo);
        driverIcon_imageView = (ImageView) findViewById(R.id.driverLogo);

        mapButton = (Button) findViewById(R.id.mapButton);
        notificationButton = (Button) findViewById(R.id.notificationButton);
        scheduleButton = (Button) findViewById(R.id.scheduleButton);
        profileButton = (Button) findViewById(R.id.profileButton);
        mapButton.setOnClickListener(this);
        notificationButton.setOnClickListener(this);
        scheduleButton.setOnClickListener(this);
        profileButton.setOnClickListener(this);

        //busIcon_imageView.setImageBitmap(resizeIcons("bus_logo", 300, 300));
        //driverIcon_imageView.setImageBitmap(resizeIcons("driver_logo", 300, 300));

        Intent intent = getIntent();
        parent = intent.getParcelableExtra("parent");
        busId = intent.getStringExtra("busId");
        studentId = intent.getStringExtra("studentId");

        Log.d("driverId",busId);
        bDocRef = FirebaseFirestore.getInstance().document("bus/"+busId);

        getDriverId();

    }
    private void getDriverId(){
        bDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    driverId= documentSnapshot.getString("driver");
                    bus_id_textView.setText(busId);
                    dDocRef = FirebaseFirestore.getInstance().document("driver/"+driverId);
                    Log.d("driverId",driverId);
                    getImage();


                    getDriverInfo();

                } else if (e != null) {
                    Log.w("fail", "Got an exception!", e);
                }
            }
        });
    }
    private void getDriverInfo(){
        dDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    driverName= documentSnapshot.getString("firstName") +" " + documentSnapshot.getString("lastName");
                    driverId= documentSnapshot.getString("driver");
                    driverGender= documentSnapshot.getString("gender");
                    driverContactNo= documentSnapshot.getString("contactNo");
                    driverAge= documentSnapshot.getString("age");
                    showBusInfo();



                } else if (e != null) {
                    Log.w("fail", "Got an exception!", e);
                }
            }
        });
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
            intent.putExtra("parent", parent);
            intent.putExtra("studentId", studentId);
            intent.putExtra("busId", busId);
            startActivity(intent);
        }
        if(v == mapButton){
            Intent intent = new Intent(BusActivity.this,MapsActivity.class);
            intent.putExtra("parent", parent);
            startActivity(intent);
        }
        if(v == scheduleButton){
            Intent intent = new Intent(BusActivity.this,ScheduleActivity.class);
            intent.putExtra("parent", parent);
            intent.putExtra("studentId", studentId);
            intent.putExtra("busId", busId);
            startActivity(intent);
        }
        if(v == profileButton){
            Intent intent = new Intent(BusActivity.this,ProfileActivity.class);
            intent.putExtra("parent", parent);
            intent.putExtra("studentId", studentId.toString());
            intent.putExtra("busId", busId);
            startActivity(intent);
        }
    }

    private void getImage(){
        try {
            Log.d("get image",driverId);


            imageRef = storage.getReference().child("test").child(driverId+".png");
            imageRef.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    driverIcon_imageView.setImageBitmap(bitmap);
                }
            });
        }catch (Exception a) {
            System.out.println("S");
        }

    }
}
