package com.fluidminds.android.studiosity.models;

/**
 * Interface defining a business rule.
 */
public interface IBusinessRule {

    String getField();

    Boolean execute(FieldDataList fieldData);

    String getError();
}
