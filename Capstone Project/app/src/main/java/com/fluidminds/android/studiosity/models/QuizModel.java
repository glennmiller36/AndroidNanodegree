package com.fluidminds.android.studiosity.models;

import android.content.ContentValues;
import android.databinding.Bindable;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.fluidminds.android.studiosity.BR;
import com.fluidminds.android.studiosity.app.StudiosityApp;
import com.fluidminds.android.studiosity.data.DataContract.QuizEntry;
import com.fluidminds.android.studiosity.utils.Converters;

import java.util.Date;

/**
 * A rich Model representing a Quiz, where the object encapsulates actual behavior (business and validation).
 */
public class QuizModel extends BaseModel implements Parcelable {

    // Field Names
    public static final String sID = "Id";
    public static final String sDECKID = "DeckId";
    public static final String sSTARTDATE = "StartDate";
    public static final String sNUMCORRECT = "NumCorrect";
    public static final String sTOTALCARDS = "TotalCards";
    public static final String sPERCENTCORRECT = "PercentCorrect";

    public QuizModel(Long deckId) {
        super();

        initializeFieldData(0L, deckId, new Date(), 0, 0, 0);

        markAsNew();
    }

    public QuizModel(Long id, Long deckId, Date startDate, Integer numCorrect, Integer totalCards, Integer percentCorrect) {
        super();

        initializeFieldData(id, deckId, startDate, numCorrect, totalCards, percentCorrect);

        markAsOld();
    }

    @Override
    protected void addBusinessRules() {
        // no rules
    }

    /**
     * Put the initial field key/value into the FieldDataList.
     */
    private void initializeFieldData(Long id, Long deckId, Date startDate, Integer numCorrect, Integer totalCards, Integer percentCorrect) {
        /* Initially load properties without dirtying the model */
        loadFieldData(sID, id);
        loadFieldData(sDECKID, deckId);
        loadFieldData(sSTARTDATE, startDate);
        loadFieldData(sNUMCORRECT, numCorrect);
        loadFieldData(sTOTALCARDS, totalCards);
        loadFieldData(sPERCENTCORRECT, percentCorrect);
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
     * DeckId
     */
    public Long getDeckId() { return getFieldData().getLong(sDECKID); }

    public void setDeckId(Long id) {
        if (!getDeckId().equals(id)) {
            setFieldData(sDECKID, id);
        }
    }

    /**
     * StartDate
     */
    @Bindable
    public Date getStartDate() { return getFieldData().getDate(sSTARTDATE); }

    public void setStartDate(Date date) {
        if (!getStartDate().equals(date)) {
            setFieldData(sSTARTDATE, date);
            }
    }

    /**
     * Number Correct
     */
    @Bindable
    public Integer getNumCorrect() { return getFieldData().getInteger(sNUMCORRECT); }

    public void setNumCorrect(Integer numCorrect) {
        if (!getNumCorrect().equals(numCorrect)) {
            setFieldData(sNUMCORRECT, numCorrect);
        }
    }

    /**
     * Total Cards
     */
    @Bindable
    public Integer getTotalCards() { return getFieldData().getInteger(sTOTALCARDS); }

    public void setTotalCards(Integer totalCards) {
        if (!getTotalCards().equals(totalCards)) {
            setFieldData(sTOTALCARDS, totalCards);
        }
    }

    /**
     * Percent Correct
     */
    @Bindable
    public Integer getPercentCorrect() { return getFieldData().getInteger(sPERCENTCORRECT); }

    public void setPercentCorrect(Integer percentCorrect) {
        if (!getPercentCorrect().equals(percentCorrect)) {
            setFieldData(sPERCENTCORRECT, percentCorrect);
        }
    }

    /**
     * Saves the object to the database.
     */
    public QuizModel save() {

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
        values.put(QuizEntry.COLUMN_DECK_ID, getDeckId());
        values.put(QuizEntry.COLUMN_NUM_CORRECT, getNumCorrect());
        values.put(QuizEntry.COLUMN_TOTAL_CARDS, getTotalCards());
        values.put(QuizEntry.COLUMN_PERCENT_CORRECT, getPercentCorrect());

        if (getId() == 0) { // insert
            values.put(QuizEntry.COLUMN_START_DATE, Converters.dateToString(getStartDate()));

            Uri insertedUri = StudiosityApp.getInstance().getContentResolver().insert(QuizEntry.buildItemUri(getId()), values);
            if (insertedUri != null && Integer.parseInt(insertedUri.getLastPathSegment()) > 0) {
                setId(Long.parseLong(insertedUri.getLastPathSegment()));
                return this;
            }
        }
        else {  // update
            int rowsUpdated = StudiosityApp.getInstance().getContentResolver().update(QuizEntry.buildItemUri(getId()), values, null, null);
            return (rowsUpdated == 1) ? this : null;
        }

        return null;
    }

    /**
     * Delete the Quiz from the database.
     */
    public String delete() {
        try {
            StudiosityApp.getInstance().getContentResolver().delete(QuizEntry.buildItemUri(getId()), QuizEntry._ID + " = " + getId(), null);
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
        parcel.writeLong(getDeckId());
        parcel.writeString(getStartDate() == null ? "" : Converters.dateToString(getStartDate()));
        parcel.writeInt(getNumCorrect());
        parcel.writeInt(getTotalCards());
        parcel.writeInt(getPercentCorrect());

        // base fields
        parcel.writeByte((byte) (getIsDirty() ? 1 : 0));   //if mIsDirty == true, byte == 1
        parcel.writeByte((byte) (getIsNew() ? 1 : 0));     //if mIsNew == true, byte == 1
    }

    /** Static field used to regenerate object, individually or as arrays */
    public static final Creator<QuizModel> CREATOR = new Creator<QuizModel>() {
        public QuizModel createFromParcel(Parcel parcel) {
            return new QuizModel(parcel);
        }
        public QuizModel[] newArray(int size) {
            return new QuizModel[size];
        }
    };

    /** Ctor from Parcel, reads back fields IN THE ORDER they were written */
    public QuizModel(Parcel parcel){
        super();

        initializeFieldData(parcel.readLong(), parcel.readLong(), Converters.stringToDate(parcel.readString()), parcel.readInt(), parcel.readInt(), parcel.readInt());

        // base fields
        if (parcel.readByte() != 0)
             markDirty();   //mIsDirty == true if byte != 0
        if (parcel.readByte() != 0)
            markAsNew();    //mIsNew == true if byte != 0
    }
}