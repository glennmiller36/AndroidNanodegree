package com.fluidminds.android.studiosity.fragments;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fluidminds.android.studiosity.BR;
import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.data.DataContract;
import com.fluidminds.android.studiosity.eventbus.ThemeColorChangedEvent;
import com.fluidminds.android.studiosity.models.SubjectModel;
import com.fluidminds.android.studiosity.viewmodels.SubjectViewModel;

import org.greenrobot.eventbus.EventBus;

/**
 * A fragment to Add or Edit an individual school Subject.
 */
public class SubjectEditFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, ColorPickerDialogFragment.OnColorPickerDialogListener {

    private ViewDataBinding mBinding;
    private SubjectViewModel mViewModel;

    public SubjectEditFragment() {
        // Required empty public constructor
    }

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // retain this fragment
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_subject_edit, container, false);

        // mViewModel not null on orientation change
        if (mViewModel == null) {
            mViewModel = new SubjectViewModel(new SubjectModel());
        }

        mBinding.setVariable(BR.viewModel, mViewModel);

        // Inflate the layout for this fragment
        View view = mBinding.getRoot();

        final Fragment targetFragment = this;

        LinearLayout button = (LinearLayout) view.findViewById(R.id.themeColor);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();

                ColorPickerDialogFragment dialogFragment = new ColorPickerDialogFragment();
                dialogFragment.setTargetFragment(targetFragment, 0);
                dialogFragment.setInitialColor(mViewModel.getModel().getColorInt());
                dialogFragment.show(fm, "SubjectEditFragment");
            }
        });

        return view;
    }

    /**
     * Called when a new Loader needs to be created.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        // Define the columns to retrieve
        String[] projectionFields = new String[] {
                DataContract.SubjectEntry.TABLE_NAME + "." + DataContract.SubjectEntry._ID,
                DataContract.SubjectEntry.COLUMN_SUBJECT,
                DataContract.SubjectEntry.COLUMN_COLOR
        };

        return new CursorLoader(getActivity(),
                DataContract.SubjectEntry.buildItemUri(1),  // URI
                projectionFields,   // projection fields
                null,   // the selection criteria
                null,   // the selection args
                null    // the sort order
        );
    }

    /**
     * Fetch existing Subject record.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            data.moveToFirst();

            SubjectModel model = new SubjectModel(
                data.getLong(0),
                data.getString(1),
                data.getInt(2)
            );

            mViewModel.setModel(model);
            mBinding.invalidateAll(); // refresh View
        }
    }

    /**
     * This is called when the last Cursor provided to onLoadFinished()
     * above is about to be closed.  We need to make sure we are no
     * longer using it.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onColorSelected(int color) {
        mViewModel.setColorInt(color);

        // Post the event
        EventBus bus = EventBus.getDefault();
        bus.post(new ThemeColorChangedEvent(color));
    }
}
