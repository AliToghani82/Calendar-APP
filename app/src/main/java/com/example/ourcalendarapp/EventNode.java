package com.example.ourcalendarapp;

import org.w3c.dom.Node;

public class EventNode {
    public int hour;
    public int minute;
    public int height;
    public EventNode left;
    public EventNode right;

    public EventNode(int h, int m) {
        this.hour = h;
        this.minute = m;
    }
}
