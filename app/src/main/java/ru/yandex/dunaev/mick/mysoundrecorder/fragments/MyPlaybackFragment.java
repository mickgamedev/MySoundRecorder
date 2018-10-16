package ru.yandex.dunaev.mick.mysoundrecorder.fragments;


import android.app.Dialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.yandex.dunaev.mick.mysoundrecorder.R;
import ru.yandex.dunaev.mick.mysoundrecorder.models.MyFileModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyPlaybackFragment extends DialogFragment {

    private static final String ARG_ITEM = "recording_item";
    private MyFileModel mMyFileModel;
    private MediaPlayer mMediaPlayer = null;
    private Handler mHandler = new Handler();
    private boolean isPlaying = false;

    @BindView(R.id.text_file_name) TextView fileNameView;
    @BindView(R.id.seekBar) SeekBar mSeekBar;
    @BindView(R.id.text_current_progress) TextView mCurrentProgressTextView;
    @BindView(R.id.image_play_button) ImageView mPlayButton;


    public MyPlaybackFragment() {
        // Required empty public constructor
    }

    public static MyPlaybackFragment newInstance(MyFileModel fm) {
        MyPlaybackFragment pf = new MyPlaybackFragment();
        pf.setMyFileModel(fm);
        return pf;
    }

    public void setMyFileModel(MyFileModel myFileModel) {
        mMyFileModel = myFileModel;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_my_playback, null);

        ButterKnife.bind(this,view);

        fileNameView.setText(mMyFileModel.getFile());
        mSeekBar.setOnSeekBarChangeListener(new MySeekListener());


        builder.setView(view);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        //set transparent background
        //Window window = getDialog().getWindow();
        //window.setBackgroundDrawableResource(android.R.color.transparent);
        //disable buttons from dialog
        AlertDialog alertDialog = (AlertDialog) getDialog();
        alertDialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
        alertDialog.getButton(Dialog.BUTTON_NEGATIVE).setEnabled(false);
        alertDialog.getButton(Dialog.BUTTON_NEUTRAL).setEnabled(false);
    }

    private class MySeekListener implements SeekBar.OnSeekBarChangeListener{

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(mMediaPlayer != null && fromUser) {
                mMediaPlayer.seekTo(progress);
                mHandler.removeCallbacks(mRunnable);

                long minutes = TimeUnit.MILLISECONDS.toMinutes(mMediaPlayer.getCurrentPosition());
                long seconds = TimeUnit.MILLISECONDS.toSeconds(mMediaPlayer.getCurrentPosition())
                        - TimeUnit.MINUTES.toSeconds(minutes);
                mCurrentProgressTextView.setText(String.format("%02d:%02d", minutes,seconds));

                updateSeekBar();

            } else if (mMediaPlayer == null && fromUser) {
                prepareMediaPlayerFromPoint(progress);
                updateSeekBar();
            }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            if(mMediaPlayer != null) {
                // remove message Handler from updating progress bar
                mHandler.removeCallbacks(mRunnable);
            }
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (mMediaPlayer != null) {
                mHandler.removeCallbacks(mRunnable);
                mMediaPlayer.seekTo(seekBar.getProgress());

                long minutes = TimeUnit.MILLISECONDS.toMinutes(mMediaPlayer.getCurrentPosition());
                long seconds = TimeUnit.MILLISECONDS.toSeconds(mMediaPlayer.getCurrentPosition())
                        - TimeUnit.MINUTES.toSeconds(minutes);
                mCurrentProgressTextView.setText(String.format("%02d:%02d", minutes,seconds));
                updateSeekBar();
            }
        }
    }

    @OnClick(R.id.image_play_button)
    public void onClickPlay(View v){
        onPlay(isPlaying);
        isPlaying = !isPlaying;
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mMediaPlayer != null) {
            stopPlaying();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mMediaPlayer != null) {
            stopPlaying();
        }
    }

    // Play start/stop
    private void onPlay(boolean isPlaying){
        if (!isPlaying) {
            //currently MediaPlayer is not playing audio
            if(mMediaPlayer == null) {
                startPlaying(); //start from beginning
            } else {
                resumePlaying(); //resume the currently paused MediaPlayer
            }

        } else {
            //pause the MediaPlayer
            pausePlaying();
        }
    }

    private void startPlaying() {
        mPlayButton.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);
        mMediaPlayer = new MediaPlayer();

        try {
            mMediaPlayer.setDataSource(mMyFileModel.getFilePath());
            mMediaPlayer.prepare();
            mSeekBar.setMax(mMediaPlayer.getDuration());

            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });
        } catch (IOException e) {
            Log.e("MediaPlayer", "prepare() failed");
        }

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlaying();
            }
        });

        updateSeekBar();

        //keep screen on while playing audio
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void prepareMediaPlayerFromPoint(int progress) {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(mMyFileModel.getFilePath());
            mMediaPlayer.prepare();
            mSeekBar.setMax(mMediaPlayer.getDuration());
            mMediaPlayer.seekTo(progress);

            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlaying();
                }
            });

        } catch (IOException e) {
            Log.e("Media player", "prepare() failed");
        }
        //keep screen on while playing audio
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    private void pausePlaying() {
        mPlayButton.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
        mHandler.removeCallbacks(mRunnable);
        mMediaPlayer.pause();
    }

    private void resumePlaying() {
        mPlayButton.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);
        mHandler.removeCallbacks(mRunnable);
        mMediaPlayer.start();
        updateSeekBar();
    }

    private void stopPlaying() {
        mPlayButton.setImageResource(R.drawable.ic_stop_play_24dp);
        mHandler.removeCallbacks(mRunnable);
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
        mMediaPlayer = null;

        mSeekBar.setProgress(mSeekBar.getMax());
        isPlaying = !isPlaying;

        mSeekBar.setProgress(mSeekBar.getMax());

        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if(mMediaPlayer != null){

                int mCurrentPosition = mMediaPlayer.getCurrentPosition();
                mSeekBar.setProgress(mCurrentPosition);

                long minutes = TimeUnit.MILLISECONDS.toMinutes(mCurrentPosition);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(mCurrentPosition)
                        - TimeUnit.MINUTES.toSeconds(minutes);
                mCurrentProgressTextView.setText(String.format("%02d:%02d", minutes, seconds));

                updateSeekBar();
            }
        }
    };


    private void updateSeekBar() {
        mHandler.postDelayed(mRunnable, 1000);
    }


}
