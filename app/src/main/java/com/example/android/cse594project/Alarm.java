package com.example.android.cse594project;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Alarm extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    DBHandler dbHandler;
    int id;
    String noteText;
    Button notificationButton;
    Button voiceButton;
    Crypt crypt;
    int day, month, year, hour, minute;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;
    int choice;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int alarmID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        notificationButton = (Button) findViewById(R.id.alarmbutton);
        voiceButton = (Button) findViewById(R.id.voicebutton);
        Bundle extras = getIntent().getExtras();
        noteText = extras.getString("notetext");
        id = extras.getInt("id");
        dbHandler = new DBHandler(this, null, null, 1);
        crypt = new Crypt();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        alarmID = pref.getInt("AlarmID", 0);
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice = 1;
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Alarm.this, Alarm.this, year, month, day);
                datePickerDialog.show();
            }
        });
        voiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice = 2;
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Alarm.this, Alarm.this, year, month, day);
                datePickerDialog.show();
            }
        });
    }


    public void onDateSet(DatePicker view, int i, int i1, int i2) {
        yearFinal = i;
        monthFinal = i1 + 1;
        dayFinal = i2;
        Calendar c = Calendar.getInstance();
        hour=c.get(Calendar.HOUR_OF_DAY);
        minute=c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(Alarm.this, Alarm.this,
                hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    public void onTimeSet(TimePicker view, int i, int i1) {
        hourFinal   = i;
        minuteFinal = i1;
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm");
        String test = yearFinal + "-" + monthFinal + "-" + dayFinal + " " + hourFinal + ":" + minuteFinal;
        Date date = new Date();
        Date d1 = new Date();
        try {
            d1 = ft.parse(test);
        }catch (ParseException e) {
            System.out.println("Unparseable using " + ft);
        }
        long diff = d1.getTime() - date.getTime();
        System.out.println(diff);
        String encryptedalarmNote = dbHandler.getNote(id);
        String alarmNote = crypt.decrypt(encryptedalarmNote);
        if(choice == 1) {
            scheduleNotification(getNotification(alarmNote), diff);
        }
        else if(choice == 2) {
            scheduleNotification(noteText, diff);
        }
        else{
            Toast.makeText(this, "something broke", Toast.LENGTH_LONG).show();
        }
    }

    private void scheduleNotification(Notification notification, long delay) {
        editor = pref.edit();
        alarmID++;
        editor.putInt("AlarmID", alarmID);
        editor.commit();
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 20);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        notificationIntent.putExtra("id", id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), alarmID, notificationIntent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent);
    }


    private void scheduleNotification(String notification, long delay) {
        editor = pref.edit();
        alarmID++;
        editor.putInt("AlarmID", alarmID);
        editor.commit();
        Intent notificationIntent = new Intent(this, AlarmPublisher.class);
        notificationIntent.putExtra(AlarmPublisher.NOTIFICATION_ID, 20);
        notificationIntent.putExtra(AlarmPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), alarmID, notificationIntent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent);
    }


    private Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_launcher);
        return builder.build();
    }
}
