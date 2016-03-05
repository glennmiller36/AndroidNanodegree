package com.fluidminds.android.studiosity.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.adapters.DeckListAdapter;
import com.fluidminds.android.studiosity.data.DataContract.DeckEntry;

/**
 * A fragment to display Card Decks for the requested Subject.
 */
public class DeckListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private DeckListAdapter mDeckAdapter;

    private RecyclerView mRecyclerDecks;
    private TextView mNoRecords;

    private static final int DECK_LOADER = 0;

    private static final String[] DECK_COLUMNS = {
            DeckEntry.TABLE_NAME + "." + DeckEntry._ID,
            DeckEntry.COLUMN_NAME,
            DeckEntry.COLUMN_CREATE_DATE
    };

    // These indices are tied to DECK_COLUMNS.
    public static final int COL_ID = 0;
    public static final int COL_NAME = 1;
    public static final int COL_CREATE_DATE = 2;

    public DeckListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mDeckAdapter = new DeckListAdapter();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_deck_list, container, false);

        // Get a reference to the RecyclerView, and attach this adapter to it.

        mRecyclerDecks = (RecyclerView) view.findViewById(R.id.recyclerDecks);
        mRecyclerDecks.setHasFixedSize(true);
        mRecyclerDecks.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mRecyclerDecks.setAdapter(mDeckAdapter);
//        mListDecks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View v,
//                                    int position, long id) {
//                Intent intent = new Intent(getContext(), DeckListActivity.class);
//
//                //SubjectModel model = mDeckAdapter.get(position);
//                //intent.putExtra("subjectmodel", model);
//
//                startActivity(intent);
//            }
//        });

        mNoRecords = (TextView) view.findViewById(R.id.textNoRecords);

        FloatingActionButton fabAdd = (FloatingActionButton) view.findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Intent intent = new Intent(getContext(), SubjectEditActivity.class);
                //intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // open activity without save into the stack
                //intent.putExtra("subjectmodel", new SubjectModel());

                //startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DECK_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Called when a new Loader needs to be created.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Sort order:  Ascending, by create_date, name.
        String sortOrder = DeckEntry.COLUMN_CREATE_DATE + ", " + DeckEntry.COLUMN_NAME + " COLLATE NOCASE ASC";

        return new CursorLoader(getActivity(),
                DeckEntry.CONTENT_URI,
                DECK_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //mDeckAdapter.swapCursor(data);

        mNoRecords.setVisibility(data.getCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        /*mDeckAdapter.swapCursor(null);*/
    }
}
