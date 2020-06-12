package com.example.ourcalendarapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Random;

public class Reminder extends BroadcastReceiver {

    // creates a notification channel based on the id and event name that is being sent through intent.
    @Override
    public void onReceive(Context context, Intent intent) {
        Random ran = new Random();
        int m = ran.nextInt(60);
        String name = null;
//        Bundle bundle = intent.getExtras();
        String eventName = intent.getStringExtra("eventName");
        String sqlIdString = intent.getStringExtra("id");
        String description = intent.getStringExtra("description");
        int sqlId = Integer.parseInt(sqlIdString);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ""+sqlId)
                .setSmallIcon(R.drawable.ic_add_alert_black_24dp)
                .setContentTitle(eventName)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(m, builder.build());
    }
}
