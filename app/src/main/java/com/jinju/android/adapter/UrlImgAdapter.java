package com.jinju.android.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.allure.lbanners.LMBanners;
import com.allure.lbanners.adapter.LBaseAdapter;
import com.jinju.android.activity.MainActivity;
import com.jinju.android.api.Advert;
import com.jinju.android.constant.AdvertType;
import com.jinju.android.util.DDJRCmdUtils;
import com.jinju.android.util.ImageUtils;
import com.jinju.android.webview.BaseJsBridgeWebViewActivity;

/**
 * Created by Libra on 2017/8/23.
 */

public class UrlImgAdapter implements  LBaseAdapter<Advert> {
    private Context mContext;

    public UrlImgAdapter(Context context) {
        mContext=context;
    }



    @Override
    public View getView(final LMBanners lBanners, final Context context, int position,final Advert advert) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.item, null);
//        ImageView imageView = (ImageView) view.findViewById(R.id.id_image);
//        ImageLoader.getInstance().displayImage(data,imageView);

        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageUtils.displayImage(imageView,advert.getImageUrl());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String type = advert.getType();

                if (TextUtils.equals(type, AdvertType.URL)) {
                   String linkUrl=  advert.getLinkUrl();
                    if (!TextUtils.isEmpty(linkUrl)) {
                        if (linkUrl.startsWith(DDJRCmdUtils.DDJR_OVERRIDE_SCHEMA)) {
                            //点击webview上的控件，执行指定跳转
                            DDJRCmdUtils.handleProtocol((Activity)context,linkUrl);
                        } else {
                            Intent intent = new Intent(context, BaseJsBridgeWebViewActivity.class);
                            intent.putExtra("url", linkUrl);
                            context.startActivity(intent);
                        }

                    }
                } else if (TextUtils.equals(type, AdvertType.FINANCIAL)) {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("index", MainActivity.TAB_LOAN);
                    context.startActivity(intent);
                }
            }
        });
        return imageView;
    }
}
