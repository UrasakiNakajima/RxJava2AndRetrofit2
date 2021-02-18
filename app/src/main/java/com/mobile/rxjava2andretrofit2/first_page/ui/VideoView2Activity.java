package com.mobile.rxjava2andretrofit2.first_page.ui;

import android.widget.FrameLayout;

import com.mobile.rxjava2andretrofit2.R;
import com.mobile.rxjava2andretrofit2.base.BaseAppActivity;
import com.mobile.rxjava2andretrofit2.custom_view.MaterialVideoView;

public class VideoView2Activity extends BaseAppActivity {

    private static final String TAG = "VideoView2Activity";
    private FrameLayout layout_video;
    private MaterialVideoView video_view;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_video_view2;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        layout_video = findViewById(R.id.layout_video);
        video_view = findViewById(R.id.video_view);
//        video_view = new MaterialVideoView(this);
//        layout_video.addView(video_view);
    }

    @Override
    protected void initLoadData() {

    }
}
