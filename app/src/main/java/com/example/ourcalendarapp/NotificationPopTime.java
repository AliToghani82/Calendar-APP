package com.example.ourcalendarapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class NotificationPopTime extends AppCompatActivity {

    @Override

    // the notification pop up which returns in the alarm preset that is selected using intent
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_alarm_selection);
        Button eventTime = (Button) findViewById(R.id.eventTime);
        Button none = (Button) findViewById(R.id.noneDay);
        Button oneMin = (Button) findViewById(R.id.oneMin);
        Button fiveMin = (Button) findViewById(R.id.fiveMin);
        Button tenMin = (Button) findViewById(R.id.tenMin);
        Button fifteenMin = (Button) findViewById(R.id.fifteenMin);
        Button thrityMin = (Button) findViewById(R.id.thirtyMin);
        Button oneHour = (Button) findViewById(R.id.oneHour);
        Button oneDay = (Button) findViewById(R.id.oneDay);

        // the none button for which no notification
        final Intent i = new Intent();
        none.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("minBefore", "-1");
                i.putExtra("hourBefore","-1");
                i.putExtra("dayBefore", "-1");
                i.putExtra("String", "None");
                i.putExtra("alarm", "0");
                setResult(RESULT_OK, i);
                finish();
            }
        });

        // event button for which notification is on time
        eventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("minBefore", "0");
                i.putExtra("hourBefore","0");
                i.putExtra("dayBefore", "0");
                i.putExtra("String", "At the Time of Event");
                i.putExtra("alarm", "1");
                setResult(RESULT_OK, i);
                finish();

            }
        });

        // notification being sent 1 min before event time
        oneMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("minBefore", "1");
                i.putExtra("hourBefore","0");
                i.putExtra("dayBefore", "0");
                i.putExtra("String", "1 min Before Event");
                i.putExtra("alarm", "2");
                setResult(RESULT_OK, i);
                finish();

            }
        });

        // notification being sent 5 min before event time
        fiveMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("minBefore", "5");
                i.putExtra("hourBefore","0");
                i.putExtra("dayBefore", "0");
                i.putExtra("String", "5 min Before Event");
                i.putExtra("alarm", "3");
                setResult(RESULT_OK, i);
                finish();

            }
        });

        // notification being sent 10 min before event time
        tenMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("minBefore", "10");
                i.putExtra("hourBefore","0");
                i.putExtra("dayBefore", "0");
                i.putExtra("String", "10 min Before Event");
                i.putExtra("alarm", "4");
                setResult(RESULT_OK, i);
                finish();

            }
        });

        // notification being sent 15 min before event time
        fifteenMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("minBefore", "15");
                i.putExtra("hourBefore","0");
                i.putExtra("dayBefore", "0");
                i.putExtra("String", "15 min Before Event");
                i.putExtra("alarm", "5");
                setResult(RESULT_OK, i);
                finish();

            }
        });

        // notification being sent 30 min before event time
        thrityMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("minBefore", "30");
                i.putExtra("hourBefore","0");
                i.putExtra("dayBefore", "0");
                i.putExtra("String", "30 min Before Event");
                i.putExtra("alarm", "6");
                setResult(RESULT_OK, i);
                finish();

            }
        });

        // notification being sent 1 hour before event time
        oneHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("minBefore", "0");
                i.putExtra("hourBefore","1");
                i.putExtra("dayBefore", "0");
                i.putExtra("String", "1 Hour Before Event");
                i.putExtra("alarm", "7");
                setResult(RESULT_OK, i);
                finish();

            }
        });

        // notification being sent 1 day before event time
        oneDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("minBefore", "0");
                i.putExtra("hourBefore","0");
                i.putExtra("dayBefore", "1");
                i.putExtra("String", "1 day Before Event");
                i.putExtra("alarm", "8");
                setResult(RESULT_OK, i);
                finish();

            }
        });
//
//
    }
}
