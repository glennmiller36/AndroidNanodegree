package com.fluidminds.android.studiosity.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

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

    /**
     * UriMatcher will match each integer constants defined above.
     */
    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DataContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, DataContract.PATH_SUBJECT, SUBJECTS);
        matcher.addURI(authority, DataContract.PATH_SUBJECT + "/#", SUBJECT_ID);

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
                return DataContract.SubjectEntry.CONTENT_LIST_TYPE;

            /**
             * Get a particular Subject
             */
            case SUBJECT_ID:
                return DataContract.SubjectEntry.CONTENT_ITEM_TYPE;

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
                        DataContract.SubjectEntry.TABLE_NAME,
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
                        DataContract.SubjectEntry.TABLE_NAME,
                        projection,
                        DataContract.SubjectEntry._ID + " = " + uri.getLastPathSegment(),
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
        Uri returnUri = null;

        switch (match) {
            case SUBJECT_ID: {
                db.insertOrThrow(DataContract.SubjectEntry.TABLE_NAME, null, values);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
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
                        DataContract.SubjectEntry.TABLE_NAME, selection, selectionArgs);
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
                rowsUpdated = db.update(DataContract.SubjectEntry.TABLE_NAME, values, DataContract.SubjectEntry._ID + " = " + uri.getLastPathSegment(),
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