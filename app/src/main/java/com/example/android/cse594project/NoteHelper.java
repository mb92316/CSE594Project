package com.example.android.cse594project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class NoteHelper extends AppCompatActivity {

    DBHandler dbHandler;
    int id;
    String noteText;
    EditText noteField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_helper);
        Bundle extras = getIntent().getExtras();
        id = extras.getInt("id");
        noteText = extras.getString("notetext");
        dbHandler = new DBHandler(this, null, null, 1);
        noteField = (EditText) findViewById(R.id.updatenotetext);
        noteField.setText(noteText);
    }

    public void deleteNote(View view) {
        dbHandler.deleteNote(id);
        finish();
    }

    public void updateNote(View view) {
        String n = noteField.getText().toString();
        dbHandler.updateNote(id, n);
        finish();
    }
}
