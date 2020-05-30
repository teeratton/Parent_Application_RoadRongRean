package com.teerat.parent_map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

public class PopSpeedWarningActivity extends AppCompatActivity {

    private List<Map<String, Object>> speedWarningList;
    private Drawable logo;
    String busId;
    String date;

    private LinearLayout speedWaringArea;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pop_speed_warning);

        speedWaringArea = (LinearLayout) findViewById(R.id.speedWarningArea);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .7), (int) (height * .8));

        Intent intent = getIntent();
        busId = intent.getStringExtra("busId");
        date = intent.getStringExtra("date");

        speedWarning();

    }

    private void speedWarning() {
        DocumentReference bDocRef = FirebaseFirestore.getInstance().document("bus/" + busId + "/speedWarning/" + date);
        Log.d("speed warning", "bus/" + busId + "/speedWarning/" + date);

        bDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                speedWarningList = (List<Map<String, Object>>) document.get("speedWarningList");

                for (int i = 0; i < speedWarningList.size(); i++) {
                    String time = (String)speedWarningList.get(i).get("time");
                    String speed = speedWarningList.get(i).get("speed").toString();
                    TextView textView = new TextView(PopSpeedWarningActivity.this);
                    logo = getResources().getDrawable(R.drawable.speed_warning_logo);
                    logo = resize(logo);
                    int h = logo.getIntrinsicHeight();
                    int w = logo.getIntrinsicWidth();
                    logo.setBounds( 0, 0, w, h );
                    textView.setCompoundDrawables(logo,null,null,null);
                    textView.setCompoundDrawablePadding(50);
                    textView.setText(speed + "Km at " +time);
                    textView.setTextSize(25);
                    textView.setGravity(Gravity.CENTER);
                    textView.setPadding(20,0,0,50);
                    speedWaringArea.addView(textView);
                }
                //Log.d("speed warning", speedWarningList.get(0).get("speed").toString());

            }

        });
    }

    private Drawable resize(Drawable image) {
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 80, 80, false);
        return new BitmapDrawable(getResources(), bitmapResized);
    }
}
