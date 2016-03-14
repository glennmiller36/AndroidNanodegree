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
 * An activity to display Cards for the requested Drck.
 */
public class CardListActivity extends BaseActivity {

    private SubjectModel mSubjectModel;
    private DeckModel mDeckModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_card_list);

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
        getMenuInflater().inflate(R.menu.menu_card_list, menu);
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
            case android.R.id.edit:
                Intent intent = new Intent(this, DeckEditActivity.class);
                intent.putExtra("subjectmodel", mSubjectModel);
                intent.putExtra("deckmodel", mDeckModel);

                startActivity(intent);
                return true;
            case R.id.action_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.alert_delete_title)
                        .setMessage(String.format(getString(R.string.delete_deck), mDeckModel.getName()))
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String error = mDeckModel.delete();
                                if (error.isEmpty())
                                    finish();
                                else {
                                    Toast.makeText(CardListActivity.this, error, Toast.LENGTH_SHORT).show();
                                    dialogInterface.dismiss();
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.show();
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
