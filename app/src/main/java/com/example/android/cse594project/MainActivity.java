package com.example.android.cse594project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText noteField;
    ListView noteList;
    DBHandler dbHandler;
    SimpleCursorAdapter simpleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHandler = new DBHandler(this, null, null, 1);
        noteField = (EditText) findViewById(R.id.notetext);
        noteList = (ListView) findViewById(R.id.list);
        showNotes();
    }

    public void newNote(View view){
        Intent intent = new Intent(this, AddNote.class);
        startActivityForResult(intent, 1);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            showNotes();
        }
    }

    public void showNotes() {
        Cursor cursor = dbHandler.getNotes();
        if (cursor!=null) {
            String[] columns = new String[]{
                    DBHandler.COLUMN_NOTE,
                    DBHandler.COLUMN_ID
            };
            int[] fields = new int[]{
                    R.id.noteID,
                    R.id.noteName
            };
            simpleCursorAdapter = new SimpleCursorAdapter(this,
                    R.layout.list_item,
                    cursor,
                    columns,
                    fields,
                    0);
            noteList.setAdapter(simpleCursorAdapter);
            noteList.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                public void onItemClick(AdapterView<?> adaptView, View view, int newInt,
                                        long newLong)
                {
                    LinearLayout parent = (LinearLayout) view;
                    LinearLayout child = (LinearLayout) parent.getChildAt(0);
                    TextView m = (TextView) child.getChildAt(1);
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", Integer.parseInt(m.getText().toString()));
                    Intent intent = new Intent(getApplicationContext(), NoteHelper.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
    }
}
