package com.jinju.android.share;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.jinju.android.R;
import com.jinju.android.api.ShareInfo;
import com.jinju.android.util.AppUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * Created by Libra on 2017/12/20.
 *  原生分享
 */

public class CommonShareSDK {
    private static Context mContext;
    private static ShareInfo mShareInfo;
    /**
     * QQ分享
     */

    public static void createQQShare(Context context,ShareInfo shareInfo) {
        if (TextUtils.isEmpty(shareInfo.getShareUrl())) {
            AppUtils.showToast(context, R.string.share_failed);
            return;
        } else {
            initShare(context,shareInfo, SHARE_MEDIA.QQ);
        }
    }
    /**
     * 微信
     */

    public static void createWeixinShare(Context context, ShareInfo shareInfo) {
        if (TextUtils.isEmpty(shareInfo.getShareUrl())) {
            AppUtils.showToast(context, R.string.share_failed);
            return;
        } else {
            initShare(context,shareInfo, SHARE_MEDIA.WEIXIN);
        }
    }
    /**
     * 朋友圈
     */

    public static void createWeixinCircle(Context context,ShareInfo shareInfo) {
        if (TextUtils.isEmpty(shareInfo.getShareUrl())) {
            AppUtils.showToast(context, R.string.share_failed);
            return;
        } else {
            initShare(context,shareInfo, SHARE_MEDIA.WEIXIN_CIRCLE);
        }
    }
    /**
     * 分享内容  链接
     * @param platform
     */
    private static void initShare(Context context, ShareInfo shareInfo, SHARE_MEDIA platform) {
//      UMImage image = new UMImage(mContext, "imageurl");//网络图片
//      UMImage image = new UMImage(ShareActivity.this, file);//本地文件
//      UMImage image = new UMImage(mActivity, bitmap);//bitmap文件
//      UMImage image = new UMImage(mActivity, byte[]);//字节流
//      UMImage image = new UMImage(mActivity, R.mipmap.ic_launcher);//资源文件
        mContext =  context;
        mShareInfo =  shareInfo;

        String linkUrl = mShareInfo.getShareUrl();
        String imgUrl = mShareInfo.getShareImg();
        UMWeb web = new UMWeb(linkUrl);//分享链接
        if (!TextUtils.isEmpty(imgUrl)) {
            UMImage thumb = new UMImage(mContext,imgUrl);//资源文件
            thumb.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
            thumb.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享
            //压缩格式设置：
            //用户分享透明背景的图片可以设置这种方式，但是qq好友，微信朋友圈，不支持透明背景图片，会变成黑色
            thumb.compressFormat = Bitmap.CompressFormat.PNG;
            web.setThumb(thumb);  //缩略图
        }

        web.setTitle(mShareInfo.getShareTitle());//标题
        web.setDescription(mShareInfo.getShareDesc());//描述
        new ShareAction((Activity)mContext).setPlatform(platform)
                .withMedia(web)
                .setCallback(umShareListener)
                .share();
    }
    private static UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat","platform"+platform);

            AppUtils.showToast(mContext,R.string.share_completed);

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {

            AppUtils.showToast(mContext,R.string.share_failed);
            if(t!=null){
                Log.d("throw","throw:"+t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            AppUtils.showToast(mContext," 分享取消");

        }
    };
}
