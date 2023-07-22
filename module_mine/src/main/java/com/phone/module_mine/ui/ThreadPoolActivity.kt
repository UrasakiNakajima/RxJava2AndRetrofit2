package com.phone.module_mine.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.FaceDetector
import android.os.Handler
import android.os.Parcelable
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.phone.library_base.base.BaseRxAppActivity
import com.phone.library_base.common.ConstantData
import com.phone.library_base.manager.LogManager
import com.phone.library_base.manager.ResourcesManager
import com.phone.library_common.bean.BiographyData
import com.phone.module_mine.R
import com.phone.module_mine.task.CachedThreadTask
import com.phone.module_mine.task.FixedThreadTask
import com.phone.module_mine.task.ScheduledThreadTask
import com.phone.module_mine.task.SingleThreadTask
import java.util.Vector
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      :
 * introduce : 多种线程池的使用
 */
@Route(path = ConstantData.Route.ROUTE_THREAD_POOL)
class ThreadPoolActivity : BaseRxAppActivity() {

    companion object {
        private val TAG = ThreadPoolActivity::class.java.simpleName
    }

    //为每一个参数声明一个字段，并使用 @Autowired 标注
    @Autowired(name = "title")
    lateinit var mTitle: String

    //为每一个参数声明一个字段，并使用 @Autowired 标注，通过ARouter api可以传递Parcelable对象
    @Autowired(name = "biographyData")
    lateinit var mBiographyData: Parcelable

    private var toolbar: Toolbar? = null
    private var layoutBack: FrameLayout? = null
    private var imvBack: ImageView? = null
    private var tevTitle: TextView? = null
    private var tevStartSingleThreadExecutor: TextView? = null
    private var tevStartFixedThreadPool: TextView? = null
    private var tevStartCachedThreadPool: TextView? = null
    private var tevStartScheduledThreadPool: TextView? = null
    private var tevStartCustomThreadPool: TextView? = null
    private var tevStartThreadPool: TextView? = null
    private var tevStopThreadPool: TextView? = null
    private var tevStartThreadPool2: TextView? = null
    private var tevStopThreadPool2: TextView? = null
    private var singleThreadExecutor: ExecutorService? = null
    private var fixedThreadPool: ExecutorService? = null
    private var cachedThreadPool: ExecutorService? = null
    private var scheduledThreadPool: ScheduledExecutorService? = null
    private var customThreadPool: ExecutorService? = null
    private var excutor: ExecutorService? = null
    private var excutor2: ExecutorService? = null
    private var handler: Handler? = null
    private val vector = Vector<Bitmap?>()
    private var future: Future<*>? = null
    private var future2: Future<*>? = null
    override fun initLayoutId(): Int {
        return R.layout.mine_activity_thread_pool
    }

    override fun initData() {
        ARouter.getInstance().inject(this)
    }

    override fun initViews() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        layoutBack = findViewById<View>(R.id.layout_back) as FrameLayout
        imvBack = findViewById<View>(R.id.imv_back) as ImageView
        tevTitle = findViewById<View>(R.id.tev_title) as TextView
        tevStartSingleThreadExecutor =
            findViewById<View>(R.id.tev_start_single_thread_executor) as TextView
        tevStartFixedThreadPool = findViewById<View>(R.id.tev_start_fixed_thread_pool) as TextView
        tevStartCachedThreadPool = findViewById<View>(R.id.tev_start_cached_thread_pool) as TextView
        tevStartScheduledThreadPool =
            findViewById<View>(R.id.tev_start_scheduled_thread_pool) as TextView
        tevStartCustomThreadPool = findViewById<View>(R.id.tev_start_custom_thread_pool) as TextView
        tevStartThreadPool = findViewById<View>(R.id.tev_start_thread_pool) as TextView
        tevStopThreadPool = findViewById<View>(R.id.tev_stop_thread_pool) as TextView
        tevStartThreadPool2 = findViewById<View>(R.id.tev_start_thread_pool2) as TextView
        tevStopThreadPool2 = findViewById<View>(R.id.tev_stop_thread_pool2) as TextView


        setToolbar(false, R.color.library_color_FF198CFF)
        imvBack?.setColorFilter(ResourcesManager.getColor(R.color.library_white))
        tevTitle?.text = mTitle
        val biographyData = mBiographyData as BiographyData
        LogManager.i(TAG, "biographyData*****" + biographyData.toString())

        layoutBack?.setOnClickListener {
            finish()
        }
        tevStartSingleThreadExecutor?.setOnClickListener {
            startSingleThreadExecutor()
        }
        tevStartFixedThreadPool?.setOnClickListener {
            startFixedThreadPool()
        }
        tevStartCachedThreadPool?.setOnClickListener {
            startCachedThreadPool()
        }
        tevStartScheduledThreadPool?.setOnClickListener {
            startScheduledThreadPool()
        }
        tevStartCustomThreadPool?.setOnClickListener {
            startCustomThreadPool()
        }
        tevStartThreadPool?.setOnClickListener {
            startThreadPool()
        }
        tevStopThreadPool?.setOnClickListener {
            stopThreadPool()
        }
        tevStartThreadPool2?.setOnClickListener {
            startThreadPool2()
        }
        tevStopThreadPool2?.setOnClickListener {
            stopThreadPool2()
        }
    }

    private fun startSingleThreadExecutor() {
        //只有一个核心线程，没有非核心线程，线程队列是int最大值
        //线程池中的任务是按照提交的次序顺序执行的
        singleThreadExecutor = Executors.newSingleThreadExecutor()
        for (i in 0..9) {
            singleThreadExecutor?.execute(SingleThreadTask())
        }
        //shutdown函数修改线程池状态为SHUTDOWN
        //不再接收新提交的任务
        //中断线程池中空闲的线程
        //第③步只是中断了空闲的线程，但正在执行的任务以及线程池任务队列中的任务会继续执行完毕
        singleThreadExecutor?.shutdown()
    }

    private fun startFixedThreadPool() {
        //CPU密集型线程池：只有几个自定义的核心线程，没有非核心线程，线程队列是int最大值
        fixedThreadPool = Executors.newFixedThreadPool(5)
        for (i in 0..9) {
            fixedThreadPool?.execute(FixedThreadTask())
        }
        //shutdown函数修改线程池状态为SHUTDOWN
        //不再接收新提交的任务
        //中断线程池中空闲的线程
        //第③步只是中断了空闲的线程，但正在执行的任务以及线程池任务队列中的任务会继续执行完毕
        fixedThreadPool?.shutdown()
    }

    private fun startCachedThreadPool() {
        //IO密集型线程池：只有非核心线程，非核心线程最多可以达到int的最大值，非核心线程的最大空闲时间是60s，
        //没有核心线程，线程队列特点
        //（1）内部容量是0
        //（2）每次删除操作都要等待插入操作
        //（3）每次插入操作都要等待删除操作
        //（4）一个元素，一旦有了插入线程和移除线程，那么很快由插入线程移交给移除线程，这个容器相当于通道，本身不存储元素
        //（5）在多任务队列，是最快的处理任务方式。
        cachedThreadPool = Executors.newCachedThreadPool()
        for (i in 0..9) {
            cachedThreadPool?.execute(CachedThreadTask())
        }
        //shutdown函数修改线程池状态为SHUTDOWN
        //不再接收新提交的任务
        //中断线程池中空闲的线程
        //第③步只是中断了空闲的线程，但正在执行的任务以及线程池任务队列中的任务会继续执行完毕
        cachedThreadPool?.shutdown()
    }

    private fun startScheduledThreadPool() {
        //核心线程是自定义的，最大线程数可以达到int的最大值，非核心线程的最大空闲时间是0.01s，
        //线程队列是最大是int最大值，使用优先级队列DelayedWorkQueue，保证添加到队列中的任务，
        //会按照任务的延时时间进行排序，延时时间少的任务首先被获取。
        scheduledThreadPool = Executors.newScheduledThreadPool(5)
        for (i in 0..9) {
            scheduledThreadPool?.scheduleAtFixedRate(
                ScheduledThreadTask(),
                0,
                500,
                TimeUnit.MILLISECONDS
            )
        }
        //shutdown函数修改线程池状态为SHUTDOWN
        //不再接收新提交的任务
        //中断线程池中空闲的线程
        //第③步只是中断了空闲的线程，但正在执行的任务以及线程池任务队列中的任务会继续执行完毕
        scheduledThreadPool?.shutdown()
    }

    private fun startCustomThreadPool() {
        //自定义线程池
        customThreadPool = ThreadPoolExecutor(
            5, 10,
            60L, TimeUnit.SECONDS,
            LinkedBlockingQueue()
        )
        for (i in 0..29) {
            customThreadPool?.execute(FixedThreadTask())
        }
        //shutdown函数修改线程池状态为SHUTDOWN
        //不再接收新提交的任务
        //中断线程池中空闲的线程
        //第③步只是中断了空闲的线程，但正在执行的任务以及线程池任务队列中的任务会继续执行完毕
        customThreadPool?.shutdown()
    }

    /**
     * 记一次Java参数传递时值发生变化问题（传Vector，就是线程安全的List）
     */
    private fun startThreadPool() {
        LogManager.i(TAG, "startThreadPool currentThread name*****" + Thread.currentThread().name)
        vector.clear()
        for (i in 0..5) {
            val mOption = BitmapFactory.Options()
            mOption.inPreferredConfig = Bitmap.Config.RGB_565
            var mBitmap: Bitmap? = null
            mBitmap = if (i == 0) {
                BitmapFactory.decodeResource(resources, R.mipmap.library_picture3, mOption)
            } else if (i == 1) {
                BitmapFactory.decodeResource(resources, R.mipmap.library_picture6, mOption)
            } else if (i == 2) {
                BitmapFactory.decodeResource(resources, R.mipmap.library_picture8, mOption)
            } else if (i == 3) {
                BitmapFactory.decodeResource(resources, R.mipmap.library_picture14, mOption)
            } else if (i == 4) {
                BitmapFactory.decodeResource(resources, R.mipmap.library_picture16, mOption)
            } else {
                BitmapFactory.decodeResource(resources, R.mipmap.library_picture18, mOption)
            }
            vector.add(mBitmap)
        }
        handler = Handler()
        //这是为了在检测人脸过程中改变原vector的数据
        handler?.postDelayed({
            LogManager.i(
                TAG,
                "startThreadPool currentThread2 name*****" + Thread.currentThread().name
            )
            val mOption = BitmapFactory.Options()
            mOption.inPreferredConfig = Bitmap.Config.RGB_565
            val mBitmap = BitmapFactory.decodeResource(
                resources, R.mipmap.library_picture15, mOption
            )
            vector.add(mBitmap)
            LogManager.i(TAG, "startThreadPool vector size*****" + vector.size)
            handler?.removeCallbacksAndMessages(null)
            handler = null
        }, 150)
        excutor = Executors.newSingleThreadExecutor()
        future = excutor?.submit({
            LogManager.i(
                TAG,
                "startThreadPool excutor currentThread name*****" + Thread.currentThread().name
            )
            //1、如果vector是由原vector.clone()传来的时候，当原vector发生变化，他是不会变化的
            val list = vector.clone() as Vector<Bitmap>
            initFaceRecognition(list)
        })
        LogManager.i(TAG, "startThreadPool&&&*****")
    }

    /**
     * 人脸个数识别方法
     *
     * @param vector 1、vector是final的，按说不会变化，可是vector是原vector传来的时候，当原vector发生变化，他还是会变化的
     * 2、如果vector是由原vector.clone()传来的时候，当原vector发生变化，他是不会变化的
     */
    private fun initFaceRecognition(vector: Vector<Bitmap>) {
        for (i in vector.indices) {
            val mBitmap = vector[i]
            LogManager.i(
                TAG,
                "initFaceRecognition i*****$i"
            )
            var maxFaces = 20
            val mFaceDetector = FaceDetector(mBitmap.width, mBitmap.height, maxFaces)
            val mFace = arrayOfNulls<FaceDetector.Face>(maxFaces)
            maxFaces = mFaceDetector.findFaces(mBitmap, mFace)
            LogManager.i(
                TAG,
                "initFaceRecognition maxFaces*****$maxFaces"
            )
        }
    }

    /**
     * （1）修改线程池状态为STOP
     * （2）不再接收任务提交
     * （3）尝试中断线程池中所有的线程（包括正在执行的线程）
     * （4）返回正在等待执行的任务列表 List<Runnable>
     * 此时线程池中等待队列中的任务不会被执行，正在执行的任务也可能被终止（为什么是可能呢？因为如果正常执行的任务
     * 如果不响应中断，那么就不会被终止，直到任务执行完毕）
     * PS：线程池中的正在执行的任务一般是停不下来的
    </Runnable> */
    private fun stopThreadPool() {
        future?.let {
            it.cancel(true)
            excutor?.let {
                if (!it.isShutdown) {
                    it.shutdownNow()
                    LogManager.i(TAG, "stopThreadPool2 shutdownNow*****")
                }
            }
        }
    }

    /**
     * （1）修改线程池状态为SHUTDOWN
     * （2）不再接收新提交的任务
     * （3）中断线程池中空闲的线程
     * 第（3）步只是中断了空闲的线程，但正在执行的任务以及线程池任务队列中的任务会继续执行完毕
     */
    private fun startThreadPool2() {
        excutor2 = Executors.newSingleThreadExecutor()
        future2 = excutor2?.submit({
//            for (i in 0..999999) {
//                LogManager.i(
//                    TAG,
//                    "startThreadPool2*****$i"
//                )
//            }
        })
    }

    private fun stopThreadPool2() {
        future2?.let {
            it.cancel(true)
            excutor2?.let {
                if (!it.isShutdown) {
                    it.shutdownNow()
                    LogManager.i(TAG, "stopThreadPool2 shutdownNow*****")
                }
            }
        }
    }

    override fun initLoadData() {

    }

    override fun onDestroy() {
        handler?.removeCallbacksAndMessages(null)
        handler = null

        //shutdownNow函数修改线程池状态为STOP
        //不再接收任务提交
        //尝试中断线程池中所有的线程（包括正在执行的线程）
        //返回正在等待执行的任务列表 List<Runnable>
        singleThreadExecutor?.shutdownNow()
        fixedThreadPool?.shutdownNow()
        cachedThreadPool?.shutdownNow()
        scheduledThreadPool?.shutdownNow()
        customThreadPool?.shutdownNow()
        stopThreadPool()
        stopThreadPool2()
        super.onDestroy()
    }
}

