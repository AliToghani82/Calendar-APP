package com.example.ourcalendarapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ourcalendarapp.R;

import java.util.ArrayList;
import java.util.Calendar;

public class AddEventActivity extends AppCompatActivity {

    private EditText eventNameEditText;
    private EditText locationEditText;
    private TextView startTimeTextView;
    private TextView endTimeTextView;
    private TextView selectedDateTextView;
    private EditText eventNotesEditText;
    private int selectedDate;
    private int mHour;
    private int mMinute;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);


        // Find the fields we are going to need to program

        // Activity Fields
        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        Button saveEventButton = (Button) findViewById(R.id.saveEventButton);
        eventNameEditText = (EditText) findViewById(R.id.eventNameEditText);
        locationEditText = (EditText) findViewById(R.id.locationEditText);
        final Switch allDaySwitch = (Switch) findViewById(R.id.allDaySwitch);
        startTimeTextView = (TextView) findViewById(R.id.startTimeTextView);
        ImageButton selectStartTimeImageButton = (ImageButton) findViewById(R.id.selectStartTimeImageButton);
        endTimeTextView = (TextView) findViewById(R.id.endTimeTextView);
        ImageButton selectEndTimeImageButton = (ImageButton) findViewById(R.id.selectEndTimeImageButton);
        selectedDateTextView = (TextView) findViewById(R.id.dateSelectedTextView);
        ImageButton selectDateImageButton = (ImageButton) findViewById(R.id.selectDateButton);
        eventNotesEditText = (EditText) findViewById(R.id.eventNotesEditText);
        Button displayEvent = (Button) findViewById(R.id.cancelButton);

        // Set OCL for cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancel activity on click
                finish();
            }
        });


        // All day toggle (If toggle is switched on, event lasts all day and start/end time text views become today's date
        allDaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startTimeTextView.setText(selectedDateTextView.getText().toString());
                    endTimeTextView.setText(selectedDateTextView.getText().toString());

                } else {
                    startTimeTextView.setText("");
                    endTimeTextView.setText("");
                }
            }
        });


        // Set OCL for select date image button
        selectDateImageButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                handleSelectButton();
            }
        });

        // Set OCL for select start time image button
        selectStartTimeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleStartButton();
            }
        });

        // Set OCL for select end time image button
        selectEndTimeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleEndButton();
            }
        });

        saveEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Event event;
                try {

                    // Extract Event name, location, and notes from the respective text edits store them in event object
                    event = new Event(-1, eventNameEditText.getText().toString(), locationEditText.getText().toString(), selectedDate, startTimeTextView.getText().toString(), endTimeTextView.getText().toString(), eventNotesEditText.getText().toString(), allDaySwitch.isChecked());
                    Toast.makeText(AddEventActivity.this, event.toString(), Toast.LENGTH_SHORT).show();
                } catch(Exception e){
                    Toast.makeText(AddEventActivity.this, "error making event", Toast.LENGTH_SHORT).show();
                    event = new Event(-1, "error", "error", 00000000, "error", "error", "error", false);
                }

                DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                boolean success =  databaseHelper.addOne(event);

                Toast.makeText(AddEventActivity.this, "Success = " + success, Toast.LENGTH_SHORT).show();

                finish();
            }
        });



    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    private void handleSelectButton() {
        Calendar c = Calendar.getInstance();

        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                String monthString  = Integer.toString(month);
                String dayOfMonthString = Integer.toString(dayOfMonth);
                String yearString = Integer.toString(year);

                String date1 = monthString + dayOfMonthString + yearString;
                selectedDate = Integer.parseInt(date1);

                String date2 = month + "/" + dayOfMonth + "/" + year;

                selectedDateTextView.setText(date2);
            }
        }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }

    private void handleStartButton() {
        // Get Current time
        Calendar c = Calendar.getInstance();

        mHour = c.get(Calendar.HOUR);
        mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


                startTimeTextView.setText(formatTime(hourOfDay,minute));

            }
        }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void handleEndButton() {
        // Get Current time
        Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR);
        mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                endTimeTextView.setText(formatTime(hourOfDay,minute));
            }
        }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private String formatTime(int hour, int minutes) {
        hour = hour;
        minutes = minutes;
        String timeSet = "";
        if (hour > 12) {
            hour -= 12;
            timeSet = "PM";
        } else if (hour == 0) {
            hour += 12;
            timeSet = "AM";
        } else if (hour == 12){
            timeSet = "PM";
        }else{
            timeSet = "AM";
        }

        String min = "";
        if (minutes < 10)
            min = "0" + minutes ;
        else
            min = String.valueOf(minutes);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hour).append(':')
                .append(min ).append(" ").append(timeSet).toString();

        return aTime;

    }


}





