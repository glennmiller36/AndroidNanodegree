package com.fluidminds.android.studiosity.fragments;

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
import com.fluidminds.android.studiosity.activities.CardListActivity;
import com.fluidminds.android.studiosity.activities.DeckEditActivity;
import com.fluidminds.android.studiosity.activities.QuizActivity;
import com.fluidminds.android.studiosity.activities.StatsTabActivity;
import com.fluidminds.android.studiosity.activities.StudyListActivity;
import com.fluidminds.android.studiosity.adapters.DeckListAdapter;
import com.fluidminds.android.studiosity.data.DataContract.DeckEntry;
import com.fluidminds.android.studiosity.eventbus.SubjectChangedEvent;
import com.fluidminds.android.studiosity.models.DeckModel;
import com.fluidminds.android.studiosity.models.SubjectModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * A fragment to display Card Decks for the requested Subject.
 */
public class DeckListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private DeckListAdapter mDeckAdapter;

    protected EventBus mEventBus = EventBus.getDefault();

    private RecyclerView mRecyclerDecks;
    private TextView mNoRecords;

    private SubjectModel mSubjectModel;

    private static final int DECK_LOADER = 0;

    private static final String[] DECK_COLUMNS = {
        DeckEntry.TABLE_NAME + "." + DeckEntry._ID,
        DeckEntry.COLUMN_SUBJECT_ID,
        DeckEntry.COLUMN_NAME
    };

    // These indices are tied to DECK_COLUMNS.
    public static final int COL_ID = 0;
    public static final int COL_NAME = 1;

    public DeckListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mDeckAdapter = new DeckListAdapter();

        Intent intent = getActivity().getIntent();
        mSubjectModel = intent.getParcelableExtra("subjectmodel");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_deck_list, container, false);

        // Get a reference to the RecyclerView, and attach this adapter to it.
        mRecyclerDecks = (RecyclerView) view.findViewById(R.id.recyclerDecks);
        mRecyclerDecks.setHasFixedSize(true);
        mRecyclerDecks.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mRecyclerDecks.setAdapter(mDeckAdapter);

        mDeckAdapter.setOnItemClickListener(new DeckListAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = new Intent(getContext(), CardListActivity.class);

                DeckModel model = mDeckAdapter.get(position);
                intent.putExtra("subjectmodel", mSubjectModel);
                intent.putExtra("deckmodel", model);

                startActivity(intent);
            }

            @Override
            public void onStudyClick(int position, View v) {
                Intent intent = new Intent(getContext(), StudyListActivity.class);

                DeckModel model = mDeckAdapter.get(position);
                intent.putExtra("subjectmodel", mSubjectModel);
                intent.putExtra("deckmodel", model);

                startActivity(intent);
            }

            @Override
            public void onQuizClick(int position, View v) {
                Intent intent = new Intent(getContext(), QuizActivity.class);

                DeckModel model = mDeckAdapter.get(position);
                intent.putExtra("subjectmodel", mSubjectModel);
                intent.putExtra("deckmodel", model);

                startActivity(intent);
            }

            @Override
            public void onStatsClick(int position, View v) {
                Intent intent = new Intent(getContext(), StatsTabActivity.class);

                DeckModel model = mDeckAdapter.get(position);
                intent.putExtra("subjectmodel", mSubjectModel);
                intent.putExtra("deckmodel", model);

                startActivity(intent);
            }
        });

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
        getLoaderManager().initLoader(DECK_LOADER, null, this).forceLoad();
        super.onActivityCreated(savedInstanceState);

        // Register as a subscriber
        mEventBus.register(this);
    }

    @Override
    public void onDestroy() {
        // Unregister
        mEventBus.unregister(this);
        super.onDestroy();
    }

    /**
     * Called when a new Loader needs to be created.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Sort order:  Ascending by name.
        String sortOrder = DeckEntry.COLUMN_NAME + " COLLATE NOCASE ASC";

        // query decks for the given parent Subject
        String selection = DeckEntry.COLUMN_SUBJECT_ID + " = ?";
        String[] selectionArgs = new String[] {
            mSubjectModel.getId().toString()
        };

        return new CursorLoader(getActivity(),
                DeckEntry.CONTENT_URI,
                DECK_COLUMNS,
                selection,
                selectionArgs,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ArrayList<DeckModel> decks = new ArrayList<>();
        while (data.moveToNext()) {
            DeckModel model = new DeckModel(data.getLong(0), data.getLong(1), data.getString(2));
            decks.add(model);
        }

        mDeckAdapter.swapData(decks);

        mNoRecords.setVisibility(data.getCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mDeckAdapter.swapData(null);
    }

    @Subscribe
    public void onEvent(SubjectChangedEvent event){
        mSubjectModel = event.getModel();
    }
}
