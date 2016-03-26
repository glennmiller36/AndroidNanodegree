package com.fluidminds.android.studiosity.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fluidminds.android.studiosity.fragments.StatsDetailTabFragment;
import com.fluidminds.android.studiosity.fragments.StatsTrendTabFragment;

/**
 * Page swipe adapter for the Stats Tab Layout.
 */
public class StatsPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Trends", "Details" };

    public StatsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new StatsTrendTabFragment();
            case 1:
                return new StatsDetailTabFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}