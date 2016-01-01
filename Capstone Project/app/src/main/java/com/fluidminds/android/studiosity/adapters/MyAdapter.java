package com.fluidminds.android.studiosity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fluidminds.android.studiosity.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MillerMac on 12/29/15.
 */
public final class MyAdapter extends BaseAdapter {
    private final List<Item> mItems = new ArrayList<Item>();
    private final LayoutInflater mInflater;

    public MyAdapter(Context context) {
        mInflater = LayoutInflater.from(context);

        //mItems.add(new Item("Red",       R.drawable.ic_media_play));
        //mItems.add(new Item("Magenta",   R.drawable.ic_media_play));
        //mItems.add(new Item("Dark Gray", R.drawable.ic_media_play));
        //mItems.add(new Item("Gray",      R.drawable.ic_media_play));
        //mItems.add(new Item("Green",     R.drawable.ic_media_play));
        //mItems.add(new Item("Cyan",      R.drawable.ic_media_play));
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Item getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mItems.get(i).drawableId;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        ImageView picture;
        TextView name;

        if (v == null) {
            v = mInflater.inflate(R.layout.grid_item_subject, viewGroup, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
            v.setTag(R.id.text, v.findViewById(R.id.text));
        }

        picture = (ImageView) v.getTag(R.id.picture);
        name = (TextView) v.getTag(R.id.text);

        Item item = getItem(i);

        picture.setImageResource(item.drawableId);
        name.setText(item.name);

        return v;
    }

    private static class Item {
        public final String name;
        public final int drawableId;

        Item(String name, int drawableId) {
            this.name = name;
            this.drawableId = drawableId;
        }
    }
}

