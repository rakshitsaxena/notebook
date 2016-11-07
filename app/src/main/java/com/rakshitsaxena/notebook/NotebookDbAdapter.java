package com.rakshitsaxena.notebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Rakshit on 11/7/2016.
 */

public class NotebookDbAdapter {

    private static final String DATABASE_NAME = "notebook.db";
    private static final int DATABASE_VERSION = 1;

    public static final String NOTE_TABLE = "note";

    /*Columns*/
    public static final String ID = "_id";
    public static final String TITLE = "title";
    public static final String MESSAGE = "message";
    public static final String CATEGORY = "category";
    public static final String DATE = "date";

    private String[] allColumns = {ID, TITLE, MESSAGE, CATEGORY, DATE};

    public static final String CREATE_TABLE_NOTE = "create table " + NOTE_TABLE +
                                                " ( " + ID + " integer primary key autoincrement, " +
                                                        TITLE + " text not null, " +
                                                        MESSAGE + " text not null, "+
                                                        CATEGORY + " text not null, " +
                                                        DATE + " );";

    private SQLiteDatabase sqlDB;
    private Context context;
    private NotebookDbHelper notebookDbHelper;

    public NotebookDbAdapter(Context ctx){
        context = ctx;
    }

    //grab and give the db
    public NotebookDbAdapter open() throws android.database.SQLException{
        notebookDbHelper = new NotebookDbHelper(context);
        sqlDB = notebookDbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        notebookDbHelper.close();
    }

    public ArrayList<Note> getAllNotes(){
        ArrayList<Note> notes = new ArrayList();
        Cursor cursor = sqlDB.query(NOTE_TABLE, allColumns, null, null, null, null, null);

        for(cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()){
            Note note = cursorToNote(cursor);
            Log.d("CALLING_ALL_NOTES", note.getId() + "");
            notes.add(note);
        }

        cursor.close();

        return notes;
    }

    private Note cursorToNote(Cursor cursor) {
        return new Note(cursor.getString(1), cursor.getString(2),
                Note.Category.valueOf(cursor.getString(3)),
                cursor.getLong(0), cursor.getLong(4));

    }

    public Note createNote(String title, String message, Note.Category category){
        ContentValues values = new ContentValues();
        values.put(TITLE, title);
        values.put(MESSAGE, message);
        values.put(CATEGORY, category.name());
        values.put(DATE, Calendar.getInstance().getTimeInMillis() + "");

        long insertId = sqlDB.insert(NOTE_TABLE, null, values);

        Cursor cursor = sqlDB.query(NOTE_TABLE, allColumns,
                ID + " = " + insertId, null, null, null, null);

        cursor.moveToFirst();
        Note newNote = cursorToNote(cursor);
        cursor.close();
        return newNote;

    }

    //returns number of rows updated
    public long updateNote(long idToUpdate, String newTitle, String newMessage,
                           Note.Category newCategory){
        ContentValues values = new ContentValues();
        values.put(TITLE, newTitle);
        values.put(MESSAGE, newMessage);
        values.put(CATEGORY, newCategory.name());
        values.put(DATE, Calendar.getInstance().getTimeInMillis() + "");

        Log.d("UPDATE_NOTE_METHOD", "Updating note with values Id: " + idToUpdate + " Title:" + newTitle +
                " Message: " + newMessage + " Category: " + newCategory.name());

        int updatedRowCount = sqlDB.update(NOTE_TABLE, values, ID + " = " + idToUpdate, null);
        Log.d("UPDATE_ROW_COUNT", "Updated row count is : " + updatedRowCount);
        return  updatedRowCount;

    }

    public long deleteNote(long idToDelete){
        int deletedRowCount = sqlDB.delete(NOTE_TABLE, ID + " = " + idToDelete, null);
        Log.d("DELETED_ROW_COUNT", "Deleted row count is : " + deletedRowCount);
        return  deletedRowCount;
    }

    private static class NotebookDbHelper extends SQLiteOpenHelper {

        NotebookDbHelper(Context ctx){
            super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
           //create note table
            db.execSQL(CREATE_TABLE_NOTE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(NotebookDbHelper.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");

            db.execSQL("DROP TABLE IF EXISTS " + NOTE_TABLE);
            onCreate(db);
        }
    }



}
