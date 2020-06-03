package com.teerat.parent_map;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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

import static com.teerat.parent_map.App.CHANNEL_1_ID;

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
    private Button speedWarningButton;
    private Button profileButton;
    private String date;
    private Drawable logo;
    private String studentName = "";
    private NotificationManagerCompat notificationManager;

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
                        Boolean notified = (Boolean) leaveHome.get("NOTIFIED");
                        if (!bLeaveHome) {
                            updateNotification("leave the home", time, leaveHomeLocation,notified);
                        }
                    } catch (Exception a) {
                        System.out.println("S");
                    }
                    try {
                        Map<String, Object> arriveSchool = (java.util.Map<String, Object>) t.get("ARRIVESCHOOL");
                        arriveSchoolLocation = (GeoPoint) arriveSchool.get("LOCATION");
                        time = (String) arriveSchool.get("TIME");
                        Boolean notified = (Boolean) arriveSchool.get("NOTIFIED");
                        if (!bArriveSchool) {
                            updateNotification("arrive at school", time, arriveSchoolLocation,notified);
                        }
                    } catch (Exception a) {
                        System.out.println("S");
                    }
                    try {
                        Map<String, Object> leaveSchool = (java.util.Map<String, Object>) t.get("LEAVESCHOOL");
                        leaveSchoolLocation = (GeoPoint) leaveSchool.get("LOCATION");
                        time = (String) leaveSchool.get("TIME");
                        Boolean notified = (Boolean) leaveSchool.get("NOTIFIED");
                        if (!bLeaveSchool) {
                            updateNotification("leave the school", time, leaveSchoolLocation,notified);
                        }
                    } catch (Exception a) {
                        System.out.println("S");
                    }
                    try {
                        Map<String, Object> arriveHome = (java.util.Map<String, Object>) t.get("ARRIVEHOME");
                        arriveHomeLocation = (GeoPoint) arriveHome.get("LOCATION");
                        time = (String) arriveHome.get("TIME");
                        Boolean notified = (Boolean) arriveHome.get("NOTIFIED");

                        if (!bArriveHome) {
                            updateNotification("arrived home", time, arriveHomeLocation,notified);
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
        speedWarningButton = (Button) findViewById(R.id.speedWarningButton);
        profileButton = (Button) findViewById(R.id.profileButton);
        mapButton.setOnClickListener(this);
        busButton.setOnClickListener(this);
        scheduleButton.setOnClickListener(this);
        profileButton.setOnClickListener(this);
        speedWarningButton.setOnClickListener(this);
        Intent intent = getIntent();
        parent = intent.getParcelableExtra("parent");
        studentId = intent.getStringExtra("studentId");
        busId = intent.getStringExtra("busId");
        notificationManager = NotificationManagerCompat.from(this);

        getStudentName();
        getDate();

    }

    private void updateNotification(String state, String time, final GeoPoint location,Boolean notified) {

        if (state.equals("leave the home")) {
            logo = getResources().getDrawable(R.drawable.get_on_off_bus);
            if(!notified) {
                Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                        .setSmallIcon(R.drawable.leave_home_or_school)
                        .setContentTitle("Student Activity")
                        .setContentText(state)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                                R.drawable.leave_home_or_school))
                        .build();

                notificationManager.notify(1, notification);
                sDocRef.update("LEAVEHOME.NOTIFIED",true);
            }
            bLeaveHome = true;
        }
        if (state.equals("arrive at school")) {
            logo = getResources().getDrawable(R.drawable.arrive_school);
            if(!notified) {
                Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                        .setSmallIcon(R.drawable.school_logo)
                        .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                                R.drawable.school_logo))
                        .setContentTitle("Student Activity")
                        .setContentText(state)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .build();

                notificationManager.notify(1, notification);
                sDocRef.update("ARRIVESCHOOL.NOTIFIED",true);
            }
            bArriveSchool = true;
        }
        if (state.equals("leave the school")) {
            logo = getResources().getDrawable(R.drawable.get_on_off_bus);
            if(!notified) {
                Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                        .setSmallIcon(R.drawable.leave_home_or_school)
                        .setContentTitle("Student Activity")
                        .setContentText(state)
                        .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                                R.drawable.leave_home_or_school))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .build();

                notificationManager.notify(1, notification);
                sDocRef.update("LEAVESCHOOL.NOTIFIED",true);
            }
            bLeaveSchool = true;
        }
        if (state.equals("arrived home")) {
            logo = getResources().getDrawable(R.drawable.arrive_home);
            if(!notified) {
                Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                        .setSmallIcon(R.drawable.home_logo)
                        .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                                R.drawable.home_logo))
                        .setContentTitle("Student Activity")
                        .setContentText(state)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .build();

                notificationManager.notify(1, notification);
                sDocRef.update("ARRIVEHOME.NOTIFIED",true);
            }
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
        textView.setBackground(getResources().getDrawable(R.drawable.layout_border_bottom));
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(20,30,0,30);

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
        date = "25-05-2020";

        textView.setText(date);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(30);
        textView.setPadding(0,50,0,20);
        notificationArea.addView(textView);

        date = "18-05-2020";
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
            intent.putExtra("studentId", studentId);
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
        if(v == speedWarningButton){
            date = "30-05-2020";
            Intent intent = new Intent(NotificationActivity.this,PopSpeedWarningActivity.class);
            intent.putExtra("date", date);
            intent.putExtra("busId", busId);
            startActivity(intent);

        }
        if(v == profileButton){
            Intent intent = new Intent(NotificationActivity.this,ProfileActivity.class);
            intent.putExtra("parent", parent);
            intent.putExtra("studentId", studentId.toString());
            intent.putExtra("busId", busId);
            startActivity(intent);
        }
    }

}
