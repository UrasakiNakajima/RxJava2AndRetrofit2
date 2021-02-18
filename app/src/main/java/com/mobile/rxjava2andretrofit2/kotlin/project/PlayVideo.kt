package com.mobile.rxjava2andretrofit2.kotlin.project

import android.content.Intent
import android.content.BroadcastReceiver
import android.os.Build
import android.annotation.TargetApi
import android.view.SurfaceHolder
import android.content.Context
import android.media.MediaMetadataRetriever
import android.graphics.Bitmap
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Handler
import android.os.Message
import android.view.SurfaceView
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.mobile.rxjava2andretrofit2.R
import java.io.IOException


class PlayVideo {

    private var context: Context? = null
    private var videoView: View? = null
    private var mediaPlayer: MediaPlayer? = null
    private var isDisplay = true
    private var progressBroadCast: ProgressBroadCast? = null
    private var surfaceView: SurfaceView? = null
    private var btnPlay: TextView? = null
    //    private var btnPause: TextView? = null
//    private var btnStop: TextView? = null
    private var seekBar: SeekBar? = null
    private var tvTime: TextView? = null
    private var ivCover: ImageView? = null
    private var relaVideo: LinearLayout? = null
    private var llLoading: LinearLayout? = null
    /** 视频播放资源地址  */
    private var mediaUrl: String? = null

    constructor(context: Context, mediaUrl: String) {
        this.context = context
        this.mediaUrl = mediaUrl
        videoView = LayoutInflater.from(context).inflate(R.layout.custom_video_player, null)

        initView(videoView!!)
        event()
    }

//    private val handler: Handler = object : Handler() {
//        override fun handleMessage(msg: Message?) {
//            if (msg != null) {
//                ivCover!!.setImageBitmap(msg!!.obj as Bitmap)
//            }
//        }
//    }

    private val handler = object : Handler(object : Callback {

        override fun handleMessage(msg: Message?): Boolean {
            if (msg != null) {
                ivCover!!.setImageBitmap(msg!!.obj as Bitmap)
            }
            return false
        }
    }) {}

//    fun PlayVideo(context: Context, mediaUrl: String) {
//        this.context = context
//        this.mediaUrl = mediaUrl
//        videoView = LayoutInflater.from(context).inflate(R.layout.custom_video_player, null)
//        initView(videoView!!)
//        event()
//    }

    private fun initView(view: View) {
        surfaceView = view.findViewById(R.id.surfaceview)
        btnPlay = view.findViewById(R.id.btn_play)
//        btnPause = view.findViewById(R.id.btn_pause)
//        btnStop = view.findViewById(R.id.btn_pause)
        seekBar = view.findViewById(R.id.play_seekbar)
        tvTime = view.findViewById(R.id.tv_video_time)
        relaVideo = view.findViewById(R.id.rela_video)
        llLoading = view.findViewById(R.id.ll_video_loading)
        ivCover = view.findViewById(R.id.iv_videoplayer_cover)
        val listener = MyClickListener()
        surfaceView!!.setOnClickListener(listener)
        btnPlay!!.setOnClickListener(listener)

        progressBroadCast = ProgressBroadCast()
        context!!.registerReceiver(progressBroadCast, IntentFilter("play"))
        surfaceView!!.holder.setKeepScreenOn(true)
        surfaceView!!.holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        surfaceView!!.holder.addCallback(HolderCallBack())

        avaterThread()
    }

    private fun event() {
//        seekBar!!.setProgress(seekBar!!.progress)
        //seekbar调节进度
        seekBar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
//                if (mediaPlayer != null) {
//                    mediaPlayer!!.seekTo(seekBar.progress)
//                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar2: SeekBar) {
                if (mediaPlayer != null) {
                    mediaPlayer!!.seekTo(seekBar2.progress)
                    seekBar!!.setProgress(mediaPlayer!!.currentPosition)
                }
            }
        })
    }

    fun getVideoView(): View {
        return videoView!!
    }

    fun getReceiver(): BroadcastReceiver? {
        return progressBroadCast
    }

    fun getMediaPlayer(): MediaPlayer? {
        return mediaPlayer
    }

    private fun avaterThread() {
        object : Thread() {
            override fun run() {
                val mediaMetadataRetriever = MediaMetadataRetriever()
                if (Build.VERSION.SDK_INT >= 14) {    //需加入api判断，不然会报IllegalArgumentException
                    mediaMetadataRetriever.setDataSource(mediaUrl, HashMap())
                } else {
                    mediaMetadataRetriever.setDataSource(mediaUrl)
                }
                val bitmap = mediaMetadataRetriever.getFrameAtTime(1000, MediaMetadataRetriever.OPTION_CLOSEST)
                val message = Message()
                message.obj = bitmap
                handler.sendMessage(message)
            }
        }.start()
    }

    /**
     * 播放视频，先判断网络，是流量就要提示用户
     */
    private fun netWorkState() {
        mediaPlayer = MediaPlayer()
        PrepareThread().start()
        llLoading!!.setVisibility(View.VISIBLE)

    }

    internal inner class HolderCallBack : SurfaceHolder.Callback {

        override fun surfaceCreated(holder: SurfaceHolder) {

        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {

        }
    }

    internal inner class MyPrepareListener : MediaPlayer.OnPreparedListener {

        override fun onPrepared(mp: MediaPlayer) {
            if (null != mediaPlayer) {
                llLoading!!.setVisibility(View.GONE)
                mediaPlayer!!.start()
                ProgressThread().start()
            }
        }
    }

    internal inner class PrepareThread : Thread() {
        @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
        override fun run() {
            super.run()
            try {
                mediaPlayer!!.reset()
                mediaPlayer!!.setDataSource(mediaUrl)
                mediaPlayer!!.setDisplay(surfaceView!!.holder)
                mediaPlayer!!.prepareAsync()
                mediaPlayer!!.setOnPreparedListener(MyPrepareListener())
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    /**
     * 时间进度广播
     */
    inner class ProgressBroadCast : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val curPosition = intent.getIntExtra("position", 0)
            val maxLen = intent.getIntExtra("max", 0)
            seekBar!!.progress = curPosition
            seekBar!!.max = maxLen

            setTime(curPosition, maxLen)
        }

        /**
         * 秒转化为00:00形式
         * @param curPosition
         * @param maxLen
         */
        private fun setTime(curPosition: Int, maxLen: Int) {
            val cm = curPosition / 1000 / 60
            val cs = curPosition / 1000 % 60
            val mm = maxLen / 1000 / 60
            val ms = maxLen / 1000 % 60
            val builder = StringBuilder()
            builder.append(cm / 10).append(cm % 10).append(":")
                    .append(cs / 10).append(cs % 10).append("/")
                    .append(mm / 10).append(mm % 10).append(":")
                    .append(ms / 10).append(ms % 10)
            tvTime!!.text = builder.toString()
        }

    }

    /**
     * 发送播放进度线程
     */
    internal inner class ProgressThread : Thread() {
        override fun run() {
            while (null != mediaPlayer && mediaPlayer!!.isPlaying()) {
                val currentProgress = mediaPlayer!!.getCurrentPosition()
                val maxLen = mediaPlayer!!.getDuration()
                //每隔一秒发送一次播放进度
                val progressIntent = Intent("play")
                progressIntent.putExtra("position", currentProgress)
                progressIntent.putExtra("max", maxLen)
                context!!.sendBroadcast(progressIntent)
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
        }
    }

    internal inner class MyClickListener : View.OnClickListener {

        override fun onClick(v: View) {
            when (v.getId()) {
                R.id.surfaceview -> {
                    if (isDisplay) {
                        relaVideo!!.visibility = View.GONE
                    } else {
                        relaVideo!!.visibility = View.VISIBLE
                    }
                    isDisplay = !isDisplay
                }
                R.id.btn_play -> if (mediaPlayer == null) {
                    netWorkState()
                } else {
                    //播放和暂停切换
                    if (mediaPlayer!!.isPlaying()) {
                        mediaPlayer!!.pause()
//                        btnPlay!!.setBackgroundResource(R.mipmap.video_btn_pause)
                        btnPlay!!.setText("暂停")
                    } else {
                        mediaPlayer!!.start()
                        ivCover!!.setVisibility(View.GONE)
                        ProgressThread().start()
//                        btnPlay!!.setBackgroundResource(R.mipmap.video_btn_start)
                        btnPlay!!.setText("开始")
                    }
                }
            }
        }
    }

    fun onDestroy() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null)
        }
    }

}