package com.example.ourcalendarapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EventPopActivity extends AppCompatActivity {

    private TextView tv_event_name;
    private TextView tv_event_location;
    private TextView tv_start_to_end;
    private Button btn_edit_event;
    private Button btn_delete_event;
    DatabaseHelper databaseHelper;
    Event todayEvent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_pop);

        // Get our object from last activity
        Intent incomingIntent = getIntent();
        Parcelable p = incomingIntent.getParcelableExtra("clickedEvent");

        // Create a new object from that object
        todayEvent = (Event) p;

        // Create the format for the pop up window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*0.7) ,(int)(height*0.4));

        // Find the Text Views and Buttons
        tv_event_name = findViewById(R.id.tv_event_name);
        tv_event_location = findViewById(R.id.tv_location);
        tv_start_to_end = findViewById(R.id.tv_start_to_end);
        btn_delete_event = findViewById(R.id.btn_delete_event);


        // Set the set text in the text views accordingly
        tv_event_name.setText(todayEvent.getEvent());
        tv_start_to_end.setText(todayEvent.getNotes());


        // IF all day, just say it is all day
        if(todayEvent.getAllDay()){
            tv_event_location.setText(todayEvent.getLocation() + ", All day");
        } else {
            tv_event_location.setText(todayEvent.getLocation() + ", From " + todayEvent.getStartTime() + " to " + todayEvent.getEndTime());
        }



        btn_delete_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper = new DatabaseHelper(getApplicationContext());
                databaseHelper.deleteOne(todayEvent);
                finish();
            }
        });




    }


}