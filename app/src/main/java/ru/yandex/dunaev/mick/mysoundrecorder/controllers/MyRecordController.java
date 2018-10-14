package ru.yandex.dunaev.mick.mysoundrecorder.controllers;

public class MyRecordController {
    private MyRecordController(){};
    private static MyRecordController mController;
    public static MyRecordController getInstance(){
        if(mController == null) mController = new MyRecordController();
        return mController;
    }

    public static void onPermissionGranted(){
        if(mController == null || mController.listener == null) return;
        mController.onStart();
    }

    public interface IRecordControlListener{
        public boolean onStart();
        public void onStop();
    }

    private boolean isRequestRecord = false;
    private boolean isRecord = false;

    private IRecordControlListener listener;

    public void pressToggleRecordButton(){
        isRequestRecord = !isRequestRecord;
        if (isRequestRecord) {
            onStart();
        }
        else {
            onStop();
        }
    }

    private void onStart(){
        isRequestRecord = true;
        if(!isRecord && listener != null) {
            if(!listener.onStart()) isRequestRecord = false;
            isRecord = isRequestRecord;
        }
    }

    private void onStop(){
        isRequestRecord = false;
        if(isRecord && listener != null){
            listener.onStop();
            isRecord = false;
        }
    }


    public void setListener(IRecordControlListener listener) {
        this.listener = listener;
    }
}
