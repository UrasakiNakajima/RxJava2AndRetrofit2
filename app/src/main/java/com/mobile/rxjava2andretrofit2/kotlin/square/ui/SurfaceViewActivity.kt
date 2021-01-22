package com.mobile.rxjava2andretrofit2.kotlin.square.ui

import android.widget.RelativeLayout
import com.mobile.rxjava2andretrofit2.base.BaseAppActivity
import com.mobile.rxjava2andretrofit2.kotlin.square.PlayVideo


class SurfaceViewActivity : BaseAppActivity() {

    private val TAG: String = "SurfaceViewActivity"

    private var rlPlayer: RelativeLayout? = null
    private val url = "http://rbv01.ku6.com/7lut5JlEO-v6a8K3X9xBNg.mp4"
    private var playVideo: PlayVideo? = null

    override fun initLayoutId(): Int {
        return com.mobile.rxjava2andretrofit2.R.layout.activity_surface_view
    }

    override fun initData() {

    }

    override fun initViews() {
        rlPlayer = findViewById(com.mobile.rxjava2andretrofit2.R.id.rl_player)
        playVideo = PlayVideo(this, url)
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