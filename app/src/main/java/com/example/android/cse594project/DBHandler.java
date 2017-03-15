package com.example.android.cse594project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
                                                    // Declares the data fields for the sql database
public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "noteDB.db";
    public static final String TABLE_NAME = "notes_table";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NOTE = "Note";
    public static final String COLUMN_DATE = "Date";
    public static final String COLUMN_ALARMID = "AlarmID";
    public static final String COLUMN_ALARMTYPE = "AlarmType";
    // DBHandler's constructor passes it's arguments to send to SQLiteOpenHelper's constructor
    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override       // Passes SQLite database object where execSQL method is called to execute the string table
    public void onCreate(SQLiteDatabase db) {
        String table = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_NOTE + " TEXT, " +
                COLUMN_DATE + " TEXT, " + " TEXT, " + COLUMN_ALARMTYPE + " INTEGER, " + COLUMN_ALARMID + " INTEGER)";
        db.execSQL(table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
                                // Cursor is used as temporary storage for a query
    public Cursor getNotes() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ID, COLUMN_NOTE}, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst(); // Move cursor to the first row
            return cursor;
        } else {
            return null;
        }
    }
                                            // ContentValues is name value pair for inserting or updating database tables.
    public void addNote(String note) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE, note); // Insert new note to column note
        values.put(COLUMN_DATE, "null");    // These are the default values for the alarm of the note
        values.put(COLUMN_ALARMTYPE, -1);   // ^ Default value
        values.put(COLUMN_ALARMID, -1);     // ^ Default value
        SQLiteDatabase db = this.getWritableDatabase(); // Open database to write in note
        db.insert(TABLE_NAME, null, values);            // Insert values to table
    }

    public void deleteNote(int id) {                    // Pass in id of the note
        String idString[] = {Integer.toString(id)};     // Convert the integer value to a string value
        SQLiteDatabase db = this.getReadableDatabase(); // Open the database
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", idString); // Delete the note in the table with the id (idStirng)
    }

    public void deleteNoteCB(int id, Context context) {
        String idString[] = {Integer.toString(id)};
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", idString);
        MainActivity main = (MainActivity) context;
        main.showNotes();
    }



    public void updateNote(int id, String n) {      
        ContentValues note = new ContentValues(); // Make an instance of ContentValues called note
        note.put(COLUMN_NOTE, n); // Add value to the set
        SQLiteDatabase db = this.getReadableDatabase();
        db.update(TABLE_NAME, note, "_id = ? ", new String[]{Integer.toString(id)}); // Update the note at the id location
    }


    public String getNote(int id) {                     // Get note by id
        SQLiteDatabase db = this.getReadableDatabase(); // Open the database
        String note = "null";                           // Temporary value for string
        Cursor c = db.query(TABLE_NAME, new String[] {COLUMN_NOTE}, COLUMN_ID + "=" + id, null, null, null, null); // Find the note by id with a query
        if(c.getCount() == 1){                          // Returns number of rows in cursor
            c.moveToFirst();
            note = c.getString(c.getColumnIndex(COLUMN_NOTE));
        }
        return note;
    }

    public String getDate(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String date = "null";
        Cursor c = db.query(TABLE_NAME, new String[] {COLUMN_DATE}, COLUMN_ID + "=" + id, null, null, null, null);
        if(c.getCount() == 1){
            c.moveToFirst();
            date = c.getString(c.getColumnIndex(COLUMN_DATE));
        }
        return date;
    }

    public int getAlarm(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        int alarmID = -1;
        Cursor c = db.query(TABLE_NAME, new String[] {COLUMN_ALARMID}, COLUMN_ID + "=" + id, null, null, null, null);
        if(c.getCount() == 1){
            c.moveToFirst();
            alarmID = c.getInt(c.getColumnIndex(COLUMN_ALARMID));
        }
        return alarmID;
    }

    public int getAlarmType(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        int alarmType = -1;
        Cursor c = db.query(TABLE_NAME, new String[] {COLUMN_ALARMTYPE}, COLUMN_ID + "=" + id, null, null, null, null);
        if(c.getCount() == 1){
            c.moveToFirst();
            alarmType = c.getInt(c.getColumnIndex(COLUMN_ALARMTYPE));
        }
        return alarmType;
    }


    public void updateDate(int id, String n) {
        ContentValues note = new ContentValues();
        note.put(COLUMN_DATE, n);
        SQLiteDatabase db = this.getReadableDatabase();
        db.update(TABLE_NAME, note, "_id = ? ", new String[]{Integer.toString(id)});
    }



    public void updateAlarm(int id, int n) {
        ContentValues note = new ContentValues();
        note.put(COLUMN_ALARMID, n);
        SQLiteDatabase db = this.getReadableDatabase();
        db.update(TABLE_NAME, note, "_id = ? ", new String[]{Integer.toString(id)});
    }

    public void updateAlarmType(int id, int n) {
        ContentValues note = new ContentValues();
        note.put(COLUMN_ALARMTYPE, n);
        SQLiteDatabase db = this.getReadableDatabase();
        db.update(TABLE_NAME, note, "_id = ? ", new String[]{Integer.toString(id)});
    }


}
