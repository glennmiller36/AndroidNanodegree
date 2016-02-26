package com.fluidminds.android.studiosity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.fragments.SubjectEditFragment;
import com.fluidminds.android.studiosity.models.SubjectModel;

/**
 * An activity to Add or Edit an individual school Subject.
 */
public class SubjectEditActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_subject_edit);

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

        final SubjectEditActivity that = this;

        LinearLayout buttonSave = (LinearLayout) findViewById(R.id.buttonDone);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SubjectEditFragment editFragment = (SubjectEditFragment)getSupportFragmentManager().findFragmentById(R.id.fragSubjectEdit);
                if(null != editFragment && editFragment.isInLayout())
                {
                    SubjectModel model = editFragment.save();
                    if (model != null) {
                        if (model.getIsNew()) {
                            Intent intent = new Intent(that, SubjectCardsActivity.class);
                            intent.putExtra("subjectmodel", model);

                            startActivity(intent);
                        }
                        else {
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
