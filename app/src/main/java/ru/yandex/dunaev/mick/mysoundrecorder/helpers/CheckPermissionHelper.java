package ru.yandex.dunaev.mick.mysoundrecorder.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.tfb.fbtoast.FBToast;

import java.util.ArrayList;
import java.util.List;

import ru.yandex.dunaev.mick.mysoundrecorder.controllers.MyRecordController;

public class CheckPermissionHelper {

    public static final int REQUEST_PERMISION_CODE = 6688;
    public static final int REQUEST_PERMISION_CODE_DATA = 6689;

    public static boolean checkPermission(Activity activity, String[] permissions){
        return checkPermission(activity,permissions,REQUEST_PERMISION_CODE);
    }

    public static boolean checkPermission(Activity activity, String[] permissions, int requestPermissionCode){
        List<String> requestPermissions = new ArrayList<>();

        //проверить все что нужно на то что уже есть
        for(String permission: permissions){
            if(ContextCompat.checkSelfPermission(activity,permission) != PackageManager.PERMISSION_GRANTED){
                requestPermissions.add(permission);
            }
        }

        //если что то надо то запросить это
        if(!requestPermissions.isEmpty()){
            ActivityCompat.requestPermissions(activity,requestPermissions.toArray(new String[requestPermissions.size()]),requestPermissionCode);
        } else return true;

        //на данный момент разрешений нету, ждем callback от activity
        return false;
    }

    private static boolean simpleCheckPermission(Activity activity, String[] permissions) {
        //еще раз проверить разрешения
        for(String permission: permissions){
            if(ContextCompat.checkSelfPermission(activity,permission) != PackageManager.PERMISSION_GRANTED){
                return false;//чего то нету
            }
        }
        return true;//разрешения выданы
    }

    public static void onPermissionGranted(Activity activity, String[] permissions) {
        if(simpleCheckPermission(activity,permissions)) MyRecordController.onPermissionGranted();
        else FBToast.errorToast(activity,"Permission failed",FBToast.LENGTH_SHORT);
    }

    public static void onPermissionGrantedData(Activity activity, String[] permissions) {
        if(simpleCheckPermission(activity,permissions)) MyRecordController.onPermissionGrantedData();
        else FBToast.errorToast(activity,"Permission failed",FBToast.LENGTH_SHORT);
    }
}
