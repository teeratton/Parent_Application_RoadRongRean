package com.teerat.parent_map;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PopSelectStudent extends AppCompatActivity {

    private LinearLayout studentListArea;
    private Button okButton;
    private Parent parent;
    private List<Integer> studentList;
    private String studentId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.8));

        setContentView(R.layout.pop_select_student);
        studentListArea = (LinearLayout) findViewById(R.id.studentListArea);
        okButton = (Button) findViewById(R.id.okButton);

        Intent intent = getIntent();
        parent = intent.getParcelableExtra("parent");
        studentId = intent.getStringExtra("studentId");

        studentList = parent.getStudentList();

        showStudentList();

    }

    private void showStudentList() {
        for(final Integer e : studentList){
            final DocumentReference sDocRef = FirebaseFirestore.getInstance().document("student/"+e.toString());

            sDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            String studentFirstName = document.getString("firstName");
                            String studentLastName = document.getString("lastName");
                            Log.d("student name", studentFirstName + studentLastName);
                            TextView textView = new TextView(PopSelectStudent.this);
                            textView.setText(studentFirstName + " " + studentLastName);
                            textView.setTextSize(20);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            params.setMargins(0, 0, 0, 30);

                            textView.setLayoutParams(params);

                            if(studentId.equals(e.toString())){
                                textView.setBackground(getResources().getDrawable(R.drawable.my_button_bg_selected));

                            }else{
                                textView.setBackground(getResources().getDrawable(R.drawable.my_button_bg));
                                textView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(PopSelectStudent.this,MapsActivity.class);
                                        intent.putExtra("parent",parent);
                                        PrefConfig.saveStudentIdPref(getApplicationContext(),e);
                                        startActivity(intent);
                                    }
                                });
                            }

                            studentListArea.addView(textView);

                        }else {
                            Log.d("test", "No such document");
                        }
                    }else {
                        Log.d("test", "get failed with ", task.getException());
                    }
                }
            });
        }
    }



}
