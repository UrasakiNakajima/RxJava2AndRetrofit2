package com.phone.module_square.function_menu.ui

import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.phone.library_base.base.BaseRxAppActivity
import com.phone.library_base.common.ConstantData
import com.phone.library_base.manager.LogManager
import com.phone.library_base.manager.ResourcesManager
import com.phone.library_custom_view.custom_view.BannerIndicator
import com.phone.module_square.R
import com.phone.module_square.adapter.NewBannerAdapter
import com.phone.module_square.bean.PictureBean
import com.phone.module_square.custom.SmoothLinearLayoutManager
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

@Route(path = ConstantData.Route.ROUTE_SQUARE_CUSTOM_BANNER)
class CustomBannerActivity : BaseRxAppActivity() {

    companion object {
        private const val TAG = "CustomBannerActivity"
    }

    private var toolbar: Toolbar? = null
    private var layoutBack: FrameLayout? = null
    private var imvBack: ImageView? = null
    private var layoutBanner: FrameLayout? = null
    private var layoutIndicator: FrameLayout? = null

    private var scheduledExecutorService: ScheduledExecutorService? = null
    private var scheduledExecutorService2: ScheduledExecutorService? = null
    private val handler = Handler(Looper.getMainLooper())
    private var time = 0
    val pictureList = mutableListOf<PictureBean>()

    override fun initLayoutId(): Int = R.layout.square_activity_custom_banner

    override fun initData() {
        // 存入图片
        pictureList.add(PictureBean("1", R.mipmap.pucture_sunrise1))
        pictureList.add(PictureBean("2", R.mipmap.pucture_sunrise2))
        pictureList.add(PictureBean("3", R.mipmap.pucture_sunrise3))
        pictureList.add(PictureBean("5", R.mipmap.pucture_sunrise5))
        pictureList.add(PictureBean("6", R.mipmap.pucture_sunrise6))
    }

    override fun initViews() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        layoutBack = findViewById<View>(R.id.layout_back) as FrameLayout
        imvBack = findViewById<View>(R.id.imv_back) as ImageView
        layoutBanner = findViewById<View>(R.id.layout_banner) as FrameLayout
        layoutIndicator = findViewById<View>(R.id.layout_indicator) as FrameLayout

        setToolbar(false, R.color.library_black)
        imvBack?.setColorFilter(ResourcesManager.getColor(R.color.library_white))
        layoutBack?.setOnClickListener {
            finish()
        }

        compareData(mutableListOf(), pictureList)
    }

    override fun initLoadData() {
        delayedUpdate()
    }

    private fun addBannerView(list: MutableList<PictureBean>) {
        layoutBanner?.removeAllViews()
        val recyclerView = RecyclerView(this@CustomBannerActivity)
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT
        )
        layoutBanner?.addView(recyclerView, layoutParams)
        val adapter = NewBannerAdapter(this@CustomBannerActivity, list)
        // LinearLayoutManager 第二个参数表示布局方向，默认是垂直的
        // 这里轮播广告要用为LinearLayoutManager.HORIZONTAL，水平方向
        // 使用LinearLayoutManager会让图片滚动太快，我们继承LinearLayoutManager写一个子类重写它的滑动，让它不要太快
        // LinearLayoutManager layoutManager = new LinearLayoutManager(this@CustomBannerActivity, LinearLayoutManager.HORIZONTAL, false);
        var smoothLinearLayoutManager: SmoothLinearLayoutManager? = null
        if (list.size > 1) {
            smoothLinearLayoutManager = SmoothLinearLayoutManager(
                this@CustomBannerActivity, LinearLayoutManager.HORIZONTAL, false
            )
            recyclerView.layoutManager = smoothLinearLayoutManager
        } else {
            val linearLayoutManager: LinearLayoutManager =
                object : LinearLayoutManager(this@CustomBannerActivity) {
                    override fun canScrollVertically(): Boolean {
                        return false
                    }
                }
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.isNestedScrollingEnabled = false //禁止滑动
        }
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        // 由于广告是一页一页的划过去，所以我们还需要用SnapHelper的子类PagerSnapHelper。直接追加到上面的recyclerView.setAdapter(adapter) 后面。
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
        // recyclerView.scrollToPosition(list.size()*10)这句使RecyclerView一开始位于 list.size()*10 处，避免了一开始position为0不能前滑的尴尬
        recyclerView.scrollToPosition(list.size * 10)
        if (list.size > 1) {
            // 指示器  显示指示器上的红点需要得到当前展示的广告轮播图片的position。RecyclerView有个addOnScrollListener()方法，可以监听当前滑动状态
            indicator(recyclerView, smoothLinearLayoutManager, list)
            // 自动轮播
            autoScroll(recyclerView, smoothLinearLayoutManager)
        }
    }

    /**
     * 比较数据，如果相同则不更新，如果不相同则更新
     * pictureList 原始数据
     * list 新的数据
     */
    private fun compareData(
        rawDataList: MutableList<PictureBean>, newDatalist: MutableList<PictureBean>
    ) {
        val rawDataListClone = mutableListOf<PictureBean>()
        for (index in 0 until rawDataList.size) {
            rawDataListClone.add(rawDataList[index].clone())
        }
        val newDatalistClone = mutableListOf<PictureBean>()
        for (index in 0 until newDatalist.size) {
            newDatalistClone.add(newDatalist[index].clone())
        }

        if (rawDataListClone.size == newDatalistClone.size) {
            for (index in rawDataListClone.size - 1 downTo 0) {
                for (index2 in newDatalistClone.size - 1 downTo 0) {
                    if (rawDataListClone[index] == newDatalistClone[index2]) {
                        rawDataListClone.removeAt(index)
                        newDatalistClone.removeAt(index2)
                        break
                    }
                }
            }
            if (rawDataListClone.size > 0 || newDatalistClone.size > 0) {
                LogManager.i(TAG, "compareData rawDataListClone.size*****${rawDataListClone.size}")
                LogManager.i(TAG, "compareData newDatalistClone.size*****${newDatalistClone.size}")

                scheduledExecutorService?.shutdownNow()
                this.pictureList.clear()
                this.pictureList.addAll(newDatalist)
                addBannerView(this.pictureList)
            } else {
                LogManager.i(TAG, "compareData 两个List完全相同")
            }
        } else {
            LogManager.i(TAG, "compareData 重新设置数据")

            scheduledExecutorService?.shutdownNow()
            this.pictureList.clear()
            this.pictureList.addAll(newDatalistClone)
            addBannerView(this.pictureList)
        }
    }

    private fun delayedUpdate() {
        scheduledExecutorService2 = Executors.newScheduledThreadPool(1)
        scheduledExecutorService2?.scheduleAtFixedRate({
            handler.post {
                this@CustomBannerActivity.time++
                LogManager.i(TAG, "delayedUpdate time*****${this@CustomBannerActivity.time}")
                val list = mutableListOf<PictureBean>()
                if (this@CustomBannerActivity.time == 1) {
                    list.add(PictureBean("1", R.mipmap.pucture_sunrise1))
                    list.add(PictureBean("2", R.mipmap.pucture_sunrise2))
                    list.add(PictureBean("0", R.mipmap.pucture_sunrise3))
                    list.add(PictureBean("5", R.mipmap.pucture_sunrise5))
                    list.add(PictureBean("6", R.mipmap.pucture_sunrise6))

//                    list.add(PictureBean("7", R.mipmap.picture_fujiyama1))
//                    list.add(PictureBean("8", R.mipmap.picture_fujiyama2))
//                    list.add(PictureBean("9", R.mipmap.picture_fujiyama3))
//                    list.add(PictureBean("10", R.mipmap.picture_fujiyama5))
//                    list.add(PictureBean("11", R.mipmap.picture_fujiyama6))
                } else if (this@CustomBannerActivity.time == 2) {
                    list.add(PictureBean("15", R.mipmap.library_picture_new))
                    list.add(PictureBean("16", R.mipmap.library_picture_new2))
                    list.add(PictureBean("17", R.mipmap.library_picture_new3))
                } else if (this@CustomBannerActivity.time == 3) {
                    list.add(PictureBean("21", R.mipmap.library_picture24))
                    list.add(PictureBean("22", R.mipmap.library_picture25))
                    list.add(PictureBean("23", R.mipmap.library_picture26))
                    list.add(PictureBean("24", R.mipmap.library_picture27))
                    list.add(PictureBean("25", R.mipmap.library_picture28))
                    list.add(PictureBean("26", R.mipmap.library_picture30))
                } else {
                    list.add(PictureBean("21", R.mipmap.library_picture24))
                    list.add(PictureBean("22", R.mipmap.library_picture25))
                    list.add(PictureBean("23", R.mipmap.library_picture26))
                    list.add(PictureBean("24", R.mipmap.library_picture27))
                    list.add(PictureBean("25", R.mipmap.library_picture28))
                    list.add(PictureBean("26", R.mipmap.library_picture30))
                }

                compareData(pictureList, list)
            }
        }, 30000, 50000, TimeUnit.MILLISECONDS) // 表示2秒后每过2秒运行一次run（）里的程序
    }

    private fun autoScroll(recyclerView: RecyclerView, layoutManager: LinearLayoutManager?) {
        scheduledExecutorService = Executors.newScheduledThreadPool(1)
        scheduledExecutorService?.scheduleAtFixedRate({ // layoutManager.findFirstVisibleItemPosition() 表示得到当前RecyclerView第一个能看到的item的位置。
            // 由于广告是每次展示一张，所以得到的就是当前图片的position。
            // recyclerView.smoothScrollToPosition（int position）表示滑动到某个position。
            // 所以上面的代码就表示每过2秒滑动到下个position，以此来完成自动轮播。
            handler.post {
                layoutManager?.let {
                    recyclerView.smoothScrollToPosition(it.findFirstVisibleItemPosition() + 1)
                }
            }
        }, 4000, 4000, TimeUnit.MILLISECONDS) // 表示2秒后每过2秒运行一次run（）里的程序
    }

    private fun indicator(
        recyclerView: RecyclerView, layoutManager: LinearLayoutManager?, list: List<PictureBean>
    ) {
        val bannerIndicator = BannerIndicator(this@CustomBannerActivity, null)
        bannerIndicator.setNumber(list.size)
        layoutIndicator?.removeAllViews()
        layoutIndicator?.addView(bannerIndicator)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //dx为横向滚动 dy为竖向滚动
                //如果为竖向滚动,则isSliding属性为true，横向滚动则为false
                val isSliding = dx > 0
                if (isSliding) {
                    scheduledExecutorService?.shutdownNow()
                    autoScroll(recyclerView, layoutManager)
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                // onScrollStateChangedd的 newState 参数有三种状态:
                // SCROLL_STATE_IDLE 静止状态
                // SCROLL_STATE_DRAGGING 拖拽状态
                // SCROLL_STATE_SETTLING手指离开后的惯性滚动状态
                // 当RecyclerView的状态为SCROLL_STATE_IDLE时得到当前图片的position，然后与图片列表取余就得到指示器红点的位置。
                if (newState == RecyclerView.SCROLL_STATE_IDLE && layoutManager != null) {
                    val i = layoutManager.findFirstVisibleItemPosition() % list.size
                    //得到指示器红点的位置
                    bannerIndicator.setPosition(i)
                }
            }
        })
    }

    override fun onDestroy() {
        scheduledExecutorService?.shutdownNow()
        scheduledExecutorService2?.shutdownNow()
        super.onDestroy()
    }
}