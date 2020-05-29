package com.teerat.parent_map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {

    private DocumentReference sDocRef;
    private LinearLayout notificationArea;
    private String time;
    private String busId;
    private String studentId;
    private Parent parent;
    private GeoPoint leaveHomeLocation;
    private GeoPoint arriveSchoolLocation;
    private GeoPoint leaveSchoolLocation;
    private GeoPoint arriveHomeLocation;
    private boolean bArriveSchool = false;
    private boolean bLeaveHome = false;
    private boolean bLeaveSchool = false;
    private boolean bArriveHome = false;
    private SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    private Button mapButton;
    private Button busButton;
    private Button scheduleButton;
    private String date;
    private Drawable logo;
    private String studentName = "";

    protected void getNotification() {
        Log.w("test", "2");

        sDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    Log.w("pass", "im here", e);

                    Map<String, Object> t = documentSnapshot.getData();
                    System.out.println(t);
                    try {
                        Map<String, Object> leaveHome = (java.util.Map<String, Object>) t.get("LEAVEHOME");
                        leaveHomeLocation = (GeoPoint) leaveHome.get("LOCATION");
                        time = (String) leaveHome.get("TIME");
                        if (!bLeaveHome) {
                            updateNotification("leave the home", time, leaveHomeLocation);
                        }
                    } catch (Exception a) {
                        System.out.println("S");
                    }
                    try {
                        Map<String, Object> arriveSchool = (java.util.Map<String, Object>) t.get("ARRIVESCHOOL");
                        arriveSchoolLocation = (GeoPoint) arriveSchool.get("LOCATION");
                        time = (String) arriveSchool.get("TIME");
                        if (!bArriveSchool) {
                            updateNotification("arrive at school", time, arriveSchoolLocation);
                        }
                    } catch (Exception a) {
                        System.out.println("S");
                    }
                    try {
                        Map<String, Object> leaveSchool = (java.util.Map<String, Object>) t.get("LEAVESCHOOL");
                        leaveHomeLocation = (GeoPoint) leaveSchool.get("LOCATION");
                        time = (String) leaveSchool.get("TIME");
                        if (!bLeaveSchool) {
                            updateNotification("leave the school", time, leaveSchoolLocation);
                        }
                    } catch (Exception a) {
                        System.out.println("S");
                    }
                    try {
                        Map<String, Object> arriveHome = (java.util.Map<String, Object>) t.get("ARRIVEHOME");
                        arriveHomeLocation = (GeoPoint) arriveHome.get("LOCATION");
                        time = (String) arriveHome.get("TIME");
                        if (!bArriveHome) {
                            updateNotification("arrived home", time, arriveHomeLocation);
                        }
                    } catch (Exception a) {
                        System.out.println("S");
                    }

                } else if (e != null) {
                    Log.w("fail", "Got an exception!", e);
                }
            }
        });

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_notification);
        notificationArea = (LinearLayout) findViewById(R.id.notificationArea);
        mapButton = (Button) findViewById(R.id.mapButton);
        busButton = (Button) findViewById(R.id.busButton);
        scheduleButton = (Button) findViewById(R.id.scheduleButton);
        mapButton.setOnClickListener(this);
        busButton.setOnClickListener(this);
        scheduleButton.setOnClickListener(this);
        Intent intent = getIntent();
        parent = intent.getParcelableExtra("parent");
        studentId = intent.getStringExtra("studentId");
        busId = intent.getStringExtra("busId");
        getStudentName();
        getDate();

    }

    private void updateNotification(String state, String time, final GeoPoint location) {
        if (state.equals("leave the home")) {
            logo = getResources().getDrawable(R.drawable.leave_home_or_school);
            bLeaveHome = true;
        }
        if (state.equals("arrive at school")) {
            logo = getResources().getDrawable(R.drawable.school_logo);
            bArriveSchool = true;
        }
        if (state.equals("leave the school")) {
            logo = getResources().getDrawable(R.drawable.leave_home_or_school);
            bLeaveSchool = true;
        }
        if (state.equals("arrived home")) {
            logo = getResources().getDrawable(R.drawable.home_logo);
            bArriveHome = true;
        }

        final Double lat = location.getLatitude();
        final Double lng = location.getLongitude();
        TextView textView = new TextView(this);
        logo = resize(logo);
        int h = logo.getIntrinsicHeight();
        int w = logo.getIntrinsicWidth();
        logo.setBounds( 0, 0, w, h );
        textView.setCompoundDrawables(logo,null,null,null);
        textView.setCompoundDrawablePadding(50);
        textView.setText(studentName +" " + state + " : " + time);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(20,0,0,50);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationActivity.this,PopMapActivity.class);
                intent.putExtra("lat",lat);
                intent.putExtra("lng",lng);
                startActivity(intent);
            }
        });


        notificationArea.addView(textView);


    }

    private void getStudentName(){
        sDocRef = FirebaseFirestore.getInstance().document("student/"+studentId);
        sDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                studentName = document.getString("firstName");
            }

    });}


    private void getDate() {
        Date c = Calendar.getInstance().getTime();
        date = df.format(c);
        TextView textView = new TextView(this);
        textView.setText(date + studentId);
        textView.setTextSize(50);
        textView.setPadding(0,0,0,50);
        notificationArea.addView(textView);

        //date = "22-04-2020";
        sDocRef = FirebaseFirestore.getInstance().document("student/"+studentId+"/Event/" + date);
        getNotification();
    }

    private Drawable resize(Drawable image) {
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 80, 80, false);
        return new BitmapDrawable(getResources(), bitmapResized);
    }


    @Override
    public void onClick(View v) {
        if(v == mapButton){
            Intent intent = new Intent(NotificationActivity.this,MapsActivity.class);
            intent.putExtra("parent", parent);
            startActivity(intent);
        }
        if(v == busButton){
            Intent intent = new Intent(NotificationActivity.this,BusActivity.class);
            intent.putExtra("parent", parent);
            intent.putExtra("studentId", studentId);
            intent.putExtra("busId", busId);
            startActivity(intent);
        }
        if(v == scheduleButton){
            Intent intent = new Intent(NotificationActivity.this,ScheduleActivity.class);
            intent.putExtra("parent", parent);
            intent.putExtra("studentId", studentId);
            intent.putExtra("busId", busId);
            startActivity(intent);
        }
    }

}
