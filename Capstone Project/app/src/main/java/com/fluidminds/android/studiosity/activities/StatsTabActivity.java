package com.fluidminds.android.studiosity.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.adapters.StatsPagerAdapter;
import com.fluidminds.android.studiosity.models.DeckModel;
import com.fluidminds.android.studiosity.models.SubjectModel;
import com.fluidminds.android.studiosity.utils.ThemeColor;

/**
 * An activity to display statistics about Quiz history.
 */
public class StatsTabActivity extends BaseActivity {

    private SubjectModel mSubjectModel;
    private DeckModel mDeckModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stats_tab);

        Intent intent = getIntent();
        mSubjectModel = intent.getParcelableExtra("subjectmodel");
        mDeckModel = intent.getParcelableExtra("deckmodel");

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            getSupportActionBar().setTitle(mDeckModel.getName());
        }

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new StatsPagerAdapter(getSupportFragmentManager()));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        // Color toolbar based on model Theme Color
        if(mSubjectModel != null) {
            tabLayout.setBackgroundColor(mSubjectModel.getColorInt());

            // Determine appropriate contrast color for the tab color
            if (!ThemeColor.isWhiteContrastColor(mSubjectModel.getColorInt()))
                tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.tabSelectedTextColor), ContextCompat.getColor(this, R.color.textColorPrimary));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_stats_list, menu);
        mMenu = menu;

        // color toolbar based on model Theme Color
        if(mSubjectModel != null)
            colorizeToolbar(mSubjectModel.getColorInt());

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
