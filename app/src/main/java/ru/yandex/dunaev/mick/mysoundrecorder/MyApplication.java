package ru.yandex.dunaev.mick.mysoundrecorder;

import android.app.Application;
import android.os.Environment;

import java.io.File;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        File folder = new File(Environment.getExternalStorageDirectory() + "/MySoundRecorder");
        if (!folder.exists()) {
            folder.mkdir();
        }

        super.onCreate();
    }
}
