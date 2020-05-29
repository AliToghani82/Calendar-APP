package com.example.ourcalendarapp;

import com.example.ourcalendarapp.EventNode;

public class EventTree {

    public EventNode overallRoot;

    public EventTree() {
        overallRoot = null;
    }

    public EventTree(EventNode root) {
        overallRoot = root;
    }

    public void add(EventNode root) {
        if(overallRoot == null) {
            overallRoot = root;
        } else if (root.hour < overallRoot.hour) {  //event is earlier
            overallRoot = root.left;
        } else if (root.hour > overallRoot.hour) {  // event is later
            overallRoot = root.right;
        } else {                                    // event same time
            if(root.minute > overallRoot.minute) {  //event later
                overallRoot = root.right;
            } else {                                //Event earlier or same time
                overallRoot = root.right;
            }
        }
        rebalance(root);
    }

    private EventNode rotateRight(EventNode y) {
        EventNode x = y.left;
        EventNode z = x.right;
        x.right = y;
        y.left = z;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private EventNode rotateLeft(EventNode y) {
        EventNode x = y.right;
        EventNode z = x.left;
        x.left = y;
        y.right = z;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private EventNode rebalance(EventNode z) {
        updateHeight(z);
        int balance = getBalance(z);
        if (balance > 1) {
            if (z.right.right.height > z.right.left.height) {
                z = rotateLeft(z);
            } else {
                z.right = rotateRight(z.right);
                z = rotateLeft(z);
            }
        } else if (balance < -1) {
            if (z.left.left.height > z.left.right.height)
                z = rotateRight(z);
            else {
                z.left = rotateLeft(z.left);
                z = rotateRight(z);
            }
        }
        return z;
    }

    public String printTree (EventNode root){
        String str = "";
        if(root != null) {
            str += overallRoot.hour + " ";
            str += printTree(root.left);
            str += printTree(root.right);
        }
        return str;
    }

    //This leads to null pointer exception and I am not sure why
    private void updateHeight(EventNode root) {
        root.height = 1 + Math.max((root.left.height), (root.right.height));
    }

    private int getHeight(EventNode root) {
        return root.height;
    }

    //This may or may not lead to null pointer
    private int getBalance(EventNode root) {
        return root.left.height - root.right.height;
    }

}
