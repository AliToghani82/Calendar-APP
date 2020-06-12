/* Program: OurCalendarApp
 *
 * Members: Natalie Haass, Ali Toghani, Jerod Hollen
 *
 * Class Description: The MyDayActivity displays the date user clicked and that particular day's events
 * in chronological order.
 *
 * Functionality: We implemented a priority queue (Min Heap) to sort the event objects by time after
 * they are retrieved from the SQL database. The return list then adds the deleted minimum from the
 * priority queue to ensure the earliest time is displayed first. All day events are shown before
 * events with times. User can also choose to add an event from this page, doing so launches the AddEventActivity.
 *
 */



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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MyDayActivity extends AppCompatActivity {

    //private static final String TAG = "MyDayActivity";
    public String clickedDate;
    private int clickedDateInt;
    private ListView lv_viewEvents;
    private SwipeRefreshLayout mySwipeRefreshLayout;

    ArrayAdapter eventArrayAdapter;
    DatabaseHelper databaseHelper = new DatabaseHelper(this);
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


        // Find our swipe layout and listview
        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_2);
        lv_viewEvents = (ListView) findViewById(R.id.lv_viewEvents);

        // Display the events in list view
        showEventOnListAdapter(databaseHelper);

        // On swipe listener for the layout
         mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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
                i.putExtra("date", clickedDateInt+"");
                startActivity(i);
                showEventOnListAdapter(databaseHelper);
            }
        });


        // Start Add Event Activity when addEventBtn is clicked
        Button addEventBtn = findViewById(R.id.addEventBtn);

        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent child = new Intent(getApplicationContext(), AddEventActivity.class);
                startActivity(child);
                showEventOnListAdapter(databaseHelper);
            }
        });
    }

    public void showEventOnListAdapter(DatabaseHelper databaseHelper) {
        eventArrayAdapter = new ArrayAdapter<Event>(getApplicationContext(), android.R.layout.simple_list_item_1, databaseHelper.getEvents(clickedDateInt));
        lv_viewEvents.setAdapter(eventArrayAdapter);
        mySwipeRefreshLayout.setRefreshing(false);
    }

    private int parseDate(String date){
        String[] parseDate = date.split("/");

        int day = Integer.parseInt(parseDate[1]);
        int month = Integer.parseInt(parseDate[0]);
        int year = Integer.parseInt(parseDate[2]);

        return concateIntegers(day, month, year);



    }

    private int concateIntegers(int day, int month, int year){
        String dayString =Integer.toString(day);
        if ( day < 10) {
           dayString = "0"+ dayString;
        }

        String monthString = Integer.toString(month);
        if ( month < 10) {
            monthString = "0"+ monthString;
        }

        String yearString = Integer.toString(year);
        String date = monthString + dayString + yearString;
        int dateInt = Integer.parseInt(date);
        return dateInt;

    }







}

