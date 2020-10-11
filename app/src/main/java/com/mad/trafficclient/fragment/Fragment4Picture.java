package com.mad.trafficclient.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.mad.trafficclient.R;
import com.mad.trafficclient.activity.ImageDetailActivity;


public class Fragment4Picture extends Fragment {
    private GridView gridView;
    private int[] imgs;
    private GridViewAdapter adapter;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fragment4_gridview, container, false);

        init();
        return view;
    }

    private void init() {
        gridView = (GridView) view.findViewById(R.id.gridView);

        imgs = new int[] {R.drawable.weizhang01, R.drawable.weizhang02, R.drawable.weizhang03, R.drawable.weizhang04, R.drawable.weizhang01};
        adapter = new GridViewAdapter(getActivity(), imgs);

        gridView.setAdapter(adapter);
    }

    private class GridViewAdapter extends BaseAdapter {
        private Context context;
        private int[] imgs;

        public GridViewAdapter(Context context, int[] imgs) {
            super();
            this.context = context;
            this.imgs = imgs;
        }

        @Override
        public int getCount() {
            return this.imgs.length;
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            View view1 = LayoutInflater.from(context).inflate(R.layout.fragment4_gridview_item, viewGroup, false);
            TextView textView = (TextView) view1.findViewById(R.id.textView);
            textView.setVisibility(View.GONE);  // 隐藏该控件
            ImageView imageView = (ImageView) view1.findViewById(R.id.imageView);
            imageView.setImageResource(imgs[i]);

            view1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), ImageDetailActivity.class);
                    intent.putExtra("img", imgs[i]);
                    startActivity(intent);
                }
            });

            return view1;
        }
    }

}