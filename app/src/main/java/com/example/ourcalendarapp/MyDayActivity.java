package com.example.ourcalendarapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MyDayActivity extends AppCompatActivity {

    //private static final String TAG = "MyDayActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_day);

        Intent incomingIntent = getIntent();
        String clickedDate = incomingIntent.getStringExtra("date");
        TextView myDateTextView = findViewById(R.id.myDateTextView);
        myDateTextView.setText(clickedDate);

        Button addEventBtn = findViewById(R.id.addEventBtn);
        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), AddEventActivity.class);
                startActivity(startIntent);
            }
        });
    }
}

