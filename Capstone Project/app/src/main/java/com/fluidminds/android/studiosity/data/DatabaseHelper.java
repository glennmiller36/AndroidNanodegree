package com.fluidminds.android.studiosity.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fluidminds.android.studiosity.data.DataContract.ColorEntry;
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
        // Create a table to hold Colors.
        final String SQL_CREATE_COLOR_TABLE = "CREATE TABLE " + ColorEntry.TABLE_NAME + " (" +
                ColorEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ColorEntry.COLUMN_COLOR + " TEXT NOT NULL, " +
                ColorEntry.COLUMN_RGB + " TEXT NOT NULL " +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_COLOR_TABLE);

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
        // Colors
        sqLiteDatabase.execSQL("INSERT INTO COLOR (Color, Rgb) VALUES ('Red', 'F44336')");
        sqLiteDatabase.execSQL("INSERT INTO COLOR (Color, Rgb) VALUES ('Pink', 'E91E63')");
        sqLiteDatabase.execSQL("INSERT INTO COLOR (Color, Rgb) VALUES ('Purple', '9C27B0')");
        sqLiteDatabase.execSQL("INSERT INTO COLOR (Color, Rgb) VALUES ('Deep Purple', '673AB7')");
        sqLiteDatabase.execSQL("INSERT INTO COLOR (Color, Rgb) VALUES ('Indigo', '3F51B5')");
        sqLiteDatabase.execSQL("INSERT INTO COLOR (Color, Rgb) VALUES ('Blue', '2196F3')");
        sqLiteDatabase.execSQL("INSERT INTO COLOR (Color, Rgb) VALUES ('Light Blue', '03A9F4')");
        sqLiteDatabase.execSQL("INSERT INTO COLOR (Color, Rgb) VALUES ('Cyan', '00BCD4')");
        sqLiteDatabase.execSQL("INSERT INTO COLOR (Color, Rgb) VALUES ('Teal', '009688')");
        sqLiteDatabase.execSQL("INSERT INTO COLOR (Color, Rgb) VALUES ('Green', '4CAF50')");
        sqLiteDatabase.execSQL("INSERT INTO COLOR (Color, Rgb) VALUES ('Light Green', '8BC34A')");
        sqLiteDatabase.execSQL("INSERT INTO COLOR (Color, Rgb) VALUES ('Lime', 'CDDC39')");
        sqLiteDatabase.execSQL("INSERT INTO COLOR (Color, Rgb) VALUES ('Yellow', 'FFEB3B')");
        sqLiteDatabase.execSQL("INSERT INTO COLOR (Color, Rgb) VALUES ('Amber', 'FFC107')");
        sqLiteDatabase.execSQL("INSERT INTO COLOR (Color, Rgb) VALUES ('Orange', 'FF9800')");
        sqLiteDatabase.execSQL("INSERT INTO COLOR (Color, Rgb) VALUES ('Deep Orange', 'FF5722')");
        sqLiteDatabase.execSQL("INSERT INTO COLOR (Color, Rgb) VALUES ('Brown', '795548')");
        sqLiteDatabase.execSQL("INSERT INTO COLOR (Color, Rgb) VALUES ('Grey', '9E9E9E')");
        sqLiteDatabase.execSQL("INSERT INTO COLOR (Color, Rgb) VALUES ('Blue Grey', '607D8B')");
        sqLiteDatabase.execSQL("INSERT INTO COLOR (Color, Rgb) VALUES ('Black', '000000')");
        sqLiteDatabase.execSQL("INSERT INTO COLOR (Color, Rgb) VALUES ('White', 'FFFFFF')");

        // Subjects
        sqLiteDatabase.execSQL("INSERT INTO SUBJECT (Subject) VALUES ('Computers')");
        sqLiteDatabase.execSQL("INSERT INTO SUBJECT (Subject) VALUES ('Chemistry')");
        sqLiteDatabase.execSQL("INSERT INTO SUBJECT (Subject) VALUES ('Science')");
        sqLiteDatabase.execSQL("INSERT INTO SUBJECT (Subject) VALUES ('Social Studies')");
        sqLiteDatabase.execSQL("INSERT INTO SUBJECT (Subject) VALUES ('U.S. Geography')");
        sqLiteDatabase.execSQL("INSERT INTO SUBJECT (Subject) VALUES ('abcdefghijklmnopqrstuvwxyz')");
    }
}
