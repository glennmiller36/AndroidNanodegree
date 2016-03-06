package com.fluidminds.android.studiosity.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.fluidminds.android.studiosity.data.DataContract.DeckEntry;
import com.fluidminds.android.studiosity.data.DataContract.SubjectEntry;

/**
 * Share data with other applications using this content provider.
 */
public class StudiosityProvider extends ContentProvider {

    // database
    private DatabaseHelper mDatabase;

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    static final int SUBJECTS = 100;
    static final int SUBJECT_ID = 101;
    static final int DECKS = 200;
    static final int DECK_ID = 201;

    /**
     * UriMatcher will match each integer constants defined above.
     */
    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DataContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, DataContract.PATH_SUBJECT, SUBJECTS);
        matcher.addURI(authority, DataContract.PATH_SUBJECT + "/#", SUBJECT_ID);
        matcher.addURI(authority, DataContract.PATH_DECK, DECKS);
        matcher.addURI(authority, DataContract.PATH_DECK + "/#", DECK_ID);

        return matcher;
    }

    /**
     * Prepare the content provider
     */
    @Override
    public boolean onCreate() {
        mDatabase = new DatabaseHelper(getContext());
        return true;
    }

    /**
     * Returns the MIME type for this URI
     */
    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            /**
             * Get all Subject records
             */
            case SUBJECTS:
                return SubjectEntry.CONTENT_LIST_TYPE;

            /**
             * Get a particular Subject
             */
            case SUBJECT_ID:
                return SubjectEntry.CONTENT_ITEM_TYPE;

            /**
             * Get all Deck records for the requested Subjects
             */
            case DECKS:
                return DeckEntry.CONTENT_LIST_TYPE;

            /**
             * Get a particular Deck
             */
            case DECK_ID:
                return DeckEntry.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    /**
     * Return records based on selection criteria
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {

            case SUBJECTS: {
                retCursor = mDatabase.getReadableDatabase().query(
                        SubjectEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case SUBJECT_ID: {
                retCursor = mDatabase.getReadableDatabase().query(
                        SubjectEntry.TABLE_NAME,
                        projection,
                        SubjectEntry._ID + " = " + uri.getLastPathSegment(),
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case DECKS: {
                retCursor = mDatabase.getReadableDatabase().query(
                        DeckEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case DECK_ID: {
                retCursor = mDatabase.getReadableDatabase().query(
                        DeckEntry.TABLE_NAME,
                        projection,
                        DeckEntry._ID + " = " + uri.getLastPathSegment(),
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    /**
     * Adds records
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDatabase.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        long rowID = 0;

        switch (match) {
            case SUBJECT_ID: {
                rowID = db.insertOrThrow(SubjectEntry.TABLE_NAME, null, values);
                if (rowID > 0)
                {
                    Uri returnUri = ContentUris.withAppendedId(SubjectEntry.CONTENT_URI, rowID);
                    getContext().getContentResolver().notifyChange(returnUri, null);
                    return returnUri;
                }
                break;
            }
            case DECK_ID: {
                rowID = db.insertOrThrow(DeckEntry.TABLE_NAME, null, values);
                if (rowID > 0)
                {
                    Uri returnUri = ContentUris.withAppendedId(DeckEntry.CONTENT_URI, rowID);
                    getContext().getContentResolver().notifyChange(returnUri, null);
                    return returnUri;
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return null;
    }

    /**
     * Deletes records
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDatabase.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case SUBJECT_ID:
                rowsDeleted = db.delete(
                        SubjectEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case DECK_ID:
                rowsDeleted = db.delete(
                        DeckEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    /**
     * Modifies data
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDatabase.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case SUBJECT_ID:
                rowsUpdated = db.update(SubjectEntry.TABLE_NAME, values, SubjectEntry._ID + " = " + uri.getLastPathSegment(),
                        selectionArgs);
                break;
            case DECK_ID:
                rowsUpdated = db.update(DeckEntry.TABLE_NAME, values, DeckEntry._ID + " = " + uri.getLastPathSegment(),
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}