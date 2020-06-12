/* Program: OurCalendarApp
 *
 * Members: Natalie Haass, Ali Toghani, Jerod Hollen
 *
 * Class Description: This activity allows the user to search through an alphabetical list of their events
 *
 * Functionality: To make this possible, we implemented an AVL tree made out of event nodes. The activity retrieves every single event
 * from the database and adds it to the AVL tree. The tree always balanced so it is very fast to search for the events in alphabetical order,
 * add them to the return list, and then remove them from the tree. We had to implement both the Comparable and Comparator interfaces in our
 * Event object. Comparable's compareTo is used to sort the events base on startTimes and the Comparator's compare method is used to sort them
 * alphabetically.
 *
 */


package com.example.ourcalendarapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchEvents extends AppCompatActivity {

    ListView search_events;
    ArrayAdapter<Event> adapter;
    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    SwipeRefreshLayout mSwipeRefreshLayout;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_events);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showEventOnListAdapter(databaseHelper);

            }


        });
        search_events = findViewById(R.id.lv_searchEvents);
        showEventOnListAdapter(databaseHelper);

        search_events.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getApplicationContext(), EventPopActivity.class);

                // Get the event
                Event clickedEvent = (Event) parent.getItemAtPosition(position);





                // Put the event into the intent
                i.putExtra("clickedEvent", clickedEvent);
                i.putExtra("date", clickedEvent.getDate()+"");
                startActivity(i);
                showEventOnListAdapter(databaseHelper);

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_events, menu);
        MenuItem list_view = menu.findItem(R.id.search_events);

        SearchView searchView = (SearchView) list_view.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void showEventOnListAdapter(DatabaseHelper databaseHelper) {
        adapter = new ArrayAdapter<Event>(getApplicationContext(), android.R.layout.simple_list_item_1, databaseHelper.getAllEvents());
        search_events.setAdapter(adapter);
        mSwipeRefreshLayout.setRefreshing(false);

    }
}