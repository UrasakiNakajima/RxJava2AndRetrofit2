package com.mobile.rxjava2andretrofit2.custom_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.mobile.rxjava2andretrofit2.R;

import java.util.Formatter;
import java.util.HashMap;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;

public class MaterialVideoView extends LinearLayout implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnInfoListener {

    /*测试地址*/
    public static final String url = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    public static final int VIDEO_TYPE_URI = 1;
    public static final int VIDEO_TYPE_FILE_PATH = 2;
    public int VIDEO_TYPE;
    private boolean isCompletion = false;
    private SeekBar mSeekBar;
    private ProgressBar progressBar;
    private MyVideoView videoView;
    private ImageView play;
    private LinearLayout lay_playControl;
    private TextView tv_current_time;
    private TextView tv_total_time;
    private ImageView placeholder;
    private ProgressBar mCurrentProgressBar;

    private int downX;
    private int moveX;
    private boolean left;

    private boolean isTrackingTouch = false;

    private int oldPosition;
    //将长度转换为时间
    StringBuilder mFormatBuilder = new StringBuilder();
    Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public MaterialVideoView(Context context) {
        this(context, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public MaterialVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public MaterialVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        setBackgroundColor(getResources().getColor(R.color.black));
        View view = LayoutInflater.from(context).inflate(R.layout.material_video_layout, null);
        initView(view);
        setData(url, VIDEO_TYPE_URI);
        addView(view);
    }


    /**
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initView(View view) {
        tv_total_time = view.findViewById(R.id.tv_total_time);
        tv_current_time = view.findViewById(R.id.tv_current_time);
        lay_playControl = view.findViewById(R.id.lay_playControl);
        mSeekBar = view.findViewById(R.id.mSeekBar);
        mCurrentProgressBar = view.findViewById(R.id.mCurrentProgressBar);
        placeholder = view.findViewById(R.id.placeholder);
        seekBarListener();
        progressBar = view.findViewById(R.id.progress_circular);
        play = view.findViewById(R.id.play);
        videoView = view.findViewById(R.id.mVideoView);
        videoView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    pause();
                } else {
                    start();
                }
            }
        });


        videoView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = (int) event.getRawX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        moveX = (int) (event.getRawX() - downX);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (moveX < 0 && Math.abs(moveX) > ViewConfiguration.getTouchSlop()) {
                            left = true;
                        } else {
                            left = false;
                        }
                        if (onMoveListener != null && left)
                            onMoveListener.onMove();
//                        LogUtils.LOG_V("====left===="+left);
                        break;
                }
                return false;
            }
        });
    }


    /**
     * @param visibility
     */
    public void setPlaceholder(int visibility) {
        if (placeholder != null)
            placeholder.setVisibility(visibility);
    }

    /**
     *
     */
    private void seekBarListener() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isTrackingTouch) {
                    float scale = (float) (progress * 1.0 / 100);
                    float msec = videoView.getDuration() * scale;
                    seekTo(msec);
                }
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTrackingTouch = true;
                pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isTrackingTouch = false;
                start();
            }
        });
    }


    /**
     * 暂停播放
     */
    public void pause() {
        if (videoView != null && videoView.isPlaying()) {
            videoView.pause();
        }
        play.setVisibility(View.VISIBLE);
    }


    /**
     * 指定位置播放
     *
     * @param m
     */
    public void seekTo(float m) {
        play.setVisibility(GONE);
        if (videoView != null) {
            videoView.seekTo((int) m);
        }
    }


    /**
     * 开始播放
     */
    public void start() {
        if (!videoView.isPlaying()) {
            videoView.start();
        }
        play.setVisibility(View.GONE);


        if (placeholder != null) {
            placeholder.setVisibility(VISIBLE);
            progressBar.setVisibility(VISIBLE);
        }
    }


    /**
     * 重新播放
     */
    public void reStart() {
        if (videoView != null && isCompletion) {
            videoView.seekTo(0);
            isCompletion = false;
            videoView.start();
            play.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * 设置视频源
     *
     * @param url
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setData(String url, int TYPE) {
        this.VIDEO_TYPE = TYPE;
        switch (TYPE) {
            case VIDEO_TYPE_FILE_PATH:
                /*设置播放源*/
                videoView.setVideoPath(url);
                break;
            case VIDEO_TYPE_URI:
                /*设置播放源*/
                videoView.setVideoURI(Uri.parse(url));
                break;
        }
        getPreviewImage(url);
        /*准备完成后回调*/
        videoView.setOnPreparedListener(this);
        /*播放内容监听*/
        videoView.setOnInfoListener(this);
        /*播放完成回调*/
        videoView.setOnCompletionListener(this);

        videoView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    pause();
                } else {
                    start();
                }
            }
        });
    }


    /**
     * 停止播放
     */
    public void stopLoad() {
        videoView.canPause();
        videoView.stopPlayback();
    }


    /**
     * 重置
     */
    public void reset() {
        tv_current_time.setText(getResources().getString(R.string.start_time));
        mSeekBar.setProgress(0);
        mCurrentProgressBar.setProgress(0);
        seekTo(0);
        play.setVisibility(VISIBLE);
    }


    /**
     * 销毁
     */
    public void destory() {
        handler.removeCallbacks(runnable);
        videoView.stopPlayback();
        videoView = null;
    }


    /**
     * 视频准完成
     *
     * @param mp
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        tv_total_time.setText(durationTime());
        sendTime();
    }


    /**
     * 显示预览图片
     *
     * @param url
     */
    public void getPreviewImage(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                Bitmap bitmap = null;
                try {
                    //这里要用FileProvider获取的Uri
                    if (url.contains("http")) {
                        retriever.setDataSource(url, new HashMap<String, String>());
                    } else {
                        retriever.setDataSource(url);
                    }
                    bitmap = retriever.getFrameAtTime();

                    final Bitmap finalBitmap = bitmap;
                    Observable.empty().subscribeOn(AndroidSchedulers.mainThread())
                            .doOnComplete(new Action() {
                                @Override
                                public void run() {
                                    placeholder.setImageBitmap(finalBitmap);
                                }
                            }).subscribe();

                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        retriever.release();
                    } catch (RuntimeException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }).start();
    }


    /**
     * 视频播放完成
     *
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        isCompletion = true;
        placeholder.setVisibility(VISIBLE);
        reset();
    }


    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
            if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        } else {
            if (videoView.isPlaying()) {
                if (progressBar != null) progressBar.setVisibility(View.GONE);
                play.setVisibility(View.GONE);
                lay_playControl.setVisibility(GONE);
            }
        }
        return true;
    }


    private Handler handler = new Handler();

    public void sendTime() {
        handler.postDelayed(runnable, 200);
    }

    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            sendTime();
            int currentPosition = videoView.getCurrentPosition();
            if (videoView.isPlaying()) {
                tv_current_time.setText(playCurrentTime());
                if (currentPosition == oldPosition) {
                    progressBar.setVisibility(VISIBLE);
                } else {
                    progressBar.setVisibility(GONE);
                    if (placeholder != null)
                        placeholder.setVisibility(GONE);
                }
                oldPosition = currentPosition;
            }
        }
    };


    /**
     * 当前播放进度
     *
     * @return
     */
    public String playCurrentTime() {
        int CurrentPosition = videoView.getCurrentPosition();
        float scale = (float) ((CurrentPosition * 1.0) / videoView.getDuration());
        mSeekBar.setProgress((int) (scale * 100));
        mCurrentProgressBar.setProgress((int) (scale * 100));
        return stringForTime(CurrentPosition);
    }


    /**
     * 视频的总长度
     *
     * @return
     */
    public String durationTime() {
        return stringForTime(videoView.getDuration());
    }

    /**
     * 将长度转换为时间
     *
     * @param timeMs
     * @return
     */
    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }


    private OnMoveListener onMoveListener;

    public void setOnMoveListener(OnMoveListener onMoveListener) {
        this.onMoveListener = onMoveListener;
    }

    public interface OnMoveListener {
        void onMove();
    }

}
