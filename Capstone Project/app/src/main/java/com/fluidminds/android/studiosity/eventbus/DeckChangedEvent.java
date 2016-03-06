package com.fluidminds.android.studiosity.eventbus;

import com.fluidminds.android.studiosity.models.DeckModel;

/**
 * Event fired when the Deck changed.
 */
public class DeckChangedEvent {
    private DeckModel mModel;

    public DeckChangedEvent(DeckModel model){
        this.mModel = model;
    }

    public DeckModel getModel(){
        return mModel;
    }
}