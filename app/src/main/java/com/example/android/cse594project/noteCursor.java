package com.example.android.cse594project;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class noteCursor extends CursorAdapter {
    public noteCursor(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView noteField = (TextView) view.findViewById(R.id.noteName);
        TextView idField = (TextView) view.findViewById(R.id.noteID);
        String body = cursor.getString(cursor.getColumnIndexOrThrow("Note"));
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        String plaintext = Crypt.decrypt(body);
        noteField.setText(plaintext);
        idField.setText(String.valueOf(id));
    }
}