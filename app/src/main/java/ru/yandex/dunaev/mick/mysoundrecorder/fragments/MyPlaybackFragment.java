package ru.yandex.dunaev.mick.mysoundrecorder.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.TextView;

import ru.yandex.dunaev.mick.mysoundrecorder.R;
import ru.yandex.dunaev.mick.mysoundrecorder.models.MyFileModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyPlaybackFragment extends DialogFragment {

    private static final String ARG_ITEM = "recording_item";
    private MyFileModel mMyFileModel;

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

        TextView fileNameView = (TextView)view.findViewById(R.id.text_file_name);
        fileNameView.setText(mMyFileModel.getFile());

        SeekBar seekBar = (SeekBar)view.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new MySeekListener());


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

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }
}
