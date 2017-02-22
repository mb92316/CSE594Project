package com.example.android.cse594project;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class NotificationPublisher extends BroadcastReceiver {
    DBHandler dbHandler;
    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";
    Context mcontext;
    public void onReceive(Context context, Intent intent) {
        mcontext = context;
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(id, notification);
    }
}
