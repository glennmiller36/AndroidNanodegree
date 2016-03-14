package com.fluidminds.android.studiosity.fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fluidminds.android.studiosity.BR;
import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.activities.BaseActivity;
import com.fluidminds.android.studiosity.eventbus.CardChangedEvent;
import com.fluidminds.android.studiosity.models.CardModel;
import com.fluidminds.android.studiosity.models.SubjectModel;
import com.fluidminds.android.studiosity.viewmodels.CardViewModel;

import org.greenrobot.eventbus.EventBus;

/**
 * A fragment to Add or Edit an individual Card.
 */
public class CardEditFragment extends Fragment {

    private ViewDataBinding mBinding;
    private CardViewModel mViewModel;

    public CardViewModel getViewModel() { return mViewModel; }

    public CardEditFragment() {
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

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_card_edit, container, false);

        // mViewModel not null on orientation change
        if (mViewModel == null) {
            Intent intent = getActivity().getIntent();
            CardModel model = intent.getParcelableExtra("cardmodel");

            mViewModel = new CardViewModel(model);
        }

        mBinding.setVariable(BR.viewModel, mViewModel);
        mBinding.setVariable(BR.handler, this);

        // Inflate the layout for this fragment
        return mBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // color toolbar based on model Theme Color
        SubjectModel subjectModel = getActivity().getIntent().getParcelableExtra("subjectmodel");
        if(subjectModel != null)
            ((BaseActivity) getActivity()).colorizeToolbar(subjectModel.getColorInt());
    }

    /**
     * Called from the Activity when user clicks Done button.
     */
    public CardModel save() {
        CardModel model = mViewModel.getModel().save();
        if (model != null) {
            // Post the event
            EventBus bus = EventBus.getDefault();
            bus.post(new CardChangedEvent(mViewModel.getModel()));
        }

        return model;
    }
}
