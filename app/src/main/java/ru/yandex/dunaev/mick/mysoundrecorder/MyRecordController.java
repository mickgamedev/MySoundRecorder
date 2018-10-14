package ru.yandex.dunaev.mick.mysoundrecorder;

public class MyRecordController {
    private MyRecordController(){};
    private static MyRecordController mController;
    public static MyRecordController getInstance(){
        if(mController == null) mController = new MyRecordController();
        return mController;
    }

    public interface IRecordControlListener{
        public void onStart();
        public void onStop();
    }

    private boolean isRecord = false;
    private IRecordControlListener listener;
    private long seconds;

    public void pressToggleRecordButton(){
        isRecord = !isRecord;
        if (isRecord) {
            if(listener != null) listener.onStart();
            startRecord();
        }
        else {
            if(listener != null) listener.onStop();
            stopRecord();
        }
    }

    private void stopRecord() {

    }

    private void startRecord() {
        seconds = 0;
    }

    public void setListener(IRecordControlListener listener) {
        this.listener = listener;
    }
}
