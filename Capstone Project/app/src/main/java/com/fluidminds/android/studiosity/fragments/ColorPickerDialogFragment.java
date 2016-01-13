package com.fluidminds.android.studiosity.fragments;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.adapters.ColorPickerAdapter;

/**
 * A Color Picker dialog.
 */
public class ColorPickerDialogFragment extends DialogFragment implements GridView.OnItemClickListener {

    private ColorPickerAdapter mAdapter;
    private OnColorPickerDialogListener mCallback;

    public interface OnColorPickerDialogListener {
        public void onColorSelected(int color);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_colorpicker_dialog, container, false);
        getDialog().setTitle(R.string.select_color);

        // convert supported colors TypedArray to Integer[]
        TypedArray ta = getResources().obtainTypedArray(R.array.color500);
        Integer[] colors = new Integer[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            colors[i] = ta.getColor(i, 0);
        }
        ta.recycle();

        Integer intvalue = Color.parseColor("#009688");
        mAdapter = new ColorPickerAdapter(this.getActivity(),
                R.layout.grid_item_color, colors, intvalue);

        GridView gridView = (GridView) rootView.findViewById(R.id.gridColors);
        gridView.setAdapter( mAdapter);
        gridView.setOnItemClickListener(this);

        return rootView;
    }

    /**
     * On Color select.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OnColorPickerDialogListener callback = (OnColorPickerDialogListener) this.getTargetFragment();

        dismiss();
        callback.onColorSelected(mAdapter.getItem(position));
    }
}