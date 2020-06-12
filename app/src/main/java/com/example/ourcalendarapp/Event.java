package com.example.ourcalendarapp;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Comparator;

public class Event implements Comparable<Event>, Comparator<Event>, Parcelable {

    // Fields
    private int id, date, bigId, alarm;
    private String event, location, startTime, endTime, notes;
    private Boolean allDay, dailyRepeat, weeklyRepeat, monthlyRepeat, yearlyRepeat;

    // an empty constructor.
    public Event() {

    }

    //Constrcutor for the class if the given Event is null
    public Event(int id, String eventName){
        this.id = id;
        this.event = eventName;
        this.location = null;
        this.date = 0;
        this.startTime = "00:00 AM";
        this.endTime = "00:00 AM";
        this.notes = "No notes";
        this.allDay = false;
        this.bigId = -1;
        this.alarm = -1;

    }

    //Constructor for class which intializes the given inputs such as event name, startTime, endTime
    public Event(int id,String event, String location, int date, String startTime, String endTime, String notes, Boolean allDay, int bigId, int alarm) {
        this.id = id;
        this.event = event;
        this.location = location;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.notes = notes;
        this.allDay = allDay;
        this.bigId = bigId;
        this.alarm = alarm;
    }

    // intilizes the parcable interface.
    protected Event(Parcel in) {
        id = in.readInt();
        date = in.readInt();
        event = in.readString();
        location = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        notes = in.readString();
        byte tmpAllDay = in.readByte();
        allDay = tmpAllDay == 0 ? null : tmpAllDay == 1;
        bigId = in.readInt();
        alarm = in.readInt();
    }

    //creates the events using parables.
    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    //Returns the toString of this event name which is the event name and starting time to end time.
    public String toString() {

        if(!allDay){
            return event + ": " + startTime + " to " + endTime;
        } else {
            return event + ": All Day";
        }
    }

    // Checks to see if it has the main components to create an event, if one is missing it would return false.
    // otherWise returns true if it is not missing any.
    public boolean hasKeyComponents(){

        // Event is valid if it has an event name date
        if(event == null || date == 0 || startTime == null || endTime == null){
            return false;
        }
        return true;
    }

    // checks to see if the ending time is after starting time or not.
    public boolean validateTimes() {
        if (!allDay) {
        // Convert the time strings to ints
        int startTimeInt = parseTime(startTime);
        int endTimeInt = parseTime(endTime);

            if (startTimeInt > endTimeInt) {
                return false;
            }
        }
        return true;
    }

    // returns the event id
    public int getId(){ return id; }

    // sets the event id by taking an id
    public void setId(int id){ this.id = id; }

    // returns the eventName
    public String getEvent() { return event; }

    // sets the event name by taking an event name
    public void setEvent(String event) {
        this.event = event;
    }

    // returns the location of the event.
    public String getLocation(){
        return location;
    }

    // sets the location of the event by taking a location
    public void setLocation(String location){
        this.location = location;
    }

    // returns the date of event.
    public int getDate(){
        return date;
    }

    // sets the date of the event by taking a date
    public void setDate(int date){
        this.date = date;
    }

    // returns the startTime of the event
    public String getStartTime() {
        return startTime;
    }

    // sets the startTime of the event by taking a startTime
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    // returns the end time of the event
    public String getEndTime() {
        return endTime;
    }

    // sets the end time of the event by taking an end time
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    // returns the notes of the event
    public String getNotes() {
        return notes;
    }

    // sets the notes of the event by taking notes
    public void setNotes(String notes) {
        this.notes = notes;
    }

    // returns a boolean for all day;
    public Boolean getAllDay() {
        return allDay;
    }

    // sets the boolean for all day by taking a boolean
    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }

    // returns the big Id
    public int getBigId () { return bigId; }

    // sets the big id by taking a big id
    public void setBigId (int bigId) { this.bigId = bigId; }

    // returns the number of the alarm
    public int getAlarm () { return alarm; }

    // sets the alarm number to given number
    public void setAlarm (int alarm) { this.alarm = alarm; }

    // Compares 2 events together
    @Override
    public int compare(Event o1, Event o2) {
        char[] event1 = o1.getEvent().toCharArray();
        char[] event2 = o2.getEvent().toCharArray();

        int i = 0; // Intiialize counter variable i

        while (i < event1.length && i < event2.length) // We reached the end, stop
        {
            if (event1[i] - event2[i] == 0) /* if the two elements are the same, tells us nothing about difference */
            {
                i++; // Keep going
            }

            else if (event1[i] - event2[i] < 0) // If true, this->str[i] comes first
            {
                return -1; // Return -1 for sorting
            }

            else if (event1[i] - event2[i] > 0) // If true,argStr.str[i] comes first
            {
                return 1;
            }
        }

        if (event1.length < event2.length)
        {
            return -1;
        }

        else if (event1.length > event2.length)
        {
            return 1;
        }

        else
        {
            return 0;
        }

    }

    // compares the current event to the given event that is in parameter
    @Override
    public int compareTo(Event e) {

        if(this.getAllDay() && !e.getAllDay()){
            return -1;
        }

        else if(e.getAllDay() && !this.getAllDay()){
            return 1;
        }

        else if(this.getAllDay() && e.getAllDay()){
            return 0;
        }

        else {
            int event1 = parseTime(this.getStartTime());
            int event2 = parseTime(e.getStartTime());

            if(event1 - event2 < 0){
                return -1;
            }

            else if(event1 - event2 > 0){
                return 1;
            }

            else {
                return 0;
            }
        }
    }

    // It parses time frm the timeString that is inputed and returns and Int of the time in 24 hour format
    private int parseTime(String timeString){

        if(timeString != null) {
            String[] parseTime = timeString.split(":|\\s");
            String intTime = parseTime[0] + parseTime[1];
            int time = Integer.parseInt((intTime));
            if (time != 1200 && parseTime[2].equals("PM")) {
                time += 1200;
            } else if (time == 1200 && parseTime[2].equals("AM")) {
                time = 0;
            }
            return time;

        } else {
            return 0;
        }
    }

    // it describes the contents of the event.
    @Override
    public int describeContents() {
        return 0;
    }

    // Writing the parcable interface.
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(date);
        dest.writeString(event);
        dest.writeString(location);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(notes);
        dest.writeByte((byte) (allDay == null ? 0 : allDay ? 1 : 2));
        dest.writeInt(bigId);
        dest.writeInt(alarm);
    }
}
