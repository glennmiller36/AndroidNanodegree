package com.glennmiller.spotifystreamer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import kaaes.spotify.webapi.android.models.Track;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlayerActivityFragment extends Fragment {

    private int _trackPosition;
    private TextView _artistNameText;
    private TextView _albumNameText;
    private ImageView _albumImageView;
    private TextView _songTitleText;
    private SeekBar _scrubBar;
    private TextView _elapsedTimeText;
    private TextView _songDurationText;
    private ImageButton _previousTrackButton;
    private ImageButton _playTrackButton;
    private ImageButton _pauseTrackButton;
    private ImageButton _nextTrackButton;
    private MediaPlayer _mediaPlayer;
    private Handler _updateScrubBarHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // retain this fragment when activity is re-initlized
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_player, container, false);

        _artistNameText = (TextView) view.findViewById(R.id.artistName);
        _albumNameText = (TextView) view.findViewById(R.id.albumName);
        _albumImageView = (ImageView) view.findViewById(R.id.albumImage);
        _songTitleText = (TextView) view.findViewById(R.id.songTitle);
        _scrubBar = (SeekBar) view.findViewById(R.id.scrubBar);
        _elapsedTimeText = (TextView) view.findViewById(R.id.elapsedTime);
        _songDurationText = (TextView) view.findViewById(R.id.songDuration);
        _previousTrackButton = (ImageButton) view.findViewById(R.id.previousButton);
        _playTrackButton = (ImageButton) view.findViewById(R.id.playButton);
        _pauseTrackButton = (ImageButton) view.findViewById(R.id.pauseButton);
        _nextTrackButton = (ImageButton) view.findViewById(R.id.nextButton);

        // get the passed-in track position
        Bundle bundle = getActivity().getIntent().getExtras();
        _trackPosition = bundle.getInt("TRACK_POSITION", 0);

        // setup scrubBar change
        _scrubBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                _elapsedTimeText.setText(String.format("%d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) progress), TimeUnit.MILLISECONDS.toSeconds((long) progress) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) progress))));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (_mediaPlayer.isPlaying()) {
                    _mediaPlayer.pause();

                    _playTrackButton.setVisibility(View.VISIBLE);
                    _pauseTrackButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                _mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        // setup onClick handlers
        _previousTrackButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (_mediaPlayer.isPlaying())
                    _mediaPlayer.pause();
                _trackPosition -= 1;
                prepareMediaPlayer();
            }
        });

        _playTrackButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                _playTrackButton.setVisibility(View.GONE);
                _pauseTrackButton.setVisibility(View.VISIBLE);

                if (!_mediaPlayer.isPlaying())
                    play();
            }
        });

        _pauseTrackButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (_mediaPlayer.isPlaying())
                    _mediaPlayer.pause();
                _playTrackButton.setVisibility(View.VISIBLE);
                _pauseTrackButton.setVisibility(View.GONE);
            }
        });

        _nextTrackButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (_mediaPlayer.isPlaying())
                    _mediaPlayer.pause();
                _trackPosition += 1;
                prepareMediaPlayer();
            }
        });

        // if configuration is changing - let the music live on
        if (_mediaPlayer == null) {
            _mediaPlayer = new MediaPlayer();
            _mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            _mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // song is done - reset
                    updateTrackInfo();
                }
            });

            prepareMediaPlayer();
        }
        else {
            updateTrackInfo();

            // fire update handler
            if (_mediaPlayer.isPlaying())
                play();
        }

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();

        // if configuration is changing - let the music live on
        if(!getActivity().isChangingConfigurations()) {

            if (_mediaPlayer.isPlaying())
                _mediaPlayer.stop();

            _updateScrubBarHandler = null;

            _mediaPlayer.release();
            _mediaPlayer = null;
        }
    }

    /*
     * Update the UI using the current track info.
     */
    private void updateTrackInfo() {
        Track currentTrack = SpotifyStreamerApp.arrayOfTracks.get(_trackPosition);

        _artistNameText.setText(currentTrack.artists.get(0).name);
        _albumNameText.setText(currentTrack.album.name);

        if (currentTrack.album.images.size() > 1)
            Picasso.with(getActivity().getApplicationContext()).load(currentTrack.album.images.get(1).url).into(_albumImageView);
        else
            Picasso.with(getActivity().getApplicationContext()).load(android.R.drawable.ic_menu_gallery).into(_albumImageView);

        _songTitleText.setText(currentTrack.name);

        _songDurationText.setText(R.string.previewSongDuration);

        // if configuration is changing - let the music live on
        if (!_mediaPlayer.isPlaying()) {
            _scrubBar.setProgress(0);
            _mediaPlayer.seekTo(0);

            _elapsedTimeText.setText(R.string.initialTrackPosition);

            _playTrackButton.setVisibility(View.VISIBLE);
            _pauseTrackButton.setVisibility(View.GONE);
        }
        else {
            final int duration = _mediaPlayer.getDuration();
            _scrubBar.setMax(duration);

            _playTrackButton.setVisibility(View.GONE);
            _pauseTrackButton.setVisibility(View.VISIBLE);
        }

        if (_trackPosition > 0) {
            _previousTrackButton.clearColorFilter();
            _previousTrackButton.setEnabled(true);
        }
        else {
            _previousTrackButton.setColorFilter(R.color.disabledButton);
            _previousTrackButton.setEnabled(false);
        }

        if (_trackPosition < SpotifyStreamerApp.arrayOfTracks.size() - 1)
        {
            _nextTrackButton.clearColorFilter();
            _nextTrackButton.setEnabled(true);
        } else {
            _nextTrackButton.setColorFilter(R.color.disabledButton);
            _nextTrackButton.setEnabled(false);
        }
    }

    private void prepareMediaPlayer() {

        disableUIDuringPrepareMediaPlayer(false);

       if (_mediaPlayer.isPlaying())
           _mediaPlayer.pause();

        updateTrackInfo();

        // prepare in background because it may take sometime
        new Thread(new Runnable() {
            @Override
            public void run() {

                Track currentTrack = SpotifyStreamerApp.arrayOfTracks.get(_trackPosition);

                try {
                    _mediaPlayer.reset();
                    _mediaPlayer.setDataSource(currentTrack.preview_url);
                } catch (IllegalArgumentException | IllegalStateException | IOException | SecurityException e) {
                    onError(e);
                }

                try {
                    _mediaPlayer.prepare();
                } catch (IllegalStateException | IOException e) {
                    onError(e);
                }

                final int duration = _mediaPlayer.getDuration();
                _scrubBar.setMax(duration);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        disableUIDuringPrepareMediaPlayer(true);
                    }
                });
            }
        }).start();
    }

    private void disableUIDuringPrepareMediaPlayer(boolean donePrepareing) {
        _scrubBar.setEnabled(donePrepareing);
        _previousTrackButton.setEnabled(donePrepareing);
        _playTrackButton.setEnabled(donePrepareing);
        _pauseTrackButton.setEnabled(donePrepareing);
        _nextTrackButton.setEnabled(donePrepareing);
    }

    /*
     * Play the selected track.
     */
    private void play() {
        if (!_mediaPlayer.isPlaying())
            _mediaPlayer.start();

        // update ScrubBar on the UI thread
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (_mediaPlayer != null) {
                    int timeElapsed = _mediaPlayer.getCurrentPosition();
                    if (timeElapsed > _scrubBar.getProgress()) {
                        _scrubBar.setProgress(timeElapsed);
                        _elapsedTimeText.setText(String.format("%d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed), TimeUnit.MILLISECONDS.toSeconds((long) timeElapsed) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed))));
                    }

                    // repeat again in 100 miliseconds
                    if (_mediaPlayer.isPlaying())
                        _updateScrubBarHandler.postDelayed(this, 100);
                }
            }
        });
    }

    /*
     * Common error handler.
     */
    private void onError(Exception e) {
        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
    }
}
