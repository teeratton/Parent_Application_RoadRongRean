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

import java.util.Map;

public class NotificationActivity extends AppCompatActivity {

    private DocumentReference mDocRef = FirebaseFirestore.getInstance().document("student/2/Event/22-04-2020");
    private LinearLayout notificationArea;
    private String time;
    private GeoPoint location;

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
                        Map<String, Object> arrivehome =(java.util.Map<String, Object>) t.get("a");
                        location = (GeoPoint) arrivehome.get("LOCATION");
                        time = (String) arrivehome.get("TIME");
                        updateNotification(time,location);
                        System.out.println(arrivehome);
                    }catch (Exception a){
                        System.out.println("S");
                    }
                    /*
                    Map<String, Object> arrivehome =(java.util.Map<String, Object>) t.get("ARRIVEHOME");
                    location = (GeoPoint) arrivehome.get("LOCATION");
                    time = (String) arrivehome.get("TIME");
                    updateNotification(time,location);
                    System.out.println(arrivehome);
                    */
                    /*
                    Map<String,Object> te = (java.util.Map<String, Object>) t.get("ARRIVEHOME");
                    location = (GeoPoint)te.get("LOCATION");
                    time = (String)te.get("TIME");
                    */
                    //updateNotification(time,location);


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


    }

    private void updateNotification(String time, GeoPoint location){

        TextView textView = new TextView(this);
        textView.setText(time);
        notificationArea.addView(textView);


    }


}
