package com.fluidminds.android.studiosity.eventbus;

/**
 * Event fired when the Subject Theme Color changed.
 */
public class ThemeColorChangedEvent {
    private Integer mColor;

    public ThemeColorChangedEvent(Integer color){
        this.mColor = color;
    }

    public Integer getColor(){
        return mColor;
    }
}