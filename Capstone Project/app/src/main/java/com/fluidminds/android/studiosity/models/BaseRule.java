package com.fluidminds.android.studiosity.models;

/**
 * Properties and Methods common to all Rules.
 */
public class BaseRule implements IBusinessRule {
    protected String mField;
    protected String mError;

    public String getField() {
        return mField;
    }

    public Boolean execute(FieldDataList fieldData) {
        return true;
    }

    public String getError() {
        return mError;
    }
}
