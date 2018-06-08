package com.jinju.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.jinju.android.adapter.InvestAdapter;
import com.jinju.android.api.InvestList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvestRecordActivity extends BaseActivity implements View.OnClickListener {

    private ListView lv_lc_invest_list;
    RequestQueue requestQueue;
    private ArrayList  listInvest;
    private ImageView investFirst;

    /**
     * 展示投资记录信息
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invest_record);

         lv_lc_invest_list = (ListView) findViewById(R.id.lv_lc_invest_list);
         investFirst = (ImageView) findViewById(R.id.invest_first);
         investFirst.setOnClickListener(this);

        initData();
    }

    private void initData() {
        requestQueue = Volley.newRequestQueue(InvestRecordActivity.this);
        //创建一个请求
        //        String url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
        String url = "http://dev.api.jinjulc.com/financial/financial_order_list_v2.2.json?signature=123456&fpId=5";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //                volley_result.setText(s);
                System.out.println("FFFFF:"+s);

//                Type type = new TypeToken<ArrayList<JsonObject>>()
//
//                {}.getType();

                Gson gson = new Gson();

                listInvest = new ArrayList<InvestList>();
                InvestList investList = gson.fromJson(String.valueOf(s), InvestList.class);

//                System.out.println("AAAAA："+s);

                if (investList!=null){
                    List<InvestList.OrderListBean> investListOrderList = investList.getOrderList();
                    listInvest.clear();

                    if (investListOrderList!=null&&investListOrderList.size()>0){

                    listInvest.addAll(investListOrderList);
                    }
                }



                lv_lc_invest_list.setAdapter(new InvestAdapter(listInvest,InvestRecordActivity.this));
//                lv_lc_invest_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                    }
//                });
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
//                map.put("currentPage","1");
//                map.put("pageSize","20");
                map.put("fpId","5");
                map.put("signature","123456");

                return map;
            }
        };


        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.invest_first:
                startActivity(new Intent(this,FinancialDetailActivity.class));
        }

    }
}
