package ru.yandex.dunaev.mick.mysoundrecorder.models;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ScrollView;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

public class MyFileModel{
    private String file;
    private String filePath;
    private Context mContext;
    private String data = "";

    private String bitRate = "Unknown";
    private String sampleRate = "Unknown";
    private String mime = "Unknown";
    private String duration = "Unknown";
    private boolean isGenerated = false;

    private String dateCreated = "Unknown";
    private String sizeOfFile = "Unknown";
    private Date dateFile;

    public MyFileModel(Context context, String file, String filePath) {
        this.file = file;
        this.filePath = filePath;
        mContext = context;
        isGenerated = false;
    }

    public String getBitRate() {
        return bitRate;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getSizeOfFile() {
        return sizeOfFile;
    }

    public String getSampleRate() {
        return sampleRate;
    }

    public String getMime() {
        return mime;
    }

    public String getDuration() {
        return duration;
    }

    public String getFile() {
        return file;
    }

    public void generateMediaInfo(){
        if(isGenerated) return;
        MediaExtractor mex = new MediaExtractor();
        try {
            mex.setDataSource(filePath);// the adresss location of the sound on sdcard.
        } catch (IOException e) {
            return;
        }

        MediaFormat mf = mex.getTrackFormat(0);

        bitRate = getIntInfo(mf, MediaFormat.KEY_BIT_RATE);
        sampleRate = getIntInfo(mf, MediaFormat.KEY_SAMPLE_RATE);
        mime = getStringInfo(mf, MediaFormat.KEY_MIME);
        duration = getLongInfo(mf,MediaFormat.KEY_DURATION);
        duration = convertDuration(duration);

        File f = new File(filePath);
        sizeOfFile = String.format("%d KB",f.length() / 1024);
        Date lastModDate = new Date(f.lastModified());
        dateCreated = lastModDate.toString();
        dateFile = lastModDate;

        isGenerated = true;
    }

    public Date getDateFile() {
        return dateFile;
    }

    private String convertDuration(String duration){
        if(duration.isEmpty()) return duration;
        long d;
        try {
            d = Long.parseLong(duration);
        } catch (NumberFormatException e){
            return duration;
        }
        d /= 10000;
        long hours = d / 360000;
        d -= hours * 360000;
        long minutes = d / 6000;
        d -= minutes * 6000;
        float seconds = (float)d;
        seconds /= 100.0f;
        String ret = "";
        if(hours != 0) ret += String.format("%d:",hours);
        ret += String.format("%02d:%.2f",minutes,seconds);
        return ret;
    }

    private String getIntInfo(MediaFormat mf,String key) {
        String info = "Unknown";
        if (mf.containsKey(key)) {
            int v = mf.getInteger(key);
            info = String.format("%d", v);
        }
        return info;
    }

    private String getLongInfo(MediaFormat mf,String key) {
        String info = "Unknown";
        if (mf.containsKey(key)) {
            long v = mf.getLong(key);
            info = String.format("%d", v);
        }
        return info;
    }

    private String getStringInfo(MediaFormat mf, String key){
        String info = "Unknown";
        if (mf.containsKey(key)) {
            info = mf.getString(key);
        }
        return info;
    }
}
