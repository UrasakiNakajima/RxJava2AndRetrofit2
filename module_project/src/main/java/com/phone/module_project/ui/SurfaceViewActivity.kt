package com.phone.module_project.ui

import android.graphics.Bitmap
import android.media.AudioManager
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.*
import com.phone.library_common.base.BaseRxAppActivity
import com.phone.library_common.manager.LogManager
import com.phone.library_common.manager.ResourcesManager
import com.phone.module_project.R
import com.phone.rxjava2andretrofit2.manager.VideoImageManager
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.concurrent.timerTask


class SurfaceViewActivity : BaseRxAppActivity() {

    companion object {
        @JvmStatic
        private val TAG: String = SurfaceViewActivity::class.java.simpleName
    }

    private var surfaceView: SurfaceView? = null
    private var mcurrentProgressBar: ProgressBar? = null
    private var imvPlaceHolder: ImageView? = null
    private var imvPlay: ImageView? = null
    private var progressCircular: ProgressBar? = null
    private var layoutPlayControl: LinearLayout? = null
    private var tevCurrentTime: TextView? = null
    private var mseekBar: SeekBar? = null
    private var tevTotalTime: TextView? = null

    /*测试地址*/
//    val url = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
//    val url = "http://rbv01.ku6.com/omtSn0z_PTREtneb3GRtGg.mp4"
//    val url = "http://rbv01.ku6.com/7lut5JlEO-v6a8K3X9xBNg.mp4"
//    val url = "https://t-cmcccos.cxzx10086.cn/statics/shopping/hidden_corner.mp4"
    val url = "https://t-cmcccos.cxzx10086.cn/statics/shopping/detective_conan_japanese.mp4"
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

    //    private var playProgress: Int = 0
    private var timer: Timer? = null
    private var timerTask2: TimerTask? = null

    //    private var timerImvPlay: Timer? = null
//    private var timerTaskImvPlay: TimerTask? = null
    private var isShowImvPlay: Boolean = false

    //    private var isStopTimerImvPlay: Boolean = false
    private var disposable: Disposable? = null

    override fun initLayoutId() = R.layout.project_activity_surface_view

    override fun initData() {
//        playProgress = 0
    }

    override fun initViews() {
        surfaceView = findViewById(R.id.surface_view)
        mcurrentProgressBar = findViewById(R.id.mcurrent_progress_bar)
        imvPlaceHolder = findViewById(R.id.imv_place_holder)
        imvPlay = findViewById(R.id.imv_play)
        progressCircular = findViewById(R.id.progress_circular)
        layoutPlayControl = findViewById(R.id.layout_play_control)
        tevCurrentTime = findViewById(R.id.tev_current_time)
        mseekBar = findViewById(R.id.mseek_bar)
        tevTotalTime = findViewById(R.id.tev_total_time)

        progressCircular?.visibility = View.VISIBLE
        surfaceView?.holder?.setKeepScreenOn(true)
        surfaceView?.holder?.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        surfaceView?.holder?.addCallback(object : SurfaceHolder.Callback {

            override fun surfaceCreated(holder: SurfaceHolder) {
                setVideoParam(url, VIDEO_TYPE_URI)
                seekBarListener()
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {

            }

        })
    }

    override fun initLoadData() {

    }

    /**
     * 设置视频参数
     *
     * @u
     */
    fun setVideoParam(url: String, TYPE: Int) {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.reset()
        mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        val uri = Uri.parse(url)
        this.VIDEO_TYPE = TYPE
        when (TYPE) {
            VIDEO_TYPE_FILE_PATH ->
                /*设置播放源*/
                mediaPlayer?.setDataSource(this, uri)
            VIDEO_TYPE_URI ->
                /*设置播放源*/
                mediaPlayer?.setDataSource(this, uri)
        }
        mediaPlayer?.setDisplay(surfaceView?.holder)

        disposable = Flowable.create(FlowableOnSubscribe<Bitmap> { emitter ->
            val bitmap: Bitmap =
                VideoImageManager.getVideoImage(this@SurfaceViewActivity, url, false)!!

            emitter.onNext(bitmap)
            emitter.onComplete()
        }, BackpressureStrategy.BUFFER)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Consumer<Bitmap> {
                @Throws(Exception::class)
                override fun accept(b: Bitmap) {
                    imvPlaceHolder?.setImageBitmap(b)
                    mediaPlayer!!.prepareAsync()
                    /*准备完成后回调*/
                    mediaPlayer!!.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
                        override fun onPrepared(mp: MediaPlayer?) {
                            if (mediaPlayer != null) {
                                isPlaying = false
                                tevTotalTime?.setText(durationTime())
//                                    sendTime()
                                startTimer()
                                progressCircular?.visibility = View.GONE
                                imvPlay?.visibility = View.VISIBLE
                                layoutPlayControl?.visibility = View.VISIBLE
                            }
                        }

                    })

//                        //播放内容监听
//                        mediaPlayer!!.setOnInfoListener((object : MediaPlayer.OnInfoListener {
//                            override fun onInfo(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
//                                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
//                                  progressCircular?.visibility = View.VISIBLE
//                                } else {
//                                    if (isPlaying) {
//                                     progressCircular?.visibility = View.GONE
////                                            imvPlay?.visibility = View.GONE
////                                            layoutPlayControl?.visibility = View.GONE
//                                    }
//                                }
//                                return true
//                            }
//
//                        }))

//                        //播放缓冲监听
//                        mediaPlayer!!.setOnBufferingUpdateListener(object : MediaPlayer.OnBufferingUpdateListener {
//                            override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
//
//                            }
//
//                        })

                    //播放完成回调
                    mediaPlayer!!.setOnCompletionListener(object :
                        MediaPlayer.OnCompletionListener {
                        override fun onCompletion(mp: MediaPlayer?) {
                            isCompletion = true
                            isPlaying = false
                            imvPlaceHolder?.visibility = View.VISIBLE
                            imvPlay?.visibility = View.VISIBLE
                            layoutPlayControl?.visibility = View.VISIBLE
//                                resetPlay()
                            resetVideo()
                        }
                    })

                    surfaceView?.setOnClickListener(View.OnClickListener {
                        if (isPlaying) {
                            if (isShowImvPlay) {
                                isShowImvPlay = false
//                                    stopTimerImvPlay()
                                imvPlay?.visibility = View.GONE
                                layoutPlayControl?.visibility = View.GONE
                                LogManager.i(TAG, "surfaceView? OnClickListener")
                            } else {
                                isShowImvPlay = true
//                                    stopTimerImvPlay()
//                                    startTimerImvPlay()
                                imvPlay?.visibility = View.VISIBLE
                                layoutPlayControl?.visibility = View.VISIBLE
                            }
                        }
                    })
                    imvPlay?.setOnClickListener(View.OnClickListener {
                        if (isPlaying) {
                            pausePlay()
//                                stopTimerImvPlay()
                            isShowImvPlay = false
                            imvPlay?.visibility = View.VISIBLE
                            layoutPlayControl?.visibility = View.VISIBLE
                        } else {
                            startPlay()
//                                startTimerImvPlay()
                            isShowImvPlay = true
                        }
                    })
                }
            })

    }

    /**
     *
     */
    private fun seekBarListener() {
        mseekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (isTrackingTouch) {
                    val scale = (progress * 1.0 / 100).toFloat()
                    val msec = mediaPlayer!!.duration * scale
                    seekTo(msec)
                }
            }


            override fun onStartTrackingTouch(seekBar: SeekBar) {
//                stopTimerImvPlay()
                isTrackingTouch = true
                pausePlay()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                isTrackingTouch = false
                startPlay()
            }
        })
    }

//    private val handler = Handler()
//
//    private val runnable = Runnable {
//        sendTime()
//        val currentPosition = mediaPlayer!!.currentPosition
//        if (isPlaying) {
//            tevCurrentTime?!!.text = playCurrentTime()
//            if (currentPosition == oldPosition) {
//                progressCircular?.visibility = View.VISIBLE
//            } else {
//                progressCircular?.visibility = View.GONE
//                 imvPlaceHolder?.visibility = View.GONE
//            }
//            oldPosition = currentPosition
//        }
//    }
//
//    fun sendTime() {
//        handler.postDelayed(runnable, 200)
//    }

    private fun startTimer() {
        timer = Timer()

        timerTask2 = timerTask {
            LogManager.i(TAG, "startTimer current thread***" + Thread.currentThread().name)
            Flowable.create(FlowableOnSubscribe<String> { emitter ->
                emitter.onNext("1")
                LogManager.i(TAG, "Flowable.create current thread***" + Thread.currentThread().name)
                emitter.onComplete()
            }, BackpressureStrategy.BUFFER)
//                    .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Consumer<String> {
                    @Throws(Exception::class)
                    override fun accept(s: String) {
                        LogManager.i(TAG, "accept current thread***" + Thread.currentThread().name)
                        val currentPosition = mediaPlayer!!.currentPosition
                        if (isPlaying) {
                            tevCurrentTime?.text = playCurrentTime()
                            if (currentPosition == oldPosition) {
                                if (isPlaying) {
                                    progressCircular?.visibility = View.VISIBLE
                                } else {
                                    progressCircular?.visibility = View.GONE
                                }
                            } else {
                                progressCircular?.visibility = View.GONE
                                imvPlaceHolder?.visibility = View.GONE
                            }
                            oldPosition = currentPosition
                        }
                    }
                })
        }

        timer!!.schedule(timerTask2, 0, 500)


//        timer = fixedRateTimer("", false, 0, 200) {
//            //            Observable.create(ObservableOnSubscribe<String> { emitter ->
////                emitter.onNext("")
////                emitter.onComplete()
////            }).subscribeOn(Schedulers.io())
////                    .observeOn(AndroidSchedulers.mainThread())
////                    .subscribe(object : Consumer<String> {
////                        @Throws(Exception::class)
////                        override fun accept(s: String) {
////
////                            val currentPosition = mediaPlayer!!.currentPosition
////                            if (isPlaying) {
////                                tevCurrentTime?!!.text = playCurrentTime()
////                                if (currentPosition == oldPosition) {
////                                    progressCircular?.visibility = View.VISIBLE
////                                } else {
////                                    progressCircular?.visibility = View.GONE
////                                    imvPlaceHolder?.visibility = View.GONE
////                                }
////                                oldPosition = currentPosition
////                            }
////                        }
////                    })
//        }
    }

    private fun stopTimer() {
        if (timer != null) {
            timer!!.cancel()
        }

        if (timerTask2 != null) {
            timerTask2!!.cancel()
        }
    }

//    private fun startTimerImvPlay() {
//        timerImvPlay = Timer()
//
//        timerTaskImvPlay = timerTask {
//            Flowable.create(FlowableOnSubscribe<String> { emitter ->
//                emitter.onNext("1")
//                emitter.onComplete()
//            }, BackpressureStrategy.BUFFER)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(object : Consumer<String> {
//                        @Throws(Exception::class)
//                        override fun accept(s: String) {
//
//                            if (!isStopTimerImvPlay) {
//                                imvPlay?.visibility = View.GONE
//                                layoutPlayControl?.visibility = View.GONE
//                                LogManager.i(TAG, "startTimerImvPlay accept")
//                                isShowImvPlay = false
//                                stopTimerImvPlay()
//                            }
//                        }
//                    })
//        }
//
//        timerImvPlay!!.schedule(timerTaskImvPlay!!, 3500)
//        isStopTimerImvPlay = false
//    }
//
//    private fun stopTimerImvPlay() {
//        if (timerImvPlay != null) {
//            timerImvPlay!!.cancel()
//        }
//
//        if (timerTaskImvPlay != null) {
//            timerTaskImvPlay!!.cancel()
//        }
//        isStopTimerImvPlay = true
//    }

    /**
     * 开始播放
     */
    fun startPlay() {
        if (!isPlaying && mediaPlayer != null) {
            mediaPlayer!!.start()
            isPlaying = true
        }
//        stopTimerImvPlay()
        imvPlay?.visibility = View.GONE
        layoutPlayControl?.visibility = View.VISIBLE
//        startTimerImvPlay()
        progressCircular?.visibility = View.GONE
    }

//    /**
//     * 开始播放
//     */
//    fun startPlay(msec: Int) {
//        if (!isPlaying) {
//            mediaPlayer!!.seekTo(msec)
//            mediaPlayer!!.start()
//            isPlaying = true
//        }
//        imvPlay?.visibility = View.GONE
//
////        imvPlaceHolder?.visibility = View.VISIBLE
//        progressCircular?.visibility = View.VISIBLE
//    }

    /**
     * 暂停播放
     */
    fun pausePlay() {
        if (isPlaying && mediaPlayer != null) {
//            playProgress = mediaPlayer!!.currentPosition
            mediaPlayer!!.pause()
            isPlaying = false
        }
//        stopTimerImvPlay()
        imvPlay?.visibility = View.VISIBLE
        layoutPlayControl?.visibility = View.VISIBLE
        progressCircular?.visibility = View.GONE
    }

    /**
     * 指定位置播放
     *
     * @param m
     */
    fun seekTo(m: Float) {
        if (mediaPlayer != null) {
            mediaPlayer!!.seekTo(m.toInt())
        }
//        imvPlay?.visibility = View.VISIBLE
//        layoutPlayControl?.visibility = View.VISIBLE
//        startTimerImvPlay()
    }

    /**
     * 停止播放
     */
    fun stopPlay() {
        mediaPlayer!!.stop()
        isPlaying = false
    }

    /**
     * 重新播放
     */
    fun resetStartPlay() {
        if (mediaPlayer != null && isCompletion) {
            mseekBar?.progress = 0
            mcurrentProgressBar?.progress = 0
            pausePlay()
            seekTo(0f)
            if (mediaPlayer!!.duration / 1000 / 60 / 60 >= 1) {
                tevCurrentTime?.text = ResourcesManager.getString(R.string.start_time)
            } else {
                tevCurrentTime?.text = ResourcesManager.getString(R.string.start_time2)
            }
            startPlay()
        }
    }

    /**
     * 重置
     */
    fun resetVideo() {
        mseekBar?.progress = 0
        mcurrentProgressBar?.progress = 0
        pausePlay()
        seekTo(0f)
        if (mediaPlayer!!.duration / 1000 / 60 / 60 >= 1) {
            tevCurrentTime?.text = ResourcesManager.getString(R.string.start_time)
        } else {
            tevCurrentTime?.text = ResourcesManager.getString(R.string.start_time2)
        }
        progressCircular?.visibility = View.GONE
    }

    /**
     * 销毁
     */
    fun destoryPlay() {
//        handler.removeCallbacks(runnable)
        stopTimer()
        isPlaying = false
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
        }
        progressCircular?.visibility = View.GONE
    }

    /**
     * 显示预览图片
     *
     * @param url
     */
    fun getPreviewImage(url: String) {
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
        imvPlaceHolder?.setImageBitmap(finalBitmap)
        retriever.release()
    }

    /**
     * 当前播放进度
     *
     * @return
     */
    fun playCurrentTime(): String {
        val currentPosition = mediaPlayer!!.currentPosition
        val scale = (currentPosition * 1.0 / mediaPlayer!!.duration).toFloat()
        mseekBar?.progress = (scale * 100).toInt()
        mcurrentProgressBar?.progress = (scale * 100).toInt()
        return stringForTime(currentPosition)
    }


    /**
     * 视频的总长度
     *
     * @return
     */
    fun durationTime(): String? {
        if (mediaPlayer !== null) {
            if (mediaPlayer!!.duration / 1000 / 60 / 60 >= 1) {
                tevCurrentTime?.setText(getString(R.string.start_time))
            } else {
                tevCurrentTime?.setText(getString(R.string.start_time2))
            }
            return stringForTime(mediaPlayer!!.duration)
        } else {
            return null
        }
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
//        playProgress = mediaPlayer!!.currentPosition
        super.onStop()
    }

    override fun onDestroy() {
        destoryPlay()
        if (disposable != null) {
            disposable!!.dispose()
        }
        super.onDestroy()
    }

}