package com.fluidminds.android.studiosity.models;

/**
 * Properties and Methods common to all Models.
 */
public class BaseModel {

    private Boolean mIsDirty = false;
    private Boolean mIsNew = false;

    public Boolean getIsDirty() {
        return mIsDirty;
    }

    public Boolean getIsNew() {
        return mIsNew;
    }

    public void markDirty() {
        this.mIsDirty = true;
    }

    public void markAsNew() {
        this.mIsNew = true;
    }

    public void markAsOld()
    {
        this.mIsNew = false;
        this.mIsDirty = false;
    }
}