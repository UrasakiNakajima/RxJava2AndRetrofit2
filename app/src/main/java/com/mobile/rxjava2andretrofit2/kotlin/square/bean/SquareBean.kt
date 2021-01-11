package com.mobile.rxjava2andretrofit2.kotlin.square.bean

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.mobile.rxjava2andretrofit2.BR

class SquareBean : BaseObservable() {

    var `data`: Data? = null
    var errorCode: Int? = null
    var errorMsg: String? = null
}
//    val `data`: Data,
//    val errorCode: Int,
//    val errorMsg: String


class Data : BaseObservable() {

    var curPage: Int? = null

    // 用 @Bindable 标记过 getxxx() 方法会在 BR 中生成一个 entry。
    // 当数据发生变化时需要调用 notifyPropertyChanged(BR.content) 通知系统
    // BR.content这个 entry 的数据已经发生变化以更新UI。

    var datas: List<DataX>
        @Bindable get() = datas
        set(value) {
            datas = value
            notifyPropertyChanged(BR.datas)
        }

    var offset: Int? = null
    var over: Boolean? = null
    var pageCount: Int? = null
    var size: Int? = null
    var total: Int? = null
}
//        val curPage: Int,
//        val datas: List<DataX>,
//        val offset: Int,
//        val over: Boolean,
//        val pageCount: Int,
//        val size: Int,
//        val total: Int


class DataX : BaseObservable() {
    var apkLink: String? = null
    var audit: Int? = null
    var author: String? = null
    var canEdit: Boolean? = null
    var chapterId: Int? = null
    var chapterName: String? = null
    var collect: Boolean? = null
    var courseId: Int? = null
    var desc: String? = null
    var descMd: String? = null
    var envelopePic: String? = null
    var fresh: Boolean? = null
    var host: String? = null
    var id: Int? = null
    var link: String? = null
    var niceDate: String? = null
    var niceShareDate: String? = null
    var origin: String? = null
    var prefix: String? = null
    var projectLink: String? = null
    var publishTime: Long? = null
    var realSuperChapterId: Int? = null
    var selfVisible: Int? = null
    var shareDate: Long? = null
    var shareUser: String? = null
    var superChapterId: Int? = null
    var superChapterName: String? = null
    var tags: List<Any>? = null
    var title: String? = null
    var type: Int? = null
    var userId: Int? = null
    var visible: Int? = null
    var zan: Int? = null
}

//val apkLink: String,
//val audit: Int,
//val author: String,
//val canEdit: Boolean,
//val chapterId: Int,
//val chapterName: String,
//val collect: Boolean,
//val courseId: Int,
//val desc: String,
//val descMd: String,
//val envelopePic: String,
//val fresh: Boolean,
//val host: String,
//val id: Int,
//val link: String,
//val niceDate: String,
//val niceShareDate: String,
//val origin: String,
//val prefix: String,
//val projectLink: String,
//val publishTime: Long,
//val realSuperChapterId: Int,
//val selfVisible: Int,
//val shareDate: Long,
//val shareUser: String,
//val superChapterId: Int,
//val superChapterName: String,
//val tags: List<Any>,
//val title: String,
//val type: Int,
//val userId: Int,
//val visible: Int,
//val zan: Int