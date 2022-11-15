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

    private lateinit var manager: DownloadManger
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

        initData()
        initViews()
    }

    private fun initData() {
        if (intent.extras != null) {
            mBundle = intent.extras!!
            url = mBundle?.getString("url")
            suffix = mBundle?.getString("suffix")
        }
        manager = DownloadManger.get()
    }

    private fun initViews() {
        tev_title.setText(resources.getString(R.string.video_play))
        imv_share.setColorFilter(ContextCompat.getColor(this, R.color.color_000000))
        setToolbar(true, R.color.white)

        dialog = ShowDownloadDialogFragment.newInstance()
        initVideo(url!!)

        layout_back.setOnClickListener {
            finish()
        }
        layout_share.setOnClickListener {
            dialog?.setOnDialogCallback(object : OnDialogCallback<Int> {

                override fun onDialogClick(view: View?, success: Int?) {
                    if (success != null) {
                        if (success == 1) {
                            progress_bar.visibility = View.VISIBLE
                            manager.download(url!!,
                                suffix!!,
                                getExternalFilesDir(null)!!.getAbsolutePath(),
                                object : OnDownloadListener {
                                    override fun onDownloadSuccess() {
                                        Toast.makeText(
                                            this@ShowVideoActivity,
                                            resources.getString(R.string.saved_successfully),
                                            Toast.LENGTH_LONG
                                        )
                                        progress_bar.visibility = View.GONE
                                    }

                                    override fun onDownloading(progress: Int) {
                                        LogManager.i(TAG, "progress*****$progress")

                                        progress_bar.setProgress(progress)
                                    }

                                    override fun onDownloadError() {
                                        Toast.makeText(
                                            this@ShowVideoActivity,
                                            resources.getString(R.string.failed_to_save_please_try_again),
                                            Toast.LENGTH_LONG
                                        )
                                        progress_bar.visibility = View.GONE
                                    }
                                })
                        }
                    }
                }

                override fun onDialogClick(
                    view: View?,
                    success: Int?,
                    params: MutableMap<String, String>?
                ) {

                }
            })
            dialog?.show(supportFragmentManager, "FOF")
        }
    }

    protected fun setToolbar(isDarkFont: Boolean, color: Int) {
        if (isDarkFont) {
            ImmersionBar.with(this) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .statusBarDarkFont(isDarkFont)
                .statusBarColor(color) //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .init()
        } else {
            ImmersionBar.with(this)
                .statusBarDarkFont(isDarkFont)
                .statusBarColor(color) //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .init()
        }
    }


    private fun initVideo(videoUrl: String?) {
        //外部辅助的旋转，帮助全屏
        orientationUtils = OrientationUtils(this, detail_player)
        //初始化不打开外部的旋转
        orientationUtils?.setEnable(false)

        //增加封面
        val imvVideo = ImageView(this)
        imvVideo.scaleType = ImageView.ScaleType.FIT_XY
        imvVideo.setImageResource(R.mipmap.ic_launcher)
        PlayerFactory.setPlayManager(Exo2PlayerManager::class.java) //EXO模式
        CacheFactory.setCacheManager(ExoPlayerCacheManager::class.java) //exo缓存模式，支持m3u8，只支持exo
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
                    //开始播放了才能旋转和全屏
                    orientationUtils?.setEnable(true)
                    isPlay = true
                }

                override fun onQuitFullscreen(url: String, vararg objects: Any) {
                    super.onQuitFullscreen(url, *objects)
                    Debuger.printfError("***** onQuitFullscreen **** " + objects[0]) //title
                    Debuger.printfError("***** onQuitFullscreen **** " + objects[1]) //当前非全屏player
                    orientationUtils?.backToProtVideo()
                }
            }).setLockClickListener { view, lock ->
                if (orientationUtils != null) {
                    //配合下方的onConfigurationChanged
                    orientationUtils?.setEnable(!lock)
                }
            }.build(detail_player)
        detail_player.getFullscreenButton().setOnClickListener(View.OnClickListener { //直接横屏
            orientationUtils?.resolveByClick()

            //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
            detail_player.startWindowFullscreen(this, true, true)
        })

        //全屏拉伸显示，使用这个属性时，surface_container建议使用FrameLayout
        GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL)
    }

    override fun onBackPressed() {
        if (orientationUtils != null) {
            orientationUtils?.backToProtVideo()
        }
        if (GSYVideoManager.backFromWindowFull(this)) {
            return
        }
        super.onBackPressed()
    }

    override fun onResume() {
        detail_player.getCurrentPlayer().onVideoResume(false)
        super.onResume()
        isPause = false
    }

    override fun onPause() {
        detail_player.getCurrentPlayer().onVideoPause()
        super.onPause()
        isPause = true
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isPlay) {
            detail_player.getCurrentPlayer().release()
        }
        orientationUtils?.releaseListener()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            detail_player.onConfigurationChanged(this, newConfig, orientationUtils!!, true, true)
        }
    }
}