/* Program: OurCalendarApp
 *
 * Members: Natalie Haass, Ali Toghani, Jerod Hollen
 *
 * Class Description: This is the main activity for the calendar up.
 * It is the first activity the user sees upon starting the app.
 *
 * Functionality: Displays a calendar view initialized with today's date. From here the user can either search all of
 * their events or tap on a specific date to add a new one. Doing so launches activity two: MyDayActivity
 *
 */

package com.example.ourcalendarapp;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ourcalendarapp.R;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static com.example.ourcalendarapp.DatabaseHelper mDatabaseHelper;
    public static SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CalendarView mCalendarView = findViewById(R.id.calendarView);

        Button btn_searchEvents = (Button) findViewById(R.id.btn_searchEvents);


        btn_searchEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SearchEvents.class);
                startActivity(i);
            }
        });

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = month+1 + "/" + dayOfMonth + "/" + year; // not sure what this is but the tutorial said to do it
                //Log.d(TAG, "onSelectedDayChange: date: " + date);
                Intent intent = new Intent(getApplicationContext(), MyDayActivity.class);
                intent.putExtra("date", date);

                startActivity(intent);
            }
        });

    }

}
