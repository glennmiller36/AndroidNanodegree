package com.fluidminds.android.studiosity.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fluidminds.android.studiosity.data.DataContract.CardEntry;
import com.fluidminds.android.studiosity.data.DataContract.DeckEntry;
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

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        if (!db.isReadOnly()) {
            // enable CASCADE DELETE
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
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
                DeckEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DeckEntry.COLUMN_SUBJECT_ID + " INTEGER NOT NULL, " +
                DeckEntry.COLUMN_NAME + " TEXT NOT NULL UNIQUE COLLATE NOCASE, " +
                " FOREIGN KEY(" + DeckEntry.COLUMN_SUBJECT_ID + ") REFERENCES " + SubjectEntry.TABLE_NAME + "(" + SubjectEntry._ID + ") ON DELETE CASCADE " +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_DECK_TABLE);

        // Create a table to hold Cards for a Deck.
        final String SQL_CREATE_CARD_TABLE = "CREATE TABLE " + CardEntry.TABLE_NAME + " (" +
                CardEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CardEntry.COLUMN_DECK_ID + " INTEGER NOT NULL, " +
                CardEntry.COLUMN_QUESTION + " TEXT NOT NULL UNIQUE COLLATE NOCASE, " +
                CardEntry.COLUMN_ANSWER + " TEXT NOT NULL, " +
                " FOREIGN KEY(" + CardEntry.COLUMN_DECK_ID + ") REFERENCES " + DeckEntry.TABLE_NAME + "(" + DeckEntry._ID + ") ON DELETE CASCADE " +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_CARD_TABLE);

        seedData(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }

    private void seedData(SQLiteDatabase sqLiteDatabase) {
        // Subjects
        sqLiteDatabase.execSQL("INSERT INTO SUBJECT (Name, Color) VALUES ('U.S. Geography', -12627531)");
        sqLiteDatabase.execSQL("INSERT INTO SUBJECT (Name, Color) VALUES ('abcdefghijklmnopqrstuvwxyz', -14575885)");

        // Decks
        sqLiteDatabase.execSQL("INSERT INTO DECK (SubjectId, Name) VALUES (1, 'State Capitals')");

        // Cards
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Alabama', 'Montgomery')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Alaska', 'Juneau')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Arizona', 'Phoenix')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Arkansas', 'Little Rock')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'California', 'Sacramento')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Colorado', 'Denver')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Connecticut', 'Hartford')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Delaware', 'Dover')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Florida', 'Tallahassee')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Georgia', 'Atlanta')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Hawaii', 'Honolulu')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Idaho', 'Boise')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Illinois', 'Springfield')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Indiana', 'Indianapolis')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Iowa', 'Des Moines')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Kansas', 'Topeka')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Kentucky', 'Frankfort')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Louisiana', 'Baton Rouge')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Maine', 'Augusta')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Maryland', 'Annapolis')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Massachusetts', 'Boston')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Michigan', 'Lansing')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Minnesota', 'St. Paul')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Mississippi', 'Jackson')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Missouri', 'Jefferson City')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Montana', 'Helena')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Nebraska', 'Lincoln')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Nevada', 'Carson City')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'New Hampshire', 'Concord')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'New Jersey', 'Trenton')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'New Mexico', 'Santa Fe')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'New York', 'Albany')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'North Carolina', 'Raleigh')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'North Dakota', 'Bismarck')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Ohio', 'Columbus')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Oklahoma', 'Oklahoma City')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Oregon', 'Salem')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Pennsylvania', 'Harrisburg')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Rhode Island', 'Providence')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'South Carolina', 'Columbia')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'South Dakota', 'Pierre')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Tennessee', 'Nashville')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Texas', 'Austin')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Utah', 'Salt Lake City')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Vermont', 'Montpelier')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Virginia', 'Richmond')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Washington', 'Olympia')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'West Virginia', 'Charleston')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Wisconsin', 'Madison')");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer) VALUES (1, 'Wyoming', 'Cheyenne')");


//        U.S. City Nicknames
//        The Windy City
//                Chicago
//        The Big Apple
//        New York City
//        The Motor City
//                Detroit
//        The City by the Bay
//        San Francisco
//        Music City
//        Nashville
//        Bean Town
//        Boston
//        The City of Angels
//        Los Angeles
//        Sin City
//        Las Vegas
//        The Biggest Little City in the World
//                Reno
//        The City of Brotherly Love
//                Philadelphia
//        Steel City
//        Pittsburgh
//        The Mile High City
//        Denver
//                Nickname
//        City
//        The Rubber City
//                Akron
//        The City Too Busy to Hate
//        Atlanta
//        The Big Easy
//        New Orleans
//        Gateway to the West
//        St. Louis
//        Derby City
//        Louisville
//        Witch City
//        Salem
//        The World's Playground
//        Atlantic City
//        The Big Pineapple
//                Honolulu
//        Cigar City
//        Tampa
//        The Conch Republic
//        Key West
//        Emerald City
//        Seattle
//        The Mistake on the Lake
//                Cleveland


        // Airport Codes?
        // License Plate slogans?
    }
}
