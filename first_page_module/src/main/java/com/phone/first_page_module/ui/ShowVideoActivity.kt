package com.phone.first_page_module.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.gyf.immersionbar.ImmersionBar
import com.phone.common_library.callback.OnDialogCallback
import com.phone.common_library.callback.OnDownloadListener
import com.phone.common_library.manager.DownloadManger
import com.phone.common_library.manager.LogManager
import com.phone.first_page_module.R
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.cache.CacheFactory
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.shuyu.gsyvideoplayer.utils.Debuger
import com.shuyu.gsyvideoplayer.utils.GSYVideoType
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import kotlinx.android.synthetic.main.activity_show_video.*
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager
import tv.danmaku.ijk.media.exo2.ExoPlayerCacheManager

/**
 *    author : Urasaki
 *    e-mail : 1164688204@qq.com
 *    date   : 2021/5/17 9:50
 *    desc   :
 *    version: 1.0
 */
class ShowVideoActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ShowVideoActivity"
    }

    private var manager: DownloadManger? = null
    private var dialog: ShowDownloadDialogFragment? = null
    private var mBundle: Bundle? = null
    private var url: String? = null
    private var suffix: String? = null
    private var orientationUtils: OrientationUtils? = null
    private var isPlay = false
    private var isPause = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_video)

        initData();
        initViews();
    }

    private fun initData() {
        mBundle = intent.extras;
        if (mBundle != null) {
            url = mBundle!!.getString("url");
            suffix = mBundle!!.getString("suffix");
        }
        manager = DownloadManger.get();
    }

    private fun initViews() {
        tev_title.setText(resources.getString(R.string.video_play))
        imv_share.setColorFilter(ContextCompat.getColor(this, R.color.color_FF474747))
        setToolbar(true, R.color.white);

        dialog = ShowDownloadDialogFragment.newInstance();
        initVideo(url!!)

        layout_back.setOnClickListener {
            finish()
        }
        layout_share.setOnClickListener {
            dialog!!.setOnDialogCallback(object : OnDialogCallback<Int> {

                override fun onDialogClick(view: View?, success: Int?) {
                    if (success != null) {
                        if (success == 1) {
                            progress_bar.visibility = View.VISIBLE
                            manager!!.download(url!!, suffix!!, getExternalFilesDir(null)!!.getAbsolutePath(),
                                    object : OnDownloadListener {
                                        override fun onDownloadSuccess() {
                                            Toast.makeText(this@ShowVideoActivity, resources.getString(R.string.saved_successfully), Toast.LENGTH_LONG)
                                            progress_bar.visibility = View.GONE
                                        }

                                        override fun onDownloading(progress: Int) {
                                            LogManager.i(TAG, "progress*****$progress")

                                            progress_bar.setProgress(progress);
                                        }

                                        override fun onDownloadError() {
                                            Toast.makeText(this@ShowVideoActivity, resources.getString(R.string.failed_to_save_please_try_again), Toast.LENGTH_LONG)
                                            progress_bar.visibility = View.GONE
                                        }
                                    })
                        }
                    }
                }

                override fun onDialogClick(view: View?, success: Int?, params: MutableMap<String, String>?) {

                }
            })
            dialog!!.show(supportFragmentManager, "FOF")
        }
    }

    protected fun setToolbar(isDarkFont: Boolean, color: Int) {
        if (isDarkFont) {
            ImmersionBar.with(this) //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    .statusBarDarkFont(isDarkFont)
                    .statusBarColor(color) //???????????????????????????????????????
                    //                    .autoStatusBarDarkModeEnable(true, 0.2f) //?????????????????????????????????????????????????????????????????????????????????
                    .init()
        } else {
            ImmersionBar.with(this)
                    .statusBarDarkFont(isDarkFont)
                    .statusBarColor(color) //???????????????????????????????????????
                    //                    .autoStatusBarDarkModeEnable(true, 0.2f) //?????????????????????????????????????????????????????????????????????????????????
                    .init()
        }
    }


    private fun initVideo(videoUrl: String) {
        //????????????????????????????????????
        orientationUtils = OrientationUtils(this, detail_player)
        //?????????????????????????????????
        orientationUtils!!.setEnable(false)

        //????????????
        val imvVideo = ImageView(this)
        imvVideo.scaleType = ImageView.ScaleType.FIT_XY
        imvVideo.setImageResource(R.mipmap.ic_launcher)
        PlayerFactory.setPlayManager(Exo2PlayerManager::class.java) //EXO??????
        CacheFactory.setCacheManager(ExoPlayerCacheManager::class.java) //exo?????????????????????m3u8????????????exo
        val gsyVideoOption = GSYVideoOptionBuilder()
        gsyVideoOption
                .setThumbImageView(imvVideo)
                .setIsTouchWiget(true)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setAutoFullWithSize(true)
                .setShowFullAnimation(false)
                .setNeedLockFull(true)
                .setUrl(videoUrl)
                .setCacheWithPlay(false)
                .setVideoTitle("")
                .setVideoAllCallBack(object : GSYSampleCallBack() {
                    override fun onPrepared(url: String, vararg objects: Any) {
                        super.onPrepared(url, *objects)
                        //????????????????????????????????????
                        orientationUtils!!.setEnable(true)
                        isPlay = true
                    }

                    override fun onQuitFullscreen(url: String, vararg objects: Any) {
                        super.onQuitFullscreen(url, *objects)
                        Debuger.printfError("***** onQuitFullscreen **** " + objects[0]) //title
                        Debuger.printfError("***** onQuitFullscreen **** " + objects[1]) //???????????????player
                        if (orientationUtils!! != null) {
                            orientationUtils!!.backToProtVideo()
                        }
                    }
                }).setLockClickListener { view, lock ->
                    if (orientationUtils != null) {
                        //???????????????onConfigurationChanged
                        orientationUtils!!.setEnable(!lock)
                    }
                }.build(detail_player)
        detail_player.getFullscreenButton().setOnClickListener(View.OnClickListener { //????????????
            orientationUtils!!.resolveByClick()

            //?????????true??????????????????actionbar????????????true??????????????????statusbar
            detail_player.startWindowFullscreen(this, true, true)
        })

        //?????????????????????????????????????????????surface_container????????????FrameLayout
        GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL)
    }

    override fun onBackPressed() {
        if (orientationUtils != null) {
            orientationUtils!!.backToProtVideo();
        }
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed()
    }

    override fun onResume() {
        detail_player.getCurrentPlayer().onVideoResume(false);
        super.onResume();
        isPause = false;
    }

    override fun onPause() {
        detail_player.getCurrentPlayer().onVideoPause();
        super.onPause();
        isPause = true;
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isPlay) {
            detail_player.getCurrentPlayer().release();
        }
        if (orientationUtils != null) {
            orientationUtils!!.releaseListener();
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        //????????????????????????
        if (isPlay && !isPause) {
            detail_player.onConfigurationChanged(this, newConfig, orientationUtils, true, true);
        }
    }
}