package com.mobile.mine_module.bean

class MineDetailsResponse() {

//    // 空参的构造方法
//    constructor()

//    //有参数的构造方法
//    constructor(age: Int, name: String) {
//        this.name = name
//        this.age = age
//    }

    var action_to_last_stick: Int? = null
    var api_base_info: Any? = null
    var data: List<Data>? = null
    var feed_flag: Int? = null
    var get_offline_pool: Boolean? = null
    var has_more: Boolean? = null
    var has_more_to_refresh: Boolean? = null
    var is_use_bytedance_stream: Boolean? = null
    var last_response_extra: LastResponseExtra? = null
    var location: Any? = null
    var login_status: Int? = null
    var message: String? = null
    var post_content_hint: String? = null
    var show_et_status: Int? = null
    var show_last_read: Boolean? = null
    var tips: Tips? = null
    var total_number: Int? = null
}

class Data() {
    var code: String? = null
    var content: String? = null
}

class LastResponseExtra() {
    var data: String? = null
}

class Tips() {
    var app_name: String? = null
    var display_duration: Int? = null
    var display_info: String? = null
    var display_template: String? = null
    var download_url: String? = null
    var open_url: String? = null
    var package_name: String? = null
    var type: String? = null
    var web_url: String? = null
}