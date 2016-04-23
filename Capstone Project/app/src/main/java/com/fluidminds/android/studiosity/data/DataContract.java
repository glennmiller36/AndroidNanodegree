package com.fluidminds.android.studiosity.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the database.
 */
public class DataContract {

    // The "Content authority" is a name for the entire content provider
    public static final String CONTENT_AUTHORITY = "com.fluidminds.android.studiosity";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    public static final String PATH_SUBJECT = "subject";
    public static final String PATH_DECK = "deck";
    public static final String PATH_CARD = "card";
    public static final String PATH_QUIZ = "quiz";

    /**
     * Inner class that defines the table contents of the Subjects table
     */
    public static final class SubjectEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SUBJECT).build();

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SUBJECT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SUBJECT;

        // Table name
        public static final String TABLE_NAME = "subject";

        // Column names
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_COLOR = "color";
        public static final String IS_SAMPLE_DATA = "issampledata";

        public static Uri buildItemUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /**
     * Inner class that defines the table contents of the Deck table
     */
    public static final class DeckEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_DECK).build();

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DECK;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DECK;

        // Table name
        public static final String TABLE_NAME = "deck";

        // Column names
        public static final String COLUMN_SUBJECT_ID = "subjectid";
        public static final String COLUMN_NAME = "name";

        public static Uri buildItemUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /**
     * Inner class that defines the table contents of the Card table
     */
    public static final class CardEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CARD).build();

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CARD;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CARD;

        // Table name
        public static final String TABLE_NAME = "card";

        // Column names
        public static final String COLUMN_DECK_ID = "deckid";
        public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_ANSWER = "answer";
        public static final String COLUMN_RECENT_SCORES = "recentScores";
        public static final String COLUMN_PERCENT_CORRECT = "percentcorrect";

        public static Uri buildItemUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /**
     * Inner class that defines the table contents of the Quiz history table
     */
    public static final class QuizEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_QUIZ).build();

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_QUIZ;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_QUIZ;

        // Table name
        public static final String TABLE_NAME = "quiz";

        // Column names
        public static final String COLUMN_DECK_ID = "deckid";
        public static final String COLUMN_START_DATE = "startdate";
        public static final String COLUMN_NUM_CORRECT = "numcorrect";
        public static final String COLUMN_TOTAL_CARDS = "totalcards";
        public static final String COLUMN_PERCENT_CORRECT = "percentcorrect";

        public static Uri buildItemUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
