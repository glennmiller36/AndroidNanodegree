package com.fluidminds.android.studiosity.fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.data.DataContract.CardEntry;
import com.fluidminds.android.studiosity.models.CardModel;
import com.fluidminds.android.studiosity.models.DeckModel;
import com.fluidminds.android.studiosity.models.QuizModel;
import com.fluidminds.android.studiosity.models.SubjectModel;
import com.fluidminds.android.studiosity.utils.Converters;
import com.fluidminds.android.studiosity.utils.ThemeColor;
import com.fluidminds.android.studiosity.views.TransparentSemicircleView;

import java.util.ArrayList;

/**
 * A fragment to quiz Flash Cards.
 */
public class QuizFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    int mCardsIndex = 0;
    ArrayList<CardModel> mCards = new ArrayList<>();
    private LinearLayout mAnswerContent;
    private SlidingPaneLayout mSlidingPanel;
    private TextView mAnswer;
    private TextView mQuestion;
    private TextView mCardCount;
    private TextView mCardCountSpacer;
    private LinearLayout mContentResults;
    private TextView mResultsNumCorrect;
    private TextView mResultsTotalCards;
    private TextView mResultsAccuracy;
    private TextView mResultsDate;

    private TextView mNoRecords;

    private SubjectModel mSubjectModel;
    private DeckModel mDeckModel;
    private QuizModel mQuizModel;

    private static final int CARD_LOADER = 0;

    private static final String[] CARD_COLUMNS = {
        CardEntry.TABLE_NAME + "." + CardEntry._ID,
        CardEntry.COLUMN_DECK_ID,
        CardEntry.COLUMN_QUESTION,
        CardEntry.COLUMN_ANSWER,
        CardEntry.COLUMN_RECENT_SCORES,
        CardEntry.COLUMN_PERCENT_CORRECT
    };

    public QuizFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        mSubjectModel = intent.getParcelableExtra("subjectmodel");
        mDeckModel = intent.getParcelableExtra("deckmodel");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        mAnswerContent = (LinearLayout) view.findViewById(R.id.answerContent);
        mSlidingPanel = (SlidingPaneLayout) view.findViewById(R.id.slidingPanel);

        LinearLayout slideButton = (LinearLayout) view.findViewById(R.id.buttonSlide);
        slideButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mSlidingPanel.isOpen())
                    mSlidingPanel.closePane();
                else
                    mSlidingPanel.openPane();
            }
        });

        // don't dim SlidePane while it's animating open
        mSlidingPanel.setSliderFadeColor(Color.TRANSPARENT);

        SlidingPaneLayout.PanelSlideListener panelListener = new SlidingPaneLayout.PanelSlideListener(){

            @Override
            public void onPanelClosed(View arg0) {
                CardModel model = mCards.get(mCardsIndex);
                mAnswer.setText(mCards.get(mCardsIndex).getAnswer());
            }

            @Override
            public void onPanelOpened(View arg0) { }

            @Override
            public void onPanelSlide(View arg0, float arg1) { }

        };
        mSlidingPanel.setPanelSlideListener(panelListener);


        // color SlidePane based on Subject Theme Color
        View roundedCornersTop = view.findViewById(R.id.roundedCornersTop);
        GradientDrawable roundedCornersTopBackground = (GradientDrawable) roundedCornersTop.getBackground();
        roundedCornersTopBackground.setColor(mSubjectModel.getColorInt());

        TransparentSemicircleView transparentSemicircle = (TransparentSemicircleView) view.findViewById(R.id.transparentSemicircle);
        transparentSemicircle.setDrawBackgroundColor(mSubjectModel.getColorInt());

        View roundedCornersBottom = view.findViewById(R.id.roundedCornersBottom);
        GradientDrawable roundedCornersBottomBackground = (GradientDrawable) roundedCornersBottom.getBackground();
        roundedCornersBottomBackground.setColor(mSubjectModel.getColorInt());

        LinearLayout slideContent = (LinearLayout) view.findViewById(R.id.slideContent);
        slideContent.setBackgroundColor(mSubjectModel.getColorInt());

        mAnswer = (TextView) view.findViewById(R.id.textAnswer);
        mQuestion = (TextView) view.findViewById(R.id.textQuestion);
        if (!ThemeColor.isWhiteContrastColor(mSubjectModel.getColorInt()))
            mQuestion.setTextColor(ContextCompat.getColor(getContext(), R.color.textColorPrimary));


        mCardCount = (TextView) view.findViewById(R.id.textCardCount);
        mCardCountSpacer = (TextView) view.findViewById(R.id.textCardCountSpacer);

        LinearLayout buttonCorrect = (LinearLayout) view.findViewById(R.id.buttonThumbUp);
        buttonCorrect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // save the Quiz to the database
                saveQuizCorrectAnswer();

                // update the individual Card accuracy
                updateCardAccuracy(mCards.get(mCardsIndex), 1);

                mCardsIndex++;

                if (mCardsIndex == mCards.size())
                    showResults();
                else {
                    mSlidingPanel.closePane();
                    bindView();
                }
            }
        });

        LinearLayout buttonIncorrect = (LinearLayout) view.findViewById(R.id.buttonThumbDown);
        buttonIncorrect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // if missed the first question -- go ahead and save the initial quiz
                if (mQuizModel.getId() == 0)
                    mQuizModel = mQuizModel.save();

                // update the individual Card accuracy
                updateCardAccuracy(mCards.get(mCardsIndex), 0);

                mCardsIndex++;

                if (mCardsIndex == mCards.size())
                    showResults();
                else {
                    mSlidingPanel.closePane();
                    bindView();
                }
            }
        });

        mContentResults = (LinearLayout) view.findViewById(R.id.contentResults);

        mResultsNumCorrect = (TextView) view.findViewById(R.id.textNumCorrect);
        mResultsTotalCards = (TextView) view.findViewById(R.id.textTotalCards);
        mResultsAccuracy = (TextView) view.findViewById(R.id.textAccuracy);
        mResultsDate = (TextView) view.findViewById(R.id.textDate);

        // color button background using primary color
        ColorStateList csl = new ColorStateList(new int[][]{new int[0]}, new int[]{0xff9C27B0});
        Button buttonClose = (Button) view.findViewById(R.id.buttonClose);
        ((AppCompatButton)buttonClose).setSupportBackgroundTintList(csl);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        mNoRecords = (TextView) view.findViewById(R.id.textNoRecords);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CARD_LOADER, null, this).forceLoad();
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Called when a new Loader needs to be created.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // query Cards for the given parent Deck
        String selection = CardEntry.COLUMN_DECK_ID + " = ?";
        String[] selectionArgs = new String[] {
            mDeckModel.getId().toString()
        };

        return new CursorLoader(getActivity(),
                CardEntry.CONTENT_URI,
                CARD_COLUMNS,
                selection,
                selectionArgs,
                "RANDOM()");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        while (data.moveToNext()) {
            CardModel model = new CardModel(data.getLong(0), data.getLong(1), data.getString(2), data.getString(3), data.getString(4), data.getInt(5));
            mCards.add(model);
        }

        data.close();

        if (data.getCount() == 0) {
            mAnswerContent.setVisibility(View.GONE);
            mSlidingPanel.setVisibility(View.GONE);
            mNoRecords.setVisibility(View.VISIBLE);
        }
        else {
            mNoRecords.setVisibility(View.GONE);

            mQuizModel = new QuizModel(mDeckModel.getId());
            mQuizModel.setTotalCards(data.getCount());

            bindView();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCards.clear();
    }

    private void bindView() {
        if (mCardsIndex < mCards.size()) {
            CardModel model = mCards.get(mCardsIndex);
            mQuestion.setText(model.getQuestion());
            mAnswer.setText(mCardsIndex == 0 ? model.getAnswer() : "");

            mCardCount.setText(String.format(getString(R.string.card_count), mCardsIndex + 1, mCards.size()));
            mCardCountSpacer.setText(String.format(getString(R.string.card_count_spacer), mCardsIndex + 1, mCards.size()));
        }
    }

    private void showResults() {
        mSlidingPanel.setVisibility(View.GONE);
        mNoRecords.setVisibility(View.GONE);

        Animation out = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out);
        mAnswerContent.startAnimation(out);
        mAnswerContent.setVisibility(View.INVISIBLE);

        Animation in = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
        mContentResults.startAnimation(in);
        mContentResults.setVisibility(View.VISIBLE);

        mResultsNumCorrect.setText(String.valueOf(mQuizModel.getNumCorrect()));
        mResultsTotalCards.setText(String.valueOf(mQuizModel.getTotalCards()));
        mResultsAccuracy.setText(String.valueOf(mQuizModel.getPercentCorrect()));
        mResultsDate.setText(Converters.dateToString(mQuizModel.getStartDate(), "E, MMM d, yyyy 'at' h:mm a"));
    }

    /**
     * Saves the Quiz to the database.
     */
    private void saveQuizCorrectAnswer() {
        mQuizModel.setNumCorrect(mQuizModel.getNumCorrect() + 1);

        int percentage = (int)(((double)mQuizModel.getNumCorrect()/(double)mQuizModel.getTotalCards()) * 100);
        mQuizModel.setPercentCorrect(percentage);

        mQuizModel = mQuizModel.save();
    }

    /**
     * Update the individual Card accuracy.
     */
    private void updateCardAccuracy(CardModel cardModel, int score) {
        cardModel.updateQuizScore(score);
        cardModel.save();
    }
}
