package com.fluidminds.android.studiosity.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fluidminds.android.studiosity.data.DataContract.CardEntry;
import com.fluidminds.android.studiosity.data.DataContract.DeckEntry;
import com.fluidminds.android.studiosity.data.DataContract.SubjectEntry;
import com.fluidminds.android.studiosity.data.DataContract.QuizEntry;

/**
 * Manages a local database data.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "studiosity.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        //context.deleteDatabase(DATABASE_NAME);
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
                SubjectEntry.COLUMN_COLOR + " INTEGER NOT NULL, " +
                SubjectEntry.IS_SAMPLE_DATA + " INTEGER NOT NULL " +
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
                CardEntry.COLUMN_RECENT_SCORES + " TEXT, " +
                CardEntry.COLUMN_PERCENT_CORRECT + " INTEGER NOT NULL, " +
                " FOREIGN KEY(" + CardEntry.COLUMN_DECK_ID + ") REFERENCES " + DeckEntry.TABLE_NAME + "(" + DeckEntry._ID + ") ON DELETE CASCADE " +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_CARD_TABLE);

        // Create a table to hold Quiz history.
        final String SQL_CREATE_QUIZ_TABLE = "CREATE TABLE " + QuizEntry.TABLE_NAME + " (" +
                QuizEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                QuizEntry.COLUMN_DECK_ID + " INTEGER NOT NULL, " +
                QuizEntry.COLUMN_START_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                QuizEntry.COLUMN_NUM_CORRECT + " INTEGER NOT NULL, " +
                QuizEntry.COLUMN_TOTAL_CARDS + " INTEGER NOT NULL, " +
                QuizEntry.COLUMN_PERCENT_CORRECT + " INTEGER NOT NULL, " +
                " FOREIGN KEY(" + QuizEntry.COLUMN_DECK_ID + ") REFERENCES " + DeckEntry.TABLE_NAME + "(" + DeckEntry._ID + ") ON DELETE CASCADE " +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_QUIZ_TABLE);

        seedData(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }

    private void seedData(SQLiteDatabase sqLiteDatabase) {
        // Subject
        sqLiteDatabase.execSQL("INSERT INTO SUBJECT (Name, Color, IsSampleData) VALUES ('Languages', -14575885, 1)");
        sqLiteDatabase.execSQL("INSERT INTO SUBJECT (Name, Color, IsSampleData) VALUES ('Math', -769226, 1)");
        sqLiteDatabase.execSQL("INSERT INTO SUBJECT (Name, Color, IsSampleData) VALUES ('Science', -16121, 1)");
        sqLiteDatabase.execSQL("INSERT INTO SUBJECT (Name, Color, IsSampleData) VALUES ('U.S. Geography', -11751600, 1)");


        // Decks
        sqLiteDatabase.execSQL("INSERT INTO DECK (SubjectId, Name) VALUES (1, 'French Numbers')");
        sqLiteDatabase.execSQL("INSERT INTO DECK (SubjectId, Name) VALUES (1, 'Spanish Travel Phrases')");
        sqLiteDatabase.execSQL("INSERT INTO DECK (SubjectId, Name) VALUES (2, 'Common Conversions')");
        sqLiteDatabase.execSQL("INSERT INTO DECK (SubjectId, Name) VALUES (2, 'Roman Numerals')");
        sqLiteDatabase.execSQL("INSERT INTO DECK (SubjectId, Name) VALUES (3, 'Human Body')");
        sqLiteDatabase.execSQL("INSERT INTO DECK (SubjectId, Name) VALUES (3, 'Solar System')");
        sqLiteDatabase.execSQL("INSERT INTO DECK (SubjectId, Name) VALUES (4, 'U.S. Airport Codes')");
        sqLiteDatabase.execSQL("INSERT INTO DECK (SubjectId, Name) VALUES (4, 'U.S. State Capitals')");


        // Cards
        /* French Numbers */
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (1, '1', 'Un (un)', '1,1,1,1,1', 100)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (1, '2', 'Deux (deh)', '0,0,1,1,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (1, '3', 'Trois (twah)', '0,1,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (1, '4', 'Quatre(KAH-trah)', '1,1,1,1,1', 100)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (1, '5', 'Cinq (sank)', '0,1,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (1, '6', 'Six (seez)', '0,1,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (1, '7', 'Sept (set)', '0,1,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (1, '8', 'Huit (wheet)', '0,0,0,1,1', 40)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (1, '9', 'Neuf (nehf)', '0,0,1,1,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (1, '10', 'Dix (deez)', '1,1,1,1,1', 100)");

        /* Spanish Travel Phrases */
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (2, 'Bus', 'Autobus (Ow-tow-boos)', '1,1,1,1,1', 100)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (2, 'Water', 'Agua (ah-gwa)', '0,1,0,1,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (2, 'Hospital', 'Hospital (ose-pee-tahl)', '1,1,0,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (2, 'Hotel', 'Hotel (oh-tell)', '1,1,1,1,1', 100)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (2, 'Restaurant', 'Restaurante (res-tau-rahn-tay)', '1,1,1,0,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (2, 'Bathroom', 'baño (bahn-yo)', '1,0,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (2, 'Please', 'Por Favor(pour fah - vore)', '0,1,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (2, 'Thank you', 'Gracias (grah-see-ahs)', '1,1,1,1,1', 100)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (2, 'Hello', 'Hola (Oh-lah)', '1,1,1,1,1', 100)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (2, 'Chicken', 'Pollo (poy-oh)', '1,1,1,0,1', 80)");

        /* Common Conversions */
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (3, 'Quarts in a gallon', '4', '1,0,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (3, 'Ounces in a pound', '16', '1,1,0,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (3, 'Weeks in a year', '52', '1,1,1,1,1', 100)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (3, 'Inches in a foot', '12', '1,1,1,1,1', 100)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (3, 'Inches in a yard', '36', '1,1,1,0,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (3, 'Feet in a mile', '5,280', '0,1,1,0,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (3, 'Cups in a quarts', '4', '0,1,0,1,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (3, 'Hours in a week', '168', '0,0,1,1,1', 60)");

        /* Roman Numerals */
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (4, 'IV', '4', '1,1,1,0,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (4, 'V', '5', '1,1,1,1,1', 100)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (4, 'VI', '6', '0,1,0,0,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (4, 'IX', '9', '0,1,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (4, 'X', '10', '1,1,1,1,1', 100)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (4, 'XX', '20', '1,1,1,1,1', 100)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (4, 'L', '50', '0,1,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (4, 'C', '100', '1,1,1,1,0', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (4, 'D', '500', '0,1,1,0,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (4, 'M', '1000', '0,0,1,1,1', 60)");

        /* Human Body */
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (5, 'What’s the smallest bone in the body?', 'Stirrup', '0,0,0,1,1', 40)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (5, 'What is the most common blood type?', 'O+', '0,1,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (5, 'How many bones does an adult human have?', '206', '1,0,0,1,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (5, 'A typical brain weighs how much?', '3 pounds (1.4 kilograms)', '1,0,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (5, 'How many chambers does the heart have?', 'Four', '1,1,1,1,1', 100)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (5, 'What is your body’s largest organ?', 'Skin', '1,0,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (5, 'How fast does the hair on your head grow every month?', '1/2 inch', '0,1 0,1,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (5, 'How fast do fingernails grow?', '1/10 of an inch each month', '0,1,1,0,1', 60)");

        /* Solar System */
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (6, 'Closest planet to the sun', 'Mercury', '0,0,1,1,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (6, 'Hottest planet', 'Mercury', '0,1,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (6, 'Only planet having intelligent life', 'Earth', '1,1,1,1,1', 100)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (6, 'Biggest planet', 'Jupiter', '1,0,0,1,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (6, 'Biggest rings', 'Saturn', '1,0,0,1,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (6, 'Windiest planet', 'Neptune', '0,0,0,1,1', 40)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (6, 'Biggest mountain', 'Mars', '1,0,0,0,1', 40)");

        /* U.S. Airport Codes */
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (7, 'BNA', 'Nashville International', '1,1,0,0,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (7, 'BWI', 'Baltimore/Washington International', '0,1,0,1,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (7, 'DFW', 'Dallas Fort Worth International', '1,0,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (7, 'DTW', 'Detroit Metropolitan Wayne County', '0,1,0,1,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (7, 'JFK', 'John F Kennedy International, New York', '1,1,1,1,1', 100)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (7, 'LAS', 'McCarran International, Las Vegas', '0,1,0,1,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (7, 'MCO', 'Orlando International', '1,0,0,1,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (7, 'MSP', 'Minneapolis-St Paul International', '0,1,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (7, 'ORD', 'Chicago O’Hare International', '1,1,1,1,1', 100)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (7, 'PDX', 'Portland International', '0,1,0,1,1', 60)");

        /* U.S. State Capitals */
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (8, 'Arizona', 'Phoenix', '0,1,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (8, 'California', 'Sacramento', '1,1,1,1,1', 100)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (8, 'Illinois', 'Springfield', '1,1,0,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (8, 'Louisiana', 'Baton Rouge', '1,0,0,1,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (8, 'Maryland', 'Annapolis', '0,1,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (8, 'Massachusetts', 'Boston', '0,1,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (8, 'Minnesota', 'St. Paul', '1,0,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (8, 'New York', 'Albany', '0,1,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (8, 'Ohio', 'Columbus', '0,1,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (8, 'Washington', 'Olympia', '1,0,0,1,1', 60)");


        // Quiz History
        /* French Numbers */
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (1, '2016-03-01 13:01:00.000', 3, 10, 30)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (1, '2016-03-02 08:04:00.000', 7, 10, 70)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (1, '2016-03-03 11:11:00.000', 9, 10, 90)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (1, '2016-03-04 15:23:00.000', 10, 10, 100)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (1, '2016-03-05 18:35:00.000', 10, 10, 100)");

        /* Spanish Travel Phrases */
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (2, '2016-03-04 11:01:00.000', 8, 10, 80)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (2, '2016-03-05 10:04:00.000', 9, 10, 90)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (2, '2016-03-06 09:11:00.000', 8, 10, 80)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (2, '2016-03-07 14:23:00.000', 8, 10, 80)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (2, '2016-03-08 16:35:00.000', 10, 10, 100)");

        /* Common Conversions */
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (3, '2016-03-11 15:01:00.000', 5, 8, 62)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (3, '2016-03-12 11:04:00.000', 6, 8, 75)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (3, '2016-03-13 08:11:00.000', 6, 8, 75)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (3, '2016-03-14 14:23:00.000', 6, 8, 75)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (3, '2016-03-15 15:35:00.000', 8, 8, 100)");

        /* Roman Numerals */
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (4, '2016-03-15 08:01:00.000', 5, 10, 50)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (4, '2016-03-16 09:04:00.000', 9, 10, 90)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (4, '2016-03-17 11:11:00.000', 9, 10, 90)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (4, '2016-03-18 13:23:00.000', 7, 10, 70)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (4, '2016-03-19 16:35:00.000', 9, 10, 90)");

        /* Human Body */
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (5, '2016-03-08 10:01:00.000', 4, 8, 50)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (5, '2016-03-09 15:04:00.000', 4, 8, 50)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (5, '2016-03-10 08:11:00.000', 5, 8, 62)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (5, '2016-03-11 10:23:00.000', 7, 8, 87)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (5, '2016-03-12 13:35:00.000', 8, 8, 100)");

        /* Solar System */
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (6, '2016-03-10 09:01:00.000', 4, 7, 57)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (6, '2016-03-11 10:04:00.000', 2, 7, 28)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (6, '2016-03-12 07:11:00.000', 3, 7, 42)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (6, '2016-03-13 11:23:00.000', 6, 7, 85)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (6, '2016-03-14 14:35:00.000', 7, 7, 100)");

        /* U.S. Airport Codes */
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (7, '2016-03-21 14:01:00.000', 5, 10, 50)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (7, '2016-03-22 07:04:00.000', 8, 10, 80)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (7, '2016-03-23 10:11:00.000', 4, 10, 40)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (7, '2016-03-24 17:23:00.000', 9, 10, 90)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (7, '2016-03-25 18:35:00.000', 10, 10, 100)");

        /* U.S. State Capitals */
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (8, '2016-03-23 18:01:00.000', 5, 10, 50)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (8, '2016-03-24 14:04:00.000', 7, 10, 70)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (8, '2016-03-25 11:11:00.000', 7, 10, 70)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (8, '2016-03-26 09:23:00.000', 10, 10, 100)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (8, '2016-03-27 21:35:00.000', 10, 10, 100)");
    }
}
