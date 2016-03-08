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
import com.fluidminds.android.studiosity.data.DataContract.DeckEntry;
import com.fluidminds.android.studiosity.utils.Converters;

import java.util.Date;

/**
 * A rich Model representing a Deck, where the object encapsulates actual behavior (business and validation).
 */
public class DeckModel extends BaseModel implements Parcelable {

    // Field Names
    public static final String sID = "Id";
    public static final String sSUBJECTID = "SubjectId";
    public static final String sNAME = "Name";
    public static final String sCREATEDATE = "CreateDate";

    public DeckModel(Long subjectId) {
        super();

        initializeFieldData(0L, subjectId, "", null);

        markAsNew();
    }

    public DeckModel(Long id, Long subjectId, String name, Date createDate) {
        super();

        initializeFieldData(id, subjectId, name, createDate);

        markAsOld();
    }

    @Override
    protected void addBusinessRules() {
        // Name is required
        mBusinessRules.addRule(new RequiredRule(sNAME, StudiosityApp.getInstance().getString(R.string.required)));
    }

    /**
     * Put the initial field key/value into the FieldDataList.
     */
    private void initializeFieldData(Long id, Long subjectId, String name, Date createDate) {
        /* Initially load properties without dirtying the model */
        loadFieldData(sID, id);
        loadFieldData(sSUBJECTID, subjectId);
        loadFieldData(sNAME, name);
        loadFieldData(sCREATEDATE, createDate);
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
     * SubjectId
     */
    public Long getSubjectId() { return getFieldData().getLong(sSUBJECTID); }

    public void setSubjectId(Long id) {
        if (!getSubjectId().equals(id)) {
            setFieldData(sSUBJECTID, id);
        }
    }

    /**
     * Name
     */
    @Bindable
    public String getName() { return getFieldData().getString(sNAME); }

    public void setName(String name) {
        if (!getName().equals(name)) {
            setFieldData(sNAME, name.trim());
        }
    }

    /**
     * CreateDate
     */
    @Bindable
    public Date getCreateDate() {
        return getFieldData().getDate(sCREATEDATE);
    }

    public void setCreateDate(Date date) {
        if (!getCreateDate().equals(date)) {
            setFieldData(sCREATEDATE, date);
        }
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
        values.put(DeckEntry.COLUMN_SUBJECT_ID, getSubjectId());
        values.put(DeckEntry.COLUMN_NAME, getName());

        try {
            if (getId() == 0) { // insert
                Uri insertedUri = StudiosityApp.getInstance().getContentResolver().insert(DeckEntry.buildItemUri(getId()), values);
                if (insertedUri != null && Integer.parseInt(insertedUri.getLastPathSegment()) > 0) {
                    setId(Long.parseLong(insertedUri.getLastPathSegment()));
                    return this;
                }
            }
            else {  // update
                values.put(DeckEntry.COLUMN_CREATE_DATE, Converters.dateToString(getCreateDate()));

                int rowsUpdated = StudiosityApp.getInstance().getContentResolver().update(DeckEntry.buildItemUri(getId()), values, null, null);
                return (rowsUpdated == 1) ? this : null;
            }
        } catch (SQLiteConstraintException e) {
            getBrokenRules().put(sNAME, StudiosityApp.getInstance().getString(R.string.duplicate_name));
            notifyPropertyChanged(BR.brokenRules);
        }

        return null;
    }

    /**
     * Delete the Subject and child Decks from the database.
     */
    public String delete() {
        try {
            StudiosityApp.getInstance().getContentResolver().delete(DeckEntry.buildItemUri(getId()), DeckEntry._ID + " = " + getId(), null);
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
        parcel.writeLong(getSubjectId());
        parcel.writeString(getName());
        parcel.writeString(getCreateDate() == null ? "" : Converters.dateToString(getCreateDate()));

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

        initializeFieldData(parcel.readLong(), parcel.readLong(), parcel.readString(), Converters.stringToDate(parcel.readString()));

        // base fields
        if (parcel.readByte() != 0)
             markDirty();   //mIsDirty == true if byte != 0
        if (parcel.readByte() != 0)
            markAsNew();    //mIsNew == true if byte != 0
    }
}