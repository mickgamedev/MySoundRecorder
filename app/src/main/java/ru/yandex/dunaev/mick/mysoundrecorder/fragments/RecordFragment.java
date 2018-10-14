package ru.yandex.dunaev.mick.mysoundrecorder.fragments;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.yandex.dunaev.mick.mysoundrecorder.R;
import ru.yandex.dunaev.mick.mysoundrecorder.controllers.MyRecordController;
import ru.yandex.dunaev.mick.mysoundrecorder.helpers.CheckPermissionHelper;
import ru.yandex.dunaev.mick.mysoundrecorder.helpers.StringWaitHelper;
import ru.yandex.dunaev.mick.mysoundrecorder.services.MyRecordingService;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecordFragment extends Fragment {

    private MyRecordController mController = MyRecordController.getInstance();
    private StringWaitHelper stringHelper = null;
    private Intent intent = null;

    @BindView(R.id.chronometer) Chronometer mChronometer;
    @BindView(R.id.text_recording) TextView textRecording;
    @BindView(R.id.toogle_record_button) FloatingActionButton recordButton;

    private final String[] permissions = new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public RecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_record, container, false);
        ButterKnife.bind(this,v);

        stringHelper = new StringWaitHelper(container.getResources().getString(R.string.recording));

        setControllerListener();
        setChronometerListener();

        intent = new Intent(getActivity(), MyRecordingService.class);
        return v;
    }

    private void setChronometerListener() {
        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                textRecording.setText(stringHelper.toNext());
            }
        });
    }

    @OnClick(R.id.toogle_record_button)
    public void OnClickTogleRecordButton(View v){
        mController.pressToggleRecordButton();
    }

    private boolean checkPermission(){
        return CheckPermissionHelper.checkPermission(getActivity(),permissions);
    }

    private void setControllerListener(){
        mController.setListener(new MyRecordController.IRecordControlListener() {
            @Override
            public boolean onStart() {
                if(!checkPermission()) return false;
                Log.v("Record","start");
                mChronometer.setBase(SystemClock.elapsedRealtime());
                mChronometer.start();
                stringHelper.reset();
                textRecording.setVisibility(View.VISIBLE);
                recordButton.setImageResource(R.drawable.ic_stop_black_24dp);
                startService();
                return true;
            }

            @Override
            public void onStop() {
                Log.v("Record","stop");
                stopService();
                mChronometer.stop();
                textRecording.setVisibility(View.GONE);
                recordButton.setImageResource(R.drawable.ic_mic_none_black_24dp);
            }
        });
    }

    private void startService(){
        if(intent == null) return;

        File folder = new File(Environment.getExternalStorageDirectory() + "/MySoundRecorder");
        if (!folder.exists()) {
            folder.mkdir();
        }
        getActivity().startService(intent);
        //держать экран включеным
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void stopService(){
        if(intent == null) return;
        getActivity().stopService(intent);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
