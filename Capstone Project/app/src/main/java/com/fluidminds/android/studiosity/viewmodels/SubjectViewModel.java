package com.fluidminds.android.studiosity.viewmodels;

import android.text.Editable;
import android.text.TextWatcher;

import com.fluidminds.android.studiosity.models.SubjectModel;

/**
 * The ViewModel is the logic behind the Subject UI.
 */
public class SubjectViewModel {

    private SubjectModel mModel;

    public SubjectViewModel(SubjectModel model) {
        this.mModel = model;
    }

    public SubjectModel getModel() {
        return mModel;
    }

    /**
     * Two-way data binding.
     */
    public final TextWatcher onSubjectChanged = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!mModel.getSubject().equals(s.toString())) {
                mModel.setSubject(s.toString());
            }
        }
    };
}