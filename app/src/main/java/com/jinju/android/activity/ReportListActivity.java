package com.jinju.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.jinju.android.R;
import com.jinju.android.adapter.MyAdapter;
import com.jinju.android.api.ReportListBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportListActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivBack;
    private ListView  reportList;
    RequestQueue requestQueue;
    private ArrayList list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);
        initView();
        initData();

    }

    private void initData() {


        getNetData();



//        reportList.setAdapter(new MyAdapter(list, ReportListActivity.this));


    }

    private void initView() {

        ivBack = (ImageView) findViewById(R.id.ivBack);
        reportList = (ListView) findViewById(R.id.report_list);
//        reportList.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ivBack:
                startActivity(new Intent(this, MainActivity.class));
                finish();

        }
    }


    public  void getNetData() {
         

        requestQueue = Volley.newRequestQueue(ReportListActivity.this);
        //创建一个请求
//        String url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
        String url = "http://dev.api.jinjulc.com/notice/media_list.json";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
//                volley_result.setText(s);
                System.out.println("FFFFF:"+s);

                Gson gson = new Gson();

                list = new ArrayList<ReportListBean>();
                
                ReportListBean reportListBean = gson.fromJson(String.valueOf(s), ReportListBean.class);


                if (reportListBean!=null){
                    List<ReportListBean.MediaListBean> reportListBeanMediaList = reportListBean.getMediaList();
                    list.clear();
                    list.addAll(reportListBeanMediaList);
                }

                reportList.setAdapter(new MyAdapter(list,ReportListActivity.this));
                reportList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                });
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                volley_result.setText("加载错误" + volleyError);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                // map.put("value1","param1");//传入参数
                map.put("currentPage","1");
                map.put("pageSize","20");
                map.put("signature","123456");

                return map;
            }
        };


        requestQueue.add(stringRequest);
    }


}
