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
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private Button mButton;
    private ProgressBar mSpinner;
    private InterstitialAd mInterstitialAd;
    private String mJoke;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        AdView mAdView = (AdView) root.findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        mButton =(Button)root.findViewById(R.id.button);

        mSpinner =(ProgressBar)root.findViewById(R.id.progressBar);
        mSpinner.setVisibility(View.GONE);

        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                startJokeActivity();
            }
        });

        requestNewInterstitial();

        return root;
    }

    /**
     * Called from MainActivity once the joke has been successfully retrieved from the Google Cloud endpoint
     */
    public void onJokeReady(String joke) {
        mJoke = joke;
        setSpinnerVisibility(View.GONE);

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            startJokeActivity();
        }
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

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }
}
