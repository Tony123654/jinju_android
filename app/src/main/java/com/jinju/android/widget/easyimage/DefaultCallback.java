package com.jinju.android.widget.easyimage;

import com.jinju.android.util.AppUtils;

import java.io.File;

/**
 * Created by Libra on 2017/5/4.
 */

public class DefaultCallback implements EasyImage.Callbacks{

    @Override
    public void onImagePicked(File imageFile) {

    }

    @Override
    public void onImagePickerError(Exception e) {
        AppUtils.showToast(null,"无法获取照片！");
    }

    @Override
    public void onCanceled() {

    }
}
