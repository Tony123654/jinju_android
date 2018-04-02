package com.jinju.android.util;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;

import com.jinju.android.widget.easyimage.EasyImage;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Libra on 2017/5/3.
 */

public class ImageSelectUtils {

    private static final int RC_CAMERA_PERM = 1;
    private static final int RC_STORAGE_PERM = 2;

    /**
     * 拍照获取图片
     *
     * @param activity
     */
    public static void doTakePhoto(Activity activity) {
        if (activity == null) {
            return;
        }
        if (EasyPermissions.hasPermissions(activity, Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)) {
            EasyImage.openCamera(activity);
        } else {
            // Ask for one permission
            ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},RC_CAMERA_PERM);
        }
    }
    /**
     * 从相册中获取图片
     */
    public static void getPickPhoto(Activity activity) {
        if (EasyPermissions.hasPermissions(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            EasyImage.openPic(activity);
        } else {
             //申请权限
            ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},RC_STORAGE_PERM);
        }
    }
}
