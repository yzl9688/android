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
import android.widget.TextView;

import com.mad.trafficclient.R;
import com.mad.trafficclient.activity.VideoDetailActivity;

public class Fragment4Video extends Fragment {
    private View view;
    private GridView gridView;
    private String[] videoSources;
    private String[] videoTitles;
    private GridViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_fragment4_gridview, container, false);

        initView();

        return view;
    }

    private void initView() {
        gridView = (GridView) view.findViewById(R.id.gridView);
        videoSources = new String[] {
                "android.resource://" + getActivity().getPackageName() + "/" + R.raw.traffic,
                "android.resource://" + getActivity().getPackageName() + "/" + R.raw.car1,
                "android.resource://" + getActivity().getPackageName() + "/" + R.raw.car2,
                "android.resource://" + getActivity().getPackageName() + "/" + R.raw.car3,
                "android.resource://" + getActivity().getPackageName() + "/" + R.raw.car4,
                "android.resource://" + getActivity().getPackageName() + "/" + R.raw.bwm,
        };
        videoTitles = new String[] {"bwm", "car1", "car2", "car3", "car4", "traffic"};
        adapter = new GridViewAdapter(getActivity(), R.layout.fragment4_gridview_item, videoTitles, videoSources);
        gridView.setAdapter(adapter);
    }

    private class GridViewAdapter extends BaseAdapter {
        private int resourceId;
        private Context context;
        private String[] titles;
        private String[] videoSource;

        public GridViewAdapter(Context context, int resourceId, String[] titles, String[] videoSource) {
            super();
            this.context = context;
            this.resourceId = resourceId;
            this.titles = titles;
            this.videoSource = videoSource;
        }

        @Override
        public int getCount() {
            return videoTitles.length;
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
            View view1 = LayoutInflater.from(context).inflate(resourceId, viewGroup, false);
            view1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Dialog dialog = new Dialog(getActivity());
//                    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//                    View view2 = LayoutInflater.from(getActivity()).inflate(R.layout.activity_fragment4_video, null, false);
//                    VideoView  videoView = (VideoView)view2.findViewById(R.id.videoView);
//                    videoView.setVideoURI(Uri.parse(videoSources[i]));
//                    videoView.start();
//                    dialog.setContentView(view2);
//                    dialog.show();
//                    WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
//                    params.width = 1000;
//                    params.height = 600;
//                    dialog.getWindow().setAttributes(params);

                    Intent intent = new Intent(getActivity(), VideoDetailActivity.class);
                    intent.putExtra("videoUri", videoSources[i]);
                    startActivity(intent);
                }
            });
            TextView textView = (TextView) view1.findViewById(R.id.textView);
            textView.setText(titles[i]);
            return view1;
        }
    }

    private class Video {
        private String title;
        private int resourceId;

        public Video(String title, int resourceId) {
            this.title = title;
            this.resourceId = resourceId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getResourceId() {
            return resourceId;
        }

        public void setResourceId(int resourceId) {
            this.resourceId = resourceId;
        }
    }
}