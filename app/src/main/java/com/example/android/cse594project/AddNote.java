package com.example.android.cse594project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;


public class AddNote extends AppCompatActivity {
    DBHandler dbHandler;
    EditText noteField;
    ListView noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note);
        dbHandler = new DBHandler(this, null, null, 1);
        noteField = (EditText) findViewById(R.id.notetext);
        noteList = (ListView) findViewById(R.id.list);
    }

    public void addNote(View view) {
        String n = noteField.getText().toString();
        dbHandler.addNote(n);
        finish();
    }
}
