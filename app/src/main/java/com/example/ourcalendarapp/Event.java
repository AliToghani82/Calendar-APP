package com.example.ourcalendarapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Event implements Parcelable {

    // Fields
    private int id, date;
    private String event, location, startTime, endTime, notes;
    private Boolean allDay;

    public Event() {

    }

    public Event(int id,String event, String location, int date, String startTime, String endTime, String notes, Boolean allDay) {
        this.id = id;
        this.event = event;
        this.location = location;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.notes = notes;
        this.allDay = allDay;
    }

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
    }

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
    }

    @Override
    public int describeContents() {
        return 0;
    }

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

    @Override
    public String toString() {

        if (!allDay) {
            return event + " at " + location + " from " + startTime + " to " + endTime + ".";
        } else {
            return event + " at " + location + " all day.";
        }
    }

    public int getId(){ return id; }

    public void setId(int id){ this.id = id; }

    public String getEvent() { return event; }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getLocation(){
        return location;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public int getDate(){
        return date;
    }

    public void setDate(int date){
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getAllDay() {
        return allDay;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }


}
