/* Program: OurCalendarApp
 *
 * Members: Natalie Haass, Ali Toghani, Jerod Hollen
 *
 * Class Description: A simple card view with event details
 *
 * Functionality: When the user selects an item in the list from either the SearchEvents or MyDay activities, this cardview will display
 * with the events information on it, if it has it!
 */




package com.example.ourcalendarapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class EventPopActivity extends AppCompatActivity {

    private TextView tv_event_name;
    private TextView tv_event_location;
    private TextView tv_notes;
    private Button btn_delete_event;
    private Button cancel;
    private TextView alarm_set_text;
    private TextView time_set_text;
    private boolean alarm;
    DatabaseHelper databaseHelper;
    Event todayEvent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_pop);

        alarm = true;

        // Get our object from last activity
        Intent incomingIntent = getIntent();
        Parcelable p = incomingIntent.getParcelableExtra("clickedEvent");
        String date = incomingIntent.getStringExtra("date");
        // Create a new object from that object
        todayEvent = (Event) p;

        // Create the format for the pop up window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * 0.6), (int) (height * 0.4));

        // Find the Text Views and Buttons
        tv_event_name = findViewById(R.id.tv_event_name);
        tv_event_location = findViewById(R.id.tv_location);
        tv_notes = findViewById(R.id.tv_start_to_end);
        btn_delete_event = findViewById(R.id.btn_delete_event);
        cancel = findViewById(R.id.cancel);
        alarm_set_text = findViewById(R.id.alarm);
        time_set_text = findViewById(R.id.time);


        // Set the set text in the text views accordingly
        tv_event_name.setText("Event: " + todayEvent.getEvent());
        tv_notes.setText("Notes: " + todayEvent.getNotes());
        tv_event_location.setText("Location: " + todayEvent.getLocation());
        time_set_text.setText(todayEvent.getStartTime());
        alarm_set_text.setText("Alarm: " + parseDate(date, todayEvent.getStartTime(), todayEvent.getAlarm(), todayEvent.getAllDay()));


        // deletes the event once the button is pressed and cancels alarm if there is any
        btn_delete_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper = new DatabaseHelper(getApplicationContext());
                if(alarm) {
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(todayEvent.getId());
                    Toast.makeText(EventPopActivity.this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
                }
                databaseHelper.deleteOne(todayEvent);
                Toast.makeText(EventPopActivity.this, "Event Deleted", Toast.LENGTH_SHORT).show();
                finish();

            }
        });

        // cancels alarms once button is clicked for this event
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( alarm) {
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(todayEvent.getId());
                    Toast.makeText(EventPopActivity.this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    // returns the time and date which notification ( alarm ) is being sent, takes in the date
    // and time as strings, and takes in the alarm id.
    private String parseDate(String date, String startTime, int x, boolean u) {

        int year  =0;
        int month = 0;
        int day =0;
        if(date.length() == 7) {
            month = Integer.parseInt(date.substring(0,1));
            day = Integer.parseInt(date.substring(1,3));
            year = Integer.parseInt(date.substring(3,7));
        } else {
            month = Integer.parseInt(date.substring(0,2));
            day = Integer.parseInt(date.substring(2,4));
            year = Integer.parseInt(date.substring(4,8));
        }




        Calendar c = Calendar.getInstance();
        if(u) {
            startTime = "09:00 AM";
        }

        int y =startTime.indexOf(':');
        int z = startTime.indexOf(' ');
        int length = startTime.length();
        String hourS = startTime.substring(0, y);
        String minS = startTime.substring(y+1, z);
        String pmAm = startTime.substring(z+1, length);
        int min = Integer.parseInt(minS);
        int hour = Integer.parseInt(hourS);

         if ( pmAm.equals("PM")) {
             hour = hour + 12;
         }
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, min);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        if ( x == 0 || x == 9) {
            alarm = false;
            return "None";
        } else if ( x == 1 || x ==10) {
            return month + "/" + day + "/" + year + " " + startTime;
        } else if ( x == 2) {
            c.setTimeInMillis(c.getTimeInMillis() - 60000);
            return getDate(c, pmAm);
        } else if ( x == 3) {
            c.setTimeInMillis(c.getTimeInMillis() - 300000);
            return getDate(c, pmAm);
        } else if ( x== 4) {
            c.setTimeInMillis(c.getTimeInMillis() - 600000);
            return getDate(c, pmAm);
        } else if ( x== 5) {
            c.setTimeInMillis(c.getTimeInMillis() - 900000);
            return getDate(c, pmAm);
        } else if ( x== 6) {
            c.setTimeInMillis(c.getTimeInMillis() - 1800000);
            return getDate(c, pmAm);
        } else if ( x== 7) {
            c.setTimeInMillis(c.getTimeInMillis() - 3600000);
            return getDate(c, pmAm);
        } else if ( x== 8 || x == 11) {
            c.setTimeInMillis(c.getTimeInMillis() - 86400000);
            return getDate(c, pmAm);
        }
        return  "";
    }

    // makes the given date and time and combines it into 1 string
    private String getDate ( Calendar c, String pmAm) {
        int hour = c.get(c.HOUR_OF_DAY);
        int min = c.get(c.MINUTE);
        int day = c.get(c.DAY_OF_MONTH);
        int month = c.get(c.MONTH);
        int year = c.get(c.YEAR);
        if ( pmAm.equals("PM")) {
            hour = hour -12;
        }
        return month + "/" + day + "/" + year + " " + hour + ":" + min + " " + pmAm;
    }


}