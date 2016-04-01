package com.fluidminds.android.studiosity.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.adapters.StatsTrendAdapter;
import com.fluidminds.android.studiosity.data.DataContract.QuizEntry;
import com.fluidminds.android.studiosity.models.DeckModel;
import com.fluidminds.android.studiosity.models.QuizModel;
import com.fluidminds.android.studiosity.utils.Converters;

import java.util.ArrayList;

/**
 * Trends tab for the Stats Activity.
 */
public class StatsTrendTabFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private StatsTrendAdapter mTrendAdapter;

    private RecyclerView mRecyclerQuizzes;
    private LinearLayout mContent;
    private TextView mNoRecords;

    private DeckModel mDeckModel;

    private static final int QUIZ_LOADER = 0;

    private static final String[] QUIZ_COLUMNS = {
            QuizEntry.TABLE_NAME + "." + QuizEntry._ID,
            QuizEntry.COLUMN_DECK_ID,
            QuizEntry.COLUMN_START_DATE,
            QuizEntry.COLUMN_NUM_CORRECT,
            QuizEntry.COLUMN_TOTAL_CARDS,
            QuizEntry.COLUMN_PERCENT_CORRECT,
    };

    private TextView mAverageAccuracy;

    // Selected item details
    private TextView mSelectedNumCorrect;
    private TextView mSelectedTotalCards;
    private TextView mSelectedAccuracy;
    private TextView mSelectedDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mTrendAdapter = new StatsTrendAdapter();

        Intent intent = getActivity().getIntent();
        mDeckModel = intent.getParcelableExtra("deckmodel");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stats_trend_tab, container, false);

        mAverageAccuracy = (TextView) view.findViewById(R.id.textAverageAccuracy);

        mSelectedNumCorrect = (TextView) view.findViewById(R.id.textNumCorrect);
        mSelectedTotalCards = (TextView) view.findViewById(R.id.textTotalCards);
        mSelectedAccuracy = (TextView) view.findViewById(R.id.textAccuracy);
        mSelectedDate = (TextView) view.findViewById(R.id.textDate);

        // Get a reference to the RecyclerView, and attach this adapter to it.
        mRecyclerQuizzes = (RecyclerView) view.findViewById(R.id.recyclerTrends);
        mRecyclerQuizzes.setHasFixedSize(true);
        mRecyclerQuizzes.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mRecyclerQuizzes.setAdapter(mTrendAdapter);

        mTrendAdapter.setOnItemClickListener(new StatsTrendAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                mTrendAdapter.setActive(position);

                // Update the fields for the selected index
                onBindSelectedItem(position);
            }
        });

        mRecyclerQuizzes.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        mContent = (LinearLayout) view.findViewById(android.R.id.content);
        mNoRecords = (TextView) view.findViewById(R.id.textNoRecords);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(QUIZ_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Called when a new Loader needs to be created.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Sort order:  Ascending, by start_date.
        String sortOrder = QuizEntry.COLUMN_START_DATE;

        // query Quizzes for the given parent Deck
        String selection = QuizEntry.COLUMN_DECK_ID + " = ?";
        String[] selectionArgs = new String[] {
            mDeckModel.getId().toString()
        };

        return new CursorLoader(getActivity(),
                QuizEntry.CONTENT_URI,
                QUIZ_COLUMNS,
                selection,
                selectionArgs,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int sumNumCorrect = 0;
        int sumTotalCards = 0;

        ArrayList<QuizModel> quizzes = new ArrayList<>();
        while (data.moveToNext()) {
            QuizModel model = new QuizModel(data.getLong(0), data.getLong(1), Converters.stringToDate(data.getString(2)), data.getInt(3), data.getInt(4), data.getInt(5));
            quizzes.add(model);

            // sum values used to calculate average accuracy
            sumNumCorrect += model.getNumCorrect();
            sumTotalCards += model.getTotalCards();
        }

        mTrendAdapter.swapData(quizzes);
        getLoaderManager().destroyLoader(QUIZ_LOADER);

        // scroll to the most current Quiz
        int position = quizzes.size() - 1;
        mTrendAdapter.setActive(position);
        mRecyclerQuizzes.scrollToPosition(position);

        // calculate Average Accuracy
        String averageAccuracy = "0";
        if (sumTotalCards > 0) {
            int percentage = (int)(((double)sumNumCorrect/(double)sumTotalCards) * 100);
            averageAccuracy = String.valueOf(percentage);
        }
        mAverageAccuracy.setText(averageAccuracy);

        // Update the fields for the selected index
        onBindSelectedItem(position);

        mContent.setVisibility(data.getCount() > 0 ? View.VISIBLE : View.GONE);
        mNoRecords.setVisibility(data.getCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mTrendAdapter.swapData(null);
    }

    /**
     * Update the fields for the selected index
     */
    private void onBindSelectedItem(int position) {
        if (position > 0) {
            QuizModel quiz = mTrendAdapter.get(position);

            mSelectedNumCorrect.setText(String.valueOf(quiz.getNumCorrect()));
            mSelectedTotalCards.setText(String.valueOf(quiz.getTotalCards()));
            mSelectedAccuracy.setText(String.valueOf(quiz.getPercentCorrect()));
            mSelectedDate.setText(Converters.dateToString(quiz.getStartDate(), "E, MMM d, yyyy h:mm a"));
        }
    }
}
