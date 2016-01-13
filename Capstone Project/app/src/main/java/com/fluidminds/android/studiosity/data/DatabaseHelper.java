package com.fluidminds.android.studiosity.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fluidminds.android.studiosity.data.DataContract.SubjectEntry;

/**
 * Manages a local database data.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "studiosity.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        context.deleteDatabase(DATABASE_NAME);
    }

    /**
     * Called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold Subjects.
        final String SQL_CREATE_SUBJECT_TABLE = "CREATE TABLE " + SubjectEntry.TABLE_NAME + " (" +
                SubjectEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SubjectEntry.COLUMN_SUBJECT + " TEXT NOT NULL " +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_SUBJECT_TABLE);

        seedData(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }

    private void seedData(SQLiteDatabase sqLiteDatabase) {

        // Subjects
        sqLiteDatabase.execSQL("INSERT INTO SUBJECT (Subject) VALUES ('Computers')");
        sqLiteDatabase.execSQL("INSERT INTO SUBJECT (Subject) VALUES ('Chemistry')");
        sqLiteDatabase.execSQL("INSERT INTO SUBJECT (Subject) VALUES ('Science')");
        sqLiteDatabase.execSQL("INSERT INTO SUBJECT (Subject) VALUES ('Social Studies')");
        sqLiteDatabase.execSQL("INSERT INTO SUBJECT (Subject) VALUES ('U.S. Geography')");
        sqLiteDatabase.execSQL("INSERT INTO SUBJECT (Subject) VALUES ('abcdefghijklmnopqrstuvwxyz')");
    }
}
