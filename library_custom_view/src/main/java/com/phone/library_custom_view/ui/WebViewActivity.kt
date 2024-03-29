package com.phone.library_custom_view.ui

import android.text.Html
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.phone.library_base.callback.OnCommonSingleParamCallback
import com.phone.library_binding.BaseBindingRxAppActivity
import com.phone.library_base.common.ConstantData
import com.phone.library_base.manager.LogManager
import com.phone.library_base.manager.ResourcesManager
import com.phone.library_custom_view.R
import com.phone.library_custom_view.databinding.CustomViewActivityWebViewBinding

@Route(path = ConstantData.Route.ROUTE_WEB_VIEW)
class WebViewActivity : BaseBindingRxAppActivity<CustomViewActivityWebViewBinding>() {

    private val TAG = WebViewActivity::class.java.simpleName
    private var loadUrl: String? = null


    private var title: String? = null//文章标题
    private var id: Int? = -1//文章id
    private var author: String? = null//作者
    private var startTime: Long = 0
    private var requiredTime: Long = 0

    override fun initLayoutId() = R.layout.custom_view_activity_web_view

    override fun initData() {
        val bundle = getIntent()?.extras
        loadUrl = bundle?.getString("loadUrl")
        title = bundle?.getString("title")
    }

    override fun initViews() {
        setToolbar(false, R.color.library_color_FF198CFF)

        mDatabind.apply {
            title?.let {
                tevTitle.setText(Html.fromHtml(it))
            }
            imvBack.setColorFilter(ResourcesManager.getColor(R.color.library_white))
            layoutBack.setOnClickListener(View.OnClickListener { v: View? -> finish() })

            mDatabind.activityWebProgressbar.progress = 100
            mBaseApplication.let {
                layoutWeb.addView(it.webView)
                //            val layoutParams = FrameLayout.LayoutParams(
                //                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT
                //            )
                //            layoutWeb.addView(mBaseApplication.webView, layoutParams)
                it.setOnCommonSingleParamCallback(object :
                    OnCommonSingleParamCallback<Int> {
                    override fun onSuccess(success: Int?) {
                        //进度小于100，显示进度条
                        success?.let {
                            if (success < 100) {
                                if (startTime == 0L) {
                                    startTime = System.currentTimeMillis()
                                }
                                mDatabind.activityWebProgressbar.visibility = View.VISIBLE
                            } else if (success == 100) {
                                if (requiredTime == 0L) {
                                    requiredTime = System.currentTimeMillis() - startTime
                                    LogManager.i(TAG, "requiredTime*****" + requiredTime)
                                }
                                //等于100隐藏
                                mDatabind.activityWebProgressbar.visibility = View.GONE
                            }
                            //改变进度
                            mDatabind.activityWebProgressbar.progress = success
                        }
                    }

                    override fun onError(error: String) {

                    }

                })
            }
        }
    }

    override fun initLoadData() {
        loadUrl?.let {
//            //清除网页访问留下的缓存
//            //由于内核缓存是全局的因此这个方法不仅仅针对webview而是针对整个应用程序.
//            mBaseApplication.webView.clearCache(true)
//            //清除当前webview访问的历史记录
//            //只会webview访问历史记录里的所有记录除了当前访问记录
//            mBaseApplication.webView.clearHistory()
//            //这个api仅仅清除自动完成填充的表单数据，并不会清除WebView存储到本地的数据
//            mBaseApplication.webView.clearFormData()

            mBaseApplication.webView.loadUrl(it)
        }
    }

    override fun onDestroy() {
        mBaseApplication.webView.apply {
            loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            clearCache(true)
            clearHistory();
            //从父容器中移除webview
            mDatabind.layoutWeb.removeView(this)
        }
        super.onDestroy()
    }

}