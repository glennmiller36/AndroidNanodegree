package com.fluidminds.android.studiosity.fragments;

import android.content.Intent;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.data.DataContract.CardEntry;
import com.fluidminds.android.studiosity.models.CardModel;
import com.fluidminds.android.studiosity.models.DeckModel;
import com.fluidminds.android.studiosity.models.SubjectModel;
import com.fluidminds.android.studiosity.utils.ThemeColor;
import com.fluidminds.android.studiosity.views.TransparentSemicircleView;

import java.util.ArrayList;

/**
 * A fragment to quiz Flash Cards.
 */
public class QuizFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    ArrayList<CardModel> mCards = new ArrayList<>();
    private LinearLayout mAnswerContent;
    private SlidingPaneLayout mSlidingPane;
    private TextView mQuestion;
    private TextView mNoRecords;

    private SubjectModel mSubjectModel;
    private DeckModel mDeckModel;

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
        mSlidingPane = (SlidingPaneLayout) view.findViewById(R.id.slidingPanel);

        LinearLayout slideButton = (LinearLayout) view.findViewById(R.id.buttonSlide);
        slideButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mSlidingPane.isOpen())
                    mSlidingPane.closePane();
                else
                    mSlidingPane.openPane();
            }
        });

        // don't dim SlidePane while it's animating open
        mSlidingPane.setSliderFadeColor(Color.TRANSPARENT);

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

        mQuestion = (TextView) view.findViewById(R.id.textQuestion);
        if (!ThemeColor.isWhiteContrastColor(mSubjectModel.getColorInt()))
            mQuestion.setTextColor(ContextCompat.getColor(getContext(), R.color.textColorPrimary));

        LinearLayout buttonCorrect = (LinearLayout) view.findViewById(R.id.buttonThumbUp);
        buttonCorrect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getContext(), "Correct", Toast.LENGTH_SHORT).show();
            }
        });

        LinearLayout buttonIncorrect = (LinearLayout) view.findViewById(R.id.buttonThumbDown);
        buttonIncorrect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getContext(), "Incorrect", Toast.LENGTH_SHORT).show();
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

        if (data.getCount() == 0) {
            mAnswerContent.setVisibility(View.GONE);
            mSlidingPane.setVisibility(View.GONE);
            mNoRecords.setVisibility(View.VISIBLE);
        }
        else
            mNoRecords.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCards.clear();
    }
}
