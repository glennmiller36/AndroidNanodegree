package com.fluidminds.android.studiosity.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.eventbus.DeckChangedEvent;
import com.fluidminds.android.studiosity.models.DeckModel;
import com.fluidminds.android.studiosity.models.SubjectModel;

import org.greenrobot.eventbus.Subscribe;

/**
 * An activity to quiz Flash Cards.
 */
public class QuizActivity extends BaseActivity {

    private SubjectModel mSubjectModel;
    private DeckModel mDeckModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_quiz);

        Intent intent = getIntent();
        mSubjectModel = intent.getParcelableExtra("subjectmodel");
        mDeckModel = intent.getParcelableExtra("deckmodel");

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            getSupportActionBar().setTitle("");
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
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
        mMenu = menu;

        // color toolbar black to blend in with the Quiz slider
        colorizeToolbar(getResources().getColor(R.color.quizBackground_black));

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

    @Subscribe
    public void onEvent(DeckChangedEvent event){
        mDeckModel = event.getModel();

        getSupportActionBar().setTitle(mDeckModel.getName());
    }
}
