package com.fluidminds.android.studiosity.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fluidminds.android.studiosity.data.DataContract.SubjectEntry;
import com.fluidminds.android.studiosity.data.DataContract.DeckEntry;

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
                SubjectEntry.COLUMN_NAME + " TEXT NOT NULL UNIQUE COLLATE NOCASE, " +
                SubjectEntry.COLUMN_COLOR + " INTEGER NOT NULL " +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_SUBJECT_TABLE);

        // Create a table to hold Card Decks.
        final String SQL_CREATE_DECK_TABLE = "CREATE TABLE " + DeckEntry.TABLE_NAME + " (" +
                SubjectEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SubjectEntry.COLUMN_NAME + " TEXT NOT NULL UNIQUE COLLATE NOCASE " +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_DECK_TABLE);

        seedData(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }

    private void seedData(SQLiteDatabase sqLiteDatabase) {
        // Subjects
        sqLiteDatabase.execSQL("INSERT INTO SUBJECT (Name, Color) VALUES ('Computers', -769226)");
        sqLiteDatabase.execSQL("INSERT INTO SUBJECT (Name, Color) VALUES ('Chemistry', -1499549)");
        sqLiteDatabase.execSQL("INSERT INTO SUBJECT (Name, Color) VALUES ('Science', -6543440)");
        sqLiteDatabase.execSQL("INSERT INTO SUBJECT (Name, Color) VALUES ('Social Studies', -10011977)");
        sqLiteDatabase.execSQL("INSERT INTO SUBJECT (Name, Color) VALUES ('U.S. Geography', -12627531)");
        sqLiteDatabase.execSQL("INSERT INTO SUBJECT (Name, Color) VALUES ('abcdefghijklmnopqrstuvwxyz', -14575885)");
    }
}
