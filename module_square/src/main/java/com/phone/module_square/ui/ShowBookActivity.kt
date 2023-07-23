package com.phone.module_square.ui

import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.fastjson.JSONObject
import com.phone.library_base.common.ConstantData
import com.phone.library_base.manager.ResourcesManager
import com.phone.library_binding.BaseBindingRxAppActivity
import com.phone.library_room.Book
import com.phone.module_square.R
import com.phone.module_square.adapter.BookAdapter
import com.phone.module_square.databinding.SquareActivityShowBookBinding


@Route(path = ConstantData.Route.ROUTE_SHOW_BOOK)
class ShowBookActivity : BaseBindingRxAppActivity<SquareActivityShowBookBinding>() {

    companion object {
        val TAG = ShowBookActivity::class.java.simpleName
    }

    //为每一个参数声明一个字段，并使用 @Autowired 标注
    @Autowired(name = "bookJsonStr")
    lateinit var mBookJsonStr: String

    val mBookList = mutableListOf<Book>()
    private val bookAdapter by lazy { BookAdapter(mRxAppCompatActivity, mBookList) }
    private val linearLayoutManager by lazy {
        LinearLayoutManager(mRxAppCompatActivity)
    }

    override fun initLayoutId(): Int {
        return R.layout.square_activity_show_book
    }

    override fun initData() {
        ARouter.getInstance().inject(this)

        mBookList.clear()
        mBookList.addAll(
            JSONObject.parseArray(
                mBookJsonStr,
                Book::class.java
            )
        )
    }

    override fun initViews() {
        setToolbar(false, R.color.library_black)
        mDatabind.imvBack.setColorFilter(ResourcesManager.getColor(R.color.library_color_FFFFFFFF))
        mDatabind.layoutBack.setOnClickListener {
            finish()
        }

        initAdapter()
    }

    override fun initLoadData() {

    }

    private fun initAdapter() {
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL)
        mDatabind.rcvBookList.apply {
            layoutManager = linearLayoutManager
            itemAnimator = DefaultItemAnimator()
            adapter = bookAdapter
        }
    }


}