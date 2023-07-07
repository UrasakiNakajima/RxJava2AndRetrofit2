package com.phone.library_common.ui

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.launcher.ARouter
import com.phone.library_common.BuildConfig
import com.phone.library_common.R
import com.phone.library_common.base.BaseRxAppActivity
import com.phone.library_common.common.ConstantData
import com.phone.library_common.manager.LogManager
import com.phone.library_common.manager.ScreenManager
import com.phone.library_common.manager.SharedPreferencesManager
import com.phone.library_common.manager.ThreadPoolManager
import com.phone.library_common.service.IHomeService
import com.phone.library_common.service.IWhichPage


@SuppressLint("CustomSplashScreen")
class LaunchActivity : BaseRxAppActivity() {

    companion object {
        @JvmStatic
        private val TAG = LaunchActivity::class.java.simpleName
    }

    private val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.CAMERA
    )
    private val REQUEST_CODE = 0xa1 //请求多个权限

    //2、创建一个mPermissionList，逐个判断哪些权限未授予，未授予的权限存储到mPerrrmissionList中
    private val mPermissionList: MutableList<String> = ArrayList()
    private var mPermissionsDialog: AlertDialog? = null

    //    private lateinit var animator: ObjectAnimator
//    private lateinit var animator: ObjectAnimator
    val animatorSet = AnimatorSet()
    val animatorSet2 = AnimatorSet()
    val animatorSet3 = AnimatorSet()

    override fun initLayoutId(): Int {
        return R.layout.library_activity_launch
    }

    override fun initData() {
    }

    override fun initViews() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        val tevAndroid = findViewById<View>(R.id.tev_android) as TextView
        val tevAppName = findViewById<View>(R.id.tev_app_name) as TextView
        val tevKotlin = findViewById<View>(R.id.tev_kotlin) as TextView
        setToolbar(true, R.color.library_color_FFFFC73B)

        ThreadPoolManager.instance().createScheduledThreadPoolToUIThread(1000) {
            LogManager.i(
                TAG,
                "LaunchActivity 500 createScheduledThreadPoolToUIThread*****${Thread.currentThread().name}"
            )

            // 构造一个在横轴上平移的属性动画
            val translationYAnimator = ObjectAnimator.ofFloat(
                tevAndroid,
                "translationY",
                0f,
                ScreenManager.dpToPx(ScreenManager.getDimenDp(R.dimen.library_dp_360).toFloat())
                    .toFloat()
            )
//            // 构造一个在透明度上变化的属性动画
//            val anim2 = ObjectAnimator.ofFloat(tevAndroid, "alpha", 1f, 0.1f, 1f, 0.5f, 1f)
            // 构造一个围绕中心点旋转的属性动画
            val rotationAnimator = ObjectAnimator.ofFloat(tevAndroid, "rotation", 0f, 360f)
            // 构造一个在纵轴上缩放的属性动画
            val scaleXAnimator = ObjectAnimator.ofFloat(tevAndroid, "scaleX", 1f, 1.5f, 1f)
            // 构造一个在纵轴上缩放的属性动画
            val scaleYAnimator = ObjectAnimator.ofFloat(tevAndroid, "scaleY", 1f, 1.5f, 1f)
            // 创建一个属性动画组合
//            val animSet = AnimatorSet()
//            // 把指定的属性动画添加到属性动画组合
//            val builder = animatorSet.play(anim2)
////            // 动画播放顺序为：anim1先执行，然后再一起执行anim2、anim3、anim3，最后执行anim5
//            builder.with(anim3).with(anim4).after(anim1).before(anim5)
            val animatorList = mutableListOf<ObjectAnimator>()
            animatorList.add(translationYAnimator)
//            animatorList.add(anim2)
            animatorList.add(rotationAnimator)
            animatorList.add(scaleXAnimator)
            animatorList.add(scaleYAnimator)
            animatorSet.playTogether(animatorList as Collection<Animator>?)
            animatorSet.setInterpolator(LinearInterpolator())
            animatorSet.duration = 2000 // 设置动画的播放时长
            animatorSet.start() // 开始播放属性动画

            // 构造一个在横轴上平移的属性动画
            val translationYAnimator2 = ObjectAnimator.ofFloat(
                tevAppName,
                "translationY",
                0f,
                -(ScreenManager.dpToPx(ScreenManager.getDimenDp(R.dimen.library_dp_409).toFloat())
                    .toFloat())
            )
//            // 构造一个在透明度上变化的属性动画
//            val anim2 = ObjectAnimator.ofFloat(tevAndroid, "alpha", 1f, 0.1f, 1f, 0.5f, 1f)
            // 构造一个围绕中心点旋转的属性动画
            val rotationAnimator2 = ObjectAnimator.ofFloat(tevAppName, "rotation", 0f, 360f)
            // 构造一个在纵轴上缩放的属性动画
            val scaleXAnimator2 = ObjectAnimator.ofFloat(tevAppName, "scaleX", 1f, 1.5f, 1f)
            // 构造一个在纵轴上缩放的属性动画
            val scaleYAnimator2 = ObjectAnimator.ofFloat(tevAppName, "scaleY", 1f, 1.5f, 1f)
            // 创建一个属性动画组合
//            val animSet = AnimatorSet()
//            // 把指定的属性动画添加到属性动画组合
//            val builder2 = animatorSet2.play(anim2)
////            // 动画播放顺序为：anim1先执行，然后再一起执行anim2、anim3、anim3，最后执行anim5
//            builder2.with(anim3).with(anim4).after(anim1).before(anim5)
            val animatorList2 = mutableListOf<ObjectAnimator>()
            animatorList2.add(translationYAnimator2)
//            animatorList2.add(anim2)
            animatorList2.add(rotationAnimator2)
            animatorList2.add(scaleXAnimator2)
            animatorList2.add(scaleYAnimator2)
            animatorSet2.playTogether(animatorList2 as Collection<Animator>?)
            animatorSet2.setInterpolator(LinearInterpolator())
            animatorSet2.duration = 2000 // 设置动画的播放时长
            animatorSet2.start() // 开始播放属性动画

            // 构造一个在横轴上平移的属性动画
            val translationXAnimator3 = ObjectAnimator.ofFloat(
                tevKotlin,
                "translationX",
                0f,
                ScreenManager.dpToPx(ScreenManager.getDimenDp(R.dimen.library_dp_100).toFloat())
                    .toFloat()
            )
            // 构造一个在横轴上平移的属性动画
            val translationYAnimator3 = ObjectAnimator.ofFloat(
                tevKotlin,
                "translationY",
                0f,
                -(ScreenManager.dpToPx(ScreenManager.getDimenDp(R.dimen.library_dp_390).toFloat())
                    .toFloat())
            )
//            // 构造一个在透明度上变化的属性动画
//            val anim2 = ObjectAnimator.ofFloat(tevAndroid, "alpha", 1f, 0.1f, 1f, 0.5f, 1f)
            // 构造一个围绕中心点旋转的属性动画
            val rotationAnimator3 = ObjectAnimator.ofFloat(tevKotlin, "rotation", 0f, 360f)
//            // 构造一个在纵轴上缩放的属性动画
//            val scaleXAnimator3 = ObjectAnimator.ofFloat(tevKotlin, "scaleX", 1f, 1.5f, 1f)
//            // 构造一个在纵轴上缩放的属性动画
//            val scaleYAnimator3 = ObjectAnimator.ofFloat(tevKotlin, "scaleY", 1f, 1.5f, 1f)
            // 创建一个属性动画组合
//            val animSet3 = AnimatorSet()
//            // 把指定的属性动画添加到属性动画组合
//            val builder3 = animatorSet3.play(anim2)
////            // 动画播放顺序为：anim1先执行，然后再一起执行anim2、anim3、anim3，最后执行anim5
//            builder3.with(anim3).with(anim4).after(anim1).before(anim5)
            val animatorList3 = mutableListOf<ObjectAnimator>()
            animatorList3.add(translationYAnimator3)
            animatorList3.add(translationXAnimator3)
            animatorList3.add(rotationAnimator3)
//            animatorList3.add(scaleXAnimator3)
//            animatorList3.add(scaleYAnimator3)
            animatorSet3.playTogether(animatorList3 as Collection<Animator>?)
            animatorSet3.setInterpolator(LinearInterpolator())
            animatorSet3.duration = 2000 // 设置动画的播放时长
            animatorSet3.start() // 开始播放属性动画
        }
    }

    override fun initLoadData() {
//        initPermissions()

        ThreadPoolManager.instance().createScheduledThreadPoolToUIThread(3000, {
            LogManager.i(
                TAG,
                "LaunchActivity 7000 createScheduledThreadPoolToUIThread*****${Thread.currentThread().name}"
            )

            //Activity 跳转一律放在UI线程去执行
            customStartActivity()
        })
    }

    private fun customStartActivity() {
        if (!BuildConfig.IS_MODULE) {
            if (SharedPreferencesManager.get("isLogin", false) as Boolean) {
                ARouter.getInstance().build(ConstantData.Route.ROUTE_MAIN)
                    .navigation()
            } else {
                ARouter.getInstance().build(ConstantData.Route.ROUTE_LOGIN)
                    .navigation()
            }
        } else {
            val whichPage = ARouter.getInstance().build(ConstantData.Route.ROUTE_TO_WHICH_PAGE)
                .navigation() as IWhichPage
            when (whichPage.mWhichPage) {
                "Home" -> {
                    ARouter.getInstance().build(ConstantData.Route.ROUTE_HOME)
                        .navigation()
                }

                "Project" -> {
                    ARouter.getInstance().build(ConstantData.Route.ROUTE_PROJECT)
                        .navigation()
                }

                "Square" -> {
                    ARouter.getInstance().build(ConstantData.Route.ROUTE_SQUARE)
                        .navigation()
                }

                "Resource" -> {
                    ARouter.getInstance().build(ConstantData.Route.ROUTE_RESOURCE)
                        .navigation()
                }

                "Mine" -> {
                    ARouter.getInstance().build(ConstantData.Route.ROUTE_MINE)
                        .navigation()
                }
            }
        }
        finish()
    }

    private fun initPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mPermissionList.clear() //清空没有通过的权限
            //逐个判断你要的权限是否已经通过
            for (i in permissions.indices) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        permissions[i]
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    mPermissionList.add(permissions[i]) //添加还未授予的权限
                }
            }

            //申请权限
            if (mPermissionList.size > 0) { //有权限没有通过，需要申请
                ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE)
            } else {
                customStartActivity()
            }
        } else {
            customStartActivity()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 207) {
            initPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var hasPermissionDismiss = false //有权限没有通过
        if (REQUEST_CODE == requestCode) {
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    hasPermissionDismiss = true
                }
            }

            //如果有权限没有被允许
            if (hasPermissionDismiss) {
                //跳转到系统设置权限页面，或者直接关闭页面，不让他继续访问
                for (i in grantResults.indices) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        LogManager.i(
                            TAG,
                            "onRequestPermissionsResult shouldShowRequestPermissionRationale*****" + ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                permissions[i]
                            )
                        )
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                permissions[i]
                            )
                        ) {
                            showSystemSetupDialog()
                            break
                        } else {
                            showPermissionsDialog()
                            break
                        }
                    }
                }
            } else {
                customStartActivity()
            }
        }
    }

    private fun showSystemSetupDialog() {
        cancelPermissionsDialog()
        if (mPermissionsDialog == null) {
            mPermissionsDialog = AlertDialog.Builder(this)
                .setTitle("权限设置")
                .setMessage("获取相关权限失败，将导致部分功能无法正常使用，请到设置页面手动授权")
                .setPositiveButton("去授权") { dialog, which ->
                    cancelPermissionsDialog()
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", applicationContext.packageName, null)
                    intent.data = uri
                    startActivityForResult(intent, 207)
                }
                .create()
        }
        mPermissionsDialog?.setCancelable(false)
        mPermissionsDialog?.show()
    }

    /**
     * 展示权限对话框
     */
    private fun showPermissionsDialog() {
        cancelPermissionsDialog()
        val title: String
        val message: String
        if (mPermissionsDialog == null) {
            title = "请授予权限"
            message = "已禁用权限，请手动授予"
            mPermissionsDialog = AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("设置") { dialog, which ->
                    cancelPermissionsDialog()
                    initPermissions()
                }
                .create()
        }
        mPermissionsDialog?.setCancelable(false)
        mPermissionsDialog?.show()
    }

    /**
     * 关闭对话框
     */
    private fun cancelPermissionsDialog() {
        mPermissionsDialog?.cancel()
        mPermissionsDialog = null
    }

    override fun onDestroy() {
        if (animatorSet.isRunning) {
            animatorSet.cancel()
        }
        if (animatorSet2.isRunning) {
            animatorSet2.cancel()
        }
        if (animatorSet3.isRunning) {
            animatorSet3.cancel()
        }
        ThreadPoolManager.instance().shutdownScheduledThreadPool()
        super.onDestroy()
    }
}