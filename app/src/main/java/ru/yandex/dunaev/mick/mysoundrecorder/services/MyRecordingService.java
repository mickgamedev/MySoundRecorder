package ru.yandex.dunaev.mick.mysoundrecorder.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import ru.yandex.dunaev.mick.mysoundrecorder.R;

public class MyRecordingService extends Service {

    private String mFileName = null;
    private String mFilePath = null;
    private MediaRecorder mRecorder = null;
    private long mStartingTimeMillis;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRecording();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mRecorder != null) {
            stopRecording();
        }
        super.onDestroy();
    }

    private void startRecording() {
        setFileNameAndPath();
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setOutputFile(mFilePath);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mRecorder.setAudioChannels(1);
        try {
            mRecorder.prepare();
            mRecorder.start();
            mStartingTimeMillis = System.currentTimeMillis();
        } catch (IOException e) {
            Log.e("MyRecordingService", "startRecording failed");
        }

    }

    private void stopRecording() {
        if(mRecorder == null) return;
        mRecorder.stop();
        mRecorder = null;
        Toast.makeText(this,"Record " + mFilePath + " saved",Toast.LENGTH_SHORT).show();
    }

    private void setFileNameAndPath() {
        int count = 0;
        File f;
        //перебрать все файлы в каталоге пока не найдется свободного номера :)
        do{
            count++;
            mFileName = getString(R.string.default_file_name)
                    + "_" + count + ".mp4";
            mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            mFilePath += "/MySoundRecorder/" + mFileName;

            f = new File(mFilePath);
        } while (f.exists() && !f.isDirectory());
    }

}
