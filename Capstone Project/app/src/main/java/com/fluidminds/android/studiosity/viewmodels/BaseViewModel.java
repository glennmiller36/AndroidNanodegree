package com.fluidminds.android.studiosity.viewmodels;

import android.databinding.BaseObservable;

import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.app.StudiosityApp;

/**
 * Properties and Methods common to all ViewModels.
 */
public class BaseViewModel extends BaseObservable {
    protected static final String sRequired = StudiosityApp.getInstance().getString(R.string.required);
}
