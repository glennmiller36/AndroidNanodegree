package com.fluidminds.android.studiosity.eventbus;

import com.fluidminds.android.studiosity.models.CardModel;

/**
 * Event fired when the Card changed.
 */
public class CardChangedEvent {
    private CardModel mModel;

    public CardChangedEvent(CardModel model){
        this.mModel = model;
    }

    public CardModel getModel(){
        return mModel;
    }
}