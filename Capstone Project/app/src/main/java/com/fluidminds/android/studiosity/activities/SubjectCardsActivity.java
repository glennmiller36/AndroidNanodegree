package com.fluidminds.android.studiosity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.eventbus.ThemeColorChangedEvent;
import com.fluidminds.android.studiosity.models.SubjectModel;

import org.greenrobot.eventbus.Subscribe;

/**
 * An activity to display Flashcards for the requested Subject.
 */
public class SubjectCardsActivity extends BaseActivity {

    private SubjectModel mSubjectModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_subject_cards);

        Intent intent = getIntent();
        mSubjectModel = intent.getParcelableExtra("subjectmodel");

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            getSupportActionBar().setTitle(mSubjectModel.getSubject());
        }

        // Register as a subscriber
        mEventBus.register(this);
    }

    @Override
    protected void onDestroy() {
        // Unregister
        mEventBus.unregister(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_subject_cards, menu);
        mMenu = menu;

        colorizeToolbar(getIntent().getIntExtra("color", 0));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case android.R.id.edit:
                Intent intent = new Intent(this, SubjectEditActivity.class);
                intent.putExtra("subjectmodel", mSubjectModel);

                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Subscribe
    public void onEvent(ThemeColorChangedEvent event){
        colorizeToolbar(event.getColor());
    }
}
