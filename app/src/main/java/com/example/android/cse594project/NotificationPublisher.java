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
    String note;
    Crypt crypt;
    Context mcontext;
    int id;
    int noteID;
    public void onReceive(Context context, Intent intent) {
        mcontext = context;
        noteID = intent.getIntExtra("id", 0);
        getNote();
        note = crypt.decrypt(note);
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = getNotification(note);
        id = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(id, notification);
    }

    public void getNote() {
        dbHandler = new DBHandler(mcontext, null, null, 1);
        note = dbHandler.getNote(noteID);
    }

    private Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(mcontext);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_launcher);
        return builder.build();
    }

}
