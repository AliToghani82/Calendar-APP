package com.example.ourcalendarapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class NotificationPopAllDay extends AppCompatActivity {

    @Override

    // the notification pop up which returns in the alarm preset that is selected using intent if all day is selected
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_alarm_selection_allday);
        Button noneDay = (Button) findViewById(R.id.noneDay);
        Button EventDay = (Button) findViewById(R.id.eventTimeDay);
        Button oneDay = (Button) findViewById(R.id.oneDayBeforeDay);

        // no alarms for this event
        final Intent info = new Intent();
        noneDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.putExtra("day", "-1");
                info.putExtra("String1", "none");
                info.putExtra("alarm1", "9");
                setResult(RESULT_OK, info);
                finish();
            }
        });

        // at the time event, which will be sent at 9 am that day
        EventDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.putExtra("day", "0");
                info.putExtra("String1", "9 AM");
                info.putExtra("alarm1", "10");
                setResult(RESULT_OK, info);
                finish();
            }
        });

        // at the time event, which will be sent at 9 am, a day before.
        oneDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.putExtra("day", "1");
                info.putExtra("String1", "1 Day Before at 9 AM");
                info.putExtra("alarm1", "11");
                setResult(RESULT_OK, info);
                finish();
            }
        });

    }
}
