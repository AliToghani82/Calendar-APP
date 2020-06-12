/* Program: OurCalendarApp
 *
 * Members: Natalie Haass, Ali Toghani, Jerod Hollen
 *
 * Class Description: AddEventActivity interacts with the user, prompting them to enter the details of their events.
 *
 * Functionality: This activity is fairly straightforward. User simply inputs the events information. One thing worth noting is that,
 * in an effort to ensure input is of valid form for time and date, we implemented a date and time picker dialog box and programmed
 * a series of buttons the user can select to open the selection widget, select a date, and have that displayed and extracted in proper format.
 * The program ensures that user has entered a valid event by ensuring it has at least a name, date, and startTime. IF it has a end time, it also
 * validates that the start time comes before it. After the user is satisfied with their event they can click the add button to insert the event into
 * out SQL database. After that, the activity finishes and returns to MyDayActivity.
 *
 */


package com.example.ourcalendarapp;
import android.content.Intent;
import android.os.*;
import android.app.*;
import android.view.View;
import android.widget.*;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class AddEventActivity extends AppCompatActivity implements View.OnClickListener { // This class implements View.OnClickListener because there are a lot of buttons. Doing this enabled me to write the code much cleaner

    // Initialize our fields
    private EditText eventNameEditText;
    private EditText locationEditText;
    private TextView startTimeTextView;
    private TextView endTimeTextView;
    private TextView selectedDateTextView;
    private EditText eventNotesEditText;
    private Switch allDaySwitch;
    private Calendar calendar;
    private int selectedDate;
    private DatabaseHelper databaseHelper = new DatabaseHelper(this);
    private boolean allDaySwitchBool;
    private int mDay1;
    private int mMonth1;
    private int mYear1;
    private int mHour1;
    private int mMinute1;
    private int random;
    private int sqlId;
    private int minBefore;
    private int hourBefore;
    private int dayBefore;
    private String eventNameprv;
    private boolean AlarmSet;
    private TextView alarmSetTextView;
    private String times;
    int setAlarm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        // creates a random number to be sent for notifications
        Random rand  =  new Random();
        random = rand.nextInt(Integer.MAX_VALUE);
        boolean check = databaseHelper.checkId(random);
        if(check) {
            random = rand.nextInt(Integer.MAX_VALUE);
        }

        // Find the Buttons and set their on Click listeners
        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this);
        Button saveEventButton = (Button) findViewById(R.id.saveEventButton);
        saveEventButton.setOnClickListener(this);
        ImageButton selectStartTimeImageButton = (ImageButton) findViewById(R.id.selectStartTimeImageButton);
        selectStartTimeImageButton.setOnClickListener(this);
        ImageButton selectEndTimeImageButton = (ImageButton) findViewById(R.id.selectEndTimeImageButton2);
        selectEndTimeImageButton.setOnClickListener(this);
        ImageButton selectDateImageButton = (ImageButton) findViewById(R.id.selectDateButton);
        selectDateImageButton.setOnClickListener(this);
        ImageButton newPopUp = (ImageButton) findViewById(R.id.SetAlarmButton);
        newPopUp.setOnClickListener(this);

        // Find the TextViews, EditText, and Switch Views
        eventNameEditText = (EditText) findViewById(R.id.eventNameEditText);
        locationEditText = (EditText) findViewById(R.id.locationEditText);
        startTimeTextView = (TextView) findViewById(R.id.startTimeTextView);
        endTimeTextView = (TextView) findViewById(R.id.endTimeTextView);
        selectedDateTextView = (TextView) findViewById(R.id.dateSelectedTextView);
        eventNotesEditText = (EditText) findViewById(R.id.eventNotesEditText);
        alarmSetTextView = (TextView) findViewById(R.id.setAlarmTextView);
        allDaySwitch = (Switch) findViewById(R.id.allDaySwitch);


        // All day toggle (If toggle is switched on, event lasts all day and start/end time text views become today's date
        allDaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startTimeTextView.setText(selectedDateTextView.getText().toString());
                    endTimeTextView.setText(selectedDateTextView.getText().toString());
                    allDaySwitchBool = true;
                } else { // Otherwise, textviews revert back to being blank
                    startTimeTextView.setText("");
                    endTimeTextView.setText("");
                    allDaySwitchBool = false;
                }
            }
        });

    }

    // This is where all of those buttons get programmed
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {

        // If they click the cancel button, cancel the activity
        if(v.getId() == R.id.cancelButton) {
            // Cancel activity
            finish();
        }

        // Or if they click the save event button,
        else if(v.getId() == R.id.saveEventButton){

            Event event;

            // Extract Event name, location, and notes from the respective text edits store them in event object
                times = startTimeTextView.getText().toString();
                event = new Event(-1, eventNameEditText.getText().toString(), locationEditText.getText().toString(), selectedDate, startTimeTextView.getText().toString(), endTimeTextView.getText().toString(), eventNotesEditText.getText().toString(), allDaySwitch.isChecked(), random, setAlarm);

                if(!event.hasKeyComponents()) { // Validates that the user entered at least an event name, date, and start time
                    Toast.makeText(this, "Invalid Event. Must have a name, date, and start time", Toast.LENGTH_SHORT).show();

                } else if (!event.validateTimes()) { // Make sure the user hasn't entered a start time that comes after the end time
                    Toast.makeText(this, "Invalid Event. Event's start time cannot be greater than its end time", Toast.LENGTH_SHORT).show();
                    startTimeTextView.setText("");
                    endTimeTextView.setText("");
                } else { // Event is valid and we can go ahead and add it to the database and create the Notification
                    boolean success = databaseHelper.addOne(event);
                        if((minBefore != -1 || hourBefore != -1 ||  dayBefore != -1)  && !allDaySwitchBool) {
                            String eventStartTime = event.getStartTime();
                            int y = eventStartTime.indexOf(':');
                            int z = eventStartTime.indexOf(' ');
                            int length = eventStartTime.length();
                            String hourS = eventStartTime.substring(0, y);
                            String minS = eventStartTime.substring(y + 1, z);
                            String pmAm = eventStartTime.substring(z + 1, length);
                            int min = Integer.parseInt(minS);
                            int hour = Integer.parseInt(hourS);
                            if (pmAm.equals("PM")) {
                                hour = hour + 12;
                            }
                            Calendar startTime = Calendar.getInstance();
                            startTime.set(Calendar.YEAR, mYear1);
                            startTime.set(Calendar.MONTH, mMonth1 - 1);
                            startTime.set(Calendar.DAY_OF_MONTH, mDay1);
                            startTime.set(Calendar.HOUR_OF_DAY, hour);
                            startTime.set(Calendar.MINUTE, min);
                            startTime.set(Calendar.SECOND, 0);
                            startTime.set(Calendar.MILLISECOND, 0);
                            sqlId = databaseHelper.getId(random);
                            dayBefore = dayBefore * 24 * 60 * 60000;
                            hourBefore = hourBefore * 60 * 60000;
                            minBefore = minBefore * 60000;
                            int alarmBefore = dayBefore + hourBefore + minBefore;
                            if(checkTime(startTime.getTimeInMillis())){
                                createNotificationChannel();
                                setTime(startTime.getTimeInMillis() - alarmBefore);
                            }
                        } else if (allDaySwitchBool) {
                            Calendar startTime = Calendar.getInstance();
                            startTime.set(Calendar.YEAR, mYear1);
                            startTime.set(Calendar.MONTH, mMonth1 - 1);
                            startTime.set(Calendar.DAY_OF_MONTH, mDay1);
                            startTime.set(Calendar.HOUR_OF_DAY, 9);
                            startTime.set(Calendar.MINUTE, 0);
                            startTime.set(Calendar.SECOND, 0);
                            startTime.set(Calendar.MILLISECOND, 0);
                            sqlId = databaseHelper.getId(random);
                            dayBefore = dayBefore * 24 * 60 * 60000;
                            int alarmBefore = dayBefore + hourBefore + minBefore;
                            if(checkTime(startTime.getTimeInMillis())){
                                createNotificationChannel();
                                setTime(startTime.getTimeInMillis() - alarmBefore);
                            }
                        }

                    if(success){ // If successful, tell the user
                        Toast.makeText(AddEventActivity.this, "Event successfully added", Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "Swipe down to refresh events list", Toast.LENGTH_SHORT).show(); // Also let them know to swipe to refresh to see their event. Couldn't figure out how to update in real time.
                        finish();
                    }


                }
             }

        // Or if they click the select start time button
        else if(v.getId() == R.id.selectStartTimeImageButton){
            handleStartButton();
        }

        // Or if they click on the select end time button
        else if(v.getId() == R.id.selectEndTimeImageButton2){
            handleEndButton();
        }

        // Or if they click on the select date button
        else if(v.getId() == R.id.selectDateButton){
            handleSelectButton();
        }

        // or if they click on the set alarm button
        else if(v.getId() == R.id.SetAlarmButton) {
            if(!allDaySwitchBool) {
                Intent intentAlarm = new Intent(AddEventActivity.this, NotificationPopTime.class);
                startActivityForResult(intentAlarm, 1);
            } else if (allDaySwitchBool) {
                Intent intentAlarm = new Intent(AddEventActivity.this, NotificationPopAllDay.class);
                startActivityForResult(intentAlarm, 2);
            }
        }
    }

    private boolean checkTime (Long time) {
        Calendar r = Calendar.getInstance();
        Long timeNow = r.getTimeInMillis();
        if ( timeNow > time) {
            return false;
        } else {
            return true;
        }
//        SimpleDateFormat dateformat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
//        String datetime = dateformat.format(r.getTime());
//        int dayS = Integer.parseInt(datetime.substring(0,2));
//        int monthS = Integer.parseInt(datetime.substring(3,5));
//        int yearS = Integer.parseInt(datetime.substring(6,10));
//        int hourS = Integer.parseInt(datetime.substring(11,13));
//        int minS = Integer.parseInt(datetime.substring(14,16));
//        int check = 0;
//        if ( c.get(Calendar.YEAR) >= yearS) {
//            if ( c.get(Calendar.MONTH) >= monthS) {
//                if ( c.get(Calendar.DAY_OF_MONTH) >= dayS) {
//                    if ( c.get(Calendar.HOUR_OF_DAY) >= hourS) {
//                        if ( c.get(Calendar.MINUTE) >= minS) {
//                            return true;
//                        } else {
//                            return false;
//                        }
//                    } else {
//                        return false;
//                    }
//                } else {
//                    return false;
//                }
//            }  else {
//                return false;
//            }
//        } else {
//            return false;
//        }



    }






    // creates the Notification in the channel based on events id and time.
    private void setTime(Long time) {
        String eventName = eventNameEditText.getText().toString();
        Intent intent = new Intent(AddEventActivity.this, Reminder.class);
        intent.putExtra("eventName", eventName);
        intent.putExtra("id", ""+sqlId);
        intent.putExtra("description" , "At  " + times);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddEventActivity.this, sqlId, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        AlarmSet = true;
        Toast.makeText(AddEventActivity.this, "done", Toast.LENGTH_SHORT).show();
    }

    // creates a Notification channel based on events Id.
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel () {
        NotificationChannel notificationChannel = new NotificationChannel(""+sqlId, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.setDescription("Channel description");
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(notificationChannel);
    }

    // This method displays the date selection widget and sets the selectedDateTextView equal to what user inputed
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void handleSelectButton() {

        calendar = Calendar.getInstance();

        int mYear = calendar.get(Calendar.YEAR);
        final int mMonth = calendar.get(Calendar.MONTH);
        final int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1; // Calendar class indexes months starting at 0, increment by 1 to get the actual month

                mMonth1 = month;
                mDay1 = dayOfMonth;
                mYear1 = year;
                String monthString  = Integer.toString(month); // Convert month int into a string
                String dayOfMonthString = Integer.toString(dayOfMonth); // Convert day int into string
                String yearString = Integer.toString(year); // Convert year int into a string
                if ( dayOfMonth < 10) {
                    dayOfMonthString = "0"+ dayOfMonthString;
                }
                String date1 = monthString + dayOfMonthString + yearString; // Concate the integers
                selectedDate = Integer.parseInt(date1); // Create an integer out of the concated integers

                String date2 = month + "/" + dayOfMonth + "/" + year;

                selectedDateTextView.setText(date2);
            }
        }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }

    // Handles the displaying of the time selection widget and sets startTimeTextView equal to user input
    private void handleStartButton() {
        // Get Current time
        Calendar c = Calendar.getInstance();

        final int mHour = c.get(Calendar.HOUR);
        int mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mHour1 = hourOfDay;
                mMinute1 = minute;
                startTimeTextView.setText(formatTime(hourOfDay,minute));

            }
        }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    // Handles the displaying of the time selection widget and sets endTimeTextView equal to user input
    private void handleEndButton() {
        // Get Current time
        Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR);
        int mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                endTimeTextView.setText(formatTime(hourOfDay,minute));
            }
        }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    // This is a private helper method that takes in the user's input, which is in military time, and converts it into standard time and then formats that time.
    private String formatTime(int hour, int minutes) {
        hour = hour;
        minutes = minutes;
        String timeSet = "";
        if (hour > 12) {
            hour -= 12;
            timeSet = "PM";
        } else if (hour == 0) {
            hour += 12;
            timeSet = "AM";
        } else if (hour == 12){
            timeSet = "PM";
        }else{
            timeSet = "AM";
        }

        String min = "";
        if (minutes < 10)
            min = "0" + minutes ;
        else
            min = String.valueOf(minutes);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hour).append(':')
                .append(min ).append(" ").append(timeSet).toString();

        return aTime;

    }

    // Takes the information from the intent for choosing the alarm
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // for time chosen notification
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String minBeforeString = data.getStringExtra("minBefore");
                String hourBeforeString  = data.getStringExtra("hourBefore");
                String dayBeforeString = data.getStringExtra("dayBefore");
                String timeString = data.getStringExtra("String");
                String getAlarm = data.getStringExtra("alarm");
                minBefore = Integer.parseInt(minBeforeString);
                hourBefore = Integer.parseInt(hourBeforeString);
                dayBefore = Integer.parseInt(dayBeforeString);
                setAlarm = Integer.parseInt(getAlarm);
                alarmSetTextView.setText(timeString);

            }
        // for all day notification
        } else if ( requestCode == 2) {
            if(resultCode == RESULT_OK) {
                String dayString = data.getStringExtra("day");
                String timeString = data.getStringExtra("String1");
                String getAlarm = data.getStringExtra("alarm1");
                dayBefore = Integer.parseInt(dayString);
                hourBefore = 0;
                minBefore = 0;
                alarmSetTextView.setText(timeString);
            }
        }
    }




}





