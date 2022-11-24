package com.phone.common_library.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class MineVideoView extends VideoView {

    private static final String TAG = MineVideoView.class.getSimpleName();

    public MineVideoView(Context context) {
        super(context);
    }

    public MineVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MineVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultSize(getWidth(), widthMeasureSpec);
        int height = getDefaultSize(getHeight(), heightMeasureSpec);
        /**//*if (mVideoWidth > 0 && mVideoHeight > 0) {
            if ( mVideoWidth * height  > width * mVideoHeight ) {
                //Log.i(TAG, "image too tall, correcting");
                height = width * mVideoHeight / mVideoWidth;
            } else if ( mVideoWidth * height  < width * mVideoHeight ) {
                //Log.i(TAG, "image too wide, correcting");
                width = height * mVideoWidth / mVideoHeight;
            } else {
                //Log.i(TAG, "aspect ratio is correct: " +
                        //width+"/"+height+"="+
                        //mVideoWidth+"/"+mVideoHeight);
            }
        }*/

        //Log.i(TAG, "setting size: " + width + 'x' + height);
        setMeasuredDimension(width, height);
    }
}

