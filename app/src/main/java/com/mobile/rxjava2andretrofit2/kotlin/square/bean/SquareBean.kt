package com.mobile.rxjava2andretrofit2.kotlin.square.bean

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.mobile.rxjava2andretrofit2.BR

class SquareBean {

    var `data`: Data? = null
    var errorCode: Int? = null
    var errorMsg: String? = null
}
//    val `data`: Data,
//    val errorCode: Int,
//    val errorMsg: String


class Data {
    var curPage: Int? = null
    var datas: List<DataX>? = null
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
        @Bindable
        get() = field?.toUpperCase()
        set(value) {
            field = value
            notifyPropertyChanged(BR.author); //通知系统数据源发生变化，刷新UI界面
        }
    var canEdit: Boolean? = null
    var chapterId: Int? = null
    var chapterName: String? = null
        @Bindable
        get() = field?.toUpperCase()
        set(value) {
            field = value
            notifyPropertyChanged(BR.chapterName); //通知系统数据源发生变化，刷新UI界面
        }
    var collect: Boolean? = null
    var courseId: Int? = null
    var desc: String? = null
    var descMd: String? = null
    var envelopePic: String? = null
    var fresh: Boolean? = null
    var host: String? = null
    var id: Int? = null
    var link: String? = null
        @Bindable
        get() = field?.toUpperCase()
        set(value) {
            field = value
            notifyPropertyChanged(BR.link); //通知系统数据源发生变化，刷新UI界面
        }
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
        @Bindable
        get() = field?.toUpperCase()
        set(value) {
            field = value
            notifyPropertyChanged(BR.title); //通知系统数据源发生变化，刷新UI界面
        }
    var type: Int? = null
    var userId: Int? = null
    var visible: Int? = null
    var zan: Int? = null
//    var isLoading: Boolean? = null
//        @Bindable
//        get() = field!!
//        set(value) {
//            field = value
//            notifyPropertyChanged(BR.title); //通知系统数据源发生变化，刷新UI界面
//        }

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