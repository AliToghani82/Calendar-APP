package com.example.ourcalendarapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MyDayActivity extends AppCompatActivity {

    //private static final String TAG = "MyDayActivity";
    public String clickedDate;
    private int clickedDateInt;
    private Button btn_viewEvent;
    private ListView lv_viewEvents;

    ArrayAdapter eventArrayAdapter;
    DatabaseHelper databaseHelper;
    TextView myDateTextView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_day);

        // Grab the incoming intent from Main_Activity and assign it
        Intent incomingIntent = getIntent();
        clickedDate = incomingIntent.getStringExtra("date");
        clickedDateInt = parseDate(clickedDate);

        myDateTextView = findViewById(R.id.myDateTextView);
        myDateTextView.setText(clickedDate); // Set text view to the date


        // Find our button and listview
        btn_viewEvent = findViewById(R.id.btn_viewEvents);
        lv_viewEvents = findViewById(R.id.lv_viewEvents);

        // Display the events in list view
        databaseHelper = new DatabaseHelper(getApplicationContext());
        showEventOnListAdapter(databaseHelper);

        btn_viewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Display new events when viewEvents is clicked
                DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());

                showEventOnListAdapter(databaseHelper);
                //Toast.makeText(getApplicationContext(), events.toString(), Toast.LENGTH_SHORT).show();
                showEventOnListAdapter(databaseHelper);
            }
        });

        // This allows the user to delete an event by clicking it
        lv_viewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getApplicationContext(), EventPopActivity.class);

                // Get the event
                Event clickedEvent = (Event) parent.getItemAtPosition(position);

                // Put the event into the intent
                i.putExtra("clickedEvent", clickedEvent);
                startActivity(i);

                //DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                /*databaseHelper.deleteOne(clickedEvent);
                showEventOnListAdapter(databaseHelper);*/
            }
        });




        // Start Add Event Activity when addEventBtn is clicked
        Button addEventBtn = findViewById(R.id.addEventBtn);

        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent child = new Intent(getApplicationContext(), AddEventActivity.class);
                startActivity(child);
            }
        });
    }

    private void showEventOnListAdapter(DatabaseHelper databaseHelper) {
        eventArrayAdapter = new ArrayAdapter<Event>(getApplicationContext(), android.R.layout.simple_list_item_1, databaseHelper.getEvents(clickedDateInt));
        lv_viewEvents.setAdapter(eventArrayAdapter);
    }

    private int parseDate(String date){
        String[] parseDate = date.split("/");

        int day = Integer.parseInt(parseDate[1]);
        int month = Integer.parseInt(parseDate[0]);
        int year = Integer.parseInt(parseDate[2]);

        return concateIntegers(day, month, year);



    }

    private int concateIntegers(int day, int month, int year){

        String dayString = Integer.toString(day);
        String monthString = Integer.toString(month);
        String yearString = Integer.toString(year);

        String date = monthString + dayString + yearString;

        int dateInt = Integer.parseInt(date);

        return dateInt;

    }



    /*private void setUpRecyclerView(List<Event> events) {
        RecyclerView recyclerView = findViewById(R.id.eventRecyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getApplicationContext(), events);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    } */



}

