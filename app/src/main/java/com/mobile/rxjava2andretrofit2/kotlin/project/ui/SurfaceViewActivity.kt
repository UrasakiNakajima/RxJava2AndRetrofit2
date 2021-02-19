package com.mobile.rxjava2andretrofit2.kotlin.project.ui

import android.content.res.Configuration
import android.graphics.Bitmap
import android.media.AudioManager
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.view.SurfaceHolder
import android.view.View
import androidx.annotation.RequiresApi
import com.mobile.rxjava2andretrofit2.base.BaseAppActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_surface_view.*
import android.net.Uri
import com.mobile.rxjava2andretrofit2.R


class SurfaceViewActivity : BaseAppActivity() {

    companion object {
        private val TAG: String = "SurfaceViewActivity"
    }

    /*测试地址*/
//    val url = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
//    val url = "http://rbv01.ku6.com/omtSn0z_PTREtneb3GRtGg.mp4";
    //    val url = "http://rbv01.ku6.com/7lut5JlEO-v6a8K3X9xBNg.mp4";
    val url = "https://t-cmcccos.cxzx10086.cn/statics/shopping/hidden_corner.mp4";


    val VIDEO_TYPE_URI = 1
    val VIDEO_TYPE_FILE_PATH = 2
    var VIDEO_TYPE: Int = 0
    private var isCompletion = false

    private var isTrackingTouch = false

    private var oldPosition: Int = 0
    //将长度转换为时间
    internal var mFormatBuilder = StringBuilder()
    internal var mFormatter = Formatter(mFormatBuilder, Locale.getDefault())

    private var mediaPlayer: MediaPlayer? = null
    //0.开始状态。1.正在播放。2.暂停。3.停止。

    private var isPlaying: Boolean = false
//    private var playProgress: Int = 0;

    override fun initLayoutId(): Int {
        return R.layout.activity_surface_view
    }

    override fun initData() {
//        playProgress = 0
    }

    override fun initViews() {
        surface_view.getHolder().setKeepScreenOn(true)
        surface_view.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        surface_view.getHolder().addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {

            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                setData(url, VIDEO_TYPE_URI)
                seekBarListener()
            }

        })
    }

    override fun initLoadData() {

    }

    private val handler = Handler()

    private val runnable = Runnable {
        sendTime()
        val currentPosition = mediaPlayer!!.getCurrentPosition()
        if (isPlaying) {
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
     * 开始播放
     */
    fun start() {
        if (!isPlaying) {
            mediaPlayer!!.start()
            isPlaying = true
        }
        imv_play!!.visibility = View.GONE

//        place_holder.visibility = View.VISIBLE
        progress_circular.setVisibility(View.VISIBLE)
    }

//    /**
//     * 开始播放
//     */
//    fun start(msec: Int) {
//        if (!isPlaying) {
//            mediaPlayer!!.seekTo(msec)
//            mediaPlayer!!.start()
//            isPlaying = true
//        }
//        imv_play!!.visibility = View.GONE
//
////        place_holder.visibility = View.VISIBLE
//        progress_circular.setVisibility(View.VISIBLE)
//    }

    /**
     * 暂停播放
     */
    fun pause() {
        if (isPlaying) {
//            playProgress = mediaPlayer!!.currentPosition;
            mediaPlayer!!.pause()
            isPlaying = false
        }
        imv_play!!.visibility = View.VISIBLE
    }

    /**
     * 设置视频源
     *
     * @param url
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun setData(url: String, TYPE: Int) {
        getPreviewImage(url)
        mediaPlayer = MediaPlayer()
        mediaPlayer!!.reset();
        mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC);
        val uri = Uri
                .parse(url)
        this.VIDEO_TYPE = TYPE
        when (TYPE) {
            VIDEO_TYPE_FILE_PATH ->
                /*设置播放源*/
                mediaPlayer!!.setDataSource(this, uri)
            VIDEO_TYPE_URI ->
                /*设置播放源*/
                mediaPlayer!!.setDataSource(this, uri)
        }
        mediaPlayer!!.setDisplay(surface_view.getHolder());
        mediaPlayer!!.prepareAsync();
        /*准备完成后回调*/
        mediaPlayer!!.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
            override fun onPrepared(mp: MediaPlayer?) {
                if (mediaPlayer != null) {
                    tev_total_time!!.setText(durationTime())
                    sendTime()
                }
            }

        })
        /*播放内容监听*/
        mediaPlayer!!.setOnInfoListener((object : MediaPlayer.OnInfoListener {
            override fun onInfo(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                    if (progress_circular != null) {
                        progress_circular.setVisibility(View.VISIBLE)
                    }
                } else {
                    if (isPlaying) {
                        if (progress_circular != null) {
                            progress_circular.setVisibility(View.GONE)
                            imv_play!!.visibility = View.GONE
                            layout_play_control!!.visibility = View.VISIBLE
                        }
                    }
                }
                return true
            }

        }))
        /*播放完成回调*/
        mediaPlayer!!.setOnCompletionListener(object : MediaPlayer.OnCompletionListener {
            override fun onCompletion(mp: MediaPlayer?) {
                isCompletion = true
                isPlaying = false
                place_holder!!.visibility = View.VISIBLE
                resetStart()
            }
        })

        surface_view!!.setOnClickListener(View.OnClickListener {
            if (isPlaying ) {
                pause()
            } else {
                start()
            }
        })
    }


    /**
     *
     */
    private fun seekBarListener() {
        mseek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (isTrackingTouch) {
                    val scale = (progress * 1.0 / 100).toFloat()
                    val msec = seekBar.progress * scale
                    seekTo(msec)
                }
            }


            override fun onStartTrackingTouch(seekBar: SeekBar) {
                isTrackingTouch = true
                pause()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                isTrackingTouch = false
                start()
            }
        })
    }

    /**
     * 停止播放
     */
    fun stop() {
        mediaPlayer!!.stop()
        isPlaying = false
    }


    /**
     * 指定位置播放
     *
     * @param m
     */
    fun seekTo(m: Float) {
        imv_play!!.setVisibility(View.GONE)
        if (mediaPlayer != null) {
            mediaPlayer!!.seekTo(m.toInt())
        }
    }

    /**
     * 重置
     */
    fun resetStart() {
        isPlaying = true
        tev_current_time!!.text = resources.getString(R.string.start_time)
        mseek_bar!!.progress = 0
        mcurrent_progress_bar.progress = 0
        seekTo(0f)
        mediaPlayer!!.start()
        imv_play!!.visibility = View.VISIBLE
    }

    /**
     * 销毁
     */
    fun destory() {
        handler.removeCallbacks(runnable)
        isPlaying = false
        mediaPlayer!!.stop()
        mediaPlayer!!.release()
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
//            try {
            //这里要用FileProvider获取的Uri
            if (url.contains("http")) {
                retriever.setDataSource(url, HashMap())
            } else {
                retriever.setDataSource(url)
            }
            bitmap = retriever.frameAtTime

            val finalBitmap = bitmap
            Observable.empty<Any>().subscribeOn(AndroidSchedulers.mainThread())
                    .doOnComplete { place_holder!!.setImageBitmap(finalBitmap) }.subscribe()

//            } catch (ex: Exception) {
//                ex.printStackTrace()
//            } finally {
//                try {
            retriever.release()
//                } catch (ex: RuntimeException) {
//                    ex.printStackTrace()
//                }
//
//            }
        }).start()
    }

    /**
     * 当前播放进度
     *
     * @return
     */
    fun playCurrentTime(): String {
        val currentPosition = mediaPlayer!!.getCurrentPosition()
        val scale = (currentPosition * 1.0 / mediaPlayer!!.getDuration()).toFloat()
        mseek_bar!!.progress = (scale * 100).toInt()
        mcurrent_progress_bar!!.progress = (scale * 100).toInt()
        return stringForTime(currentPosition)
    }


    /**
     * 视频的总长度
     *
     * @return
     */
    fun durationTime(): String {
        return stringForTime(mediaPlayer!!.getDuration())
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

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

    }

    override fun onStop() {
        pause()
//        playProgress = mediaPlayer!!.currentPosition;
        super.onStop()
    }

    override fun onDestroy() {
        destory()
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