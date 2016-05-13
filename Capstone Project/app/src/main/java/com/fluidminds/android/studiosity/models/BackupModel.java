package com.fluidminds.android.studiosity.models;

import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.fluidminds.android.studiosity.BR;
import com.fluidminds.android.studiosity.utils.Converters;

import java.util.Date;

/**
 * A rich Model representing a Backup, where the object encapsulates actual behavior (business and validation).
 */
public class BackupModel extends BaseModel implements Parcelable {

    // Field Names
    public static final String sACCOUNT = "Account";
    public static final String sFILESIZE = "FileSize";
    public static final String sMODIFIEDDATE = "ModifiedDate";

    public BackupModel() {
        super();

        initializeFieldData("", null, null);

        markAsNew();
    }

    public BackupModel(String account, Long fileSize, Date modifiedDate) {
        super();

        initializeFieldData(account, fileSize, modifiedDate);

        markAsOld();
    }

    @Override
    protected void addBusinessRules() {
        // no rules
    }

    /**
     * Put the initial field key/value into the FieldDataList.
     */
    private void initializeFieldData(String account, Long fileSize, Date modifiedDate) {
        /* Initially load properties without dirtying the model */
        loadFieldData(sACCOUNT, account);
        loadFieldData(sFILESIZE, fileSize);
        loadFieldData(sMODIFIEDDATE, modifiedDate);
    }

    /**
     * Account
     */
    @Bindable
    public String getAccount() {
        return getFieldData().getString(sACCOUNT);
    }

    public void setAccount(String account) {
        if (!getAccount().equals(account)) {
            setFieldData(sACCOUNT, account.trim());

            notifyPropertyChanged(BR.account);
        }
    }

    /**
     * File Size
     */
    public Long getFileSize() { return getFieldData().getLong(sFILESIZE); }

    @Bindable
    public String getFileSizeAsString() { return humanReadableByteCount(getFileSize()); }

    public void setFileSize(Long fileSize) {
        if (getFileSize() == null || !getFileSize().equals(fileSize)) {
            setFieldData(sFILESIZE, fileSize);

            notifyPropertyChanged(BR.fileSizeAsString);
        }
    }

    private String humanReadableByteCount(Long bytes) {
        if (bytes == null)
            return "";

        int unit = 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = String.valueOf(("KMGTPE").charAt(exp - 1));
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    /**
     * Last Modified Date
     */
    public Date getModifiedDate() { return getFieldData().getDate(sMODIFIEDDATE); }

    @Bindable
    public String getModifiedDateAsString() {
        if (getModifiedDate() == null)
            return "";
        else
            return Converters.dateToString(getFieldData().getDate(sMODIFIEDDATE), "E, MMM d, yyyy 'at' h:mm a");
    }

    public void setModifiedDate(Date date) {
        if (getModifiedDate() == null || !getModifiedDate().equals(date)) {
            setFieldData(sMODIFIEDDATE, date);

            notifyPropertyChanged(BR.modifiedDateAsString);
        }
    }


    /** Used to give additional hints on how to process the received parcel.*/
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getAccount());
        parcel.writeLong(getFileSize());
        parcel.writeString(getModifiedDate() == null ? "" : Converters.dateToString(getModifiedDate()));

        // base fields
        parcel.writeByte((byte) (getIsDirty() ? 1 : 0));   //if mIsDirty == true, byte == 1
        parcel.writeByte((byte) (getIsNew() ? 1 : 0));     //if mIsNew == true, byte == 1
    }

    /** Static field used to regenerate object, individually or as arrays */
    public static final Creator<BackupModel> CREATOR = new Creator<BackupModel>() {
        public BackupModel createFromParcel(Parcel parcel) {
            return new BackupModel(parcel);
        }
        public BackupModel[] newArray(int size) {
            return new BackupModel[size];
        }
    };

    /** Ctor from Parcel, reads back fields IN THE ORDER they were written */
    public BackupModel(Parcel parcel){
        super();

        initializeFieldData(parcel.readString(), parcel.readLong(), Converters.stringToDate(parcel.readString()));

        // base fields
        if (parcel.readByte() != 0)
             markDirty();   //mIsDirty == true if byte != 0
        if (parcel.readByte() != 0)
            markAsNew();    //mIsNew == true if byte != 0
    }
}