package com.fluidminds.android.studiosity.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.fragments.CardEditFragment;
import com.fluidminds.android.studiosity.models.CardModel;

/**
 * An activity to Add or Edit an individual Card.
 */
public class CardEditActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_card_edit);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        LinearLayout buttonCancel = (LinearLayout) findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        final CardEditActivity that = this;

        LinearLayout buttonSave = (LinearLayout) findViewById(R.id.buttonDone);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CardEditFragment editFragment = (CardEditFragment) getSupportFragmentManager().findFragmentById(R.id.fragCardEdit);
                if (null != editFragment && editFragment.isInLayout()) {
                    CardModel model = editFragment.save();
                    if (model != null) {
                        finish();
                    }
                }
            }
        });
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