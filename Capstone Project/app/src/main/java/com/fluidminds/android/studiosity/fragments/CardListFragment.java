package com.fluidminds.android.studiosity.fragments;

import android.content.Context;
import android.content.Intent;
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
import com.fluidminds.android.studiosity.activities.DeckEditActivity;
import com.fluidminds.android.studiosity.activities.DeckListActivity;
import com.fluidminds.android.studiosity.adapters.DeckListAdapter;
import com.fluidminds.android.studiosity.data.DataContract.DeckEntry;
import com.fluidminds.android.studiosity.models.DeckModel;
import com.fluidminds.android.studiosity.models.SubjectModel;
import com.fluidminds.android.studiosity.utils.Converters;

import java.util.ArrayList;

/**
 * A fragment to display Cards for the requested Deck.
 */
public class CardListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    //private DeckListAdapter mDeckAdapter;

    //private RecyclerView mRecyclerDecks;
    private TextView mNoRecords;

    private SubjectModel mSubjectModel;

    private static final int DECK_LOADER = 0;

    private static final String[] DECK_COLUMNS = {
        DeckEntry.TABLE_NAME + "." + DeckEntry._ID,
        DeckEntry.COLUMN_SUBJECT_ID,
        DeckEntry.COLUMN_NAME,
        DeckEntry.COLUMN_CREATE_DATE
    };

    // These indices are tied to DECK_COLUMNS.
    public static final int COL_ID = 0;
    public static final int COL_NAME = 1;
    public static final int COL_CREATE_DATE = 2;

    public CardListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //mDeckAdapter = new DeckListAdapter();

        Intent intent = getActivity().getIntent();
        mSubjectModel = intent.getParcelableExtra("subjectmodel");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card_list, container, false);

        // Get a reference to the RecyclerView, and attach this adapter to it.
//        mRecyclerDecks = (RecyclerView) view.findViewById(R.id.recyclerCards);
//        mRecyclerDecks.setHasFixedSize(true);
//        mRecyclerDecks.setLayoutManager(new LinearLayoutManager(this.getContext()));
//        mRecyclerDecks.setAdapter(mDeckAdapter);
//
//        mDeckAdapter.setOnItemClickListener(new DeckListAdapter.MyClickListener() {
//             @Override
//             public void onItemClick(int position, View v) {
//             Intent intent = new Intent(getContext(), DeckListActivity.class);
//
//             DeckModel model = mDeckAdapter.get(position);
//             intent.putExtra("deckmodel", model);
//
//             startActivity(intent);
//             }
//         });
//
        mNoRecords = (TextView) view.findViewById(R.id.textNoRecords);

        FloatingActionButton fabAdd = (FloatingActionButton) view.findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            Intent intent = new Intent(getContext(), DeckEditActivity.class);
            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // open activity without save into the stack
            intent.putExtra("subjectmodel", mSubjectModel);
            intent.putExtra("deckmodel", new DeckModel(mSubjectModel.getId()));
            startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //getLoaderManager().initLoader(DECK_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Called when a new Loader needs to be created.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Sort order:  Ascending, by create_date, name.
        String sortOrder = DeckEntry.COLUMN_CREATE_DATE + ", " + DeckEntry.COLUMN_NAME + " COLLATE NOCASE ASC";

        // query decks for the given parent Subject
        //String selection = DeckEntry.COLUMN_SUBJECT_ID + " = ?";
        //String[] selectionArgs = new String[] {
        //    mSubjectModel.getId().toString()
        //};

        return new CursorLoader(getActivity(),
                DeckEntry.CONTENT_URI,
                DECK_COLUMNS,
                null,//selection,
                null,//selectionArgs,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        ArrayList<DeckModel> decks = new ArrayList<>();
//        try {
//            while (data.moveToNext()) {
//                DeckModel model = new DeckModel(data.getLong(0), data.getLong(1), data.getString(2), Converters.stringToDate(data.getString(3)));
//                decks.add(model);
//            }
//        } finally {
//            data.close();
//        }
//
//        mDeckAdapter.swapData(decks);
//
//        mNoRecords.setVisibility(data.getCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //mDeckAdapter.swapData(null);
    }
}
