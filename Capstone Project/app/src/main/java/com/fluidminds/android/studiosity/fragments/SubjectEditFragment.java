package com.fluidminds.android.studiosity.fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fluidminds.android.studiosity.BR;
import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.activities.BaseActivity;
import com.fluidminds.android.studiosity.eventbus.SubjectChangedEvent;
import com.fluidminds.android.studiosity.models.SubjectModel;
import com.fluidminds.android.studiosity.viewmodels.SubjectViewModel;

import org.greenrobot.eventbus.EventBus;

/**
 * A fragment to Add or Edit an individual school Subject.
 */
public class SubjectEditFragment extends Fragment implements ColorPickerDialogFragment.OnColorPickerDialogListener, DataBindingHandler {

    private ViewDataBinding mBinding;
    private SubjectViewModel mViewModel;

    public SubjectEditFragment() {
        // Required empty public constructor
    }

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This fragment really doesn't have a fragment menu but it's an
        // opportunity to color the toolbar after the Activity menu is loaded
        setHasOptionsMenu(true);

        // retain this fragment
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_subject_edit, container, false);

        // mViewModel not null on orientation change
        if (mViewModel == null) {
            Intent intent = getActivity().getIntent();

            SubjectModel model = intent.getParcelableExtra("subjectmodel");

            mViewModel = new SubjectViewModel(model);
        }

        mBinding.setVariable(BR.viewModel, mViewModel);
        mBinding.setVariable(BR.handler, this);

        // Inflate the layout for this fragment
        View view = mBinding.getRoot();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // color toolbar based on model Theme Color
        if(mViewModel != null)
            ((BaseActivity) getActivity()).colorizeToolbar(mViewModel.getModel().getColorInt());
    }

    /**
     * Data binding listeners.
     */
    @Override
    public void onClick(View view) {
        FragmentManager fm = getFragmentManager();

        ColorPickerDialogFragment dialogFragment = new ColorPickerDialogFragment();
        dialogFragment.setTargetFragment(this, 0);
        dialogFragment.setInitialColor(mViewModel.getModel().getColorInt());
        dialogFragment.show(fm, "SubjectEditFragment");
    }

    /**
     * Called from the Activity when user clicks Done button.
     */
    public SubjectModel save() {
        SubjectModel model = mViewModel.getModel().save();
        if (model != null) {
            // Post the event
            EventBus bus = EventBus.getDefault();
            bus.post(new SubjectChangedEvent(mViewModel.getModel()));
        }

        return model;
    }

    /**
     * ColorPickerDialogFragment.OnColorPickerDialogListener
     */
    @Override
    public void onColorSelected(int color) {
        mViewModel.getModel().setColorInt(color);

        // color toolbar based on model Theme Color
        ((BaseActivity) getActivity()).colorizeToolbar(color);
    }
}
