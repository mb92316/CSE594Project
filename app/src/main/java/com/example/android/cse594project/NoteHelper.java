package com.example.android.cse594project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NoteHelper extends AppCompatActivity {

    DBHandler dbHandler;
    int id;
    String noteText;
    EditText noteField;
    Button b_pick;
    Crypt crypt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_helper);
        Bundle extras = getIntent().getExtras();
        id = extras.getInt("id");
        noteText = extras.getString("notetext");
        dbHandler = new DBHandler(this, null, null, 1);
        crypt = new Crypt();
        noteField = (EditText) findViewById(R.id.updatenotetext);
        noteField.setText(noteText);
        noteField.setSelection(noteField.getText().length());
        getDate();
    }

    public void deleteNote(View view) {
        dbHandler.deleteNote(id);           // Deletes note by id
        Intent resultIntent = new Intent();
        resultIntent.putExtra("noteinfo", "Note Deleted");
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    public void updateNote(View view) {
        String n = noteField.getText().toString();
        String encryptedNote = Crypt.encrypt(n);
        dbHandler.updateNote(id, encryptedNote );   // Update note by id
        Intent resultIntent = new Intent();
        resultIntent.putExtra("noteinfo", "Note Updated");
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    public void alarm(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("notetext", noteText);
        bundle.putInt("id", id);
        Intent intent = new Intent(getApplicationContext(), Alarm.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

    public void getDate() {
       TextView alarmField = (TextView) findViewById(R.id.alarmfield);
        String date =  dbHandler.getDate(id);
        if(!date.equals("null")) {
            alarmField.setText("Alarm: " + date);
        }
        else {
            alarmField.setText("Alarm: ");
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        getDate();
    }
}

