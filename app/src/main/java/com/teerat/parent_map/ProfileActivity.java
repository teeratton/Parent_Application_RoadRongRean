package com.teerat.parent_map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity  implements View.OnClickListener{

    private Button selectLocationButton;
    private Button selectStudentButton;
    private Button logoutButton;
    private TextView studentNameTextView;
    private ImageView studentImageView;
    private Parent parent;
    private String busId;
    private String studentId;
    private String studentFirstName;
    private String studentLastName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        selectLocationButton = (Button) findViewById(R.id.setLocationButton);
        selectStudentButton = (Button) findViewById(R.id.selectStudentButton);
        logoutButton = (Button) findViewById(R.id.logOutButton);
        studentNameTextView = (TextView) findViewById(R.id.studentname);
        studentImageView = (ImageView) findViewById(R.id.studentImage);

        selectLocationButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);

        studentImageView.setImageResource(R.drawable.korkeaw);

        Intent intent = getIntent();
        parent = intent.getParcelableExtra("parent");
        busId = intent.getStringExtra("busId");
        studentId = intent.getStringExtra("studentId");
        getStudentInfo();



    }

    @Override
    public void onClick(View v) {
        if (v == selectLocationButton){
            Intent intent = new Intent(ProfileActivity.this,PopSelectLocation.class);
            intent.putExtra("Parent",parent);
            startActivity(intent);
        }
        if (v == selectStudentButton){

        }
        if (v == logoutButton){
            Intent intent = new Intent(ProfileActivity.this,LoginActivity.class);
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




}
