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
import com.fluidminds.android.studiosity.data.DataContract.CardEntry;

/**
 * A rich Model representing a Card, where the object encapsulates actual behavior (business and validation).
 */
public class CardModel extends BaseModel implements Parcelable {

    // Field Names
    public static final String sID = "Id";
    public static final String sDECKID = "DeckId";
    public static final String sQUESTION = "Question";
    public static final String sANSWER = "Answer";
    public static final String sRECENTSCORES = "RecentScores";
    public static final String sPERCENTCORRECT = "PercentCorrect";

    public CardModel(Long deckId) {
        super();

        initializeFieldData(0L, deckId, "", "", null, 0);

        markAsNew();
    }

    public CardModel(Long id, Long deckId, String question, String answer, String recentScores, Integer percentCorrect) {
        super();

        initializeFieldData(id, deckId, question, answer, recentScores, percentCorrect);

        markAsOld();
    }

    @Override
    protected void addBusinessRules() {
        mBusinessRules.addRule(new RequiredRule(sQUESTION, StudiosityApp.getInstance().getString(R.string.required)));
        mBusinessRules.addRule(new RequiredRule(sANSWER, StudiosityApp.getInstance().getString(R.string.required)));
    }

    /**
     * Put the initial field key/value into the FieldDataList.
     */
    private void initializeFieldData(Long id, Long deckId, String question, String answer, String recentScores, Integer percentCorrect) {
        /* Initially load properties without dirtying the model */
        loadFieldData(sID, id);
        loadFieldData(sDECKID, deckId);
        loadFieldData(sQUESTION, question);
        loadFieldData(sANSWER, answer);
        loadFieldData(sRECENTSCORES, recentScores);
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
     * Question
     */
    @Bindable
    public String getQuestion() { return getFieldData().getString(sQUESTION); }

    public void setQuestion(String question) {
        if (!getQuestion().equals(question)) {
            setFieldData(sQUESTION, question.trim());
        }
    }

    /**
     * Answer
     */
    @Bindable
    public String getAnswer() { return getFieldData().getString(sANSWER); }

    public void setAnswer(String answer) {
        if (!getAnswer().equals(answer)) {
            setFieldData(sANSWER, answer.trim());
        }
    }

    /**
     * Recent Scores
     */
    @Bindable
    public String getRecentScores() { return getFieldData().getString(sRECENTSCORES); }

    public void setRecentScores(String recentScores) {
        if (!getRecentScores().equals(recentScores)) {
            setFieldData(sRECENTSCORES, recentScores);
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
    public CardModel save() {

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
        values.put(CardEntry.COLUMN_DECK_ID, getDeckId());
        values.put(CardEntry.COLUMN_QUESTION, getQuestion());
        values.put(CardEntry.COLUMN_ANSWER, getAnswer());
        values.put(CardEntry.COLUMN_RECENT_SCORES, getRecentScores());
        values.put(CardEntry.COLUMN_PERCENT_CORRECT, getPercentCorrect());

        try {
            if (getId() == 0) { // insert
                Uri insertedUri = StudiosityApp.getInstance().getContentResolver().insert(CardEntry.buildItemUri(getId()), values);
                if (insertedUri != null && Integer.parseInt(insertedUri.getLastPathSegment()) > 0) {
                    setId(Long.parseLong(insertedUri.getLastPathSegment()));
                    return this;
                }
            }
            else {  // update
                int rowsUpdated = StudiosityApp.getInstance().getContentResolver().update(CardEntry.buildItemUri(getId()), values, null, null);
                return (rowsUpdated == 1) ? this : null;
            }
        } catch (SQLiteConstraintException e) {
            getBrokenRules().put(sQUESTION, StudiosityApp.getInstance().getString(R.string.duplicate_name));
            notifyPropertyChanged(BR.brokenRules);
        }

        return null;
    }

    /**
     * Delete the Card from the database.
     */
    public String delete() {
        try {
            StudiosityApp.getInstance().getContentResolver().delete(CardEntry.buildItemUri(getId()), CardEntry._ID + " = " + getId(), null);
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
        parcel.writeString(getQuestion());
        parcel.writeString(getAnswer());
        parcel.writeString(getRecentScores());
        parcel.writeInt(getPercentCorrect());

        // base fields
        parcel.writeByte((byte) (getIsDirty() ? 1 : 0));   //if mIsDirty == true, byte == 1
        parcel.writeByte((byte) (getIsNew() ? 1 : 0));     //if mIsNew == true, byte == 1
    }

    /** Static field used to regenerate object, individually or as arrays */
    public static final Creator<CardModel> CREATOR = new Creator<CardModel>() {
        public CardModel createFromParcel(Parcel parcel) {
            return new CardModel(parcel);
        }
        public CardModel[] newArray(int size) {
            return new CardModel[size];
        }
    };

    /** Ctor from Parcel, reads back fields IN THE ORDER they were written */
    public CardModel(Parcel parcel){
        super();

        initializeFieldData(parcel.readLong(), parcel.readLong(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readInt());

        // base fields
        if (parcel.readByte() != 0)
             markDirty();   //mIsDirty == true if byte != 0
        if (parcel.readByte() != 0)
            markAsNew();    //mIsNew == true if byte != 0
    }
}