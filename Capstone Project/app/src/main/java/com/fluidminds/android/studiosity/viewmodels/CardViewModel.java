package com.fluidminds.android.studiosity.viewmodels;

import android.text.Editable;
import android.text.TextWatcher;

import com.fluidminds.android.studiosity.models.CardModel;

/**
 * The ViewModel is the logic behind the Card UI.
 */
public class CardViewModel {

    private CardModel mModel;

    public CardViewModel(CardModel model) {
        this.mModel = model;
    }

    public CardModel getModel() {
        return mModel;
    }

    /**
     * Two-way data binding.
     */
    public final TextWatcher onQuestionChanged = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!mModel.getQuestion().equals(s.toString())) {
                mModel.setQuestion(s.toString());
            }
        }
    };

    public final TextWatcher onAnswerChanged = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!mModel.getAnswer().equals(s.toString())) {
                mModel.setAnswer(s.toString());
            }
        }
    };
}