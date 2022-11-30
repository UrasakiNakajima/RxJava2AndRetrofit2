package com.phone.library_common.bean

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.phone.library_common.BR

class SquareBean {
    var `data`: Data? = null
    var errorCode: Int? = null
    var errorMsg: String? = null
}

class Data {
    var curPage: Int? = null
    var datas: MutableList<DataX>? = null
    var offset: Int? = null
    var over: Boolean? = null
    var pageCount: Int? = null
    var size: Int? = null
    var total: Int? = null
}

class DataX : BaseObservable() {
    var apkLink: String? = null
    var audit: Int? = null

    @get:Bindable
    var author: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.author); //通知系统数据源发生变化，刷新UI界面
        }
    var canEdit: Boolean? = null
    var chapterId: Int? = null

    @get:Bindable
    var chapterName: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.chapterName); //通知系统数据源发生变化，刷新UI界面
        }

    @get:Bindable
    var collect: Boolean? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.collect); //通知系统数据源发生变化，刷新UI界面
        }
    var courseId: Int? = null

    @get:Bindable
    var desc: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.desc); //通知系统数据源发生变化，刷新UI界面
        }
    var descMd: String? = null

    @get:Bindable
    var envelopePic: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.envelopePic); //通知系统数据源发生变化，刷新UI界面
        }
    var fresh: Boolean? = null
    var host: String? = null
    var id: Int? = null

    @get:Bindable
    var link: String? = null
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

    @get:Bindable
    var title: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.title); //通知系统数据源发生变化，刷新UI界面
        }
    var type: Int? = null
    var userId: Int? = null
    var visible: Int? = null
    var zan: Int? = null
    override fun toString(): String {
        return "DataX(apkLink=$apkLink, audit=$audit, canEdit=$canEdit, chapterId=$chapterId, courseId=$courseId, descMd=$descMd, fresh=$fresh, host=$host, id=$id, niceDate=$niceDate, niceShareDate=$niceShareDate, origin=$origin, prefix=$prefix, projectLink=$projectLink, publishTime=$publishTime, realSuperChapterId=$realSuperChapterId, selfVisible=$selfVisible, shareDate=$shareDate, shareUser=$shareUser, superChapterId=$superChapterId, superChapterName=$superChapterName, tags=$tags, type=$type, userId=$userId, visible=$visible, zan=$zan)"
    }

}
