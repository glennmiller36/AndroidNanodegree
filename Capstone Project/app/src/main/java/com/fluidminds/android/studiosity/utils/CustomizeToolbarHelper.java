package com.fluidminds.android.studiosity.utils;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fluidminds.android.studiosity.R;

import java.util.ArrayList;

/**
 * Dynamically change Toolbar icons color.
 */
public class CustomizeToolbarHelper {

    /**
     * Colorize mToolbar icons to the desired target color.
     */
    public static void colorizeToolbar(Activity activity, Toolbar toolbarView, Menu menu, int toolbarBackgroundColor) {

        Integer toolbarIconsColor = ThemeColor.isWhiteContrastColor(toolbarBackgroundColor) ? Color.WHITE : Color.BLACK;
        final PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(toolbarIconsColor, PorterDuff.Mode.MULTIPLY);

        // change the background color of a mToolbar
        toolbarView.setBackgroundColor(toolbarBackgroundColor);

        // walk down any view group hierarchy
        for (int i = 0; i < toolbarView.getChildCount(); i++) {
            final View view = toolbarView.getChildAt(i);

            setColorToViewGroupChildren(view, toolbarIconsColor, colorFilter);
        }

        // change the color of the Overflow Menu icon
        setOverflowButtonColor(activity, colorFilter);

        // change the color of the non-Overflow icons
        setMenuItemColor(menu, toolbarIconsColor);
    }

    /**
     * Walk down any view group hierarchy.
     */
    private static void setColorToViewGroupChildren(View view, int toolbarIconsColor, PorterDuffColorFilter colorFilter) {

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                final View childView = ((ViewGroup) view).getChildAt(i);

                // recursively walk down the current view's children
                setColorToViewGroupChildren(childView, toolbarIconsColor, colorFilter);
            }
        }

        // Action Bar back button
        if (view instanceof ImageButton) {
            ((ImageButton) view).getDrawable().setColorFilter(colorFilter);
        }

        // Custom button image (Cancel and Done)
        if (view instanceof ImageView) {
            ((ImageView) view).getDrawable().setColorFilter(colorFilter);
        }

        // Custom button text (Cancel and Done)
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(toolbarIconsColor);
        }
    }

    /**
     * Set overflowDescription atribute in styles, so we can grab the reference to the overflow icon.
     */
    private static void setOverflowButtonColor(final Activity activity, final PorterDuffColorFilter colorFilter) {
        final String overflowDescription = activity.getString(R.string.abc_action_menu_overflow_description);
        final ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        final ViewTreeObserver viewTreeObserver = decorView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final ArrayList<View> outViews = new ArrayList<View>();
                decorView.findViewsWithText(outViews, overflowDescription,
                        View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                if (outViews.isEmpty()) {
                    return;
                }
                AppCompatImageView overflow=(AppCompatImageView) outViews.get(0);
                overflow.setColorFilter(colorFilter);

                removeOnGlobalLayoutListener(decorView, this);
            }
        });
    }

    private static void removeOnGlobalLayoutListener(View v, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            v.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        }
        else {
            v.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }

    /**
     * Sets the color filter and/or the alpha transparency on a {@link MenuItem}'s icon.
     */
    public static void setMenuItemColor(Menu menu, Integer color) {
        for (int i = 0, size = menu.size(); i < size; i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            if (drawable != null) {
                // If we don't mutate the drawable, then all drawables with this id will have the ColorFilter
                drawable.mutate();
                if (color != null) {
                    drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                }
            }
        }
    }
}


