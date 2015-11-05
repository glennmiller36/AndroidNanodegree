package com.udacity.gradle.builditbigger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity implements AsyncResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tellJoke(View view)
    {
        // Add a loading indicator that is shown while the joke is being retrieved
        MainActivityFragment mainFrag = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        mainFrag.setSpinnerVisibility(View.VISIBLE);

        // call Google Cloud to get a random joke
        new EndpointsAsyncTask(this.getApplicationContext(), this).execute();
    }

    /**
     * Return event from EndpointsAsyncTask
     */
    public void processFinish(String joke){

        // Hide loading indicator when the joke is ready.
        MainActivityFragment mainFrag = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        mainFrag.onJokeReady(joke);
    }
}
