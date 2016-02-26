package com.fluidminds.android.studiosity.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.HashMap;

import com.fluidminds.android.studiosity.BR;

/**
 * Properties and Methods common to all Models.
 */
public abstract class BaseModel extends BaseObservable {

    private Boolean mIsDirty = false;
    private Boolean mIsNew = false;
    private FieldDataList mFieldData = new FieldDataList();
    protected BusinessRules mBusinessRules = new BusinessRules(mFieldData);

    public Boolean getIsDirty() {
        return mIsDirty;
    }

    public Boolean getIsNew() {
        return mIsNew;
    }

    public FieldDataList getFieldData() { return mFieldData; }

    public BaseModel() {
        addBusinessRules();
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

    abstract protected void addBusinessRules();

    /**
     * Sets the value for a specific field.
     */
    public void setFieldData(String field, Object value) {
        mFieldData.setValue(field, value);

        if (mBusinessRules.checkRulesForField(field)) {
            notifyPropertyChanged(BR.brokenRules);
        }

        markDirty();
    }

    /**
     * Sets the value for a specific field without marking the field as dirty.
     */
    public void loadFieldData(String key, Object value) {
        mFieldData.setValue(key, value);
    }

    /**
     * Returns the first broken rule for the requested field.
     */
    @Bindable
    public HashMap<String, String> getBrokenRules() {
        return mBusinessRules.getBrokenRules();
    }
}