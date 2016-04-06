package com.fluidminds.android.studiosity.eventbus;

import com.fluidminds.android.studiosity.models.DeckModel;

/**
 * Event fired when Stats Trend has determined that no Quizzes exist for the requested Deck.
 */
public class NoQuizzesForDeckEvent {
    private DeckModel mModel;

    public NoQuizzesForDeckEvent(DeckModel model){
        this.mModel = model;
    }

    public DeckModel getModel(){
        return mModel;
    }
}