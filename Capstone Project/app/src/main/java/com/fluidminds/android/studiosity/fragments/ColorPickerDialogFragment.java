package com.fluidminds.android.studiosity.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.adapters.ColorPickerAdapter;
import com.fluidminds.android.studiosity.utils.ThemeColor;

import java.util.LinkedHashMap;

/**
 * A Color Picker dialog.
 */
public class ColorPickerDialogFragment extends DialogFragment implements GridView.OnItemClickListener {

    private Integer mInitialColor;
    private ColorPickerAdapter mAdapter;

    public interface OnColorPickerDialogListener {
        public void onColorSelected(int color);
    }

    public void setInitialColor(Integer color) {
        mInitialColor = color;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // savedInstanceState not null on orientation change
        if (savedInstanceState != null)
            mInitialColor = savedInstanceState.getInt("InitialColor", 0);

        View rootView = inflater.inflate(R.layout.fragment_colorpicker_dialog, container, false);
        getDialog().setTitle(R.string.select_color);

        LinkedHashMap<Integer, String> colorList = ThemeColor.getColor500List();
        Integer[] colors = new Integer[colorList.size()];
        int i = 0;
        for (Integer key : colorList.keySet()) {
            colors[i] = key;
            i++;
        }

        mAdapter = new ColorPickerAdapter(this.getActivity(),
                R.layout.grid_item_color, colors, mInitialColor);

        GridView gridView = (GridView) rootView.findViewById(R.id.gridColors);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(this);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("InitialColor", mInitialColor);
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