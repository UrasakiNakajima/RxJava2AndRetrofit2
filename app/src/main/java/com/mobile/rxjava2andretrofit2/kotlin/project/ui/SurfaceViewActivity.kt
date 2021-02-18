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
import com.mobile.rxjava2andretrofit2.R
import com.mobile.rxjava2andretrofit2.base.BaseAppActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_surface_view.*
import android.net.Uri


class SurfaceViewActivity : BaseAppActivity() {

    companion object {
        private val TAG: String = "SurfaceViewActivity"
    }

    /*测试地址*/
//    val url = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
    val url = "http://rbv01.ku6.com/omtSn0z_PTREtneb3GRtGg.mp4";
    //    val url = "http://rbv01.ku6.com/7lut5JlEO-v6a8K3X9xBNg.mp4";
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
    private var isPlaying: Int = 0
//    private var playProgress: Int = 0;

    override fun initLayoutId(): Int {
        return com.mobile.rxjava2andretrofit2.R.layout.activity_surface_view
    }

    override fun initData() {

    }

    override fun initViews() {
        surface_view.setOnClickListener(View.OnClickListener {
            if (isPlaying == 1) {
                pause()
            } else {
//                if (playProgress != 0) {
//
//                }
                start()
            }
        })

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
        if (isPlaying == 1) {
            tv_current_time!!.text = playCurrentTime()
            if (currentPosition == oldPosition) {
                progress_circular!!.setVisibility(View.VISIBLE)
            } else {
                progress_circular!!.setVisibility(View.GONE)
                if (placeholder != null)
                    placeholder!!.visibility = View.GONE
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
        if (isPlaying != 1) {
            mediaPlayer!!.start()
            isPlaying = 1
        }
        play!!.visibility = View.GONE

//        placeholder.visibility = View.VISIBLE
        progress_circular.setVisibility(View.VISIBLE)
    }

    /**
     * 暂停播放
     */
    fun pause() {
        if (isPlaying == 1) {
//            playProgress = mediaPlayer!!.currentPosition;
            mediaPlayer!!.pause()
            isPlaying = 2
        }
        play!!.visibility = View.VISIBLE
    }

    /**
     * 重新播放
     */
    fun resetStart() {
        if (isCompletion) {
            mediaPlayer!!.seekTo(0)
            isCompletion = false
            mediaPlayer!!.start()
            play!!.visibility = View.INVISIBLE
        }
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
                    tv_total_time!!.setText(durationTime())
                    sendTime()
//                    start()
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
                    if (isPlaying == 1) {
                        if (progress_circular != null) {
                            progress_circular.setVisibility(View.GONE)
                            play!!.visibility = View.GONE
                            lay_playControl!!.visibility = View.VISIBLE
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
                isPlaying = 3
                placeholder!!.visibility = View.VISIBLE
                reset()
            }
        })

        surface_view!!.setOnClickListener(View.OnClickListener {
            if (isPlaying == 1) {
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
        mSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

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
        isPlaying = 3
    }


    /**
     * 指定位置播放
     *
     * @param m
     */
    fun seekTo(m: Float) {
        play!!.setVisibility(View.GONE)
        if (mediaPlayer != null) {
            mediaPlayer!!.seekTo(m.toInt())
        }
    }

    /**
     * 重置
     */
    fun reset() {
        isPlaying = 0
        tv_current_time!!.text = resources.getString(com.mobile.rxjava2andretrofit2.R.string.start_time)
        mSeekBar!!.progress = 0
        mCurrentProgressBar.progress = 0
        seekTo(0f)
        play!!.visibility = View.VISIBLE
    }

    /**
     * 销毁
     */
    fun destory() {
        handler.removeCallbacks(runnable)
        isPlaying = 0
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
                    .doOnComplete { placeholder!!.setImageBitmap(finalBitmap) }.subscribe()

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
        mSeekBar!!.progress = (scale * 100).toInt()
        mCurrentProgressBar!!.progress = (scale * 100).toInt()
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