package com.fluidminds.android.studiosity.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Tracks the business rules for a business object.
 */
public class BusinessRules {

    private FieldDataList nFieldData;
    private List<IBusinessRule> mRuleSet = new ArrayList<IBusinessRule>();
    private HashMap<String, String> mBrokenRules = new HashMap<>();

    public HashMap<String, String> getBrokenRules() { return mBrokenRules; }

    public BusinessRules(FieldDataList fieldData) {
        this.nFieldData = fieldData;
    }

    /**
     * Associates a business rule with the business object.
     */
    public void addRule(IBusinessRule rule)
    {
        mRuleSet.add(rule);
    }

    /**
     * Invokes all rules for the business type.
     */
    public Boolean checkRules()
    {
        mBrokenRules.clear();

        for (IBusinessRule rule : mRuleSet) {
            // run the rule
            if (!rule.execute(nFieldData)) {
                mBrokenRules.put(rule.getField(), rule.getError());
            }
        }

        return mBrokenRules.isEmpty();
    }

    /**
     * Invokes all rules for a specific field.
     */
    protected Boolean checkRulesForField(String field)
    {
        Boolean rulesChanged = false;

        if (mBrokenRules.containsKey(field)) {
            mBrokenRules.remove(field);
            rulesChanged = true;
        }

        for (IBusinessRule rule : mRuleSet) {
            if (rule.getField().equals(field)) {

                // run the rule
                if (!rule.execute(nFieldData)) {
                    mBrokenRules.put(field, rule.getError());
                    rulesChanged = true;
                }
            }
        }

        return rulesChanged;
    }
}