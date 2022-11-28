package com.phone.main_module.login.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.phone.common_library.base.BaseRxAppActivity
import com.phone.common_library.manager.LogManager
import com.phone.main_module.R
import com.phone.main_module.main.MainActivity

class LaunchActivity : BaseRxAppActivity() {

    private val TAG = LaunchActivity::class.java.simpleName

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initLayoutId(): Int {
        return R.layout.activity_launch
    }

    override fun initData() {}

    override fun initViews() {}

    override fun initLoadData() {
//        initPermissions();
        if (baseApplication.isLogin()) {
            startActivity(MainActivity::class.java)
        } else {
            startActivity(LoginActivity::class.java)
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
                if (baseApplication.isLogin()) {
                    startActivity(MainActivity::class.java)
                } else {
                    startActivity(MainActivity::class.java)
                }
                finish()
            }
        } else {
            if (baseApplication.isLogin()) {
                startActivity(MainActivity::class.java)
            } else {
                startActivity(MainActivity::class.java)
            }
            finish()
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
        permissions: Array<String?>,
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
                                permissions[i]!!
                            )
                        )
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                permissions[i]!!
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
                if (baseApplication.isLogin()) {
                    startActivity(MainActivity::class.java)
                } else {
                    startActivity(MainActivity::class.java)
                }
                finish()
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
                    intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", applicationContext.packageName, null)
                    intent!!.data = uri
                    startActivityForResult(intent, 207)
                }
                .create()
        }
        mPermissionsDialog!!.setCancelable(false)
        mPermissionsDialog!!.show()
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
        mPermissionsDialog!!.setCancelable(false)
        mPermissionsDialog!!.show()
    }

    /**
     * 关闭对话框
     */
    private fun cancelPermissionsDialog() {
        if (mPermissionsDialog != null) {
            mPermissionsDialog!!.cancel()
            mPermissionsDialog = null
        }
    }

}