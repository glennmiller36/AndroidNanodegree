package com.fluidminds.android.studiosity.viewmodels;

import android.text.Editable;
import android.text.TextWatcher;

import com.fluidminds.android.studiosity.models.DeckModel;

/**
 * The ViewModel is the logic behind the Deck UI.
 */
public class DeckViewModel {

    private DeckModel mModel;

    public DeckViewModel(DeckModel model) {
        this.mModel = model;
    }

    public DeckModel getModel() {
        return mModel;
    }

    /**
     * Two-way data binding.
     */
    public final TextWatcher onDeckChanged = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!mModel.getName().equals(s.toString())) {
                mModel.setName(s.toString());
            }
        }
    };
}