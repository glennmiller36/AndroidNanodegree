package com.fluidminds.android.studiosity.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.adapters.SubjectAdapter;
import com.fluidminds.android.studiosity.data.DataContract;

/**
 * A fragment representing the list of school Subjects.
 */
public class SubjectListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private SubjectAdapter mSubjectAdapter;

    private GridView mGridSubjects;

    private static final int SUBJECT_LOADER = 0;

    private static final String[] SUBJECT_COLUMNS = {
            DataContract.SubjectEntry.TABLE_NAME + "." + DataContract.SubjectEntry._ID,
            DataContract.SubjectEntry.COLUMN_SUBJECT
    };

    // These indices are tied to SUBJECT_COLUMNS.
    static final int COL_ID = 0;
    public static final int COL_SUBJECT = 1;

    public SubjectListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mSubjectAdapter = new SubjectAdapter(getActivity(), null, 0);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subject_list, container, false);

        // Get a reference to the GridView, and attach this adapter to it.
        mGridSubjects = (GridView) view.findViewById(R.id.gridSubjects);
        mGridSubjects.setAdapter(mSubjectAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(SUBJECT_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Called when a new Loader needs to be created.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Sort order:  Ascending, by subject name.
        String sortOrder = DataContract.SubjectEntry.COLUMN_SUBJECT + " ASC";

        return new CursorLoader(getActivity(),
                DataContract.SubjectEntry.CONTENT_URI,
                SUBJECT_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mSubjectAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mSubjectAdapter.swapCursor(null);
    }
}
