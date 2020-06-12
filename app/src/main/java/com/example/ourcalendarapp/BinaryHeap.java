package com.example.ourcalendarapp;




import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;



public class BinaryHeap<Event extends Comparable<? super Event>> {

    // Fields
    private static final int DEFAULT_CAPACITY = 4;

    private int currentSize;      // Number of elements in heap
    private Event[] array; // The heap array

    // Default Constructor for binary heap
    // No parameters
    public BinaryHeap() {
        this(DEFAULT_CAPACITY);
    }

    // Constructor for binary heap
    // Parameters: Integer for capacity
    public BinaryHeap(int capacity) {
        currentSize = 0;
        array = (Event[]) new Comparable[capacity + 1];
    }

    // Constructs a binary heap out of an array of items
    // Parameters: An array of event objects
    public BinaryHeap(Event[] items) {
        currentSize = items.length;
        array = (Event[]) new Comparable[(currentSize + 2) * 11 / 10];

        int i = 1;
        for (Event item : items)
            array[i++] = item;
        buildHeap();
    }

    // This function allows user to insert an event object into the priority queue
    // Parameters: An event object
    public void insert(Event x) {
        if (currentSize == array.length - 1)
            enlargeArray(array.length * 2 + 1);

        // Percolate up
        int hole = ++currentSize;
        for (array[0] = x; x.compareTo(array[hole / 2]) < 0; hole /= 2)
            array[hole] = array[hole / 2];
        array[hole] = x;
    }


    //makes the array larger in size.
    private void enlargeArray(int newSize) {
        Event[] old = array;
        array = (Event[]) new Comparable[newSize];
        for (int i = 0; i < old.length; i++)
            array[i] = old[i];
    }

    // Find the smallest item in the priority queue.
    // Return the smallest event in the queue, item at index 1 is min
    public Event findMin() {
        if (isEmpty()) {
             throw new BufferUnderflowException();
        }
        return array[1];
    }

    // Remove the smallest item from the priority queue
    // Returns the item that was deleted
    public Event deleteMin() {
        if (isEmpty()) {
            // throw new UnderflowException();
        }

        Event minItem = findMin();
        array[1] = array[currentSize--];
        percolateDown(1);

        return minItem;
    }

   // Build a heap out of an arbitrary input of items
   // Runs in linear time
    private void buildHeap() {
        for (int i = currentSize / 2; i > 0; i--)
            percolateDown(i);
    }

    // Check if queue is empty
    // returns true if queue is empty
    public boolean isEmpty() {
        return currentSize == 0;
    }

    // Empties the priority queue
    public void makeEmpty() {
        currentSize = 0;
    }



    // Percolate down
    // This function takes in an integer, hole, and uses it to determine where the new event should go according to heap property
    private void percolateDown(int hole) {
        int child;
        Event tmp = array[hole];

        for (; hole * 2 <= currentSize; hole = child) {
            child = hole * 2;
            if (child != currentSize &&
                    array[child + 1].compareTo(array[child]) < 0)
                child++;
            if (array[child].compareTo(tmp) < 0)
                array[hole] = array[child];
            else
                break;
        }
        array[hole] = tmp;
    }
}