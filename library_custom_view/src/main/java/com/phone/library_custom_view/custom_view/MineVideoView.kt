package com.phone.library_custom_view.custom_view

import android.content.Context
import android.util.AttributeSet
import android.widget.VideoView

class MineVideoView(context: Context, attrs: AttributeSet?) : VideoView(context, attrs) {

    private val TAG = MineVideoView::class.java.simpleName

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        val width = getDefaultSize(width, widthMeasureSpec)
        val height = getDefaultSize(height, heightMeasureSpec)
        /**/ /*if (mVideoWidth > 0 && mVideoHeight > 0) {
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
        setMeasuredDimension(width, height)
    }
}