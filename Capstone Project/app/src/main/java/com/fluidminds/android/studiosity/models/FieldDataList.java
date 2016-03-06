package com.fluidminds.android.studiosity.models;

import java.util.Date;
import java.util.HashMap;

/**
 * List of each fields value represented as a string key and object value.
 * Storing fields in a complex object like a list always passing by reference.
 */
public class FieldDataList {
    protected HashMap<String, Object> mFieldData = new HashMap<>();

    /**
     * Replace the value of an existing key and will create it if doesn't exist
     */
    public void setValue(String key, Object value)
    {
        mFieldData.put(key, value);
    }

    /**
     * Returns the value of the requested field as a String.
     */
    public String getString(String field) {
        return String.valueOf(mFieldData.get(field));
    }

    /**
     * Returns the value of the requested field as an Integer.
     */
    public Integer getInteger(String field) {
        return (Integer) mFieldData.get(field);
    }

    /**
     * Returns the value of the requested field as an Long.
     */
    public Long getLong(String field) {
        return (Long) mFieldData.get(field);
    }

    /**
     * Returns the value of the requested field as an Date.
     */
    public Date getDate(String field) {
        return (Date) mFieldData.get(field);
    }
}
