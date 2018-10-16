package ru.yandex.dunaev.mick.mysoundrecorder.helpers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

public class MyFileActions {
    public static boolean deleteFile(String filePath){
        File file = new File(filePath);
        return file.delete();
    }

    public static void shareFile(Context context, String filePath){
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
        intentShareFile.setType("audio/*");
        intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(intentShareFile, "Share audio record"));
    }
}
