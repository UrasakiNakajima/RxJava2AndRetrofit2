package com.mobile.rxjava2andretrofit2.kotlin.mine.bean

data class MineDetailsResponse(

//    // 空参的构造方法
//    constructor()

//    //有参数的构造方法
//    constructor(age: Int, name: String) {
//        this.name = name
//        this.age = age
//    }

        val action_to_last_stick: Int,
        val api_base_info: Any,
        val data: List<Data>,
        val feed_flag: Int,
        val get_offline_pool: Boolean,
        val has_more: Boolean,
        val has_more_to_refresh: Boolean,
        val is_use_bytedance_stream: Boolean,
        val last_response_extra: LastResponseExtra,
        val location: Any,
        val login_status: Int,
        val message: String,
        val post_content_hint: String,
        val show_et_status: Int,
        val show_last_read: Boolean,
        val tips: Tips,
        val total_number: Int
)

data class Data(
        val code: String,
        val content: String
)

data class LastResponseExtra(
        val data: String
)

data class Tips(
        val app_name: String,
        val display_duration: Int,
        val display_info: String,
        val display_template: String,
        val download_url: String,
        val open_url: String,
        val package_name: String,
        val type: String,
        val web_url: String
)