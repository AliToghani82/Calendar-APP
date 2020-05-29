package com.example.ourcalendarapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String EVENTS_TABLE = "EVENTS_TABLE";
    public static final String COLUMN_EVENT_NAME = "EVENT_NAME";
    public static final String COLUMN_EVENT_LOCATION = "EVENT_LOCATION";
    public static final String COLUMN_EVENT_DATE = "EVENT_DATE";
    public static final String COLUMN_EVENT_START = "EVENT_START";
    public static final String COLUMN_EVENT_END = "EVENT_END";
    public static final String COLUMN_EVENT_NOTES = "EVENT_NOTES";
    public static final String COLUMN_EVENT_ALL_DAY = "EVENT_ALL_DAY";
    public static final String COLUMN_ID = "ID";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "calendareventdatabase.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + EVENTS_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_EVENT_NAME + " TEXT, " + COLUMN_EVENT_LOCATION + " TEXT, " + COLUMN_EVENT_DATE + " INT, " + COLUMN_EVENT_START + " TEXT, " + COLUMN_EVENT_END + " TEXT, " + COLUMN_EVENT_NOTES + " TEXT, " + COLUMN_EVENT_ALL_DAY + " BOOL)";

        db.execSQL(createTableStatement);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(Event event){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_EVENT_NAME, event.getEvent());
        cv.put(COLUMN_EVENT_LOCATION, event.getLocation());
        cv.put(COLUMN_EVENT_DATE, event.getDate());
        cv.put(COLUMN_EVENT_START, event.getStartTime());
        cv.put(COLUMN_EVENT_END, event.getEndTime());
        cv.put(COLUMN_EVENT_NOTES, event.getNotes());
        cv.put(COLUMN_EVENT_ALL_DAY, event.getAllDay());

        long insert = db.insert(EVENTS_TABLE, null, cv);

        if(insert == -1){
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteOne(Event event){
        // Find event in database, if found then delete it and return true
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + EVENTS_TABLE + " WHERE " + COLUMN_ID + " = " + event.getId();

       Cursor cursor = db.rawQuery(queryString, null);

       if(cursor.moveToFirst()) {
           return true;
       } else { return false; }

    }

    public List<Event> getEvents(int selectedDate){
        List<Event> returnList = new ArrayList<>();

        // get data from the database

        String queryString = "SELECT * FROM " + EVENTS_TABLE + " WHERE " + COLUMN_EVENT_DATE + " = " + selectedDate;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            // loop through cursor (results) and create new event object. Put them into the list and return list
            do{
                int eventID = cursor.getInt(0);
                String eventName = cursor.getString(1);
                String eventLocation = cursor.getString(2);
                int eventDate = cursor.getInt(3);
                String eventStart = cursor.getString(4);
                String eventEnd = cursor.getString(5);
                String eventNotes = cursor.getString(6);
                boolean allDay = cursor.getInt(7) == 1 ? true : false; // Ternary operator, if int at index 7 is, set true, else set false

                Event newEvent = new Event(eventID, eventName, eventLocation, eventDate, eventStart, eventEnd, eventNotes, allDay);
                returnList.add(newEvent);
            } while (cursor.moveToNext());
        } else {

            // If there are no results from database, don't add anything.

        }

        // Close both the cursor and the db when done
        cursor.close();
        db.close();
        return returnList;


    }


}
