package com.phone.library_common.bean

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.phone.library_common.BR

class TabBean() : BaseObservable() {

    /**
     * children : []
     * courseId : 13
     * id : 294
     * name : 完整项目
     * order : 145000
     * parentChapterId : 293
     * userControlSetTop : false
     * visible : 0
     */
    var courseId = 0
    var id = 0

    @get:Bindable
    var name: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.chapterName); //通知系统数据源发生变化，刷新UI界面
        }
    var order = 0
    var parentChapterId = 0
    var userControlSetTop = false
    var visible = 0
    var children: List<*>? = null
    override fun toString(): String {
        return "TabBean(courseId=$courseId, id=$id, order=$order, parentChapterId=$parentChapterId, userControlSetTop=$userControlSetTop, visible=$visible, children=$children)"
    }


}