package com.fluidminds.android.studiosity.models;

/**
 * Business rule for a required field string.
 */
public class RequiredRule extends BaseRule {

    public RequiredRule(String field, String error) {
        this.mField = field;
        this.mError = error;
    }

    @Override
    public Boolean execute(FieldDataList fieldData) {
        return !fieldData.getString(mField).isEmpty();
    }
}
