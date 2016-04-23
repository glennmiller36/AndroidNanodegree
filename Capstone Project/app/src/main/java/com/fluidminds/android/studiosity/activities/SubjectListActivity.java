package com.fluidminds.android.studiosity.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.app.StudiosityApp;
import com.fluidminds.android.studiosity.data.DataContract;

/**
 * An activity representing the list of school Subjects.
 */
public class SubjectListActivity extends BaseActivity {

    public static final String STUDIOSITY_PREFERENCES = "StudiosityPrefs" ;
    public static final String SAMPLE_DATA_DELETED = "SampleDataDeleted" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_subject_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_subject_list, menu);
        mMenu = menu;

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        // hide Delete Sample Data option if the sample data has already been deleted
        SharedPreferences prefs = this.getSharedPreferences(STUDIOSITY_PREFERENCES, Context.MODE_PRIVATE);
        if (prefs.getBoolean(SAMPLE_DATA_DELETED, false)) {
            getSharedPreferences(STUDIOSITY_PREFERENCES, Context.MODE_PRIVATE);
            MenuItem item = menu.findItem(R.id.action_delete_sample);
            if (item != null)
                item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_sample:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.alert_delete_title)
                        .setMessage(getString(R.string.delete_sample_data))
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String error = "";
                                try {
                                    // delete only data where IsSampleData = true
                                    StudiosityApp.getInstance().getContentResolver().delete(DataContract.SubjectEntry.CONTENT_URI, DataContract.SubjectEntry.IS_SAMPLE_DATA + " = " + 1, null);

                                    // remember Sample Data has been deleted so the option is removed from the Menu
                                    Editor editor = getSharedPreferences(STUDIOSITY_PREFERENCES, Context.MODE_PRIVATE).edit();
                                    editor.putBoolean(SAMPLE_DATA_DELETED, true);
                                    editor.commit();
                                } catch (Exception e) {
                                    error = e.getMessage();
                                }

                                if (!error.isEmpty())
                                    Toast.makeText(SubjectListActivity.this, error, Toast.LENGTH_SHORT).show();

                                dialogInterface.dismiss();
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
}
