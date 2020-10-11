package com.mad.trafficclient.fragment;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.EventLogTags;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.mad.trafficclient.R;
import com.mad.trafficclient.database.Fragment5DatabaseHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Fragment5Detail extends Fragment {
    private View view;
    private ViewPager viewPager;
    private LinearLayout btnLayout;
    private View[] btns;
    private int id;
    private String[] titles = new String[]{"温度", "湿度", "光照", "pm2.5", "co2", "道路状态"};
    private ArrayList<ArrayList<Integer>> datas;
    private String[] xValues;
    private ScheduledExecutorService ses;
    private Fragment5DatabaseHelper helper;
    private SQLiteDatabase db;
    private ArrayList<View> views;
    private ViewPagerAdapter adapter;
    private LineChart[] charts = new LineChart[6];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment5_detail, container, false);



        Bundle bundle = getArguments();
        id = bundle.getInt("id");
        initViews();
        initView();

        initData();


        initListener();

        viewPager.setCurrentItem(id);

        return view;
    }

    private void initViews() {
        views = new ArrayList<>();
        for (int i = 0; i < 6; i ++) {
            View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.fragment5_detail_chart, null, false);
            TextView tvTitle = (TextView) view1.findViewById(R.id.title);
            tvTitle.setText(titles[i]);
            LineChart chart = (LineChart) view1.findViewById(R.id.chart);
            charts[i] = chart;
            chart.setEnabled(false);
            XAxis xAxis = chart.getXAxis();
            xAxis.setEnabled(true);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            YAxis ylAxis = chart.getAxisLeft();
            ylAxis.setEnabled(true);
            YAxis yrAxis = chart.getAxisRight();
            yrAxis.setEnabled(false);
            Legend legend = chart.getLegend();
            legend.setEnabled(false);


            views.add(view1);
        }
    }

    private void initChart(int k) {
//        for (int i = 0; i < charts.length; i ++) {
//            Log.d("Test", String.valueOf(i));
            ArrayList<Entry> values = new ArrayList<>();
            for (int j = 0; j < datas.get(k).size(); j ++) {
                values.add(new Entry(j, datas.get(k).get(j)));
            }
            LineDataSet dataSet = new LineDataSet(values, titles[k]);
            LineData data = new LineData(xValues, dataSet);

            charts[k].setData(data);
//            charts[i].invalidate();
//        }
    }

    private void initData() {
        xValues = new String[20];
        for (int i = 0; i < 20; i ++) {
            xValues[i] = String.valueOf(i*3);
        }
        datas = new ArrayList<>();
        ses.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = db.query("env_info", null, null, null, null, null, null);
                datas.clear();
                if (cursor.moveToFirst()) {
                    do {
                        ArrayList<Integer> yValues = new ArrayList<>();
                        yValues.add(cursor.getInt(cursor.getColumnIndex("temperature")));
                        yValues.add(cursor.getInt(cursor.getColumnIndex("humidity")));
                        yValues.add(cursor.getInt(cursor.getColumnIndex("lightIntensity")));
                        yValues.add(cursor.getInt(cursor.getColumnIndex("pm25")));
                        yValues.add(cursor.getInt(cursor.getColumnIndex("co2")));
                        yValues.add(cursor.getInt(cursor.getColumnIndex("road")));
                        datas.add(yValues);
                    } while (cursor.moveToNext());
                }

            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    private void initBtns(int selected) {
        for (int i = 0; i < btns.length; i ++) {
            btns[i].setBackgroundResource(R.drawable.fragment5_detail_circle_unselected);
        }
        btns[selected].setBackgroundResource(R.drawable.fragment5_detail_circle_selected);
    }

    private void initListener() {
        for (int i = 0; i < btns.length; i ++) {
            final int finalI = i;
            btns[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initBtns(finalI);
                    viewPager.setCurrentItem(finalI);
                }
            });
        }


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                initBtns(position);
                initChart(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initView() {
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        btnLayout = (LinearLayout) view.findViewById(R.id.btnLayout);
        int len = btnLayout.getChildCount();
        btns = new View[len];
        for (int i = 0; i < len; i ++) {
            View view = btnLayout.getChildAt(i);
            btns[i] = view;
        }
        adapter = new ViewPagerAdapter(views);
        viewPager.setAdapter(adapter);
        ses = Executors.newSingleThreadScheduledExecutor();
        helper = new Fragment5DatabaseHelper(getActivity());
        db = helper.getReadableDatabase();
    }

    private class ViewPagerAdapter extends PagerAdapter {
        private ArrayList<View> views;

        public ViewPagerAdapter(ArrayList<View> views) {
            super();
            this.views = views;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

    }


}
