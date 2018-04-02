package com.jinju.android.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.Info;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.AuthStatus;
import com.jinju.android.constant.UmengTouchType;
import com.jinju.android.dialog.CustomRoundDialog;
import com.jinju.android.manager.CommonManager;
import com.jinju.android.manager.UserManager;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.ImageUtils;
import com.jinju.android.widget.CircleImageView;
import com.umeng.analytics.MobclickAgent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InfoActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout mLayoutAvatar;
    private CircleImageView mImgAvatar;
    private LinearLayout mBtnSetNickname;
    private RelativeLayout mLayoutModifyPhone;
    private RelativeLayout mLayoutVerifyName;

    private TextView mInfoMemberid;
    private TextView mInfoRealName;
    private TextView mInfoIdcard;
    private TextView mInfoName;
    private TextView mTxtPhone;
    private TextView mTxtVerifyName;

    private Info mInfo;
    private Dialog mProgressDialog;
    private ImageView mIconSetNickname;
    
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;
    private static final int PHOTO_REQUEST_GALLERY = 2;
    private static final int PHOTO_REQUEST_CROP = 3;
    
    private File tempFile;
    private String tempFilepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        RelativeLayout btnBack = (RelativeLayout) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);
        TextView txtTitle = (TextView) findViewById(R.id.txt_title);
        txtTitle.setText(R.string.profile_info);

        mLayoutAvatar = (LinearLayout) findViewById(R.id.layout_avatar);
        mLayoutAvatar.setOnClickListener(this);

        mImgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
        
        mBtnSetNickname = (LinearLayout) findViewById(R.id.btn_set_nickname);
        mBtnSetNickname.setOnClickListener(this);

        mLayoutVerifyName = (RelativeLayout) findViewById(R.id.layout_verify_name);
        mLayoutVerifyName.setOnClickListener(mLayoutVerifyNameOnClickListener);
        mTxtVerifyName = (TextView) findViewById(R.id.txt_verify_name);

        mLayoutModifyPhone = (RelativeLayout) findViewById(R.id.layout_modify_phone);
        mLayoutModifyPhone.setOnClickListener(mLayoutModifyPhoneOnClickListener);
        mTxtPhone = (TextView) findViewById(R.id.txt_phone);

        mInfoMemberid = (TextView) findViewById(R.id.txt_info_memberid);
        mInfoRealName = (TextView) findViewById(R.id.txt_info_real_name);
        mInfoIdcard = (TextView) findViewById(R.id.txt_info_idcard);

        mInfoName = (TextView) findViewById(R.id.txt_info_nickname);
        mIconSetNickname = (ImageView) findViewById(R.id.icon_set_nickname);
        tempFilepath = Environment.getExternalStorageState() + "/temp.jpg";
        tempFile = new File(Environment.getExternalStorageDirectory(), "temp.jpg");

        mProgressDialog = AppUtils.createLoadingDialog(this);

    }

    @Override
    protected void onResume() {
        if (tempFile == null) {
            tempFile = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
        }
        getInfoDetail();
        super.onResume();

    }

    public void getInfoDetail() {
        mProgressDialog.show();
        DdApplication.getUserManager().getInfo(mOnGetInfoFinishedListener);
    }

    private UserManager.OnGetInfoFinishedListener mOnGetInfoFinishedListener = new UserManager.OnGetInfoFinishedListener() {
        @Override
        public void onGetInfoFinished(Response response, Info info) {
            if (response.isSuccess()) {
                mInfo = info;
                //ImageUtils.displayImage(mImgAvatar, info.getHeadImg());
                mInfoName.setText(info.getNick().equals("") ? getString(R.string.operate_not_set) : info.getNick());
                mIconSetNickname.setVisibility(info.getNick().equals("") ? View.VISIBLE : View.GONE);
                mBtnSetNickname.setClickable(info.getNick().equals("") ? true : false);
                mInfoMemberid.setText(info.getMemberId() + "");
                mInfoRealName.setText(info.getName());
                mInfoIdcard.setText(info.getIdCardNumber());
                mTxtPhone.setText(info.getMobile());

                if(mInfo.getAuthStatus() == AuthStatus.AUTHED) {
                    mTxtVerifyName.setText(R.string.verified);
                    mLayoutVerifyName.setClickable(false);
                } else if(mInfo.getAuthStatus() == AuthStatus.NO_AUTH) {
                    mTxtVerifyName.setText(R.string.nonverify);
                    mTxtVerifyName.setTextColor(getResources().getColor(R.color.main_blue));
                }
            } else {
                AppUtils.handleErrorResponse(InfoActivity.this, response);
            }
            mProgressDialog.dismiss();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_avatar:

                MobclickAgent.onEvent(InfoActivity.this, UmengTouchType.RP107_1);
                //头像替换
//                showChangeAvatarChoice();
                break;
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_set_nickname:
                MobclickAgent.onEvent(InfoActivity.this, UmengTouchType.RP107_2);

                if (TextUtils.isEmpty(mInfo.getNick())) {
                    Intent intent = new Intent(InfoActivity.this, EditNicknameActivity.class);
                    startActivity(intent);
                } else {
                    AppUtils.showToast(InfoActivity.this, R.string.nickname_exist_tip);
                }
                break;

        }
    }

    /**
     * 修改手机号
     */

    private View.OnClickListener mLayoutModifyPhoneOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            final CustomRoundDialog customRoundDialog = new CustomRoundDialog(InfoActivity.this, 2);
            customRoundDialog.setMessageTitle(R.string.change_phonenum_title);
            customRoundDialog.setContent(R.string.change_phonenum_content);
            customRoundDialog.setPositiveButton(R.string.confirm, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(InfoActivity.this, ModifyPhoneActivity.class);
                    intent.putExtra("mobile", mInfo.getMobile());
                    startActivity(intent);
                    customRoundDialog.dismiss();
                }
            });
            customRoundDialog.setNegativeButton(R.string.cancel, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customRoundDialog.dismiss();
                }
            });
            customRoundDialog.show();
        }

    };
    /**
     *
     *   实名认证
     */
    private View.OnClickListener mLayoutVerifyNameOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
//            if(mSetting.getAuthStatus() == AuthStatus.AUTHED) {
//                return;
//            } else if(mSetting.getAuthStatus() == AuthStatus.NO_AUTH) {
//                MobclickAgent.onEvent(InfoActivity.this, UmengTouchType.RP106_2);
//                Intent intent = new Intent(InfoActivity.this, VerifyBankCardActivity.class);
//                startActivity(intent);
//            }
        }

    };
    private void showChangeAvatarChoice() {
        final Dialog dialog = new Dialog(InfoActivity.this, R.style.custom_animation_dialog);
        dialog.setContentView(R.layout.dialog_select_photo);
        View.OnClickListener mDialogListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_image_capture:
                        //拍照
                        Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                        startActivityForResult(takePhoto, PHOTO_REQUEST_TAKEPHOTO);
                        break;
                    case R.id.btn_photo_library:
                        //图片库
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                        break;
                    case R.id.btn_cancel:
                        break;
                }

                dialog.dismiss();
            }
        };
        dialog.findViewById(R.id.btn_image_capture).setOnClickListener(mDialogListener);
        dialog.findViewById(R.id.btn_photo_library).setOnClickListener(mDialogListener);
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(mDialogListener);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case PHOTO_REQUEST_TAKEPHOTO:
                    readPictureDegree(tempFilepath);
                    Uri uri = Uri.fromFile(tempFile);
                    startToZoom(uri);
                    break;
                case PHOTO_REQUEST_GALLERY:
                    if (data != null) {
                        startToZoom(data.getData());
                    }
                    break;
                case PHOTO_REQUEST_CROP:
                    if (data != null) {
                        readPictureDegree(tempFilepath);
                        startToNext(data);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startToNext(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            Bitmap bitmap = bundle.getParcelable("data");
            if (bitmap != null) {
                updateAvatar(compressImage(bitmap));
            }
        }

    }

    private void updateAvatar(Bitmap bitmap) {
        try {
            FileOutputStream fos = new FileOutputStream(tempFile);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)) {
                fos.flush();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bitmap bm = null;
        try {
            bm = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
            mImgAvatar.setImageBitmap(bm);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DdApplication.getCommonManager().uploadAvatar("headImg", tempFile, mOnUploadAvatarFinishedListener);
    }
    
    private Bitmap compressImage(Bitmap image) {

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
    
    private CommonManager.OnUploadAvatarFinishedListener mOnUploadAvatarFinishedListener=new CommonManager.OnUploadAvatarFinishedListener() {
        @Override
        public void onUploadAvatarFinished(Response response, String imgUrl) {
            if (response.isSuccess()) {
                ImageUtils.displayImage(mImgAvatar, imgUrl);

                tempFile.getAbsoluteFile().delete();
            } else {
                AppUtils.handleErrorResponse(InfoActivity.this, response);
                mProgressDialog.dismiss();
            }
        }
    };


    private void startToZoom(Uri uri) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(uri, "image/*");
        //自定义裁剪，输出图片位置
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        cropIntent.putExtra("crop", true);
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", 300);
        cropIntent.putExtra("outputY", 300);
        cropIntent.putExtra("return-data", true);
        cropIntent.putExtra("noFaceDetection", true);

        startActivityForResult(cropIntent, PHOTO_REQUEST_CROP);
    }
    public static int readPictureDegree(String path) {
        int degree  = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            Log.d("InfoActivity", "readPictureDegree: " + orientation);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
    public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }
    public String getPhotoName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date)+".jpg";
    }
}
