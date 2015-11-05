package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.androidjokes.JokeActivity;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private Button mButton;
    private ProgressBar mSpinner;
    private String mJoke;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

       mButton =(Button)root.findViewById(R.id.button);

        mSpinner =(ProgressBar)root.findViewById(R.id.progressBar);
        mSpinner.setVisibility(View.GONE);

        return root;
    }

    /**
     * Called from MainActivity once the joke has been successfully retrieved from the Google Cloud endpoint
     */
    public void onJokeReady(String joke) {
        mJoke = joke;
        setSpinnerVisibility(View.GONE);

        startJokeActivity();
    }

    /**
     * Kick-off the intent to display the JokeActivity
     */
    private void startJokeActivity() {
        Intent intent = new Intent(getActivity(), JokeActivity.class);
        intent.putExtra("JOKE", mJoke);

        startActivity(intent);
    }

    public void setSpinnerVisibility(int value) {
        mButton.setVisibility(value == View.GONE ? View.VISIBLE : View.GONE);
        mSpinner.setVisibility(value);
    }

    @Override
    public void onPause() {
        super.onPause();

        setSpinnerVisibility(View.GONE);
    }
}