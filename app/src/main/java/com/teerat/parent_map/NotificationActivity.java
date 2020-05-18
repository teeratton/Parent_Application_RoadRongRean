package com.teerat.parent_map;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {

    private DocumentReference mDocRef;
    private LinearLayout notificationArea;
    private String time;
    private GeoPoint location;
    private boolean bArriveSchool = false;
    private boolean bLeaveHome = false;
    private boolean bLeaveSchool = false;
    private boolean bArriveHome = false;
    private SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    private String date;

    @Override
    protected void onStart() {
        super.onStart();
        Log.w("test", "2");

        mDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    Log.w("pass", "im here", e);

                    Map<String, Object> t = documentSnapshot.getData();
                    System.out.println(t);
                    try{
                        Map<String, Object> leaveHome =(java.util.Map<String, Object>) t.get("LEAVEHOME");
                        location = (GeoPoint) leaveHome.get("LOCATION");
                        time = (String) leaveHome.get("TIME");
                        if(!bLeaveHome){
                            updateNotification("Leave the home",time,location);
                        }
                    }catch (Exception a){
                        System.out.println("S");
                    }
                    try{
                        Map<String, Object> arriveSchool =(java.util.Map<String, Object>) t.get("ARRIVESCHOOL");
                        location = (GeoPoint) arriveSchool.get("LOCATION");
                        time = (String) arriveSchool.get("TIME");
                        if(!bArriveSchool){
                            updateNotification("Arrive at school",time,location);
                        }
                    }catch (Exception a){
                        System.out.println("S");
                    }
                    try{
                        Map<String, Object> leaveSchool =(java.util.Map<String, Object>) t.get("LEAVESCHOOL");
                        location = (GeoPoint) leaveSchool.get("LOCATION");
                        time = (String) leaveSchool.get("TIME");
                        if(!bLeaveSchool){
                            updateNotification("Leave the school",time,location);
                        }
                    }catch (Exception a){
                        System.out.println("S");
                    }
                    try{
                        Map<String, Object> arriveHome =(java.util.Map<String, Object>) t.get("ARRIVEHOME");
                        location = (GeoPoint) arriveHome.get("LOCATION");
                        time = (String) arriveHome.get("TIME");
                        if(!bArriveHome){
                            updateNotification("Arrived home",time,location);
                        }
                    }catch (Exception a){
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
        getDate();




    }

    private void updateNotification(String state,String time, GeoPoint location){

        TextView textView = new TextView(this);
        textView.setText(state + " : " + time);
        notificationArea.addView(textView);
        if(state.equals("Leave the home")){
            bLeaveHome = true;
        }
        if(state.equals("Arrive at school")){
            bArriveSchool = true;
        }
        if(state.equals("Leave the school")){
            bLeaveSchool = true;
        }
        if(state.equals("Arrived home")){
            bArriveHome = true;
        }

    }
    private void getDate(){
        Date c = Calendar.getInstance().getTime();
        String date = df.format(c);
        TextView textView = new TextView(this);
        textView.setText(date);
        notificationArea.addView(textView);
        mDocRef = FirebaseFirestore.getInstance().document("student/2/Event/"+date);
    }


}
