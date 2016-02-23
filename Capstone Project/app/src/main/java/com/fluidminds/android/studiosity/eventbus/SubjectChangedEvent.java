package com.fluidminds.android.studiosity.eventbus;

import com.fluidminds.android.studiosity.models.SubjectModel;

/**
 * Event fired when the Subject changed.
 */
public class SubjectChangedEvent {
    private SubjectModel mModel;

    public SubjectChangedEvent(SubjectModel model){
        this.mModel = model;
    }

    public SubjectModel getModel(){
        return mModel;
    }
}