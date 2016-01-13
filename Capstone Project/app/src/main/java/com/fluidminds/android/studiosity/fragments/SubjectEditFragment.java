package com.fluidminds.android.studiosity.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.fluidminds.android.studiosity.R;

/**
 * A fragment to Add or Edit an individual school Subject.
 */
public class SubjectEditFragment extends Fragment implements ColorPickerDialogFragment.OnColorPickerDialogListener {

    public SubjectEditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subject_edit, container, false);

        final Fragment targetFragment = this;

        Button button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                ColorPickerDialogFragment dialogFragment = new ColorPickerDialogFragment();
                dialogFragment.setTargetFragment(targetFragment, 0);
                dialogFragment.show(fm, "SubjectEditFragment");
            }
        });

        ImageButton circle = (ImageButton) view.findViewById(R.id.circle);
        circle.setColorFilter(Color.argb(255, 255, 255, 255)); // White Tint
        circle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        return view;
    }

    @Override
    public void onColorSelected(int color) {
        Toast.makeText(this.getActivity(), String.valueOf(color), Toast.LENGTH_SHORT).show();
    }
}
