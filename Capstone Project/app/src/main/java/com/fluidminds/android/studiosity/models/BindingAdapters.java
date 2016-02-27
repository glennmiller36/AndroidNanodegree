package com.fluidminds.android.studiosity.models;

import android.databinding.BindingAdapter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.fluidminds.android.studiosity.R;

/**
 * BindingAdapter is applied to methods that are used to manipulate how values with expressions are set to views.
 */
public class BindingAdapters {

    @BindingAdapter("android:background")
    public static void setCircleBackground(View view, int color) {

        Drawable drawable = view.getContext().getResources().getDrawable(R.drawable.circle);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        view.setBackground(drawable);
    }
}
