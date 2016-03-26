package com.fluidminds.android.studiosity.fragments;

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
import com.fluidminds.android.studiosity.data.DataContract;
import com.fluidminds.android.studiosity.models.CardModel;
import com.fluidminds.android.studiosity.utils.DividerItemDecoration;

import java.util.ArrayList;

/**
 * 'Trends' tab for the Stats Activity.
 */
public class StatsTrendTabFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private StatsTrendAdapter mTrendAdapter;

    private RecyclerView mRecyclerCards;

    private static final int CARD_LOADER = 0;

    private static final String[] CARD_COLUMNS = {
            DataContract.CardEntry.TABLE_NAME + "." + DataContract.CardEntry._ID,
            DataContract.CardEntry.COLUMN_DECK_ID,
            DataContract.CardEntry.COLUMN_QUESTION,
            DataContract.CardEntry.COLUMN_ANSWER
    };

    // These indices are tied to CARD_COLUMNS.
    public static final int COL_ID = 0;
    public static final int COL_QUESTION = 1;
    public static final int COL_ASNSWER = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mTrendAdapter = new StatsTrendAdapter();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stats_trend_tab, container, false);

        // Get a reference to the RecyclerView, and attach this adapter to it.
        mRecyclerCards = (RecyclerView) view.findViewById(R.id.recyclerTrends);
        mRecyclerCards.setHasFixedSize(true);
        mRecyclerCards.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mRecyclerCards.setAdapter(mTrendAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL);
        mRecyclerCards.addItemDecoration(itemDecoration);

        mRecyclerCards.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CARD_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Called when a new Loader needs to be created.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // query Cards for the given parent Deck
        String selection = DataContract.CardEntry.COLUMN_DECK_ID + " = ?";
        String[] selectionArgs = new String[] {
            "2"
        };

        return new CursorLoader(getActivity(),
                DataContract.CardEntry.CONTENT_URI,
                CARD_COLUMNS,
                selection,
                selectionArgs,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ArrayList<CardModel> cards = new ArrayList<>();
        while (data.moveToNext()) {
            CardModel model = new CardModel(data.getLong(0), data.getLong(1), data.getString(2), data.getString(3));
            cards.add(model);
        }

        mTrendAdapter.swapData(cards);
        getLoaderManager().destroyLoader(CARD_LOADER);

        //mNoRecords.setVisibility(data.getCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mTrendAdapter.swapData(null);
    }
}
