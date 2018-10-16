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

    public static void onPermissionGrantedData() {
        if(mController == null || mController.listener == null) return;
        mController.onUpdateData();
    }

    private void onUpdateData() {
        if(listener != null) listener.updateData();
    }

    public interface IRecordControlListener{
        boolean onStart();
        void onStop();
        void updateData();
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
