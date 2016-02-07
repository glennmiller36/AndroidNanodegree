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
 * A fragment to display Flashcards for the requested Subject.
 */
public class SubjectCardsFragment extends Fragment {

    public SubjectCardsFragment() {
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subject_cards, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {


        // If we have a saved state then we can restore it now
        if (savedInstanceState != null) {
            // orientation change
            //mCounter = savedInstanceState.getInt(STATE_COUNTER, 0);
        }
        else {
            //getLoaderManager().initLoader(0, null, this);
        }
        super.onActivityCreated(savedInstanceState);
    }
}
