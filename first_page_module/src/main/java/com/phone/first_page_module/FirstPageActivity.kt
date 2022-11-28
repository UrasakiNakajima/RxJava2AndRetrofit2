package com.phone.first_page_module

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amap.api.location.AMapLocation
import com.phone.common_library.base.BaseMvpRxAppActivity
import com.phone.common_library.base.IBaseView
import com.phone.common_library.bean.FirstPageResponse.ResultData.JuheNewsBean
import com.phone.common_library.callback.OnCommonRxPermissionsCallback
import com.phone.common_library.callback.OnCommonSingleParamCallback
import com.phone.common_library.manager.*
import com.phone.common_library.manager.ScreenManager.dpToPx
import com.phone.common_library.manager.SystemManager.getSystemId
import com.phone.common_library.ui.WebViewActivity
import com.phone.first_page_module.adapter.FirstPageAdapter
import com.phone.first_page_module.manager.AMAPLocationManager
import com.phone.first_page_module.presenter.FirstPagePresenterImpl
import com.phone.first_page_module.view.IFirstPageView
import com.qmuiteam.qmui.widget.QMUILoadingView
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

class FirstPageActivity : BaseMvpRxAppActivity<IBaseView, FirstPagePresenterImpl>(),
    IFirstPageView {

    private val TAG = FirstPageActivity::class.java.simpleName
    private var toolbar: Toolbar? = null
    private var tevTitle: TextView? = null
    private var tevRequestPermissionAndStartLocating: TextView? = null
    private var refreshLayout: SmartRefreshLayout? = null
    private var rcvData: RecyclerView? = null

    private var firstPageAdapter: FirstPageAdapter? = null
    private var isRefresh = false

    private var amapLocationManager: AMAPLocationManager? = null

    private var mPermissionsDialog: AlertDialog? = null
    private val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun initLayoutId() = R.layout.activity_first_page

    override fun initData() {
        isRefresh = true
        amapLocationManager = AMAPLocationManager.get()
        amapLocationManager?.setOnCommonSingleParamCallback(object :
            OnCommonSingleParamCallback<AMapLocation> {
            override fun onSuccess(success: AMapLocation) {
                LogManager.i(TAG, "address*****" + success.address)
            }

            override fun onError(error: String) {
                LogManager.i(TAG, "error*****$error")
            }
        })
    }

    override fun initViews() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        tevTitle = findViewById<View>(R.id.tev_title) as TextView
        tevRequestPermissionAndStartLocating =
            findViewById<View>(R.id.tev_request_permission_and_start_locating) as TextView
        refreshLayout = findViewById<View>(R.id.refresh_layout) as SmartRefreshLayout
        rcvData = findViewById<View>(R.id.rcv_data) as RecyclerView
        setToolbar(false, R.color.color_FFE066FF)
        tevRequestPermissionAndStartLocating?.setOnClickListener {
            showToast(
                ResourcesManager.getString(R.string.this_function_can_only_be_used_under_componentization),
                false
            )
            //                initRxPermissions();
        }
        initAdapter()
    }

    private fun initAdapter() {
        val linearLayoutManager = LinearLayoutManager(rxAppCompatActivity)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        rcvData?.layoutManager = linearLayoutManager
        rcvData?.itemAnimator = DefaultItemAnimator()
        firstPageAdapter = FirstPageAdapter(rxAppCompatActivity)
        //		firstPageAdapter = new FirstPageAdapter2(activity, R.layout.item_first_page);
        firstPageAdapter?.setRcvOnItemViewClickListener { position, view -> //				if (view.getId() == R.id.tev_data) {
            //					//					url = "http://rbv01.ku6.com/omtSn0z_PTREtneb3GRtGg.mp4";
            //					//					url = "http://rbv01.ku6.com/7lut5JlEO-v6a8K3X9xBNg.mp4";
            //					url = "https://t-cmcccos.cxzx10086.cn/statics/shopping/detective_conan_japanese.mp4";
            //					//					fileFullname = mFileDTO.getFName();
            //					String[] arr = url.split("\\.");
            //					if (arr != null && arr.length > 0) {
            //						String fileName = "";
            //						StringBuilder stringBuilder = new StringBuilder();
            //						for (int i = 0; i < arr.length - 1; i++) {
            //							stringBuilder.append(arr[i]);
            //						}
            //						fileName = stringBuilder.toString();
            //						String suffix = arr[arr.length - 1];
            //
            //						paramMap.clear();
            //						paramMap.put("url", url);
            //						paramMap.put("suffix", suffix);
            //						startActivityCarryParams(ShowVideoActivity.class, paramMap);
            //					}
            //
            //				}
            if (view.id == R.id.ll_root) {
                val intent = Intent(rxAppCompatActivity, WebViewActivity::class.java)
                intent.putExtra(
                    "loadUrl",
                    firstPageAdapter?.mJuheNewsBeanList?.get(position)?.getUrl()
                )
                startActivity(intent)
            }
        }
        rcvData?.adapter = firstPageAdapter
        refreshLayout?.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                LogManager.i(TAG, "onLoadMore")
                isRefresh = false
                initFirstPage()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                isRefresh = true
                initFirstPage()
            }
        })
    }

    override fun initLoadData() {
        refreshLayout?.autoRefresh()
    }

    override fun attachPresenter(): FirstPagePresenterImpl {
        return FirstPagePresenterImpl(this)
    }

    override fun showLoading() {
        if (!mLoadView.isShown) {
            mLoadView.visibility = View.VISIBLE
            mLoadView.start()
        }
    }

    override fun hideLoading() {
        if (mLoadView.isShown) {
            mLoadView.stop()
            mLoadView.visibility = View.GONE
        }
    }

    override fun firstPageDataSuccess(success: List<JuheNewsBean?>) {
        if (!this.isFinishing) {
            if (isRefresh) {
                firstPageAdapter?.mJuheNewsBeanList?.clear()
                firstPageAdapter?.mJuheNewsBeanList?.addAll(success)
                firstPageAdapter?.clearData()
                firstPageAdapter?.addData(firstPageAdapter?.mJuheNewsBeanList)
                refreshLayout?.finishRefresh()
            } else {
                firstPageAdapter?.addData(firstPageAdapter?.mJuheNewsBeanList)
                refreshLayout?.finishLoadMore()
            }
            hideLoading()
        }
    }

    override fun firstPageDataError(error: String?) {
        if (!this.isFinishing) {
            //            showToast(error, true);
            showCustomToast(
                dpToPx(20f), dpToPx(20f),
                16, ResourcesManager.getColor(R.color.white),
                ResourcesManager.getColor(R.color.color_FFE066FF), dpToPx(40f),
                dpToPx(20f), error,
                true
            )
            if (isRefresh) {
                refreshLayout?.finishRefresh(false)
            } else {
                refreshLayout?.finishLoadMore(false)
            }
            hideLoading()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 207) {
//            initRxPermissions();
        }
    }

    /**
     * RxAppCompatActivity里需要的时候直接调用就行了
     */
    private fun initRxPermissions() {
        val rxPermissionsManager = RxPermissionsManager.get()
        rxPermissionsManager.initRxPermissions(
            this,
            permissions,
            object : OnCommonRxPermissionsCallback {
                override fun onRxPermissionsAllPass() {
                    //所有的权限都授予
//                //製造一個不會造成App崩潰的異常（类强制转换异常java.lang.ClassCastException）
//                User user = new User2();
//                User3 user3 = (User3) user;
//                LogManager.i(TAG, user3.toString());
                    if (TextUtils.isEmpty(
                            mBaseApplication?.getSystemId()
                        )
                    ) {
                        val systemId = getSystemId()
                        mBaseApplication?.setSystemId(systemId)
                        LogManager.i(
                            TAG,
                            "isEmpty systemId*****" + mBaseApplication?.getSystemId()
                        )
                    } else {
                        LogManager.i(TAG, "systemId*****" + mBaseApplication?.getSystemId())
                    }
                    amapLocationManager?.startLocation()
                }

                override fun onNotCheckNoMorePromptError() {
                    //至少一个权限未授予且未勾选不再提示
                    showSystemSetupDialog()
                }

                override fun onCheckNoMorePromptError() {
                    //至少一个权限未授予且勾选了不再提示
                    showSystemSetupDialog()
                }
            })
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
        mPermissionsDialog?.setCanceledOnTouchOutside(false)
        mPermissionsDialog?.show()
    }

    /**
     * 关闭对话框
     */
    private fun cancelPermissionsDialog() {
        mPermissionsDialog?.cancel()
        mPermissionsDialog = null
    }

    private fun initFirstPage() {
        showLoading()
        if (RetrofitManager.isNetworkAvailable()) {
            mBodyParams.clear()
            mBodyParams["type"] = "yule"
            mBodyParams["key"] = "d5cc661633a28f3cf4b1eccff3ee7bae"
            presenter.firstPage2(rxAppCompatActivity, mBodyParams)
        } else {
            firstPageDataError(ResourcesManager.getString(R.string.please_check_the_network_connection))
            hideLoading()
        }
    }

}