package com.fluidminds.android.studiosity.models;

import android.databinding.BaseObservable;

import java.util.LinkedHashMap;

/**
 * Properties and Methods common to all Models.
 */
public class BaseModel extends BaseObservable {

    private Boolean mIsDirty = false;
    private Boolean mIsNew = false;
    private LinkedHashMap<String, String> mBrokenRules = new LinkedHashMap<>();

    public Boolean getIsDirty() {
        return mIsDirty;
    }

    public Boolean getIsNew() {
        return mIsNew;
    }

    public LinkedHashMap<String, String> getBrokenRules() { return mBrokenRules; }

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
        mBrokenRules.clear();
    }

    /**
     * Returns the first broken rule for the requested property.
     */
    public String getBrokenRule(String propertyName) {
        for (String key : mBrokenRules.keySet()) {
            if (key.equals(propertyName))
                return mBrokenRules.get(key);
        }

        return "";
    }
}