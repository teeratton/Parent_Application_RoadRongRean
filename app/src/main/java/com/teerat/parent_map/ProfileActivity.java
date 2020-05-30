package com.teerat.parent_map;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProfileActivity extends AppCompatActivity  implements View.OnClickListener{

    private Button selectLocationButton;
    private Button selectStudentButton;
    private Button logoutButton;
    private Parent parent;
    private String busId;
    private String studentId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        selectLocationButton = (Button) findViewById(R.id.setLocationButton);
        selectStudentButton = (Button) findViewById(R.id.selectStudentButton);
        logoutButton = (Button) findViewById(R.id.logOutButton);

        selectLocationButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);

        Intent intent = getIntent();
        parent = intent.getParcelableExtra("parent");
        busId = intent.getStringExtra("busId");
        studentId = intent.getStringExtra("studentId");

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




}
