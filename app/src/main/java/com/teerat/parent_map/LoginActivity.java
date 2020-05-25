package com.teerat.parent_map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText usernameTextView;
    private EditText passwordTextView;
    private Button loginButton;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference parentRef = db.collection("parent");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameTextView = (EditText) findViewById(R.id.usernameTextView);
        passwordTextView = (EditText) findViewById(R.id.passwordTextView);
        loginButton = (Button) findViewById(R.id.loginButton);

        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

    }


    private void login() {
        String username =  usernameTextView.getText().toString();
        Query query = parentRef.whereEqualTo("username", username);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Parent parent = document.toObject(Parent.class);
                        if (passwordTextView.getText().toString().equals(parent.getPassword())){
                            Log.d("TAG", "LOGIN successful");
                            Log.d("TAG", parent.getStudentList().toString());


                            Intent intent = new Intent(LoginActivity.this,MapsActivity.class);
                            intent.putExtra("parent", parent);
                            startActivity(intent);
                        }
                        else{
                            Log.d("TAG", "Incorrect password");

                        }

                    }
                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v == loginButton){
            login();
        }
    }
}
