package com.fluidminds.android.studiosity.models;

import android.content.ContentValues;
import android.database.sqlite.SQLiteConstraintException;
import android.databinding.Bindable;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.fluidminds.android.studiosity.BR;
import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.app.StudiosityApp;
import com.fluidminds.android.studiosity.data.DataContract;
import com.fluidminds.android.studiosity.utils.ThemeColor;

import java.security.Provider;

/**
 * A POJO Model representing a Subject.
 */
public class SubjectModel extends BaseModel implements Parcelable {

    private Long mId = 0L;
    private String mSubject = "";
    private Integer mColorInt = 0;

    public SubjectModel() {
        mColorInt = ThemeColor.generateRandomColor();
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
    @Bindable
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
    @Bindable
    public Integer getColorInt() {
        return mColorInt;
    }

    public void setColorInt(Integer color) {
        if (!this.mColorInt.equals(color)) {
            this.mColorInt = color;
            markDirty();
            notifyPropertyChanged(BR.colorInt);
            notifyPropertyChanged(BR.colorName);
        }
    }

    @Bindable
    public String getColorName() {
        return ThemeColor.getColorName(mColorInt);
    }

    private boolean isValid() {
        getBrokenRules().clear();

        if (getSubject().length() == 0)
            getBrokenRules().put("subject", StudiosityApp.getInstance().getString(R.string.required));

        return getBrokenRules().isEmpty();
    }

    /**
     * Commit the changes to the database.
     */
    public SubjectModel save() {

        if (!isValid())
            return this;

        if (!getIsNew() && !getIsDirty()) {
            return this;  // no changes
        }

        ContentValues values = new ContentValues();

        // Then add the data, along with the corresponding name of the data type,
        // so the content provider knows what kind of value is being inserted.
        values.put(DataContract.SubjectEntry.COLUMN_SUBJECT, getSubject());
        values.put(DataContract.SubjectEntry.COLUMN_COLOR, getColorInt());

        try {
            if (getId() == 0) { // insert
                Uri insertedUri = StudiosityApp.getInstance().getContentResolver().insert(DataContract.SubjectEntry.buildItemUri(getId()), values);
                if (insertedUri != null && Integer.parseInt(insertedUri.getLastPathSegment()) > 0) {
                    mId = Long.parseLong(insertedUri.getLastPathSegment());
                    return this;
                }
            }
            else {  // update
                int rowsUpdated = StudiosityApp.getInstance().getContentResolver().update(DataContract.SubjectEntry.buildItemUri(getId()), values, null, null);
                return (rowsUpdated == 1) ? this : null;
            }
        } catch (SQLiteConstraintException e) {
            getBrokenRules().put("subject", StudiosityApp.getInstance().getString(R.string.duplicate_name));
        }

        return this;
    }

    /**
     * Delete the Subject and child Cards from the database.
     */
    public int delete() {
        try {
            return StudiosityApp.getInstance().getContentResolver().delete(DataContract.SubjectEntry.buildItemUri(getId()), DataContract.SubjectEntry._ID + " = " + mId, null);
        } catch (Exception e) {
            //for (BusinessRule rule: getBusinessRules()) {
            //    if (rule.getRuleName().equals("noduplicate")) {
            //        // the rule is broken
            //        if (!getBrokenRules().contains(rule)) {
            //           getBrokenRules().add(rule);
            //           notifyListeners(this, "subject", "", rule.getErrorMessage());
            //       }
            //   }
            //}
            // ELSE generic fail msg
        }
        return 0;
    }

        /** Used to give additional hints on how to process the received parcel.*/
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(getId());
        parcel.writeString(getSubject());
        parcel.writeInt(getColorInt());

        // base fields
        parcel.writeByte((byte) (getIsDirty() ? 1 : 0));   //if mIsDirty == true, byte == 1
        parcel.writeByte((byte) (getIsNew() ? 1 : 0));     //if mIsNew == true, byte == 1
    }

    /** Static field used to regenerate object, individually or as arrays */
    public static final Parcelable.Creator<SubjectModel> CREATOR = new Parcelable.Creator<SubjectModel>() {
        public SubjectModel createFromParcel(Parcel parcel) {
            return new SubjectModel(parcel);
        }
        public SubjectModel[] newArray(int size) {
            return new SubjectModel[size];
        }
    };

    /** Ctor from Parcel, reads back fields IN THE ORDER they were written */
    public SubjectModel(Parcel parcel){
        mId = parcel.readLong();
        mSubject = parcel.readString();
        mColorInt = parcel.readInt();

        // base fields
        if (parcel.readByte() != 0)
             markDirty();   //mIsDirty == true if byte != 0
        if (parcel.readByte() != 0)
            markAsNew();    //mIsNew == true if byte != 0
    }
}