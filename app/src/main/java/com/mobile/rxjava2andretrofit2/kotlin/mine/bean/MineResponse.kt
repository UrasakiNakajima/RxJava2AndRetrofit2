package com.mobile.rxjava2andretrofit2.kotlin.mine.bean

data class MineResponse(
    val ans_list: List<Ans>,
    val api_param: String,
    val can_answer: Boolean,
    val candidate_invite_user: List<Any>,
    val err_no: Int,
    val err_tips: String,
    val has_more: Boolean,
    val has_profit: Boolean,
    val module_count: Int,
    val module_list: List<Module>,
    val offset: Int,
    val question: Question,
    val question_header_content_fold_max_count: Int,
    val related_question_banner_type: Int,
    val related_question_reason_url: String,
    val repost_params: RepostParams,
    val show_format: ShowFormat
)

data class Ans(
    val ans_url: String,
    val ansid: String,
    val answer_detail: Any,
    val answer_detail_cdn: String,
    val brow_count: Int,
    val bury_count: Int,
    val category_name: String,
    val comment_count: Int,
    val comment_schema: String,
    val content_abstract: ContentAbstract,
    val create_time: Int,
    val digg_count: Int,
    val display_count: Int,
    val enable_prefetch: Boolean,
    val enable_prefetch_cdn: Boolean,
    val enter_from: String,
    val forward_count: Int,
    val is_buryed: Boolean,
    val is_digg: Boolean,
    val is_origin_pair_ans: Boolean,
    val is_show_bury: Boolean,
    val log_pb: String,
    val read_count: Int,
    val schema: String,
    val share_data: ShareData,
    val show_count: Int,
    val show_text: String,
    val title: String,
    val user: User,
    val user_repin: Boolean
)

data class Module(
    val day_icon_url: String,
    val icon_type: Int,
    val night_icon_url: String,
    val schema: String,
    val text: String
)

data class Question(
    val can_delete: Boolean,
    val can_edit: Boolean,
    val can_not_edit_reason: String,
    val can_public_edit: Boolean,
    val concern_tag_list: List<ConcernTag>,
    val content: Content,
    val count_statistics: List<CountStatistic>,
    val create_time: Int,
    val dispute_info: Any,
    val fold_reason: FoldReason,
    val follow_count: Int,
    val hidden_answer: String,
    val is_anonymous: Int,
    val is_follow: Boolean,
    val is_public_edit: Boolean,
    val modify_record_schema: String,
    val need_user_name: Boolean,
    val nice_ans_count: Int,
    val normal_ans_count: Int,
    val post_answer_url: String,
    val public_edit_reasons: Any,
    val qid: String,
    val qid_type: String,
    val share_data: ShareDataX,
    val share_info: ShareInfo,
    val show_count: Int,
    val show_delete: Boolean,
    val show_edit: Boolean,
    val show_modify_record: Boolean,
    val show_public_edit: Boolean,
    val show_tag_module: Boolean,
    val show_text: String,
    val tips: Tips2,
    val title: String,
    val user: UserX
)

data class RepostParams(
    val cover_url: String,
    val fw_id: Long,
    val fw_id_type: Int,
    val fw_user_id: Long,
    val opt_id: Long,
    val opt_id_type: Int,
    val repost_type: Int,
    val schema: String,
    val title: String
)

data class ShowFormat(
    val answer_full_context_color: Int,
    val font_size: String,
    val show_module: Int
)

data class ContentAbstract(
    val content_rich_span: String,
    val large_image_list: List<LargeImage>,
    val origin_image_list: Any,
    val text: String,
    val thumb_image_list: List<ThumbImage>,
    val video_list: List<Any>,
    val wenda_cv_image_list: Any
)

data class ShareData(
    val content: String,
    val image_url: String,
    val share_url: String,
    val title: String
)

data class User(
    val avatar_url: String,
    val create_time: Int,
    val desc_icon_url: String,
    val is_following: Int,
    val is_verify: Int,
    val schema: String,
    val total_answer: Int,
    val total_digg: Int,
    val uname: String,
    val user_auth_info: String,
    val user_id: String,
    val user_intro: String
)

data class LargeImage(
    val height: Int,
    val type: Int,
    val uri: String,
    val url: String,
    val url_list: List<Url>,
    val width: Int
)

data class ThumbImage(
    val height: Int,
    val type: Int,
    val uri: String,
    val url: String,
    val url_list: List<UrlX>,
    val width: Int
)

data class Url(
    val url: String
)

data class UrlX(
    val url: String
)

data class ConcernTag(
    val concern_id: String,
    val name: String,
    val schema: String
)

data class Content(
    val content_rich_span: String,
    val large_image_list: List<LargeImageX>,
    val origin_image_list: Any,
    val rich_text: String,
    val text: String,
    val thumb_image_list: List<ThumbImageX>
)

data class CountStatistic(
    val count_name: String,
    val count_num: String,
    val count_type: Int
)

data class FoldReason(
    val open_url: String,
    val title: String
)

data class ShareDataX(
    val content: String,
    val image_url: String,
    val share_url: String,
    val title: String
)

data class ShareInfo(
    val share_type: ShareType,
    val share_url: String,
    val title: String,
    val token_type: Int
)

data class Tips2(
    val tips_button_text: String,
    val tips_schema: String,
    val tips_text: String,
    val tips_type: Int
)

data class UserX(
    val avatar_url: String,
    val desc_icon_url: String,
    val is_following: Int,
    val is_verify: Int,
    val schema: String,
    val uname: String,
    val user_auth_info: String,
    val user_id: String,
    val user_intro: String,
    val user_schema: String
)

data class LargeImageX(
    val height: Int,
    val type: Int,
    val uri: String,
    val url: String,
    val url_list: List<UrlXX>,
    val width: Int
)

data class ThumbImageX(
    val height: Int,
    val type: Int,
    val uri: String,
    val url: String,
    val url_list: List<UrlXXX>,
    val width: Int
)

data class UrlXX(
    val url: String
)

data class UrlXXX(
    val url: String
)

data class ShareType(
    val pyq: Int,
    val qq: Int,
    val qzone: Int,
    val wx: Int
)