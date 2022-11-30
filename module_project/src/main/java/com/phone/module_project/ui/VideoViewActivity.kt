package com.phone.module_project.ui

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.phone.common_library.base.BaseRxAppActivity
import com.phone.common_library.custom_view.MineVideoView
import com.phone.module_project.R
import com.phone.rxjava2andretrofit2.manager.VideoImageManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import java.util.*
import kotlin.concurrent.fixedRateTimer
import io.reactivex.BackpressureStrategy
import io.reactivex.FlowableOnSubscribe
import io.reactivex.Flowable


class VideoViewActivity : BaseRxAppActivity() {

    companion object {
        private val TAG: String = VideoViewActivity::class.java.simpleName
    }

    private lateinit var mvideoView: MineVideoView
    private lateinit var mcurrentProgressBar: ProgressBar
    private lateinit var placeHolder: ImageView
    private lateinit var imvPlay: ImageView
    private lateinit var progressCircular: ProgressBar
    private lateinit var layoutPlayControl: LinearLayout
    private lateinit var tevCurrentTime: TextView
    private lateinit var mseekBar: SeekBar
    private lateinit var tevTotalTime: TextView

    /*测试地址*/
//    val url = "http://rbv01.ku6.com/7lut5JlEO-v6a8K3X9xBNg.mp4"
    val url = "https://t-cmcccos.cxzx10086.cn/statics/shopping/hidden_corner.mp4"
    val VIDEO_TYPE_URI = 1
    val VIDEO_TYPE_FILE_PATH = 2
    var VIDEO_TYPE: Int = 0
    private var isCompletion = false
    private var isTrackingTouch = false

    private var oldPosition: Int = 0

    //将长度转换为时间
    internal var mFormatBuilder = StringBuilder()
    internal var mFormatter = Formatter(mFormatBuilder, Locale.getDefault())
    private var timer: Timer? = null

    override fun initLayoutId(): Int {
        return R.layout.activity_video_view
    }

    override fun initData() {

    }

    override fun initViews() {
        mvideoView = findViewById(R.id.mvideo_view)
        mcurrentProgressBar = findViewById(R.id.mcurrent_progress_bar)
        placeHolder = findViewById(R.id.place_holder)
        imvPlay = findViewById(R.id.imv_play)
        progressCircular = findViewById(R.id.progress_circular)
        layoutPlayControl = findViewById(R.id.layout_play_control)
        tevCurrentTime = findViewById(R.id.tev_current_time)
        mseekBar = findViewById(R.id.mseek_bar)
        tevTotalTime = findViewById(R.id.tev_total_time)

        mseekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (isTrackingTouch) {
                    val scale = (progress * 1.0 / 100).toFloat()
                    val msec = mvideoView.duration * scale
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
        setVideoParam(url, VIDEO_TYPE_URI)
    }

    override fun initLoadData() {

    }

//    private val handler = Handler()
//
//    private val runnable = Runnable {
//        sendTime()
//        val currentPosition = mvideoView.currentPosition
//        if (mvideoView.isPlaying()) {
//            tevCurrentTime.text = playCurrentTime()
//            if (currentPosition == oldPosition) {
//                progressCircular.setVisibility(View.VISIBLE)
//            } else {
//                progressCircular.setVisibility(View.GONE)
//                if (placeHolder != null)
//                    placeHolder.visibility = View.GONE
//            }
//            oldPosition = currentPosition
//        }
//    }
//
//    fun sendTime() {
//        handler.postDelayed(runnable, 200)
//    }

    private fun startTimer() {
        timer = fixedRateTimer("", false, 0, 200) {
//            Observable.create(ObservableOnSubscribe<String> { emitter ->
//                emitter.onNext("1")
//                emitter.onComplete()
//            }).subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(object : Consumer<String> {
//                        @Throws(Exception::class)
//                        override fun accept(s: String) {
//                            val currentPosition = mvideoView.currentPosition
//                            if (mvideoView.isPlaying()) {
//                                tevCurrentTime.text = playCurrentTime()
//                                if (currentPosition == oldPosition) {
//                                    progressCircular.setVisibility(View.VISIBLE)
//                                } else {
//                                    progressCircular.setVisibility(View.GONE)
//                                    if (placeHolder != null)
//                                        placeHolder.visibility = View.GONE
//                                }
//                                oldPosition = currentPosition
//                            }
//                        }
//                    })


            Flowable.create(FlowableOnSubscribe<String> { emitter ->
                emitter.onNext("1")
                emitter.onComplete()
            }, BackpressureStrategy.BUFFER)
//                    .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Consumer<String> {
                    @Throws(Exception::class)
                    override fun accept(s: String) {
                        val currentPosition = mvideoView.currentPosition
                        if (mvideoView.isPlaying()) {
                            tevCurrentTime.text = playCurrentTime()
                            if (currentPosition == oldPosition) {
                                progressCircular.setVisibility(View.VISIBLE)
                            } else {
                                progressCircular.setVisibility(View.GONE)
                                if (placeHolder != null)
                                    placeHolder.visibility = View.GONE
                            }
                            oldPosition = currentPosition
                        }
                    }
                })
        }
    }

    private fun stopTimer() {
        timer?.cancel()
    }


    /**
     * 设置视频源
     *
     * @param url
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun setVideoParam(url: String, TYPE: Int) {
        placeHolder.setImageBitmap(VideoImageManager.getVideoImage(this, url, false))
//        getPreviewImage(url)
        this.VIDEO_TYPE = TYPE
        when (TYPE) {
            VIDEO_TYPE_FILE_PATH ->
                /*设置播放源*/
                mvideoView.setVideoPath(url)
            VIDEO_TYPE_URI ->
                /*设置播放源*/
                mvideoView.setVideoURI(Uri.parse(url))
        }

        /*准备完成后回调*/
        mvideoView.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
            override fun onPrepared(mp: MediaPlayer?) {
                tevTotalTime.setText(durationTime())
//                sendTime()
                startTimer()
            }

        })
        /*播放内容监听*/
        mvideoView.setOnInfoListener((object : MediaPlayer.OnInfoListener {
            override fun onInfo(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                    progressCircular.setVisibility(View.VISIBLE)
                } else {
                    if (mvideoView.isPlaying()) {
                        progressCircular.setVisibility(View.GONE)
                        imvPlay.visibility = View.GONE
                        layoutPlayControl.visibility = View.VISIBLE
                    }
                }
                return true
            }

        }))
        /*播放完成回调*/
        mvideoView.setOnCompletionListener(object : MediaPlayer.OnCompletionListener {
            override fun onCompletion(mp: MediaPlayer?) {
                isCompletion = true
                placeHolder.visibility = View.VISIBLE
                resetPlay()
            }

        })

        mvideoView.setOnClickListener {
            if (mvideoView.isPlaying()) {
                pausePlay()
            } else {
                startPlay()
            }
        }
    }

    /**
     * 指定位置播放
     *
     * @param m
     */
    fun seekTo(m: Float) {
        imvPlay.setVisibility(View.GONE)
        mvideoView.seekTo(m.toInt())
    }

    /**
     * 开始播放
     */
    fun startPlay() {
        if (!mvideoView.isPlaying()) {
            mvideoView.start()
        }
        imvPlay.visibility = View.GONE

//        placeHolder.visibility = View.VISIBLE
        progressCircular.setVisibility(View.VISIBLE)
    }

    /**
     * 暂停播放
     */
    fun pausePlay() {
        if (mvideoView.isPlaying()) {
            mvideoView.pause()
        }
        imvPlay.visibility = View.VISIBLE
    }

    /**
     * 重新播放
     */
    fun resetStartPlay() {
        if (isCompletion) {
            mvideoView.seekTo(0)
            isCompletion = false
            mvideoView.start()
            imvPlay.visibility = View.INVISIBLE
        }
    }

    /**
     * 重置
     */
    fun resetPlay() {
        tevCurrentTime.text = resources?.getString(R.string.start_time)
        mseekBar.progress = 0
        mcurrentProgressBar.progress = 0
        pausePlay()
        seekTo(0f)
    }

    /**
     * 停止播放
     */
    fun stopPlay() {
        mvideoView.canPause()
        mvideoView.stopPlayback()
    }

    /**
     * 销毁
     */
    fun destoryPlay() {
//        handler.removeCallbacks(runnable)
        stopTimer()
        mvideoView.stopPlayback()
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
                    placeHolder.setImageBitmap(finalBitmap)
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
        val CurrentPosition = mvideoView.currentPosition
        val scale = (CurrentPosition * 1.0 / mvideoView.duration).toFloat()
        mseekBar.progress = (scale * 100).toInt()
        mcurrentProgressBar.progress = (scale * 100).toInt()
        return stringForTime(CurrentPosition)
    }

    /**
     * 视频的总长度
     *
     * @return
     */
    fun durationTime(): String {
        return stringForTime(mvideoView.duration)
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

}