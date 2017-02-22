package com.example.android.cse594project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class AlarmPublisher extends BroadcastReceiver implements TextToSpeech.OnInitListener {

    String noteText;
    private TextToSpeech tts;
    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    public void onReceive(Context context, Intent intent) {
        noteText = intent.getStringExtra(NOTIFICATION);
        Intent intent1 = new Intent(context, TTS.class);
        intent1.putExtra(NOTIFICATION, noteText);
        context.startService(intent1);
    }

    private void speakOut(String text) {
        String utteranceId=this.hashCode() + "";
        //Get the text typed
        //If no text is typed, tts will read out 'You haven't typed text'
        //else it reads out the text you typed
        if (text.length() == 0) {
            tts.speak("No text typed", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        }
    }


    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language is not supported");
            }
        } else {
            Log.e("TTS", "Initilization Failed");
        }
    }
}

