package com.teerat.parent_map;

import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.ImageView;
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

import java.util.List;


public class ScheduleActivity extends AppCompatActivity {

    private CalendarView calendar_view;
    private TextView date_textView;
    private Switch morning_switch;
    private Switch afternoon_switch;
    private String date;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference studentRef = db.collection("student");
    private DocumentReference document = studentRef.document("1");





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        calendar_view = (CalendarView) findViewById(R.id.calendar);
        date_textView = (TextView) findViewById(R.id.dateTextView);
        morning_switch = (Switch) findViewById(R.id.morningSwitch);
        afternoon_switch = (Switch) findViewById(R.id.afternoonSwitch);


        calendar_view.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = dayOfMonth + "-" + (month+1) + "-" + year;
                Log.d("test", date);
                date_textView.setText(date);

                document.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

                            if(date.equals(newDate)){
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
        studentRef.document("1").update("Absent", FieldValue.arrayUnion(schedule));
    } else {
            studentRef.document("1").update("Absent", FieldValue.arrayRemove(schedule));
        }}
}
