package com.mad.trafficclient.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mad.trafficclient.MainActivity;
import com.mad.trafficclient.R;
import com.mad.trafficclient.database.Fragment5DatabaseHelper;
import com.mad.trafficclient.util.LoadingDialog;
import com.mad.trafficclient.util.UrlBean;
import com.mad.trafficclient.util.Util;
import com.mad.trafficclient.utilhttp.RequestNetwork;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Fragment_5 extends Fragment {
    private View view;
    private GridView gridView;
    private ArrayList<Environment> datas;
    private GridViewAdapter adapter;
    private ScheduledExecutorService ses;
    private UrlBean urlBean;  // 配置信息
    private String username = "user1";  // 用户名
    private int upper = 1000;  // 阈值上限
    private int lower = 10;   //  阈值下限
    private int roadId = 1;  // 默认道路ID
    private Fragment5DatabaseHelper helper;
    private SQLiteDatabase db;
    private TextView tvTitle;  // 标题
    private RequestQueue queue;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_layout05, container, false);
        init();
        initData();

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        View view2 = activity.getWindow().getDecorView();
        tvTitle = (TextView) view2.findViewById(R.id.tv_title);  // 设置标题
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        Log.d("Test", "onDetach");

        super.onDetach();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("Test", "onDestroyView");
        ses.shutdownNow();  // 释放线程池
    }

    @Override
    public void onDestroy() {
        // 释放资源
        Log.d("Test", "onDestroy");
        db.delete("env_info", null, null); // 清空数据
        db.close();
        helper.close();
        super.onDestroy();
    }

    // 初始化数据，开启线程实现3秒一刷新
    private void initData() {
        datas = new ArrayList<>();
//        ses.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                Log.d("Test", "run");
//                datas.clear();
//                ses.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.d("Test", "run1");
//                        getEnv();
//                    }
//                });
//                ses.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.d("Test", "run2");
//                        getRoad();
//                    }
//                });
//
//            }
//        }, 0, 3, TimeUnit.SECONDS);

        JSONObject data1 = new JSONObject();
        try {
            data1.put("UserName", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url1 = "http://" + urlBean.getUrl() + ":" + urlBean.getPort() + "/api/v2/get_all_sense";
        final JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.POST, url1, data1, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject.optString("RESULT").equals("S")) {
                    datas.add(new Environment("温度", jsonObject.optString("temperature")));
                    datas.add(new Environment("湿度", jsonObject.optString("humidity")));
                    datas.add(new Environment("光照", jsonObject.optString("LightIntensity")));
                    datas.add(new Environment("pm2.5", jsonObject.optString("pm2.5")));
                    datas.add(new Environment("co2", jsonObject.optString("co2")));
//                    getRoad();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        String url2 = "http://"+urlBean.getUrl()+":"+urlBean.getPort()+"/api/v2/get_road_status";
        JSONObject data2 = new JSONObject();
        try {
            data2.put("RoadId", roadId);
            data2.put("UserName", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.POST, url2, data2, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject.optString("RESULT").equals("S")) {
                    datas.add(new Environment("道路状态", jsonObject.optString("Status")));
                    adapter.notifyDataSetChanged();
                    Cursor cursor = db.query("env_info", null, null, null, null, null, null);
//                    Log.d("Test", String.valueOf(cursor.getCount()));

                    int size = cursor.getCount();
                    if (size == 20) {  // 如果数据有20条，则清空
//                        if (cursor.moveToFirst()) {
//                            do {
//
//                            } while(cursor.moveToNext());
//                        }
                        db.delete("env_info", null, null);
                    }
                    String[] keys = new String[]{"temperature", "humidity", "lightIntensity", "pm25", "co2", "road"};
                    ContentValues values = new ContentValues();
                    for (int i = 0; i < datas.size(); i ++) {
                        Environment env = datas.get(i);
                        values.put(keys[i], Integer.parseInt(env.getNumber()));
                    }
                    db.insert("env_info", null, values);  // 插入数据
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
//


        ses.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                queue.add(request1);
                queue.add(request2);
            }
        }, 0, 3, TimeUnit.SECONDS);

        adapter = new GridViewAdapter(getActivity(), R.layout.fragment05_gridview_item, datas);
        gridView.setAdapter(adapter);
    }

    // 从接口得到道路状态数据
    private void getRoad() {
        String url2 = "http://"+urlBean.getUrl()+":"+urlBean.getPort()+"/api/v2/get_road_status";
        JSONObject data2 = new JSONObject();
        try {
            data2.put("RoadId", roadId);
            data2.put("UserName", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestNetwork.request(getActivity(), url2, data2, new RequestNetwork.Callback() {
            @Override
            public void invoke(JSONObject jsonObject) {
                if (jsonObject.optString("RESULT").equals("S")) {
                    datas.add(new Environment("道路状态", jsonObject.optString("Status")));
                    adapter.notifyDataSetChanged();
                    Cursor cursor = db.query("env_info", null, null, null, null, null, null);
//                    Log.d("Test", String.valueOf(cursor.getCount()));

                    int size = cursor.getCount();
                    if (size == 20) {  // 如果数据有20条，则清空
//                        if (cursor.moveToFirst()) {
//                            do {
//
//                            } while(cursor.moveToNext());
//                        }
                        db.delete("env_info", null, null);
                    }
                    String[] keys = new String[]{"temperature", "humidity", "lightIntensity", "pm25", "co2", "road"};
                    ContentValues values = new ContentValues();
                    for (int i = 0; i < datas.size(); i ++) {
                        Environment env = datas.get(i);
                        values.put(keys[i], Integer.parseInt(env.getNumber()));
                    }
                    db.insert("env_info", null, values);  // 插入数据
                }
            }
        });
    }


     //从接口得到环境指标数据
    private void getEnv() {
        JSONObject data1 = new JSONObject();
        try {
            data1.put("UserName", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url1 = "http://" + urlBean.getUrl() + ":" + urlBean.getPort() + "/api/v2/get_all_sense";
        RequestNetwork.request(getActivity(), url1, data1, new RequestNetwork.Callback() {
            @Override
            public void invoke(JSONObject jsonObject) {
                if (jsonObject.optString("RESULT").equals("S")) {
                    datas.add(new Environment("温度", jsonObject.optString("temperature")));
                    datas.add(new Environment("湿度", jsonObject.optString("humidity")));
                    datas.add(new Environment("光照", jsonObject.optString("LightIntensity")));
                    datas.add(new Environment("pm2.5", jsonObject.optString("pm2.5")));
                    datas.add(new Environment("co2", jsonObject.optString("co2")));
//                    getRoad();
                }
            }
        });
    }

    // 初始化操作
    private void init() {
        gridView = (GridView) view.findViewById(R.id.gridView);
        urlBean = Util.loadSetting(getActivity());
        username = urlBean.getUsername();
        helper = new Fragment5DatabaseHelper(getActivity());
        db = helper.getWritableDatabase();
        ses =  Executors.newSingleThreadScheduledExecutor();
        queue = Volley.newRequestQueue(getActivity());
        getYuzhi();
    }

    // 为gridView 自定义的adapter
    private class GridViewAdapter extends BaseAdapter {
        private Context context;
        private int resourceId;
        private ArrayList<Environment> datas;


        public GridViewAdapter(Context context, int resourceId, ArrayList<Environment> datas) {
            super();
            this.context = context;
            this.resourceId = resourceId;
            this.datas = datas;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, final ViewGroup viewGroup) {
            View view1 = LayoutInflater.from(context).inflate(resourceId, viewGroup, false);
            Environment env = datas.get(i);
            if (Integer.parseInt(env.getNumber()) < lower || Integer.parseInt(env.getNumber()) > upper) {
                view1.setBackgroundResource(R.drawable.fragment05_gridview_red);
            } else {
                view1.setBackgroundResource(R.drawable.fragment05_gridview_green);
            }
            if (env.getText().equals("道路状态")) {
                if (Integer.parseInt(env.getNumber()) > 2) {
                    view1.setBackgroundResource(R.drawable.fragment05_gridview_red);
                } else {
                    view1.setBackgroundResource(R.drawable.fragment05_gridview_green);
                }
            }
            TextView textView = (TextView) view1.findViewById(R.id.textView);
            TextView number = (TextView) view1.findViewById(R.id.number);
            textView.setText(env.getText());
            number.setText(env.getNumber());
            view1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Fragment detail = new Fragment5Detail();
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("id", i);
//                    detail.setArguments(bundle);
//                    getFragmentManager().beginTransaction().add(R.id.maincontent, detail).commit();
//                    tvTitle.setText("实时显示");
                }
            });
            return view1;
        }
    }

    // 指标信息类
    private class Environment {
        private String text;
        private String number;

        public Environment(String text, String number) {
            this.text = text;
            this.number = number;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }

    // 从接口得到阈值
    private void getYuzhi() {
        String url = "http://"+urlBean.getUrl()+":"+urlBean.getPort()+"/api/v2/get_light_sense_value";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("UserName", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestNetwork.request(getActivity(), url, jsonObject, new RequestNetwork.Callback() {
            @Override
            public void invoke(JSONObject jsonObject) {
                if(jsonObject.optString("RESULT").equals("S")) {
                    upper = Integer.parseInt(jsonObject.optString("upper"));
                    lower = Integer.parseInt(jsonObject.optString("lower"));
                }
            }
        });
    }


}
