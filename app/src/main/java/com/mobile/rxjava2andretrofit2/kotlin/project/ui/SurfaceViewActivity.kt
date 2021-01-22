package com.mobile.rxjava2andretrofit2.kotlin.project.ui

import android.widget.FrameLayout
import com.mobile.rxjava2andretrofit2.R
import com.mobile.rxjava2andretrofit2.base.BaseAppActivity
import com.mobile.rxjava2andretrofit2.kotlin.project.PlayVideo


class SurfaceViewActivity : BaseAppActivity() {

    private val TAG: String = "SurfaceViewActivity"

    private var rlPlayer: FrameLayout? = null
    private val url = "http://rbv01.ku6.com/7lut5JlEO-v6a8K3X9xBNg.mp4"
    private var playVideo: PlayVideo? = null

    override fun initLayoutId(): Int {
        return R.layout.activity_surface_view
    }

    override fun initData() {
        playVideo = PlayVideo(this, url)
    }

    override fun initViews() {
        rlPlayer = findViewById(R.id.rl_player)
        rlPlayer!!.addView(playVideo!!.getVideoView())
    }

    override fun initLoadData() {

    }

    override fun onStop() {
        unregisterReceiver(playVideo!!.getReceiver());
        super.onStop()
    }

    override fun onDestroy() {
        try {
            if (playVideo != null) {
                playVideo!!.getMediaPlayer()!!.release()
//                playVideo!!.getMediaPlayer()!! = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onDestroy()
    }
}