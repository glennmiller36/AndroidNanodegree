package com.fluidminds.android.studiosity.views;

import android.content.Context;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.fluidminds.android.studiosity.R;

/**
 * Custom SlidingPaneLayout so click events outside the right pane will be forwarded to left pane event listeners.
 */
public class SlidingPaneLayoutExtended extends SlidingPaneLayout {

    public SlidingPaneLayoutExtended(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        // if the click is outside the right pane (flash card cover) then dispatch the event to another listener
        View view = this.getChildAt(1);
        if (view != null && event.getX() < view.getX())
            return false; // return false so that another event's listener should be called

        return super.onTouchEvent(event);   // works as a normal SlidingPaneLayout
    }
}

