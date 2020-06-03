package com.teerat.parent_map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private Button selectLocationButton;
    private Button selectStudentButton;
    private Button logoutButton;
    private Button mapButton;
    private Button notificationButton;
    private Button scheduleButton;
    private Button busButton;
    private TextView studentNameTextView;
    private ImageView studentImageView;
    private Parent parent;
    private String busId;
    private String studentId;
    private String studentFirstName;
    private String studentLastName;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference imageRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mapButton = (Button) findViewById(R.id.mapButton);
        busButton = (Button) findViewById(R.id.busButton);
        notificationButton = (Button) findViewById(R.id.notificationButton);
        scheduleButton = (Button) findViewById(R.id.scheduleButton);

        mapButton.setOnClickListener(this);
        busButton.setOnClickListener(this);
        notificationButton.setOnClickListener(this);
        scheduleButton.setOnClickListener(this);


        selectLocationButton = (Button) findViewById(R.id.setLocationButton);
        selectStudentButton = (Button) findViewById(R.id.selectStudentButton);
        logoutButton = (Button) findViewById(R.id.logOutButton);
        studentNameTextView = (TextView) findViewById(R.id.studentname);
        studentImageView = (ImageView) findViewById(R.id.studentImage);

        selectLocationButton.setOnClickListener(this);
        selectStudentButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);

        //studentImageView.setImageResource(R.drawable.korkeaw);

        Intent intent = getIntent();
        parent = intent.getParcelableExtra("parent");
        busId = intent.getStringExtra("busId");
        studentId = intent.getStringExtra("studentId");
        getImage();
        getStudentInfo();



    }

    @Override
    public void onClick(View v) {
        if (v == selectLocationButton){
            Intent intent = new Intent(ProfileActivity.this,PopSelectLocation.class);
            intent.putExtra("parent",parent);
            startActivity(intent);
        }
        if (v == selectStudentButton){
            Intent intent = new Intent(ProfileActivity.this,PopSelectStudent.class);
            intent.putExtra("parent",parent);
            intent.putExtra("studentId",studentId);
            startActivity(intent);
        }
        if (v == logoutButton){
            Intent intent = new Intent(ProfileActivity.this,LoginActivity.class);
            startActivity(intent);
        }

        if(v == notificationButton){
            Intent intent = new Intent(ProfileActivity.this,NotificationActivity.class);
            intent.putExtra("parent", parent);
            intent.putExtra("studentId", studentId);
            intent.putExtra("busId", busId);
            startActivity(intent);
        }
        if(v == mapButton){
            Intent intent = new Intent(ProfileActivity.this,MapsActivity.class);
            intent.putExtra("parent", parent);
            startActivity(intent);
        }
        if(v == scheduleButton){
            Intent intent = new Intent(ProfileActivity.this,ScheduleActivity.class);
            intent.putExtra("parent", parent);
            intent.putExtra("studentId", studentId);
            intent.putExtra("busId", busId);
            startActivity(intent);
        }
        if(v == busButton){
            Intent intent = new Intent(ProfileActivity.this,BusActivity.class);
            intent.putExtra("parent", parent);
            intent.putExtra("studentId", studentId.toString());
            intent.putExtra("busId", busId);
            startActivity(intent);
        }
    }

    public void getStudentInfo(){
        DocumentReference sDocRef = FirebaseFirestore.getInstance().document("student/"+studentId);
        sDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                         studentFirstName = document.getString("firstName");
                         studentLastName = document.getString("lastName");
                         Log.d("student name", studentFirstName + studentLastName);
                         studentNameTextView.setText(studentFirstName + " " + studentLastName);
                    }else {
                        Log.d("test", "No such document");
                }
                }else {
                    Log.d("test", "get failed with ", task.getException());
                }
            }
        });
    }

    private void getImage(){
        try {
            imageRef = storage.getReference().child("test").child(studentId+".png");
            imageRef.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    studentImageView.setImageBitmap(bitmap);
                }
            });
        }catch (Exception a) {
            System.out.println("S");
        }

    }

}
