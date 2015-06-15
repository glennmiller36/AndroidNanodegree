package com.glennmiller.myappportfolio;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/*
 * Displays buttons to launch apps created during nanodegree studies.
 */
public class PortfolioActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_portfolio, menu);
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

    /*
     * Handle button clicks
     */
    public void onClickSpotifyStreamer(View view) {
        String launchMessage = getResources().getString(R.string.button_launch_message);
        String message = String.format(launchMessage, "spotify streamer");

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void onClickScoresApps(View view) {
        String launchMessage = getResources().getString(R.string.button_launch_message);
        String message = String.format(launchMessage, "scores app");

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void onClickLibraryApp(View view) {
        String launchMessage = getResources().getString(R.string.button_launch_message);
        String message = String.format(launchMessage, "library app");

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void onClickBuildItBigger(View view) {
        String launchMessage = getResources().getString(R.string.button_launch_message);
        String message = String.format(launchMessage, "build it bigger");

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void onClickXYZReader(View view) {
        String launchMessage = getResources().getString(R.string.button_launch_message);
        String message = String.format(launchMessage, "XYZ reader");

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void onClickMyOwnApp(View view) {
        String launchMessage = getResources().getString(R.string.button_launch_message);
        String message = String.format(launchMessage, "my capstone app");

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
