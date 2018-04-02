package com.jinju.android.share;



import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

import com.jinju.android.R;
import com.jinju.android.api.JsShareInfo;
import com.jinju.android.util.AppUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * Created by Libra on 2017/5/11.
 *
 * jsBaridge调用 分享
 */

public class ShareSDK {

    private static JsShareInfo mJsShareInfo;

    private Context mContext;
    /**
     * QQ分享
     */
    public ShareSDK(Context context,ShareListener shareListener) {
        mContext = context;
        mShareListener = shareListener;
    }

    public void createQQShare(FragmentActivity activity, JsShareInfo mJsShareInfo) {
        if (TextUtils.isEmpty(mJsShareInfo.getLinkUrl())) {
            AppUtils.showToast(activity, R.string.share_failed);
            return;
        } else {
            initShare(activity,mJsShareInfo,SHARE_MEDIA.QQ);
        }
    }
    public  void createQQShare(Activity activity, JsShareInfo mJsShareInfo) {
        if (TextUtils.isEmpty(mJsShareInfo.getLinkUrl())) {
            AppUtils.showToast(activity, R.string.share_failed);
            return;
        } else {
            initShare(activity,mJsShareInfo,SHARE_MEDIA.QQ);
        }
    }
    /**
     * 微信分享
     */
    public void createWeixinShare(Activity activity,JsShareInfo mJsShareInfo) {
        if (TextUtils.isEmpty(mJsShareInfo.getLinkUrl())) {
            AppUtils.showToast(activity, R.string.share_failed);
            return;
        } else {
            initShare(activity, mJsShareInfo, SHARE_MEDIA.WEIXIN);
        }

    }
    public void createWeixinShare(FragmentActivity activity,JsShareInfo mJsShareInfo) {
        if (TextUtils.isEmpty(mJsShareInfo.getLinkUrl())) {
            AppUtils.showToast(activity, R.string.share_failed);
            return;
        } else {
            initShare(activity, mJsShareInfo, SHARE_MEDIA.WEIXIN);
        }

    }
    /**
     * 朋友圈分享
     */
    public void createWeixinCircle(Activity activity,JsShareInfo mJsShareInfo) {
        if (TextUtils.isEmpty(mJsShareInfo.getLinkUrl())) {
            AppUtils.showToast(activity, R.string.share_failed);
            return;
        } else {
            initShare(activity,mJsShareInfo,SHARE_MEDIA.WEIXIN_CIRCLE);
        }

    }
    public void createWeixinCircle(FragmentActivity activity,JsShareInfo mJsShareInfo) {
        if (TextUtils.isEmpty(mJsShareInfo.getLinkUrl())) {
            AppUtils.showToast(activity, R.string.share_failed);
            return;
        } else {
            initShare(activity,mJsShareInfo,SHARE_MEDIA.WEIXIN_CIRCLE);
        }

    }

    private  UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
            mShareListener.onStart();
        }
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat","platform"+platform);
            AppUtils.showToast(mContext,R.string.share_completed);
            mShareListener.onSuccess();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {

            AppUtils.showToast(mContext,R.string.share_failed);

            mShareListener.onError();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            AppUtils.showToast(mContext," 分享取消");
            mShareListener.onCancel();
        }
    };

    /**
     * 分享内容  链接
     * @param jsShareInfo
     * @param platform
     */
    private void initShare(Activity activity,JsShareInfo jsShareInfo,SHARE_MEDIA platform) {
//      UMImage image = new UMImage(mContext, "imageurl");//网络图片
//      UMImage image = new UMImage(ShareActivity.this, file);//本地文件
//      UMImage image = new UMImage(mActivity, bitmap);//bitmap文件
//      UMImage image = new UMImage(mActivity, byte[]);//字节流
//      UMImage image = new UMImage(mActivity, R.mipmap.ic_launcher);//资源文件

        mJsShareInfo =  jsShareInfo;

        String linkUrl = mJsShareInfo.getLinkUrl();
        String  imgUrl = mJsShareInfo.getImgUrl();

        UMWeb web = new UMWeb(linkUrl);//分享链接
        if (!TextUtils.isEmpty(imgUrl)) {
            UMImage thumb = new UMImage(activity,imgUrl);//资源文件
            thumb.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
            thumb.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享
            //压缩格式设置：
            //用户分享透明背景的图片可以设置这种方式，但是qq好友，微信朋友圈，不支持透明背景图片，会变成黑色
            thumb.compressFormat = Bitmap.CompressFormat.PNG;
            web.setThumb(thumb);  //缩略图
        }

        web.setTitle(mJsShareInfo.getTitle());//标题
        web.setDescription(mJsShareInfo.getDesc());//描述
        new ShareAction(activity).setPlatform(platform)
                .withMedia(web)
                .setCallback(umShareListener)
                .share();
    }
    private void initShare(FragmentActivity activity,JsShareInfo jsShareInfo,SHARE_MEDIA platform) {
//      UMImage image = new UMImage(mContext, "imageurl");//网络图片
//      UMImage image = new UMImage(ShareActivity.this, file);//本地文件
//      UMImage image = new UMImage(mActivity, bitmap);//bitmap文件
//      UMImage image = new UMImage(mActivity, byte[]);//字节流
//      UMImage image = new UMImage(mActivity, R.mipmap.ic_launcher);//资源文件

        mJsShareInfo =  jsShareInfo;

        String linkUrl = mJsShareInfo.getLinkUrl();
        String  imgUrl = mJsShareInfo.getImgUrl();

        UMWeb web = new UMWeb(linkUrl);//分享链接
        if (!TextUtils.isEmpty(imgUrl)) {
            UMImage thumb = new UMImage(activity,imgUrl);//资源文件
            thumb.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
            thumb.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享
            //压缩格式设置：
            //用户分享透明背景的图片可以设置这种方式，但是qq好友，微信朋友圈，不支持透明背景图片，会变成黑色
            thumb.compressFormat = Bitmap.CompressFormat.PNG;
            web.setThumb(thumb);  //缩略图
        }

        web.setTitle(mJsShareInfo.getTitle());//标题
        web.setDescription(mJsShareInfo.getDesc());//描述
        new ShareAction(activity).setPlatform(platform)
                .withMedia(web)
                .setCallback(umShareListener)
                .share();
    }
    public interface ShareListener {
        void onStart();
        void onSuccess();
        void onError();
        void onCancel();
    }
    private ShareListener mShareListener;


}
