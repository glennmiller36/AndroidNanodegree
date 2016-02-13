package com.fluidminds.android.studiosity.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;

import com.fluidminds.android.studiosity.data.DataContract;
import com.fluidminds.android.studiosity.utils.ThemeColor;

/**
 * A POJO Model representing a Subject.
 */
public class SubjectModel extends BaseModel {

    private Long mId = 0L;
    private String mSubject = "";
    private Integer mColorInt = 0;

    public SubjectModel() {
        markAsNew();
    }

    public SubjectModel(Long id, String subject, Integer colorInt) {
        /* Initially load properties without dirtying the model */
        mId = id;
        mSubject = subject.trim();
        mColorInt = colorInt;

        markAsOld();
    }

    /**
     * Id
     */
    public Long getId() {
        return mId;
    }

    /**
     * Subject
     */
    public String getSubject() {
        return mSubject;
    }

    public void setSubject(String subject) {
        if (!this.mSubject.equals(subject)) {
            this.mSubject = subject.trim();
            markDirty();
        }
    }

    /**
     * Color
     */
    public Integer getColorInt() {
        return mColorInt;
    }

    public void setColorInt(Integer color) {
        if (!this.mColorInt.equals(color)) {
            this.mColorInt = color;
            markDirty();
        }
    }

    public String getColorName() {
        return ThemeColor.getColorName(mColorInt);
    }

    /**
     * Commit the changes to the database.
     */
    public boolean Save(Context context) {

// check is dirty

        // or just call CheckRules???

// if not dirty - track dirty status - reset on setmodel
// viewmodel call SAVE - also re-do is valid
        // handle duplica
        ContentValues values = new ContentValues();

// ADD TRY CATCH SQL EXCEPTION - TRY DUPLICATE NAMES


        // Then add the data, along with the corresponding name of the data type,
        // so the content provider knows what kind of value is being inserted.
        values.put(DataContract.SubjectEntry.COLUMN_SUBJECT, getSubject());
        values.put(DataContract.SubjectEntry.COLUMN_COLOR, getColorInt());

        try {
            if (getId() == 0) {
                int rowsUpdated = context.getContentResolver().update(DataContract.SubjectEntry.buildItemUri(getId()), values, null, null);
                return rowsUpdated == 1;
            }
            else {
                Uri insertedUri = context.getContentResolver().insert(DataContract.SubjectEntry.buildItemUri(getId()), values);
                return Integer.parseInt(insertedUri.getLastPathSegment()) > 0;
            }
        } catch (SQLiteConstraintException e) {

            //for (BusinessRule rule: getBusinessRules()) {
            //    if (rule.getRuleName().equals("noduplicate")) {
            //        // the rule is broken
            //        if (!getBrokenRules().contains(rule)) {
             //           getBrokenRules().add(rule);
             //           notifyListeners(this, "subject", "", rule.getErrorMessage());
             //       }
             //   }
            //}
        }

        return false;
    }
}