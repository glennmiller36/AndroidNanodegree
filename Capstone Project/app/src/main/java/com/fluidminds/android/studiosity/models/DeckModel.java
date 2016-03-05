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
import com.fluidminds.android.studiosity.data.DataContract.SubjectEntry;
import com.fluidminds.android.studiosity.utils.ThemeColor;

/**
 * A rich Model representing a Deck, where the object encapsulates actual behavior (business and validation).
 */
public class DeckModel extends BaseModel implements Parcelable {

    // Field Names
    public static final String sID = "Id";
    public static final String sSUBJECT = "Subject";
    public static final String sCOLORINT = "ColorInt";

    public DeckModel() {
        super();

        initializeFieldData(0L, "", ThemeColor.generateRandomColor());

        markAsNew();
    }

    public DeckModel(Long id, String subject, Integer colorInt) {
        super();

        initializeFieldData(id, subject, colorInt);

        markAsOld();
    }

    @Override
    protected void addBusinessRules() {
        // Subject is required
        mBusinessRules.addRule(new RequiredRule(sSUBJECT, StudiosityApp.getInstance().getString(R.string.required)));
    }

    /**
     * Put the initial field key/value into the FieldDataList.
     */
    private void initializeFieldData(Long id, String subject, Integer colorInt) {
        /* Initially load properties without dirtying the model */
        loadFieldData(sID, id);
        loadFieldData(sSUBJECT, subject);
        loadFieldData(sCOLORINT, colorInt);
    }

    /**
     * Id
     */
    public Long getId() { return getFieldData().getLong(sID); }

    public void setId(Long id) {
        if (!getId().equals(id)) {
            setFieldData(sID, id);
        }
    }

    /**
     * Subject
     */
    @Bindable
    public String getSubject() {
        return getFieldData().getString(sSUBJECT);
    }

    public void setSubject(String subject) {
        if (!getSubject().equals(subject)) {
            setFieldData(sSUBJECT, subject.trim());
        }
    }

    /**
     * Color
     */
    @Bindable
    public Integer getColorInt() {
        return getFieldData().getInteger(sCOLORINT);
    }

    public void setColorInt(Integer color) {
        if (!getColorInt().equals(color)) {
            setFieldData(sCOLORINT, color);

            notifyPropertyChanged(BR.colorInt);
            notifyPropertyChanged(BR.colorName);
        }
    }

    @Bindable
    public String getColorName() {
        return ThemeColor.getColorName(getColorInt());
    }

    /**
     * Saves the object to the database.
     */
    public DeckModel save() {

        if (!mBusinessRules.checkRules()) {
            notifyPropertyChanged(BR.brokenRules);
            return null;
        }

        if (!getIsNew() && !getIsDirty()) {
            return this;  // no changes
        }

        ContentValues values = new ContentValues();

        // Then add the data, along with the corresponding name of the data type,
        // so the content provider knows what kind of value is being inserted.
        values.put(SubjectEntry.COLUMN_NAME, getSubject());
        values.put(SubjectEntry.COLUMN_COLOR, getColorInt());

        try {
            if (getId() == 0) { // insert
                Uri insertedUri = StudiosityApp.getInstance().getContentResolver().insert(SubjectEntry.buildItemUri(getId()), values);
                if (insertedUri != null && Integer.parseInt(insertedUri.getLastPathSegment()) > 0) {
                    setId(Long.parseLong(insertedUri.getLastPathSegment()));
                    return this;
                }
            }
            else {  // update
                int rowsUpdated = StudiosityApp.getInstance().getContentResolver().update(SubjectEntry.buildItemUri(getId()), values, null, null);
                return (rowsUpdated == 1) ? this : null;
            }
        } catch (SQLiteConstraintException e) {
            getBrokenRules().put(sSUBJECT, StudiosityApp.getInstance().getString(R.string.duplicate_name));
            notifyPropertyChanged(BR.brokenRules);
        }

        return null;
    }

    /**
     * Delete the Subject and child Decks from the database.
     */
    public String delete() {
        try {
            StudiosityApp.getInstance().getContentResolver().delete(SubjectEntry.buildItemUri(getId()), SubjectEntry._ID + " = " + getId(), null);
            return "";
        } catch (Exception e) {
            return e.getMessage();
        }
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
    public static final Creator<DeckModel> CREATOR = new Creator<DeckModel>() {
        public DeckModel createFromParcel(Parcel parcel) {
            return new DeckModel(parcel);
        }
        public DeckModel[] newArray(int size) {
            return new DeckModel[size];
        }
    };

    /** Ctor from Parcel, reads back fields IN THE ORDER they were written */
    public DeckModel(Parcel parcel){
        super();

        initializeFieldData(parcel.readLong(), parcel.readString(), parcel.readInt());

        // base fields
        if (parcel.readByte() != 0)
             markDirty();   //mIsDirty == true if byte != 0
        if (parcel.readByte() != 0)
            markAsNew();    //mIsNew == true if byte != 0
    }
}