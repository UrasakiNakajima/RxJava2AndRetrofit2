package com.phone.library_base.common

object ConstantData {

    const val TO_FIRST_PAGR_FLAG = "FIRST_PAGR_FLAG"

    const val TO_DOWNLOAD_FILE_FLAG = "DOWNLOAD_FILE_FLAG"

    const val TO_PROJECT_FLAG = "PROJECT_FLAG"

    const val TO_RESOURCE_FLAG = "RESOURCE_FLAG"

    const val TO_MINE_FLAG = "MINE_FLAG"

    const val TO_USER_DATA_FLAG = "USER_DATA_FLAG"

    @JvmStatic
    val TO_FIRST_PAGR_URL = ConstantUrl.BASE_URL

    @JvmStatic
    val TO_DOWNLOAD_FILE_URL = ConstantUrl.BASE_DOWNLOAD_FILE_URL

    @JvmStatic
    val TO_PROJECT_URL = ConstantUrl.BASE_URL3

    @JvmStatic
    val TO_RESOURCE_URL = ConstantUrl.BASE_URL2

    @JvmStatic
    val TO_MINE_URL = ConstantUrl.BASE_URL

    @JvmStatic
    val TO_USER_DATA_URL = ConstantUrl.BASE_URL0

    object Route {
        const val ROUTE_MAIN = "/module_main/main"


        const val ROUTE_HOME = "/module_home/ui/home"
        const val ROUTE_HOME_FRAGMENT = "/module_home/fragment/home_fragment"
        const val ROUTE_HOME_SERVICE = "/module_home/provider/home_service_impl"


        const val ROUTE_PROJECT = "/module_project/ui/project"
        const val ROUTE_PROJECT_FRAGMENT = "/module_project/fragment/project_fragment"
        const val ROUTE_EVENT_SCHEDULE = "/module_project/ui/event_schedule"


        const val ROUTE_SQUARE = "/module_square/ui/square"
        const val ROUTE_SQUARE_FRAGMENT = "/module_square/fragment/square_fragment"
        const val ROUTE_JSBRIDGE = "/module_square/ui/jsbridge"
        const val ROUTE_PICKER_VIEW = "/module_square/ui/picker_view"
        const val ROUTE_CREATE_USER = "/module_square/ui/create_user"
        const val ROUTE_KOTLIN_COROUTINE = "/module_square/ui/kotlin_coroutine"
        const val ROUTE_DECIMAL_OPERATION = "/module_square/ui/decimal_operation"
        const val ROUTE_EDIT_TEXT_INPUT_LIMITS = "/module_square/ui/edit_text_input_limits"
        const val ROUTE_SQUARE_SERVICE = "/module_square/provider/square_service_impl"


        const val ROUTE_RESOURCE = "/module_resource/ui/resource"
        const val ROUTE_RESOURCE_FRAGMENT = "/module_resource/fragment/resource_fragment"


        const val ROUTE_MINE = "/module_mine/ui/mine"
        const val ROUTE_MINE_FRAGMENT = "/module_mine/fragment/mine_fragment"
        const val ROUTE_USER_DATA = "/module_mine/ui/user_data"
        const val ROUTE_THREAD_POOL = "/module_mine/ui/thread_pool"
        const val ROUTE_PARAMS_TRANSFER_CHANGE_PROBLEM =
            "/module_mine/ui/params_transfer_change_problem"


        const val ROUTE_ANDROID_AND_JS = "/library_android_and_js/android_and_js"


        const val ROUTE_BASE64_AND_FILE = "/library_base64_and_file/base64_and_file"


        const val ROUTE_LOGIN = "/library_login/ui/login"
        const val ROUTE_REGISTER = "/library_login/ui/register"
        const val ROUTE_WEB_VIEW = "/library_custom_view/ui/web_view_activity"


        const val ROUTE_MOUNTING = "/library_mounting/mounting"
    }


}