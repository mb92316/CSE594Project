package com.example.android.cse594project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;

public class AlarmPublisher extends BroadcastReceiver {

    String note;
    private TextToSpeech tts;
    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";
    DBHandler dbHandler;
    Crypt crypt;
    Context mcontext;
    int id;
    public void onReceive(Context context, Intent intent) {
        mcontext = context;
        //noteText = intent.getStringExtra(NOTIFICATION);
        Intent intent1 = new Intent(context, TTS.class);
        id = intent.getIntExtra("id", 0);
        getNote();
        if(note != null) {
            note = crypt.decrypt(note);
            intent1.putExtra(NOTIFICATION, note);
            context.startService(intent1);
        }
    }

    public void getNote() {
        dbHandler = new DBHandler(mcontext, null, null, 1);
        note = dbHandler.getNote(id);
    }

}

