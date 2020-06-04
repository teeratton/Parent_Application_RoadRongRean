package com.teerat.parent_map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ScheduleActivity extends AppCompatActivity implements View.OnClickListener {

    private CalendarView calendar_view;
    private TextView date_textView;
    private Switch morning_switch;
    private Switch afternoon_switch;
    private Button notificationButton;
    private Button profileButton;
    private Button busButton;
    private Button mapButton;
    private String date;
    private Parent parent;
    private String studentId;
    private String busId;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference studentRef = db.collection("student");
    private DocumentReference sDocRef;
    private SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        calendar_view = (CalendarView) findViewById(R.id.calendar);
        calendar_view.setMinDate(System.currentTimeMillis());
        date_textView = (TextView) findViewById(R.id.dateTextView);
        morning_switch = (Switch) findViewById(R.id.morningSwitch);
        afternoon_switch = (Switch) findViewById(R.id.afternoonSwitch);
        notificationButton = (Button) findViewById(R.id.notificationButton);
        busButton = (Button) findViewById(R.id.busButton);
        mapButton = (Button) findViewById(R.id.mapButton);
        profileButton = (Button) findViewById(R.id.profileButton);
        notificationButton.setOnClickListener(this);
        busButton.setOnClickListener(this);
        mapButton.setOnClickListener(this);
        profileButton.setOnClickListener(this);
        Intent intent = getIntent();
        parent = intent.getParcelableExtra("parent");
        studentId = intent.getStringExtra("studentId");
        busId = intent.getStringExtra("busId");
        Log.d("studentId", studentId);

        sDocRef = studentRef.document(studentId);

        getDate();

        calendar_view.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String dateString = String.valueOf(dayOfMonth);
                String monthString = String.valueOf(month+1);

                if(String.valueOf(dayOfMonth).length() == 1){
                     dateString = "0"+dayOfMonth;
                    Log.d("dateString", dateString);

                }
                if(String.valueOf(month).length() == 1){
                    monthString = "0"+(month+1);
                    Log.d("monthString", monthString);
                }
                date = dateString + "-" + monthString + "-" + year;

                Log.d("test", date);
                date_textView.setText(date);
                readData(date);


            }
        });

        morning_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    String schedule = date +",MORNING";
                    Boolean check = Boolean.TRUE;
                    updateSchedule(schedule,check);

                } else {
                    String schedule = date +",MORNING";
                    Boolean check = Boolean.FALSE;
                    updateSchedule(schedule,check);


                }
            }
        });

        afternoon_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    String schedule = date +",AFTERNOON";
                    Boolean check = Boolean.TRUE;
                    updateSchedule(schedule,check);

                } else {
                    String schedule = date +",AFTERNOON";
                    Boolean check = Boolean.FALSE;
                    updateSchedule(schedule,check);
                }
            }
        });

    }
    private void updateSchedule(String schedule,Boolean check){
        if (check) {
            sDocRef.update("Absent", FieldValue.arrayUnion(schedule));
    } else {
           sDocRef.update("Absent", FieldValue.arrayRemove(schedule));
        }}

    @Override
    public void onClick(View v) {
        if(v == mapButton){
            Intent intent = new Intent(ScheduleActivity.this,MapsActivity.class);
            intent.putExtra("parent", parent);
            startActivity(intent);
        }
        if(v == busButton){
            Intent intent = new Intent(ScheduleActivity.this,BusActivity.class);
            intent.putExtra("parent", parent);
            intent.putExtra("studentId", studentId);
            intent.putExtra("busId", busId);
            startActivity(intent);
        }
        if(v == notificationButton){
            Intent intent = new Intent(ScheduleActivity.this,NotificationActivity.class);
            intent.putExtra("parent", parent);
            intent.putExtra("studentId", studentId);
            intent.putExtra("busId", busId);
            startActivity(intent);
        }
        if(v == profileButton){
            Intent intent = new Intent(ScheduleActivity.this,ProfileActivity.class);
            intent.putExtra("parent", parent);
            intent.putExtra("studentId", studentId.toString());
            intent.putExtra("busId", busId);
            startActivity(intent);
        }
    }
    private void getDate() {
        Date c = Calendar.getInstance().getTime();
        date = df.format(c);
        date_textView.setText(date);
        readData(date);
    }

    private void readData(final String inDate){
        sDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                List<String> absent = (List<String>) document.get("Absent");
                Boolean morningFound = false;
                Boolean afternoonFound = false;


                for(String item : absent){
                    Log.d("myTag", item);
                    String[] datenTime = item.split(",");
                    String newDate = datenTime[0];
                    String time = datenTime[1];
                    Log.d("newDate", newDate);
                    Log.d("time", time);

                    if(inDate.equals(newDate)){
                        if(time.equals("MORNING")){
                            morningFound = true;
                            morning_switch.setChecked(true);
                        }
                        else {
                            afternoonFound = true;
                            afternoon_switch.setChecked(true);
                        }
                    }
                }
                if(!morningFound){
                    morning_switch.setChecked(false);
                }
                if(!afternoonFound){
                    afternoon_switch.setChecked(false);
                }
            }
        });
    }

}
