package com.phone.module_square.ui

import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.phone.library_mvvm.BaseMvvmAppRxActivity
import com.phone.library_network.bean.State
import com.phone.library_common.common.ConstantData
import com.phone.library_base.manager.LogManager
import com.phone.library_base.manager.ResourcesManager
import com.phone.module_square.R
import com.phone.module_square.databinding.SquareActivityKotlinCoroutineBinding
import com.phone.module_square.view_model.CoroutineViewModel
import kotlinx.coroutines.*

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      :
 * introduce : 多种协程的创建方式及其使用方式
 */

@Route(path = ConstantData.Route.ROUTE_KOTLIN_COROUTINE)
class KotlinCoroutineActivity :
    BaseMvvmAppRxActivity<CoroutineViewModel, SquareActivityKotlinCoroutineBinding>() {

    companion object {
        private val TAG = KotlinCoroutineActivity::class.java.simpleName
    }

    var mJob: Job? = null
    var mCoroutineScope: CoroutineScope? = null
    var mMainScope: CoroutineScope? = null

    override fun initLayoutId() = R.layout.square_activity_kotlin_coroutine
    override fun initViewModel() =
        ViewModelProvider(mRxAppCompatActivity).get(CoroutineViewModel::class.java)

    override fun initData() {

    }

    override fun initObservers() {
        viewModel.executeSuccess.observe(this, {
            LogManager.i(TAG, "onChanged*****tabRxActivity")
            when (it) {
                is State.SuccessState -> {
                    if ("success".equals(it.success)) {
                        hideLoading()
                    } else {
                        hideLoading()
                    }
                }

                is State.ErrorState -> {
                    hideLoading()
                }
            }
        })
    }

    override fun initViews() {
        setToolbar(true)
        mDatabind.imvBack.setColorFilter(ResourcesManager.getColor(R.color.library_color_80000000))
        mDatabind.layoutBack.setOnClickListener { v -> finish() }

        mDatabind.tevStartRunBlocking.setOnClickListener {
            startRunBlocking()
        }
        mDatabind.tevStartGlobalScope.setOnClickListener {
            startGlobalScope()
        }
        mDatabind.tevStartCoroutineScope.setOnClickListener {
            startCoroutineScope()
        }
        mDatabind.tevStartMainScope.setOnClickListener {
            startMainScope()
        }
        mDatabind.tevStartViewModelScope.setOnClickListener {
            startViewModelScope()
        }
        mDatabind.tevStartLifecycleScope.setOnClickListener {
            startLifecycleScope()
        }
    }

    private fun startRunBlocking() {
        //方法一：使用 runBlocking 顶层函数（不建议使用）
        //启动一个新协程，并阻塞当前线程，直到其内部所有逻辑及子协程逻辑全部执行完成。
        //开启 runBlocking{} 这种协程之后就是在MAIN线程执行了
        runBlocking {
            LogManager.i(TAG, "runBlocking thread name*****${Thread.currentThread().name}")

            requestUserInfo()
            downloadFile()
        }
    }

    private fun startGlobalScope() {
        //方法二：使用GlobalScope 单例对象直接调用launch/async开启协程（不建议使用）
        //适合在应用范围内启动一个新协程，协程的生命周期与应用程序一致。
        //如果在Activity/Fragment启动，即使Activity/Fragment已经被销毁，协程仍然在执行，极限情况下可能导致资源耗尽，所以需要绑定生命周期（就是在Activity/Fragment 销毁的时候取消这个协程），避免内存泄漏。
        //不建议使用，尤其是在客户端这种需要频繁创建销毁组件的场景。
        //开启GlobalScope.launch{} 或GlobalScope.async{} 方法的时候可以指定运行线程（根据指定的线程来，不指定默认是子线程）。
        mJob?.cancel()
        mJob = GlobalScope.launch(Dispatchers.Main) {
            LogManager.i(TAG, "GlobalScope thread name*****${Thread.currentThread().name}")

            showLoading()
            //进入挂起的函数后UI线程会进入阻塞状态，挂起函数执行完毕之后，UI线程再进入唤醒状态，然后往下执行
            requestUserInfo()
            //进入挂起的函数后UI线程会进入阻塞状态，挂起函数执行完毕之后，UI线程再进入唤醒状态，然后往下执行
            downloadFile()
            hideLoading()
        }


//        mJob = GlobalScope.async(Dispatchers.Main) {
//            LogManager.i(TAG, "GlobalScope thread name*****${Thread.currentThread().name}")
//
//            showLoading()
//            //进入挂起的函数后UI线程会进入阻塞状态，挂起函数执行完毕之后，UI线程再进入唤醒状态，然后往下执行
//            requestUserInfo()
//            //进入挂起的函数后UI线程会进入阻塞状态，挂起函数执行完毕之后，UI线程再进入唤醒状态，然后往下执行
//            downloadFile()
//            hideLoading()
//        }
    }

    private fun startCoroutineScope() {
        //方法三：创建一个CoroutineScope 对象，创建的时候可以指定运行线程（默认运行在子线程）
        //即使Activity/Fragment已经被销毁，协程仍然在执行，所以需要绑定生命周期（就是在Activity/Fragment 销毁的时候取消这个协程），避免内存泄漏。
        //开启mCoroutineScope?.launch{} 或mCoroutineScope?.async{} 方法的时候可以指定运行线程（根据指定的线程来，不指定是创建的时候的线程）。
        mCoroutineScope = CoroutineScope(Dispatchers.Main)
        mCoroutineScope?.launch {
            LogManager.i(TAG, "mCoroutineScope thread name*****${Thread.currentThread().name}")
            showLoading()
            calculatePi()
            hideLoading()
        }


//        mCoroutineScope?.async {
//            LogManager.i(TAG, "mCoroutineScope2 async thread name*****${Thread.currentThread().name}")
//            requestUserInfo()
//        }
//        mCoroutineScope?.async {
//            LogManager.i(TAG, "mCoroutineScope3 async thread name*****${Thread.currentThread().name}")
//            videoDecoding()
//        }
    }

    private fun startMainScope() {
        //方法四：创建一个MainScope 对象，默认运行在UI线程
        //即使Activity/Fragment已经被销毁，协程仍然在执行，所以需要绑定生命周期（就是在Activity/Fragment 销毁的时候取消这个协程），避免内存泄漏。
        //开启mMainScope?.launch{} 或 mMainScope?.async{} 方法的时候可以指定运行线程（根据指定的线程来，不指定是创建的时候的线程）。
        mMainScope = MainScope()
        mMainScope?.launch {//开启MainScope这种协程之后就是在MAIN线程执行了
            LogManager.i(TAG, "mainScope launch thread name*****${Thread.currentThread().name}")

            showLoading()
            //进入挂起的函数后UI线程会进入阻塞状态，挂起函数执行完毕之后，UI线程再进入唤醒状态，然后往下执行
            requestUserInfo()
            //进入挂起的函数后UI线程会进入阻塞状态，挂起函数执行完毕之后，UI线程再进入唤醒状态，然后往下执行
            videoDecoding()
            hideLoading()
        }

//        mMainScope?.async {
//            LogManager.i(TAG, "mainScope async thread name*****${Thread.currentThread().name}")
//
//            showLoading()
//            //进入挂起的函数后UI线程会进入阻塞状态，挂起函数执行完毕之后，UI线程再进入唤醒状态，然后往下执行
//            requestUserInfo()
//            //进入挂起的函数后UI线程会进入阻塞状态，挂起函数执行完毕之后，UI线程再进入唤醒状态，然后往下执行
//            videoDecoding()
//            hideLoading()
//        }
    }

    private fun startViewModelScope() {
        //方法五：在Android MVVM架构的ViewModel中启动一个新协程（如果你的项目架构是MVVM架构，则推荐在ViewModel中使用），
        //该协程默认运行在UI线程，协程和ViewModel的生命周期绑定，组件销毁时，协程一并销毁，从而实现安全可靠地协程调用。
        //调用viewModelScope.launch{} 或 viewModelScope.async{} 方法的时候可以指定运行线程（根据指定的线程来，不指定默认是UI线程）。
        showLoading()
        viewModel.startViewModelScope()
    }

    private fun startLifecycleScope() {
        //方法六：在Activity/Fragment 启动一个协程，该协程默认运行在UI线程（推荐使用），
        //协程和该组件生命周期绑定，Activity/Fragment销毁时，协程一并销毁，从而实现安全可靠地协程调用。
        //调用lifecycleScope.launch{} 或 lifecycleScope.async{} 方法的时候可以指定运行线程（根据指定的线程来，不指定默认是UI线程）。
        lifecycleScope.launch {
            LogManager.i(TAG, "lifecycleScope launch thread name*****${Thread.currentThread().name}")

            showLoading()
            //进入挂起的函数后UI线程会进入阻塞状态，挂起函数执行完毕之后，UI线程再进入唤醒状态，然后往下执行
            requestUserInfo()
            //进入挂起的函数后UI线程会进入阻塞状态，挂起函数执行完毕之后，UI线程再进入唤醒状态，然后往下执行
            videoDecoding()
            hideLoading()
        }

//        lifecycleScope.async {
//            LogManager.i(TAG, "lifecycleScope async thread name*****${Thread.currentThread().name}")
//
//            showLoading()
//            //进入挂起的函数后UI线程会进入阻塞状态，挂起函数执行完毕之后，UI线程再进入唤醒状态，然后往下执行
//            requestUserInfo()
//            //进入挂起的函数后UI线程会进入阻塞状态，挂起函数执行完毕之后，UI线程再进入唤醒状态，然后往下执行
//            videoDecoding()
//            hideLoading()
//        }
    }

    override fun initLoadData() {

    }

    override fun showLoading() {
        if (!mDatabind.loadView.isShown) {
            mDatabind.loadView.visibility = View.VISIBLE
            mDatabind.loadView.start()
        }
    }

    override fun hideLoading() {
        if (mDatabind.loadView.isShown) {
            mDatabind.loadView.stop()
            mDatabind.loadView.visibility = View.GONE
        }
    }

    /**
     * IO密集型协程：模拟请求用户信息（需要时间比较短的）
     * 指定协程在Dispatchers.IO工作，IO密集型协程主要是指执行IO操作的协程：包括网络请求、数据库增删改查、文件下载等等。
     */
    private suspend fun requestUserInfo() {
        LogManager.i(TAG, "start requestUserInfo")
        withContext(Dispatchers.IO) {
            delay(2 * 1000)
        }
        LogManager.i(TAG, "end requestUserInfo")
    }

    /**
     * IO密集型协程：模拟下载文件
     * 指定协程在Dispatchers.IO工作，IO密集型协程主要是指执行IO操作的协程：包括网络请求、数据库增删改查、文件下载等等。
     */
    private suspend fun downloadFile() {
        LogManager.i(TAG, "start downloadFile")
        withContext(Dispatchers.IO) {
//            //这个是真正的模拟下载文件需要的时长
//            delay(60 * 1000)

            //这里只是想早点看到效果，所以减少了时长
            delay(5 * 1000)
        }
        LogManager.i(TAG, "end downloadFile")
    }

    /**
     * CPU密集型协程：模拟计算圆周率（需要时间比较长的）
     * 指定协程在Dispatchers.Default工作，CPU密集型也叫计算密集型，此时，系统运作大部分的状况是CPU Loading 100%，
     * 主要是指执行大量逻辑运算的协程：包括计算圆周率、高清视频解码等等。
     */
    private suspend fun calculatePi() {
        LogManager.i(TAG, "start calculatePi")
        withContext(Dispatchers.Default) {
//            //这个是真正的模拟计算圆周率需要的时长
//            delay(5 * 60 * 1000)

            //这里只是想早点看到效果，所以减少了时长
            delay(10 * 1000)
        }
        LogManager.i(TAG, "end calculatePi")
    }

    /**
     * CPU密集型协程：模拟视频解码（需要时间比较长的）
     * 指定协程在Dispatchers.Default工作，CPU密集型也叫计算密集型，此时，系统运作大部分的状况是CPU Loading 100%，
     * 主要是指执行大量逻辑运算的协程：包括计算圆周率、高清视频解码等等。
     */
    private suspend fun videoDecoding() {
        LogManager.i(TAG, "start videoDecoding")
        withContext(Dispatchers.Default) {
//            //这个是真正的模拟视频解码需要的时长
//            delay(20 * 60 * 1000)

            //这里只是想早点看到效果，所以减少了时长
            delay(10 * 1000)
        }
        LogManager.i(TAG, "end videoDecoding")
    }

    override fun onDestroy() {
        //这几个任务要在Activity 销毁的时候取消，避免内存泄漏
        mJob?.cancel()
        mCoroutineScope?.cancel()
        mMainScope?.cancel()
        super.onDestroy()
    }

}