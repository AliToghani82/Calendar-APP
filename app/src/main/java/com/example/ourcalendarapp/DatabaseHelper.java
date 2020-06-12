/* Program: OurCalendarApp
 *
 * Members: Natalie Haass, Ali Toghani, Jerod Hollen
 *
 * Class Description: This class is the messenger between our application and the SQLDatabase.
 *
 * Functionality: This class creates the structure for events table and provides methods that allows the user and the program to interact
 * with the database. i.e. adding, deleting, or retrieving events.
 *
 * IMPORTANT** Event's date are in integer format (i.e. June 6th, 2020 == 662020) to make comparisons possible for SQL language.
 */




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


    // Create constant string values for our columns to save time with SQL commands
    public static final String EVENTS_TABLE = "EVENTS_TABLE";
    public static final String COLUMN_EVENT_NAME = "EVENT_NAME";
    public static final String COLUMN_EVENT_LOCATION = "EVENT_LOCATION";
    public static final String COLUMN_EVENT_DATE = "EVENT_DATE";
    public static final String COLUMN_EVENT_START = "EVENT_START";
    public static final String COLUMN_EVENT_END = "EVENT_END";
    public static final String COLUMN_EVENT_NOTES = "EVENT_NOTES";
    public static final String COLUMN_EVENT_ALL_DAY = "EVENT_ALL_DAY";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_BIGID = "BIGID";
    public static final String COLUMN_ALARM = "ALARM";
    public static final String COLUMN_DATESTRING = "DATE";


    // Constructor for databaseHelper
    public DatabaseHelper(@Nullable Context context) {
        super(context, "calendareventdatabase.db", null, 5);
    }

    // Create events table in "calendareventdatabase.db"
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + EVENTS_TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_EVENT_NAME + " TEXT," + COLUMN_EVENT_LOCATION + " TEXT," + COLUMN_EVENT_DATE + " INT," + COLUMN_EVENT_START + " TEXT," + COLUMN_EVENT_END + " TEXT," + COLUMN_EVENT_NOTES + " TEXT," + COLUMN_EVENT_ALL_DAY + " BOOL," + COLUMN_BIGID + " INT," + COLUMN_ALARM + " INT)";
        db.execSQL(createTableStatement);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // This method adds and event to the database
    // Parameters: Event Object
    // Returns: Boolean; True if insert successfull

    public boolean addOne(Event event) {

        // Get a writable database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        // ContentValues is a map, the key is the column name and the value is the Event object's field.
        cv.put(COLUMN_EVENT_NAME, event.getEvent());
        cv.put(COLUMN_EVENT_LOCATION, event.getLocation());
        cv.put(COLUMN_EVENT_DATE, event.getDate());
        cv.put(COLUMN_EVENT_START, event.getStartTime());
        cv.put(COLUMN_EVENT_END, event.getEndTime());
        cv.put(COLUMN_EVENT_NOTES, event.getNotes());
        cv.put(COLUMN_EVENT_ALL_DAY, event.getAllDay());
        cv.put(COLUMN_BIGID, event.getBigId());
        cv.put(COLUMN_ALARM, event.getAlarm());


        // Insert into EVENT_TABLE all those content values
        long insert = db.insert(EVENTS_TABLE, null, cv);

        // Ensure it actually go inserted
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    // This method deletes an event from the database.
    // Parameters: Event Object
    // Return: Boolean, true if deletion successful
    // This basically says hey, delete the event I clicked on.
    public boolean deleteOne(Event event) {
        // Find event in database, if found then delete it and return true
        SQLiteDatabase db = this.getWritableDatabase();

        // Using the event id is an effective way to locate from the database the same event user clicked on in the listview.
        String queryString = "DELETE FROM " + EVENTS_TABLE + " WHERE " + COLUMN_ID + " = " + event.getId();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }

    }

    // it counts how many events are currently saved in the database
    public int count() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT COUNT (*) FROM " + EVENTS_TABLE;
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.getCount() > 0 ) {
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
        return 0;

    }

    // gives the event id in database by getting the big id which is a random id number that is different from sql one.
    public int getId (int bigId) {
        String queryStrings = "SELECT * FROM " + EVENTS_TABLE + " WHERE " + COLUMN_BIGID + " = " + bigId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryStrings, null);
        if(cursor.moveToFirst()){
            //if (cursor.getString(8).equals(bigId)) {
            int red = cursor.getInt(0);
            cursor.close();
            db.close();
            return red;
            //}
        }
        cursor.close();
        db.close();
        return -1;
    }


    // This method retrieves all the events matching the date that the user clicked on in the mainactivity page
    // Parameters: Int selectedDate, this value is initialized the second the user selects a specific date on the calendarView in main
    // Returns: List<Events>, this is used to set the list view in the MyDayActivity page (Uses an array adapter)
    public List<Event> getEvents(int selectedDate) {
        // Initialize our list and Priority Queue
        List<Event> returnList = new ArrayList<>();
        BinaryHeap<Event> pq = new BinaryHeap<>();

        // Select only all the rows from event table where the value at column event date in db is equal to the date the user clicked
        String queryString = "SELECT * FROM " + EVENTS_TABLE + " WHERE " + COLUMN_EVENT_DATE + " = " + selectedDate;
        Event newEvent;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        // If the search query found stuff, extract it from the database into an event object
        if (cursor.moveToFirst()) {

            do {
                newEvent = extractEventFromSQL(queryString, cursor, db);
                pq.insert(newEvent); // Insert event object into the priority queue
            } while (cursor.moveToNext());

            // While the priority queue has events, put the minimum one into the array list
            while (!pq.isEmpty()) {
                returnList.add(pq.deleteMin()); // Add the minimum value (Earliest time) to the return list from the priority queue
            }
        } else {
            // Don't do anything
        }

        cursor.close();
        db.close();
        return returnList;
    }

    // gets the event name using its id
    public String getEvent (int i) {
        String queryStrings = "SELECT * FROM " + EVENTS_TABLE + " WHERE " + COLUMN_ID + " = " + i;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryStrings, null);
        if(cursor.moveToFirst()){
            return cursor.getString(1);
        }
        return "null";
    }

    // this method takes in a random number, and makes sure that number is not being used already in the databse
    // if it is being used, sends true, if it is not being used, sends false.
    public boolean checkId ( int random ) {
        String queryStrings = "SELECT * FROM " + EVENTS_TABLE + " WHERE " + COLUMN_BIGID + " = " + random;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryStrings, null);
        if(cursor.moveToFirst()){
            cursor.close();
            db.close();
            return true;
        }
        return false;
    }

    // This method is used for searching all the events alphabetically. The other method takes in a parameter because we are searching
    // for a specific date, with this one we want all of them. It grabs each column from a row, creates an event object with it, and inserts
    // it into the AVL Tree.
    public List<Event> getAllEvents() {
        List<Event> returnList = new ArrayList<>(); // Initialize our returnList
        EventTree<Event> searchTree = new EventTree<>(); // Initialize our AVL tree
        Event newEvent;
        String queryString = "SELECT * FROM " + EVENTS_TABLE; // Select all columns in table EVENTS_TABLE
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        // If the database's first row has data in it
        if (cursor.moveToFirst()) {
            do {
                // Create an event from that row
                newEvent = extractEventFromSQL(queryString, cursor, db);
                searchTree.insert(newEvent); // Insert event into the avl tree
            } while (cursor.moveToNext()); // Repeat while there is still data

            // While the tree is not empty
            while (!searchTree.isEmpty()) {
                Event event = searchTree.findMin(); // find minimum (Alphabetically) event from the tree, O (log n ) time
                returnList.add(event); // add it to the return list
                searchTree.remove(event); // remove that event from the tree
            }

        } else {
            // Don't do anything
        }

        cursor.close();
        db.close();

        return returnList;
    }

    // Simple method that extracts the event from the SQL.
    // Parameters: String queryString, Cursor cursor, SQLiteDabase db;
    // Query string tells us what to retrieve, cursor iterates through columns,
    // db access gets the database
    // Returns: An Event Object; Newly initialized from the database
    public Event extractEventFromSQL(String queryString, Cursor cursor, SQLiteDatabase db) {

                int eventID = cursor.getInt(0);
                String eventName = cursor.getString(1);
                String eventLocation = cursor.getString(2);
                int eventDate = cursor.getInt(3);
                String eventStart = cursor.getString(4);
                String eventEnd = cursor.getString(5);
                String eventNotes = cursor.getString(6);
                boolean allDay = cursor.getInt(7) == 1 ? true : false; // Ternary operator, if int at index 7 is, set true, else set false
                int bigIdInt = cursor.getInt(8);
                int alarm = cursor.getInt(9);


                Event newEvent = new Event(eventID, eventName, eventLocation, eventDate, eventStart, eventEnd, eventNotes, allDay, bigIdInt, alarm);
                return newEvent;

        }


    }




