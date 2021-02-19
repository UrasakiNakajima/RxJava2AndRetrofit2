package com.mobile.rxjava2andretrofit2.kotlin.project.ui

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.view.View
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import com.mobile.rxjava2andretrofit2.R
import com.mobile.rxjava2andretrofit2.base.BaseAppActivity
import kotlinx.android.synthetic.main.activity_video_view.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*


class VideoViewActivity : BaseAppActivity() {

    companion object {
        private val TAG: String = "VideoViewActivity"
    }

    /*测试地址*/
//    val url = "http://rbv01.ku6.com/7lut5JlEO-v6a8K3X9xBNg.mp4";
    val url = "https://t-cmcccos.cxzx10086.cn/statics/shopping/hidden_corner.mp4";
    val VIDEO_TYPE_URI = 1
    val VIDEO_TYPE_FILE_PATH = 2
    var VIDEO_TYPE: Int = 0
    private var isCompletion = false
    private var downX: Int = 0
    private var moveX: Int = 0
    private var left: Boolean = false

    private var isTrackingTouch = false

    private var oldPosition: Int = 0
    //将长度转换为时间
    internal var mFormatBuilder = StringBuilder()
    internal var mFormatter = Formatter(mFormatBuilder, Locale.getDefault())

    override fun initLayoutId(): Int {
        return R.layout.activity_video_view
    }

    override fun initData() {

    }

    override fun initViews() {
        mseek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (isTrackingTouch) {
                    val scale = (progress * 1.0 / 100).toFloat()
                    val msec = mvideo_view.duration * scale
                    seekTo(msec)
                }
            }


            override fun onStartTrackingTouch(seekBar: SeekBar) {
                isTrackingTouch = true
                pausePlay()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                isTrackingTouch = false
                startPlay()
            }
        })
    }

    override fun initLoadData() {
        setData(url, VIDEO_TYPE_URI)
    }

    private val handler = Handler()

    private val runnable = Runnable {
        sendTime()
        val currentPosition = mvideo_view!!.currentPosition
        if (mvideo_view!!.isPlaying()) {
            tev_current_time!!.text = playCurrentTime()
            if (currentPosition == oldPosition) {
                progress_circular!!.setVisibility(View.VISIBLE)
            } else {
                progress_circular!!.setVisibility(View.GONE)
                if (place_holder != null)
                    place_holder!!.visibility = View.GONE
            }
            oldPosition = currentPosition
        }
    }

    fun sendTime() {
        handler.postDelayed(runnable, 200)
    }

    /**
     * 设置视频源
     *
     * @param url
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun setData(url: String, TYPE: Int) {
        this.VIDEO_TYPE = TYPE
        when (TYPE) {
            VIDEO_TYPE_FILE_PATH ->
                /*设置播放源*/
                mvideo_view!!.setVideoPath(url)
            VIDEO_TYPE_URI ->
                /*设置播放源*/
                mvideo_view!!.setVideoURI(Uri.parse(url))
        }
        getPreviewImage(url)
        /*准备完成后回调*/
        mvideo_view!!.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
            override fun onPrepared(mp: MediaPlayer?) {
                tev_total_time!!.setText(durationTime())
                sendTime()
            }

        })
        /*播放内容监听*/
        mvideo_view!!.setOnInfoListener((object : MediaPlayer.OnInfoListener {
            override fun onInfo(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                    if (progress_circular != null) {
                        progress_circular!!.setVisibility(View.VISIBLE)
                    }
                } else {
                    if (mvideo_view!!.isPlaying()) {
                        if (progress_circular != null) progress_circular!!.setVisibility(View.GONE)
                        imv_play!!.visibility = View.GONE
                        layout_play_control!!.visibility = View.VISIBLE
                    }
                }
                return true
            }

        }))
        /*播放完成回调*/
        mvideo_view!!.setOnCompletionListener(object : MediaPlayer.OnCompletionListener {
            override fun onCompletion(mp: MediaPlayer?) {
                isCompletion = true
                place_holder!!.visibility = View.VISIBLE
                resetPlay()
            }

        })

        mvideo_view!!.setOnClickListener(View.OnClickListener {
            if (mvideo_view!!.isPlaying()) {
                pausePlay()
            } else {
                startPlay()
            }
        })
    }

    /**
     * 指定位置播放
     *
     * @param m
     */
    fun seekTo(m: Float) {
        imv_play!!.setVisibility(View.GONE)
        if (mvideo_view != null) {
            mvideo_view!!.seekTo(m.toInt())
        }
    }

    /**
     * 开始播放
     */
    fun startPlay() {
        if (!mvideo_view!!.isPlaying()) {
            mvideo_view!!.start()
        }
        imv_play!!.visibility = View.GONE

//        place_holder!!.visibility = View.VISIBLE
        progress_circular!!.setVisibility(View.VISIBLE)
    }

    /**
     * 暂停播放
     */
    fun pausePlay() {
        if (mvideo_view != null && mvideo_view!!.isPlaying()) {
            mvideo_view!!.pause()
        }
        imv_play!!.visibility = View.VISIBLE
    }

    /**
     * 重新播放
     */
    fun resetStartPlay() {
        if (mvideo_view != null && isCompletion) {
            mvideo_view!!.seekTo(0)
            isCompletion = false
            mvideo_view!!.start()
            imv_play!!.visibility = View.INVISIBLE
        }
    }

    /**
     * 重置
     */
    fun resetPlay() {
        tev_current_time!!.text = resources.getString(R.string.start_time)
        mseek_bar!!.progress = 0
        mcurrent_progress_bar.progress = 0
        seekTo(0f)
        imv_play!!.visibility = View.VISIBLE
    }

    /**
     * 停止播放
     */
    fun stopPlay() {
        mvideo_view!!.canPause()
        mvideo_view!!.stopPlayback()
    }

    /**
     * 销毁
     */
    fun destoryPlay() {
        handler.removeCallbacks(runnable)
        mvideo_view!!.stopPlayback()
    }

    /**
     * 显示预览图片
     *
     * @param url
     */
    fun getPreviewImage(url: String) {
        Thread(Runnable {
            val retriever = MediaMetadataRetriever()
            var bitmap: Bitmap? = null

            //这里要用FileProvider获取的Uri
            if (url.contains("http")) {
                retriever.setDataSource(url, HashMap())
            } else {
                retriever.setDataSource(url)
            }
            bitmap = retriever.frameAtTime

            val finalBitmap = bitmap
            Observable.empty<Any>().subscribeOn(AndroidSchedulers.mainThread())
                    .doOnComplete {
                        place_holder!!.setImageBitmap(finalBitmap)
                        retriever.release()
                    }.subscribe()
        }).start()
    }

    /**
     * 当前播放进度
     *
     * @return
     */
    fun playCurrentTime(): String {
        val CurrentPosition = mvideo_view!!.currentPosition
        val scale = (CurrentPosition * 1.0 / mvideo_view!!.duration).toFloat()
        mseek_bar!!.progress = (scale * 100).toInt()
        mcurrent_progress_bar!!.progress = (scale * 100).toInt()
        return stringForTime(CurrentPosition)
    }

    /**
     * 视频的总长度
     *
     * @return
     */
    fun durationTime(): String {
        return stringForTime(mvideo_view!!.duration)
    }

    /**
     * 将长度转换为时间
     *
     * @param timeMs
     * @return
     */
    private fun stringForTime(timeMs: Int): String {
        val totalSeconds = timeMs / 1000
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600

        mFormatBuilder.setLength(0)
        return if (hours > 0) {
            mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
        } else {
            mFormatter.format("%02d:%02d", minutes, seconds).toString()
        }
    }

    override fun onStop() {
        pausePlay()
        super.onStop()
    }

    override fun onDestroy() {
        destoryPlay()
        super.onDestroy()
    }

    private var onMoveListener: OnMoveListener? = null

    fun setOnMoveListener(onMoveListener: OnMoveListener) {
        this.onMoveListener = onMoveListener
    }

    interface OnMoveListener {
        fun onMove()
    }
}