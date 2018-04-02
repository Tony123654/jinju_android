package com.jinju.android.activity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.About;
import com.jinju.android.api.Index;
import com.jinju.android.api.JsModalInfo;
import com.jinju.android.api.JsShareInfo;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.builder.JsModalInfoBuilder;
import com.jinju.android.builder.JsShareInfoBuilder;
import com.jinju.android.dialog.GetPicDialog;
import com.jinju.android.dialog.ModelDialog;
import com.jinju.android.dialog.ShareDialog;
import com.jinju.android.help.UIHelper;
import com.jinju.android.manager.CommonManager;
import com.jinju.android.manager.UserManager;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.NetworkUtil;
import com.jinju.android.util.SDPathUtils;
import com.jinju.android.util.StringUtils;
import com.jinju.android.util.Utils;
import com.jinju.android.webview.MyWebViewClient;
import com.jinju.android.widget.SlidingTabStrip;
import com.jinju.android.widget.easyimage.DefaultCallback;
import com.jinju.android.widget.easyimage.EasyImage;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.umeng.socialize.UMShareAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Libra on 2018/2/9.
 */

public class AboutActivity extends BaseActivity {
    public static final String TAG = "BridgeWebView" ;
    private SlidingTabStrip tabs;
    private List<About> mAboutList;
    private Map<String, String> headers;

    private static final int MODAL = 1;
    private static final int SHARE_DIALOG = 2;
    private static final int REQUEST_LOGIN = 100;
    private BridgeWebView mWebView;
    private String mUrl;
    private Boolean isAdvertView;//是否在启动页跳转到关于
    private Boolean isJsFirstPage = true;
    private Index mIndex;
    private TextView titleRight;
    private TextView txtFinish;
    private String mTriggerId;
    private ImageInfo mImageInfo;
    private JsShareInfo mShareInfo = null;
    private static JsModalInfo jsModalInfo;
    private boolean eventTouch = true;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MODAL:
                    if (eventTouch) {
                        eventTouch = false;
                        ModelDialog.showModelDialog(AboutActivity.this, jsModalInfo, new ModelDialog.DialogListener() {
                            @Override
                            public void dialogFinish(String value) {
                                if (!TextUtils.isEmpty(value)) {
                                    modalHandler(value);
                                }
                                //窗口消失之后，才可以继续点击（防止连续点击出现多个窗口）
                                eventTouch =true;
                            }
                        });
                    }
                    break;
                case SHARE_DIALOG:
                    ShareDialog mShareDialog = new ShareDialog();
                    mShareDialog.setShareListener(new ShareDialog.ShareResultListener() {
                        @Override
                        public void onShareStart() {

                        }

                        @Override
                        public void onShareSuccess() {
                            //分享成功的js回调
                            getShareBackHandler("success",mShareInfo.getTriggerId());
                        }

                        @Override
                        public void onShareError() {
                            //分享失败的js回调
                            getShareBackHandler("fail",mShareInfo.getTriggerId());
                        }

                        @Override
                        public void onShareCancel() {
                            getShareBackHandler("fail",mShareInfo.getTriggerId());
                        }
                    });
                    mShareDialog.showShareDialog(AboutActivity.this,mShareInfo);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Intent intent = getIntent();
        isAdvertView = intent.getBooleanExtra("advert",false);
        mIndex = (Index)intent.getSerializableExtra("index");
        initView();
        getAbout();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //图片上传的回调
        EasyImage.handleActivityResult(requestCode,resultCode,data,AboutActivity.this, new DefaultCallback(){
            @Override
            public void onImagePicked(File imageFile) {

                if (mImageInfo != null) {
                    try {
                        uploadImage(mImageInfo.clone(),imageFile);
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                        singleImageBack(null, null, mImageInfo.imgId);
                    }
                }
            }
        });

        if (resultCode == RESULT_OK && requestCode == REQUEST_LOGIN) {
            initUserInfo();
        }
        //分享回调 ，友盟提示说不可在Fragment中实现，可在fragment依赖的Activity中实现
        //如果不实现onActivityResult方法，会导致分享或回调无法正常进行
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
    //登录成功回调
    private void initUserInfo() {
        if (mWebView != null) {
            if (!mWebView.getSettings().getJavaScriptEnabled())
                mWebView.getSettings().setJavaScriptEnabled(true);
        }
        //用户登录后
        if (DdApplication.getConfigManager().isLogined()) {
            LoginHandler(mTriggerId);//登录成功后的调用
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //分享防止内存泄漏
        UMShareAPI.get(this).release();
    }

    private void initView(){
        Button btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(btnBackOnClickListener);
        TextView txtTitle = (TextView) findViewById(R.id.txt_title);
        txtTitle.setText("关于我们");

        titleRight = (TextView) findViewById(R.id.tv_title_right);
        txtFinish = (TextView)findViewById(R.id.txt_finish);
        txtFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tabs = (SlidingTabStrip) findViewById(R.id.tabs);


        mWebView = (BridgeWebView)findViewById(R.id.activity_web_wv);
        mWebView.setDefaultHandler(new DefaultHandler());
        mWebView.setWebViewClient( new MyWebViewClient(mWebView,this,null));
        //不使用缓存，只从网络获取数据.
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);

            }
        });
        mWebView.requestFocus();
        String token = DdApplication.getConfigManager().getToken();
        headers = new HashMap<String, String>();
        headers.put("Http-X-User-Access-Token", token);

        tabs.setOnTabSelectListener(mOnTabSelectListener);

        registerJavascriptBridgeHandler();
    }
    private View.OnClickListener btnBackOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackHandler();
        }
    };
    private SlidingTabStrip.OnTabSelectListener mOnTabSelectListener= new SlidingTabStrip.OnTabSelectListener() {
        @Override
        public void onTabSelect(int position) {
            mUrl = mAboutList.get(position).getLinkUrl();

            if (!TextUtils.isEmpty(mUrl)) {
                mWebView.loadUrl(mUrl,headers);
            }
        }
    };
    private void getAbout() {

        DdApplication.getUserManager().getAbout(mOnGetAboutFinishedListener);
    }

    private UserManager.OnGetAboutFinishedListener mOnGetAboutFinishedListener = new UserManager.OnGetAboutFinishedListener() {

        @Override
        public void OnGetAboutFinished(Response response, List<About> aboutList) {
            // TODO Auto-generated method stub
            if (response.isSuccess()) {
                mAboutList = aboutList;

                if (aboutList!=null&&aboutList.size()>0) {
                    mWebView.loadUrl(aboutList.get(0).getLinkUrl(),headers);
                }

                tabs.setTabContent(aboutList);

            } else {
                AppUtils.handleErrorResponse(AboutActivity.this, response);
            }
        }

    };

    /**
     * 注入js
     */
    private void registerJavascriptBridgeHandler() {
        // 拨打电话
        mWebView.registerHandler("getPhoneCall", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Log.d(TAG, "getPhoneCall data: " + data);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(data);
                    UIHelper.callPhoneWindow(AboutActivity.this, "拨打电话",
                            jsonObject.getString("phoneNum"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                function.onCallBack(null);
            }
        });

        /**
         *   传递用户Token
         */
        mWebView.registerHandler("getUserToken", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                Log.e(TAG, "getUserToken onCallBack: " + data);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(data);
                    String mTriggerId = jsonObject.getString("triggerId");
                    if (DdApplication.getConfigManager().isLogined()) {
                        //返回给html的消息
                        JSONObject json= new JSONObject();
                        json.put("token",DdApplication.getConfigManager().getToken());
                        json.put("phone",DdApplication.getConfigManager().getPhone());
                        json.put("triggerId",mTriggerId);
                        Log.e(TAG, "login onCallBack: " + json.toString());
                        function.onCallBack(json.toString());
                    } else {
                        JSONObject json= new JSONObject();
                        json.put("token","");
                        json.put("phone","");
                        json.put("triggerId","");
                        Log.e(TAG, "login onCallBack: " + json.toString());
                        function.onCallBack(json.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        /**
         * 弹出登录窗口
         */
        mWebView.registerHandler("login", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                if (DdApplication.getConfigManager().isLogined()) {
                    return;
                }
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(data);
                    mTriggerId = jsonObject.getString("triggerId");
                    //没有登录去登陆窗口
                    Intent intent = new Intent(AboutActivity.this, LoginInAdvanceActivity.class);
                    startActivityForResult(intent,REQUEST_LOGIN);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        /**
         *弹出modal模态窗口
         */
        mWebView.registerHandler("modal", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {

                Log.e(TAG, "modal onCallBack: " + data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    jsModalInfo = JsModalInfoBuilder.build(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jsModalInfo!=null) {
                    handler.sendEmptyMessage(MODAL);
                }

            }
        });
        /**
         *弹出sendToastMsg窗口
         */
        mWebView.registerHandler("sendToastMsg", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String message = jsonObject.getString("message");

                    AppUtils.showToast(AboutActivity.this,message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        /**
         *右上角按钮barNavBtn窗口
         */
        mWebView.registerHandler("barNavBtn", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    Log.e(TAG,jsonObject.toString());
                    String title = jsonObject.getString("title");
                    String type = jsonObject.getString("type");
                    String triggerId =  jsonObject.getString("triggerId");

                    titleRight.setVisibility(View.VISIBLE);
                    titleRight.setText(title);
                    disPlayRightMenu(type.equals("0") && !StringUtils.isEmpty(title), title,triggerId);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        /**
         * 打开新的View加载url指定的页面
         */
        mWebView.registerHandler("openView", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String url = jsonObject.getString("url");
                    UIHelper.showJsBridgeWebViewActivity(AboutActivity.this,url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        /**
         *   获取当前网络状态
         */
        mWebView.registerHandler("callForNetworkState", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {

                int state = NetworkUtil.getNetType(AboutActivity.this);
                //state = 0代表无网络 state = 1代表运营商网络 state = 2代表无线网络
                if (state == ConnectivityManager.TYPE_WIFI){
                    state = 2;
                } else if (state == ConnectivityManager.TYPE_MOBILE) {
                    state = 1;
                } else if (state < 0) {
                    state = 0;
                }
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject();
                    jsonObject.put("state",state+"");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "callForNetworkState send data: " + jsonObject.toString());

                function.onCallBack(jsonObject.toString());

            }
        });
        /**
         *   拨打电话
         */
        mWebView.registerHandler("getPhoneCall", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(data);
                    UIHelper.callPhoneWindow(AboutActivity.this, "拨打电话",
                            jsonObject.getString("phoneNum"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                function.onCallBack(null);
            }
        });
        /**
         *   t弹出分享窗口
         */
        mWebView.registerHandler("callShareAction", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {

                getShareParamHandler();
            }
        });
        /**
         *   上传图片 ，选择图片
         */
        mWebView.registerHandler("selectSingleImage", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(data);
                    ImageInfo imageInfo = new ImageInfo();
                    imageInfo.imgId = jsonObject.getString("imgId");
                    imageInfo.type = jsonObject.getString("type");
                    imageInfo.width = jsonObject.getInt("width");
                    imageInfo.height = jsonObject.getInt("height");
                    imageInfo.uploadUrl = jsonObject.getString("uploadUrl");
                    mImageInfo = imageInfo;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (jsonObject != null) {
                    //弹出窗口
                    GetPicDialog.initDialog(AboutActivity.this);
                }
            }
        });
    }
    /***
     * 显示右上角按钮
     *
     * @param isShow 是否显示
     * @param title  按钮文案
     */
    private void disPlayRightMenu(boolean isShow, String title, final String triggerId) {
        if (isShow) {
            // 显示 按钮
            titleRight.setVisibility(View.VISIBLE);
            titleRight.setText(title);
            findViewById(R.id.tv_title_right).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    barNavBtnHandler(triggerId);
                }
            });
        } else {
            titleRight.setVisibility(View.INVISIBLE);
        }
    }

    /**
     *  点击右上角按钮回调事件
     * @param triggerId
     */

    private void barNavBtnHandler(String triggerId) {
        if (!StringUtils.isEmpty(triggerId)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("triggerId", triggerId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String data = jsonObject.toString();
            Log.d(TAG, "barNavBtnHandler data: " + data);
            mWebView.callHandler("barNavBtnHandler", data, new CallBackFunction() {

                @Override
                public void onCallBack(String data) {

                }
            });
        } else {
            Log.d(TAG, "mTriggerId: " + triggerId);
        }
    }

    @Override
    public void onBackPressed() {
        onBackHandler();
    }

    /**
     * 获取分享信息
     */
    private void getShareParamHandler() {
        mWebView.callHandler("getShareParamHandler", null, new CallBackFunction() {

            @Override
            public void onCallBack(String data) {

                Log.d(TAG, "getShareParamHandler onCallBack: " + data);

                try {
                    JSONObject jsonObject = new JSONObject(data);
                    mShareInfo = JsShareInfoBuilder.build(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (mShareInfo!=null) {
                    handler.sendEmptyMessage(SHARE_DIALOG);
                }

            }
        });
    }

    /**
     * 分享结果回调
     */
    public void getShareBackHandler(String result,String triggerId) {
        //分享回调，执行js
        if (mWebView != null) {
            mWebView.getSettings().setJavaScriptEnabled(true);
        }
        JSONObject json= new JSONObject();
        try {
            json.put("triggerId",triggerId);
            json.put("result",result);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String data = json.toString();

        mWebView.callHandler("getShareBackHandler", data, new CallBackFunction() {

            @Override
            public void onCallBack(String data) {

            }
        });
    }

    /**
     *  当前页是否是第一页
     */
    private void returnBack() {
        //当前页是否是第一页
        mWebView.callHandler("returnBackHandler", null, new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                JSONObject object;
                try {
                    object = new JSONObject(data);
                    isJsFirstPage = object.getBoolean("isFirstPage");
                    Log.e(TAG,isJsFirstPage+"");
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if(isJsFirstPage) {//是否直接关闭页面
                        finish();
                    }
                }
            }
        });
    }

    /**
     * model弹窗中的按钮点击调用
     */
    public void modalHandler(String value){

        JSONObject json= new JSONObject();
        try {
            json.put("modalId",jsModalInfo.getModalId());
            json.put("value",value);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String data = json.toString();

        mWebView.callHandler("modalHandler", data, new CallBackFunction() {
            @Override
            public void onCallBack(String data) {

            }
        });
    }
    /**
     *  登录成功后传给HTML的数据
     */
    private void LoginHandler(String mTriggerId) {

        JSONObject json= new JSONObject();
        try {
            json.put("token",DdApplication.getConfigManager().getToken());
            json.put("phone",DdApplication.getConfigManager().getPhone());
            json.put("triggerId",mTriggerId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String data = json.toString();
        mWebView.callHandler("loginHandler", data, new CallBackFunction() {
            @Override
            public void onCallBack(String data) {

            }
        });
    }
    /**
     * 上传图片
     * @param
     */
    private void uploadImage(ImageInfo imageInfo, File file) {
        //通知jsBridge开始上传
        singleImageBefore(imageInfo.imgId);
        DdApplication.getCommonManager().sendUploadPicture(imageInfo.uploadUrl,"imgFile", file,mOnSendUploadPictureFinishedListener);

    }

    private CommonManager.onSendUploadPictureFinishedListener mOnSendUploadPictureFinishedListener=new CommonManager.onSendUploadPictureFinishedListener() {
        @Override
        public void onSendUploadPictureFinished(Response response) {

            if (response.isSuccess()) {

                try {
                    JSONObject jsonObject = new JSONObject(response.getBody());

                    String imgUrl = jsonObject.getString("imgUrl");
                    //将图片转化成前端显示的图片图片形式
                    File file = new File(SDPathUtils.getCachePath(), "crop.jpg");
                    String thumb = Utils.encodeFile(file);
                    singleImageBack(thumb,imgUrl, mImageInfo.imgId);
                    file.getAbsoluteFile().delete();
                    AppUtils.showToast(AboutActivity.this,jsonObject.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                AppUtils.showToast(AboutActivity.this,"上传失败");
            }
        }
    };
    /**
     *  告知web图片开始上传
     * @param imgid
     */
    private void singleImageBefore(String imgid) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("imgId", imgid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String data = jsonObject.toString();
        Log.d(TAG, "singleImageBefore send data: " + data);
        mWebView.callHandler("singleImageBefore", data, new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                Log.d(TAG, "singleImageBefore onCallBack");
            }
        });
    }


    /**
     * 图片上传之后返回结果
     * @param imageCodeStr
     * @param imageUrl
     * @param imgid
     */
    private void singleImageBack(String imageCodeStr, String imageUrl, String imgid) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("imageCodeStr", "data:image/jpeg;base64," + imageCodeStr);
            jsonObject.put("imageUrl", imageUrl);
            jsonObject.put("imgId", imgid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String data = jsonObject.toString();
        Log.d(TAG, "singleImageBack send data: " + data);

        mWebView.post(new Runnable() {
            @Override
            public void run() {
                mWebView.callHandler("singleImageBack", data, new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        Log.d(TAG, "singleImageBack onCallBack");
                    }
                });
            }
        });
    }
    private void onBackHandler() {

        returnBack();

        if (mWebView.canGoBack()) {
            mWebView.goBack();//返回上一頁
            txtFinish.setVisibility(View.VISIBLE);
        } else {
            //如果是在广告页中做返回，调到首页
            if (isAdvertView) {
                Intent intent = new Intent(this, MainActivity.class);
                if (mIndex != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("index", mIndex);
                    intent.putExtras(bundle);
                }
                startActivity(intent);
                finish();
            } else {
                finish();
            }
        }

    }

    //图片信息
    class ImageInfo implements Cloneable {
        public String imgId;
        public String type;
        public int width;
        public int height;
        public String uploadUrl;

        @Override
        protected ImageInfo clone() throws CloneNotSupportedException {
            return (ImageInfo) super.clone();
        }
    }

}
