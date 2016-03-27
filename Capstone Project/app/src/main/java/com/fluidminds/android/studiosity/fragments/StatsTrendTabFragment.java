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

import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.adapters.StatsTrendAdapter;
import com.fluidminds.android.studiosity.data.DataContract.QuizEntry;
import com.fluidminds.android.studiosity.models.DeckModel;
import com.fluidminds.android.studiosity.models.QuizModel;
import com.fluidminds.android.studiosity.utils.Converters;
import com.fluidminds.android.studiosity.utils.DividerItemDecoration;

import java.util.ArrayList;

/**
 * Trends tab for the Stats Activity.
 */
public class StatsTrendTabFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private StatsTrendAdapter mTrendAdapter;

    private RecyclerView mRecyclerQuizzes;

    private DeckModel mDeckModel;

    private static final int QUIZ_LOADER = 0;

    private static final String[] QUIZ_COLUMNS = {
            QuizEntry.TABLE_NAME + "." + QuizEntry._ID,
            QuizEntry.COLUMN_DECK_ID,
            QuizEntry.COLUMN_CREATE_DATE,
            QuizEntry.COLUMN_NUM_CORRECT,
            QuizEntry.COLUMN_TOTAL_CARDS,
            QuizEntry.COLUMN_PERCENT_CORRECT,
    };

    // These indices are tied to QUIZ_COLUMNS.
    public static final int COL_ID = 0;
    public static final int COL_DECK_ID = 1;
    public static final int COL_CREATE_DATE = 2;
    public static final int COL_NUM_CORRECT = 3;
    public static final int COL_TOTAL_CARDS = 4;
    public static final int COL_PERCENT_CORRECT = 5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mTrendAdapter = new StatsTrendAdapter();

        Intent intent = getActivity().getIntent();
        mDeckModel = intent.getParcelableExtra("deckmodel");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stats_trend_tab, container, false);

        // Get a reference to the RecyclerView, and attach this adapter to it.
        mRecyclerQuizzes = (RecyclerView) view.findViewById(R.id.recyclerTrends);
        mRecyclerQuizzes.setHasFixedSize(true);
        mRecyclerQuizzes.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mRecyclerQuizzes.setAdapter(mTrendAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL);
        mRecyclerQuizzes.addItemDecoration(itemDecoration);

        mRecyclerQuizzes.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

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
        // Sort order:  Ascending, by create_date.
        String sortOrder = QuizEntry.COLUMN_CREATE_DATE;

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
        ArrayList<QuizModel> quizzes = new ArrayList<>();
        while (data.moveToNext()) {
            QuizModel model = new QuizModel(data.getLong(0), data.getLong(1), Converters.stringToDate(data.getString(2)), data.getInt(3), data.getInt(4), data.getInt(5));
            quizzes.add(model);
        }

        mTrendAdapter.swapData(quizzes);
        getLoaderManager().destroyLoader(QUIZ_LOADER);

        //mNoRecords.setVisibility(data.getCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mTrendAdapter.swapData(null);
    }
}
