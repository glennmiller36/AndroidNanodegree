package com.fluidminds.android.studiosity.viewmodels;

import com.fluidminds.android.studiosity.models.BackupModel;

/**
 * The ViewModel is the logic behind the Backup and Restore app data.
 */
public class BackupViewModel {

    private BackupModel mModel;

    public BackupViewModel(BackupModel model) {
        this.mModel = model;
    }

    public BackupModel getModel() {
        return mModel;
    }
}