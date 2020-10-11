package com.mad.trafficclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.MediaController;
import android.widget.VideoView;

import com.mad.trafficclient.R;

public class VideoDetailActivity extends Activity {
    private VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment4_video);

        videoView = (VideoView) findViewById(R.id.videoView);

        Intent intent = getIntent();
        String uri = intent.getStringExtra("videoUri");

        videoView.setVideoURI(Uri.parse(uri));

        MediaController mediaController = new MediaController(this);
        mediaController.setMediaPlayer(videoView);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.start();


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
