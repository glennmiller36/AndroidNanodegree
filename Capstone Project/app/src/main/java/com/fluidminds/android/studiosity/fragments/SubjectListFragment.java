package com.fluidminds.android.studiosity.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.fluidminds.android.studiosity.adapters.MyAdapter;
import com.fluidminds.android.studiosity.R;

/**
 * A fragment representing the list of school Subjects.
 */
public class SubjectListFragment extends Fragment {

    public SubjectListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subject_list, container, false);

        GridView gridView = (GridView)view.findViewById(R.id.gridSubjects);
        final MyAdapter adapter = new MyAdapter(this.getActivity());
        gridView.setAdapter(adapter);

        gridView.setEmptyView(view.findViewById( R.id.textNoRecords ));

        return view;
    }
}
