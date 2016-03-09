package com.fluidminds.android.studiosity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.fragments.DeckEditFragment;
import com.fluidminds.android.studiosity.models.DeckModel;

/**
 * An activity to Add or Edit an individual Deck of Cards.
 */
public class DeckEditActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_deck_edit);

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

        final DeckEditActivity that = this;

        LinearLayout buttonSave = (LinearLayout) findViewById(R.id.buttonDone);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DeckEditFragment editFragment = (DeckEditFragment) getSupportFragmentManager().findFragmentById(R.id.fragDeckEdit);
                if (null != editFragment && editFragment.isInLayout()) {
                    DeckModel model = editFragment.save();
                    if (model != null) {
                        if (model.getIsNew()) {
                            Intent intent = new Intent(that, CardListActivity.class);
                            intent.putExtra("subjectmodel", getIntent().getParcelableExtra("subjectmodel"));
                            intent.putExtra("deckmodel", model);

                            startActivity(intent);
                        } else {
                            finish();
                        }
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
