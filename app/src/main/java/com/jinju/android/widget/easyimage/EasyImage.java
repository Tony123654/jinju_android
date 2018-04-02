package com.jinju.android.widget.easyimage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;

import com.jinju.android.util.AppUtils;
import com.jinju.android.util.SDPathUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;


/**
 * Created by Libra on 2017/5/3.
 */

public class EasyImage {


    private static final int PICK_PICTURE_REQUEST_CODE = 3;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    //拍照图片存放地址
    private static File tempFile = new File(SDPathUtils.getCachePath(), "temp.jpg");
    //裁剪后图片存放地址
    private static File mCropFile = new File(SDPathUtils.getCachePath(), "crop.jpg");
    //压缩图片存放地址
    private static File compressFile = new File(SDPathUtils.getCachePath(), "compress.jpg");

    public interface Callbacks {
        void onImagePickerError(Exception e);

        void onImagePicked(File imageFile);

        void onCanceled();
    }
    /**
     *   打开相册
     * @param activity
     *
     */
    public static void openPic(Activity activity) {

        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            activity.startActivityForResult(intent,PICK_PICTURE_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showToast(activity,"未能打开系统相册，请先确认是否已安装相册");
        }
    }
    public static void openPic(FragmentActivity activity) {

        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            activity.startActivityForResult(intent,PICK_PICTURE_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showToast(activity,"未能打开系统相册，请先确认是否已安装相册");
        }
    }

    public static void openCamera(Activity activity) {

            //适配安卓7.0拍照
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
            } else {
                Uri imageUri = FileProvider.getUriForFile(activity, "com.jinju.android.fileprovider",tempFile);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            }
            activity.startActivityForResult(intent, CAMERA_REQUEST_CODE);


    }
    public static void openCamera(FragmentActivity activity) {
        //拍照
        //适配安卓7.0
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        } else {
            //如果大于等于7.0使用FileProvider
            Uri imageUri = FileProvider.getUriForFile(activity, "com.jinju.android.fileprovider",tempFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }
        activity.startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    /**
     *  ActivityResult处理
     *
     */
    public static void handleActivityResult(int requestCode, int resultCode, Intent data, Activity activity,Callbacks callback) {

        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {
                case CAMERA_REQUEST_CODE://拍照后裁剪
                    //裁剪
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri inputUri = FileProvider.getUriForFile(activity, "com.jinju.android.fileprovider", tempFile);//通过FileProvider创建一个content类型的Uri
                        startPhotoZoom(activity,inputUri);//设置输入类型
                    } else {
                        Uri inputUri = Uri.fromFile(tempFile);
                        startPhotoZoom(activity,inputUri);
                    }

                    break;

                case PICK_PICTURE_REQUEST_CODE://相册裁剪

                    startPhotoZoom(activity,data.getData());

                    break;

                case RESULT_REQUEST_CODE: { //拍照后裁剪结果

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        Uri inputUri = FileProvider.getUriForFile(activity, "com.jinju.android.fileprovider", compressFile);//通过FileProvider创建一个content类型的Uri
                        callback.onImagePicked(mCropFile);
                    } else {
                        callback.onImagePicked(mCropFile);
                    }
                }
                break;

            }

        }
    }



    /**
     * 裁剪图片
     * @param activity
     * @param inputUri
     */
    public static void startPhotoZoom(Activity activity,Uri inputUri) {
        if (inputUri == null) {
            return;
        }

        Intent intent = new Intent("com.android.camera.action.CROP");
        //sdk>=24
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            Uri outPutUri = Uri.fromFile(mCropFile);
            intent.setDataAndType(inputUri, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
            intent.putExtra("noFaceDetection", false);//去除默认的人脸识别，否则和剪裁匡重叠
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        } else {
            Uri outPutUri = Uri.fromFile(mCropFile);
            if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                //这个方法是处理Android4.4以上图片返回的Uri对象不同的处理方法
                String url = GetImagePath.getPath(activity, inputUri);
                intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
            } else {
                intent.setDataAndType(inputUri, "image/*");
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
        }

        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());// 图片格式
        activity.startActivityForResult(intent,RESULT_REQUEST_CODE);//这里就将裁剪后的图片的Uri返回了
    }


    private static void startToNext(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            Bitmap bitmap = bundle.getParcelable("data");
            if (bitmap != null) {
                updateAvatar(compressImage(bitmap));
            }
        }
    }
    //压缩文件输出到指定文件
    private static void updateAvatar(Bitmap bitmap) {
        try {

            FileOutputStream fos = new FileOutputStream(compressFile);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)) {
                fos.flush();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //压缩图片
    private static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        while ( baos.toByteArray().length / 1024>100) {
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 10;
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }

}
