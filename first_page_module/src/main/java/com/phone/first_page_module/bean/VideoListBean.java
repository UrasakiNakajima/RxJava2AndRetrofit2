package com.phone.first_page_module.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoListBean {

    /**
     * abstract :
     * action_extra : {"channel_id": 3431225546}
     * action_list : [{"action":1,"desc":"","extra":{}},{"action":3,"desc":"","extra":{}},{"action":7,"desc":"","extra":{}},{"action":9,"desc":"","extra":{}}]
     * aggr_type : 1
     * allow_download : false
     * article_sub_type : 0
     * article_type : 0
     * article_url : http://toutiao.com/group/6896711085110657544/
     * article_version : 0
     * ban_comment : 0
     * ban_danmaku : false
     * ban_immersive : 0
     * behot_time : 1605773155
     * bury_count : 0
     * bury_style_show : 1
     * cell_flag : 268697611
     * cell_layout_style : 1
     * cell_type : 0
     * comment_count : 15
     * content_decoration :
     * control_panel : {"recommend_sponsor":{"label":"帮上头条","icon_url":"http://p3-tt.bytecdn.cn/origin/13ef000096960314fff4","night_icon_url":"http://p3-tt.bytecdn.cn/origin/dc1d0001ad958473e24b","target_url":"https://i.snssdk.com/ad/pgc_promotion/mobile/create/?group_id=6896711085110657544&item_id=6896711085110657544"}}
     * cursor : 1605773155000
     * danmaku_count : 0
     * digg_count : 60
     * display_url : http://toutiao.com/group/6896711085110657544/
     * filter_words : [{"id":"8:0","name":"看过了","is_selected":false},{"id":"9:1","name":"内容太水","is_selected":false},{"id":"5:2619879040","name":"拉黑作者:西瓜大学","is_selected":false},{"id":"6:16030","name":"不想看:视频","is_selected":false}]
     * forward_info : {"forward_count":1}
     * group_flags : 32832
     * group_id : 6896711085110657544
     * group_source : 2
     * has_m3u8_video : false
     * has_mp4_video : 0
     * has_video : true
     * hot : 0
     * ignore_web_transform : 1
     * interaction_data :
     * is_key_video : false
     * is_subject : false
     * item_id : 6896711085110657544
     * item_version : 0
     * large_image_list : [{"url":"http://p6-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:1920:1080.jpg","width":580,"url_list":[{"url":"http://p6-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:1920:1080.jpg"},{"url":"http://p3-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:1920:1080.jpg"},{"url":"http://p26-tt.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:1920:1080.jpg"}],"uri":"video1609/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a","height":327}]
     * level : 0
     * log_pb : {"impr_id":"202011191613250102040590901004672C","is_following":"0"}
     * media_info : {"avatar_url":"http://p6-tt-ipv6.byteimg.com/large/user-avatar/be28e3e2923ea554c9f541a4ed1e2b08","name":"西瓜大学","user_verified":true,"media_id":1561008133659650,"user_id":57015909540,"verified_content":"","is_star_user":false,"recommend_reason":"","recommend_type":0,"follow":false}
     * media_name : 西瓜大学
     * middle_image : {"url":"http://p1-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:300:196.webp","width":300,"url_list":[{"url":"http://p1-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:300:196.webp"},{"url":"http://p1-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:300:196.webp"},{"url":"http://p3-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:300:196.webp"}],"uri":"list/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a","height":196}
     * need_client_impr_recycle : 1
     * opcat_activity : null
     * play_auth_token : HMAC-SHA1:2.0:1605860006042278954:bab42eac5b9e4a8eb25a91fc371ad533:lI1B4zHHxwFCD5QomApD/CRAjcY=
     * play_biz_token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MDU4NjAwMDYsInZlciI6InYxIiwiYWsiOiJiYWI0MmVhYzViOWU0YThlYjI1YTkxZmMzNzFhZDUzMyIsInN1YiI6InBnY18xMDgwcCJ9.Y28D0r2DkeMYgCVkzTZogmSTE1Cx0Q1gBiwIuGCVpao
     * publish_time : 1605765681
     * read_count : 217
     * repin_count : 4
     * rid : 202011191613250102040590901004672C
     * share_count : 3
     * share_info : {"share_url":"https://m.toutiaoimg.cn/a6896711085110657544/?app=news_article&is_hit_share_recommend=0","title":"「审核规则」视频涉嫌违背社会公序良俗？官方运营解读发文规范 - 今日头条","description":null,"cover_image":null,"share_type":{"pyq":0,"qq":0,"qzone":0,"wx":0},"weixin_cover_image":{"url":"http://p3-tt-ipv6.byteimg.com/img/tos-cn-i-0000/723925ae-2a2d-11eb-b2a5-fa163e1906f9~tplv-tt-cs0:640:360.jpg","width":640,"url_list":[{"url":"http://p3-tt-ipv6.byteimg.com/img/tos-cn-i-0000/723925ae-2a2d-11eb-b2a5-fa163e1906f9~tplv-tt-cs0:640:360.jpg"},{"url":"http://p1-tt-ipv6.byteimg.com/img/tos-cn-i-0000/723925ae-2a2d-11eb-b2a5-fa163e1906f9~tplv-tt-cs0:640:360.jpg"},{"url":"http://p6-tt-ipv6.byteimg.com/img/tos-cn-i-0000/723925ae-2a2d-11eb-b2a5-fa163e1906f9~tplv-tt-cs0:640:360.jpg"}],"uri":"large/tos-cn-i-0000/723925ae-2a2d-11eb-b2a5-fa163e1906f9","height":360},"token_type":1,"on_suppress":0,"hidden_url":null,"video_url":null,"share_control":null}
     * share_large_image : {"url":"http://p6-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:960:540.webp","width":960,"url_list":[{"url":"http://p6-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:960:540.webp"},{"url":"http://p26-tt.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:960:540.webp"},{"url":"http://p6-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:960:540.webp"}],"uri":"large/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a","height":540}
     * share_type : 2
     * share_url : https://m.toutiaoimg.cn/a6896711085110657544/?app=news_article&is_hit_share_recommend=0
     * show_dislike : true
     * show_portrait : false
     * show_portrait_article : false
     * small_image : null
     * source : 西瓜大学
     * source_icon_style : 6
     * source_open_url : sslocal://profile?refer=video&uid=57015909540
     * tag : impact_xigua
     * tag_id : 6896711085110657544
     * tip : 0
     * title : 「审核规则」视频涉嫌违背社会公序良俗？官方运营解读发文规范
     * ugc_recommend : {"activity":"","reason":"西瓜视频官方账号"}
     * url : http://toutiao.com/group/6896711085110657544/
     * user_info : {"avatar_url":"https://sf3-ttcdn-tos.pstatp.com/img/user-avatar/be28e3e2923ea554c9f541a4ed1e2b08~300x300.image","name":"西瓜大学","description":"学拍短视频，就上西瓜大学。","user_id":57015909540,"user_verified":true,"verified_content":"西瓜视频官方账号","follow":false,"follower_count":0,"user_auth_info":"{\"auth_type\":\"0\",\"auth_info\":\"西瓜视频官方账号\"}","schema":"sslocal://profile?uid=57015909540&refer=video","living_count":0}
     * user_repin : 0
     * user_verified : 1
     * verified_content : 西瓜视频官方账号
     * video_detail_info : {"group_flags":32832,"detail_video_large_image":{"url":"http://p6-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:1920:1080.jpg","width":580,"url_list":[{"url":"http://p6-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:1920:1080.jpg"},{"url":"http://p9-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:1920:1080.jpg"},{"url":"http://p1-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:1920:1080.jpg"}],"uri":"video1609/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a","height":327},"video_id":"v03004b40000bur0jgn7ptql8dj90b70","direct_play":1,"show_pgc_subscribe":1,"video_watch_count":487,"video_type":0,"video_watching_count":0,"video_preloading_flag":1}
     * video_duration : 334
     * video_id : v03004b40000bur0jgn7ptql8dj90b70
     * video_style : 3
     * xi_related : false
     */

    @SerializedName("abstract")
    private String abstractX;
    private String action_extra;
    private int aggr_type;
    private boolean allow_download;
    private int article_sub_type;
    private int article_type;
    private String article_url;
    private int article_version;
    private int ban_comment;
    private boolean ban_danmaku;
    private int ban_immersive;
    private int behot_time;
    private int bury_count;
    private int bury_style_show;
    private int cell_flag;
    private int cell_layout_style;
    private int cell_type;
    private int comment_count;
    private String content_decoration;
    private ControlPanelBean control_panel;
    private long cursor;
    private int danmaku_count;
    private int digg_count;
    private String display_url;
    private ForwardInfoBean forward_info;
    private int group_flags;
    private long group_id;
    private int group_source;
    private boolean has_m3u8_video;
    private int has_mp4_video;
    private boolean has_video;
    private int hot;
    private int ignore_web_transform;
    private String interaction_data;
    private boolean is_key_video;
    private boolean is_subject;
    private long item_id;
    private int item_version;
    private int level;
    private LogPbBean log_pb;
    private MediaInfoBean media_info;
    private String media_name;
    private MiddleImageBean middle_image;
    private int need_client_impr_recycle;
    private Object opcat_activity;
    private String play_auth_token;
    private String play_biz_token;
    private int publish_time;
    private int read_count;
    private int repin_count;
    private String rid;
    private int share_count;
    private ShareInfoBean share_info;
    private ShareLargeImageBean share_large_image;
    private int share_type;
    private String share_url;
    private boolean show_dislike;
    private boolean show_portrait;
    private boolean show_portrait_article;
    private Object small_image;
    private String source;
    private int source_icon_style;
    private String source_open_url;
    private String tag;
    private long tag_id;
    private int tip;
    private String title;
    private UgcRecommendBean ugc_recommend;
    private String url;
    private UserInfoBean user_info;
    private int user_repin;
    private int user_verified;
    private String verified_content;
    private VideoDetailInfoBean video_detail_info;
    private int video_duration;
    private String video_id;
    private int video_style;
    private boolean xi_related;
    private List<ActionListBean> action_list;
    private List<FilterWordsBean> filter_words;
    private List<LargeImageListBean> large_image_list;

    public String getAbstractX() {
        return abstractX;
    }

    public void setAbstractX(String abstractX) {
        this.abstractX = abstractX;
    }

    public String getAction_extra() {
        return action_extra;
    }

    public void setAction_extra(String action_extra) {
        this.action_extra = action_extra;
    }

    public int getAggr_type() {
        return aggr_type;
    }

    public void setAggr_type(int aggr_type) {
        this.aggr_type = aggr_type;
    }

    public boolean isAllow_download() {
        return allow_download;
    }

    public void setAllow_download(boolean allow_download) {
        this.allow_download = allow_download;
    }

    public int getArticle_sub_type() {
        return article_sub_type;
    }

    public void setArticle_sub_type(int article_sub_type) {
        this.article_sub_type = article_sub_type;
    }

    public int getArticle_type() {
        return article_type;
    }

    public void setArticle_type(int article_type) {
        this.article_type = article_type;
    }

    public String getArticle_url() {
        return article_url;
    }

    public void setArticle_url(String article_url) {
        this.article_url = article_url;
    }

    public int getArticle_version() {
        return article_version;
    }

    public void setArticle_version(int article_version) {
        this.article_version = article_version;
    }

    public int getBan_comment() {
        return ban_comment;
    }

    public void setBan_comment(int ban_comment) {
        this.ban_comment = ban_comment;
    }

    public boolean isBan_danmaku() {
        return ban_danmaku;
    }

    public void setBan_danmaku(boolean ban_danmaku) {
        this.ban_danmaku = ban_danmaku;
    }

    public int getBan_immersive() {
        return ban_immersive;
    }

    public void setBan_immersive(int ban_immersive) {
        this.ban_immersive = ban_immersive;
    }

    public int getBehot_time() {
        return behot_time;
    }

    public void setBehot_time(int behot_time) {
        this.behot_time = behot_time;
    }

    public int getBury_count() {
        return bury_count;
    }

    public void setBury_count(int bury_count) {
        this.bury_count = bury_count;
    }

    public int getBury_style_show() {
        return bury_style_show;
    }

    public void setBury_style_show(int bury_style_show) {
        this.bury_style_show = bury_style_show;
    }

    public int getCell_flag() {
        return cell_flag;
    }

    public void setCell_flag(int cell_flag) {
        this.cell_flag = cell_flag;
    }

    public int getCell_layout_style() {
        return cell_layout_style;
    }

    public void setCell_layout_style(int cell_layout_style) {
        this.cell_layout_style = cell_layout_style;
    }

    public int getCell_type() {
        return cell_type;
    }

    public void setCell_type(int cell_type) {
        this.cell_type = cell_type;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public String getContent_decoration() {
        return content_decoration;
    }

    public void setContent_decoration(String content_decoration) {
        this.content_decoration = content_decoration;
    }

    public ControlPanelBean getControl_panel() {
        return control_panel;
    }

    public void setControl_panel(ControlPanelBean control_panel) {
        this.control_panel = control_panel;
    }

    public long getCursor() {
        return cursor;
    }

    public void setCursor(long cursor) {
        this.cursor = cursor;
    }

    public int getDanmaku_count() {
        return danmaku_count;
    }

    public void setDanmaku_count(int danmaku_count) {
        this.danmaku_count = danmaku_count;
    }

    public int getDigg_count() {
        return digg_count;
    }

    public void setDigg_count(int digg_count) {
        this.digg_count = digg_count;
    }

    public String getDisplay_url() {
        return display_url;
    }

    public void setDisplay_url(String display_url) {
        this.display_url = display_url;
    }

    public ForwardInfoBean getForward_info() {
        return forward_info;
    }

    public void setForward_info(ForwardInfoBean forward_info) {
        this.forward_info = forward_info;
    }

    public int getGroup_flags() {
        return group_flags;
    }

    public void setGroup_flags(int group_flags) {
        this.group_flags = group_flags;
    }

    public long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(long group_id) {
        this.group_id = group_id;
    }

    public int getGroup_source() {
        return group_source;
    }

    public void setGroup_source(int group_source) {
        this.group_source = group_source;
    }

    public boolean isHas_m3u8_video() {
        return has_m3u8_video;
    }

    public void setHas_m3u8_video(boolean has_m3u8_video) {
        this.has_m3u8_video = has_m3u8_video;
    }

    public int getHas_mp4_video() {
        return has_mp4_video;
    }

    public void setHas_mp4_video(int has_mp4_video) {
        this.has_mp4_video = has_mp4_video;
    }

    public boolean isHas_video() {
        return has_video;
    }

    public void setHas_video(boolean has_video) {
        this.has_video = has_video;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public int getIgnore_web_transform() {
        return ignore_web_transform;
    }

    public void setIgnore_web_transform(int ignore_web_transform) {
        this.ignore_web_transform = ignore_web_transform;
    }

    public String getInteraction_data() {
        return interaction_data;
    }

    public void setInteraction_data(String interaction_data) {
        this.interaction_data = interaction_data;
    }

    public boolean isIs_key_video() {
        return is_key_video;
    }

    public void setIs_key_video(boolean is_key_video) {
        this.is_key_video = is_key_video;
    }

    public boolean isIs_subject() {
        return is_subject;
    }

    public void setIs_subject(boolean is_subject) {
        this.is_subject = is_subject;
    }

    public long getItem_id() {
        return item_id;
    }

    public void setItem_id(long item_id) {
        this.item_id = item_id;
    }

    public int getItem_version() {
        return item_version;
    }

    public void setItem_version(int item_version) {
        this.item_version = item_version;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public LogPbBean getLog_pb() {
        return log_pb;
    }

    public void setLog_pb(LogPbBean log_pb) {
        this.log_pb = log_pb;
    }

    public MediaInfoBean getMedia_info() {
        return media_info;
    }

    public void setMedia_info(MediaInfoBean media_info) {
        this.media_info = media_info;
    }

    public String getMedia_name() {
        return media_name;
    }

    public void setMedia_name(String media_name) {
        this.media_name = media_name;
    }

    public MiddleImageBean getMiddle_image() {
        return middle_image;
    }

    public void setMiddle_image(MiddleImageBean middle_image) {
        this.middle_image = middle_image;
    }

    public int getNeed_client_impr_recycle() {
        return need_client_impr_recycle;
    }

    public void setNeed_client_impr_recycle(int need_client_impr_recycle) {
        this.need_client_impr_recycle = need_client_impr_recycle;
    }

    public Object getOpcat_activity() {
        return opcat_activity;
    }

    public void setOpcat_activity(Object opcat_activity) {
        this.opcat_activity = opcat_activity;
    }

    public String getPlay_auth_token() {
        return play_auth_token;
    }

    public void setPlay_auth_token(String play_auth_token) {
        this.play_auth_token = play_auth_token;
    }

    public String getPlay_biz_token() {
        return play_biz_token;
    }

    public void setPlay_biz_token(String play_biz_token) {
        this.play_biz_token = play_biz_token;
    }

    public int getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(int publish_time) {
        this.publish_time = publish_time;
    }

    public int getRead_count() {
        return read_count;
    }

    public void setRead_count(int read_count) {
        this.read_count = read_count;
    }

    public int getRepin_count() {
        return repin_count;
    }

    public void setRepin_count(int repin_count) {
        this.repin_count = repin_count;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public int getShare_count() {
        return share_count;
    }

    public void setShare_count(int share_count) {
        this.share_count = share_count;
    }

    public ShareInfoBean getShare_info() {
        return share_info;
    }

    public void setShare_info(ShareInfoBean share_info) {
        this.share_info = share_info;
    }

    public ShareLargeImageBean getShare_large_image() {
        return share_large_image;
    }

    public void setShare_large_image(ShareLargeImageBean share_large_image) {
        this.share_large_image = share_large_image;
    }

    public int getShare_type() {
        return share_type;
    }

    public void setShare_type(int share_type) {
        this.share_type = share_type;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public boolean isShow_dislike() {
        return show_dislike;
    }

    public void setShow_dislike(boolean show_dislike) {
        this.show_dislike = show_dislike;
    }

    public boolean isShow_portrait() {
        return show_portrait;
    }

    public void setShow_portrait(boolean show_portrait) {
        this.show_portrait = show_portrait;
    }

    public boolean isShow_portrait_article() {
        return show_portrait_article;
    }

    public void setShow_portrait_article(boolean show_portrait_article) {
        this.show_portrait_article = show_portrait_article;
    }

    public Object getSmall_image() {
        return small_image;
    }

    public void setSmall_image(Object small_image) {
        this.small_image = small_image;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getSource_icon_style() {
        return source_icon_style;
    }

    public void setSource_icon_style(int source_icon_style) {
        this.source_icon_style = source_icon_style;
    }

    public String getSource_open_url() {
        return source_open_url;
    }

    public void setSource_open_url(String source_open_url) {
        this.source_open_url = source_open_url;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getTag_id() {
        return tag_id;
    }

    public void setTag_id(long tag_id) {
        this.tag_id = tag_id;
    }

    public int getTip() {
        return tip;
    }

    public void setTip(int tip) {
        this.tip = tip;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UgcRecommendBean getUgc_recommend() {
        return ugc_recommend;
    }

    public void setUgc_recommend(UgcRecommendBean ugc_recommend) {
        this.ugc_recommend = ugc_recommend;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UserInfoBean getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfoBean user_info) {
        this.user_info = user_info;
    }

    public int getUser_repin() {
        return user_repin;
    }

    public void setUser_repin(int user_repin) {
        this.user_repin = user_repin;
    }

    public int getUser_verified() {
        return user_verified;
    }

    public void setUser_verified(int user_verified) {
        this.user_verified = user_verified;
    }

    public String getVerified_content() {
        return verified_content;
    }

    public void setVerified_content(String verified_content) {
        this.verified_content = verified_content;
    }

    public VideoDetailInfoBean getVideo_detail_info() {
        return video_detail_info;
    }

    public void setVideo_detail_info(VideoDetailInfoBean video_detail_info) {
        this.video_detail_info = video_detail_info;
    }

    public int getVideo_duration() {
        return video_duration;
    }

    public void setVideo_duration(int video_duration) {
        this.video_duration = video_duration;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public int getVideo_style() {
        return video_style;
    }

    public void setVideo_style(int video_style) {
        this.video_style = video_style;
    }

    public boolean isXi_related() {
        return xi_related;
    }

    public void setXi_related(boolean xi_related) {
        this.xi_related = xi_related;
    }

    public List<ActionListBean> getAction_list() {
        return action_list;
    }

    public void setAction_list(List<ActionListBean> action_list) {
        this.action_list = action_list;
    }

    public List<FilterWordsBean> getFilter_words() {
        return filter_words;
    }

    public void setFilter_words(List<FilterWordsBean> filter_words) {
        this.filter_words = filter_words;
    }

    public List<LargeImageListBean> getLarge_image_list() {
        return large_image_list;
    }

    public void setLarge_image_list(List<LargeImageListBean> large_image_list) {
        this.large_image_list = large_image_list;
    }

    public static class ControlPanelBean {
        /**
         * recommend_sponsor : {"label":"帮上头条","icon_url":"http://p3-tt.bytecdn.cn/origin/13ef000096960314fff4","night_icon_url":"http://p3-tt.bytecdn.cn/origin/dc1d0001ad958473e24b","target_url":"https://i.snssdk.com/ad/pgc_promotion/mobile/create/?group_id=6896711085110657544&item_id=6896711085110657544"}
         */

        private RecommendSponsorBean recommend_sponsor;

        public RecommendSponsorBean getRecommend_sponsor() {
            return recommend_sponsor;
        }

        public void setRecommend_sponsor(RecommendSponsorBean recommend_sponsor) {
            this.recommend_sponsor = recommend_sponsor;
        }

        public static class RecommendSponsorBean {
            /**
             * label : 帮上头条
             * icon_url : http://p3-tt.bytecdn.cn/origin/13ef000096960314fff4
             * night_icon_url : http://p3-tt.bytecdn.cn/origin/dc1d0001ad958473e24b
             * target_url : https://i.snssdk.com/ad/pgc_promotion/mobile/create/?group_id=6896711085110657544&item_id=6896711085110657544
             */

            private String label;
            private String icon_url;
            private String night_icon_url;
            private String target_url;

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public String getIcon_url() {
                return icon_url;
            }

            public void setIcon_url(String icon_url) {
                this.icon_url = icon_url;
            }

            public String getNight_icon_url() {
                return night_icon_url;
            }

            public void setNight_icon_url(String night_icon_url) {
                this.night_icon_url = night_icon_url;
            }

            public String getTarget_url() {
                return target_url;
            }

            public void setTarget_url(String target_url) {
                this.target_url = target_url;
            }
        }
    }

    public static class ForwardInfoBean {
        /**
         * forward_count : 1
         */

        private int forward_count;

        public int getForward_count() {
            return forward_count;
        }

        public void setForward_count(int forward_count) {
            this.forward_count = forward_count;
        }
    }

    public static class LogPbBean {
        /**
         * impr_id : 202011191613250102040590901004672C
         * is_following : 0
         */

        private String impr_id;
        private String is_following;

        public String getImpr_id() {
            return impr_id;
        }

        public void setImpr_id(String impr_id) {
            this.impr_id = impr_id;
        }

        public String getIs_following() {
            return is_following;
        }

        public void setIs_following(String is_following) {
            this.is_following = is_following;
        }
    }

    public static class MediaInfoBean {
        /**
         * avatar_url : http://p6-tt-ipv6.byteimg.com/large/user-avatar/be28e3e2923ea554c9f541a4ed1e2b08
         * name : 西瓜大学
         * user_verified : true
         * media_id : 1561008133659650
         * user_id : 57015909540
         * verified_content :
         * is_star_user : false
         * recommend_reason :
         * recommend_type : 0
         * follow : false
         */

        private String avatar_url;
        private String name;
        private boolean user_verified;
        private long media_id;
        private long user_id;
        private String verified_content;
        private boolean is_star_user;
        private String recommend_reason;
        private int recommend_type;
        private boolean follow;

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isUser_verified() {
            return user_verified;
        }

        public void setUser_verified(boolean user_verified) {
            this.user_verified = user_verified;
        }

        public long getMedia_id() {
            return media_id;
        }

        public void setMedia_id(long media_id) {
            this.media_id = media_id;
        }

        public long getUser_id() {
            return user_id;
        }

        public void setUser_id(long user_id) {
            this.user_id = user_id;
        }

        public String getVerified_content() {
            return verified_content;
        }

        public void setVerified_content(String verified_content) {
            this.verified_content = verified_content;
        }

        public boolean isIs_star_user() {
            return is_star_user;
        }

        public void setIs_star_user(boolean is_star_user) {
            this.is_star_user = is_star_user;
        }

        public String getRecommend_reason() {
            return recommend_reason;
        }

        public void setRecommend_reason(String recommend_reason) {
            this.recommend_reason = recommend_reason;
        }

        public int getRecommend_type() {
            return recommend_type;
        }

        public void setRecommend_type(int recommend_type) {
            this.recommend_type = recommend_type;
        }

        public boolean isFollow() {
            return follow;
        }

        public void setFollow(boolean follow) {
            this.follow = follow;
        }
    }

    public static class MiddleImageBean {
        /**
         * url : http://p1-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:300:196.webp
         * width : 300
         * url_list : [{"url":"http://p1-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:300:196.webp"},{"url":"http://p1-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:300:196.webp"},{"url":"http://p3-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:300:196.webp"}]
         * uri : list/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a
         * height : 196
         */

        private String url;
        private int width;
        private String uri;
        private int height;
        private List<UrlListBean> url_list;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public List<UrlListBean> getUrl_list() {
            return url_list;
        }

        public void setUrl_list(List<UrlListBean> url_list) {
            this.url_list = url_list;
        }

        public static class UrlListBean {
            /**
             * url : http://p1-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:300:196.webp
             */

            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }

    public static class ShareInfoBean {
        /**
         * share_url : https://m.toutiaoimg.cn/a6896711085110657544/?app=news_article&is_hit_share_recommend=0
         * title : 「审核规则」视频涉嫌违背社会公序良俗？官方运营解读发文规范 - 今日头条
         * description : null
         * cover_image : null
         * share_type : {"pyq":0,"qq":0,"qzone":0,"wx":0}
         * weixin_cover_image : {"url":"http://p3-tt-ipv6.byteimg.com/img/tos-cn-i-0000/723925ae-2a2d-11eb-b2a5-fa163e1906f9~tplv-tt-cs0:640:360.jpg","width":640,"url_list":[{"url":"http://p3-tt-ipv6.byteimg.com/img/tos-cn-i-0000/723925ae-2a2d-11eb-b2a5-fa163e1906f9~tplv-tt-cs0:640:360.jpg"},{"url":"http://p1-tt-ipv6.byteimg.com/img/tos-cn-i-0000/723925ae-2a2d-11eb-b2a5-fa163e1906f9~tplv-tt-cs0:640:360.jpg"},{"url":"http://p6-tt-ipv6.byteimg.com/img/tos-cn-i-0000/723925ae-2a2d-11eb-b2a5-fa163e1906f9~tplv-tt-cs0:640:360.jpg"}],"uri":"large/tos-cn-i-0000/723925ae-2a2d-11eb-b2a5-fa163e1906f9","height":360}
         * token_type : 1
         * on_suppress : 0
         * hidden_url : null
         * video_url : null
         * share_control : null
         */

        private String share_url;
        private String title;
        private Object description;
        private Object cover_image;
        private ShareTypeBean share_type;
        private WeixinCoverImageBean weixin_cover_image;
        private int token_type;
        private int on_suppress;
        private Object hidden_url;
        private Object video_url;
        private Object share_control;

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Object getDescription() {
            return description;
        }

        public void setDescription(Object description) {
            this.description = description;
        }

        public Object getCover_image() {
            return cover_image;
        }

        public void setCover_image(Object cover_image) {
            this.cover_image = cover_image;
        }

        public ShareTypeBean getShare_type() {
            return share_type;
        }

        public void setShare_type(ShareTypeBean share_type) {
            this.share_type = share_type;
        }

        public WeixinCoverImageBean getWeixin_cover_image() {
            return weixin_cover_image;
        }

        public void setWeixin_cover_image(WeixinCoverImageBean weixin_cover_image) {
            this.weixin_cover_image = weixin_cover_image;
        }

        public int getToken_type() {
            return token_type;
        }

        public void setToken_type(int token_type) {
            this.token_type = token_type;
        }

        public int getOn_suppress() {
            return on_suppress;
        }

        public void setOn_suppress(int on_suppress) {
            this.on_suppress = on_suppress;
        }

        public Object getHidden_url() {
            return hidden_url;
        }

        public void setHidden_url(Object hidden_url) {
            this.hidden_url = hidden_url;
        }

        public Object getVideo_url() {
            return video_url;
        }

        public void setVideo_url(Object video_url) {
            this.video_url = video_url;
        }

        public Object getShare_control() {
            return share_control;
        }

        public void setShare_control(Object share_control) {
            this.share_control = share_control;
        }

        public static class ShareTypeBean {
            /**
             * pyq : 0
             * qq : 0
             * qzone : 0
             * wx : 0
             */

            private int pyq;
            private int qq;
            private int qzone;
            private int wx;

            public int getPyq() {
                return pyq;
            }

            public void setPyq(int pyq) {
                this.pyq = pyq;
            }

            public int getQq() {
                return qq;
            }

            public void setQq(int qq) {
                this.qq = qq;
            }

            public int getQzone() {
                return qzone;
            }

            public void setQzone(int qzone) {
                this.qzone = qzone;
            }

            public int getWx() {
                return wx;
            }

            public void setWx(int wx) {
                this.wx = wx;
            }
        }

        public static class WeixinCoverImageBean {
            /**
             * url : http://p3-tt-ipv6.byteimg.com/img/tos-cn-i-0000/723925ae-2a2d-11eb-b2a5-fa163e1906f9~tplv-tt-cs0:640:360.jpg
             * width : 640
             * url_list : [{"url":"http://p3-tt-ipv6.byteimg.com/img/tos-cn-i-0000/723925ae-2a2d-11eb-b2a5-fa163e1906f9~tplv-tt-cs0:640:360.jpg"},{"url":"http://p1-tt-ipv6.byteimg.com/img/tos-cn-i-0000/723925ae-2a2d-11eb-b2a5-fa163e1906f9~tplv-tt-cs0:640:360.jpg"},{"url":"http://p6-tt-ipv6.byteimg.com/img/tos-cn-i-0000/723925ae-2a2d-11eb-b2a5-fa163e1906f9~tplv-tt-cs0:640:360.jpg"}]
             * uri : large/tos-cn-i-0000/723925ae-2a2d-11eb-b2a5-fa163e1906f9
             * height : 360
             */

            private String url;
            private int width;
            private String uri;
            private int height;
            private List<UrlListBeanX> url_list;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public String getUri() {
                return uri;
            }

            public void setUri(String uri) {
                this.uri = uri;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public List<UrlListBeanX> getUrl_list() {
                return url_list;
            }

            public void setUrl_list(List<UrlListBeanX> url_list) {
                this.url_list = url_list;
            }

            public static class UrlListBeanX {
                /**
                 * url : http://p3-tt-ipv6.byteimg.com/img/tos-cn-i-0000/723925ae-2a2d-11eb-b2a5-fa163e1906f9~tplv-tt-cs0:640:360.jpg
                 */

                private String url;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }
        }
    }

    public static class ShareLargeImageBean {
        /**
         * url : http://p6-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:960:540.webp
         * width : 960
         * url_list : [{"url":"http://p6-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:960:540.webp"},{"url":"http://p26-tt.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:960:540.webp"},{"url":"http://p6-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:960:540.webp"}]
         * uri : large/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a
         * height : 540
         */

        private String url;
        private int width;
        private String uri;
        private int height;
        private List<UrlListBeanXX> url_list;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public List<UrlListBeanXX> getUrl_list() {
            return url_list;
        }

        public void setUrl_list(List<UrlListBeanXX> url_list) {
            this.url_list = url_list;
        }

        public static class UrlListBeanXX {
            /**
             * url : http://p6-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:960:540.webp
             */

            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }

    public static class UgcRecommendBean {
        /**
         * activity :
         * reason : 西瓜视频官方账号
         */

        private String activity;
        private String reason;

        public String getActivity() {
            return activity;
        }

        public void setActivity(String activity) {
            this.activity = activity;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }

    public static class UserInfoBean {
        /**
         * avatar_url : https://sf3-ttcdn-tos.pstatp.com/img/user-avatar/be28e3e2923ea554c9f541a4ed1e2b08~300x300.image
         * name : 西瓜大学
         * description : 学拍短视频，就上西瓜大学。
         * user_id : 57015909540
         * user_verified : true
         * verified_content : 西瓜视频官方账号
         * follow : false
         * follower_count : 0
         * user_auth_info : {"auth_type":"0","auth_info":"西瓜视频官方账号"}
         * schema : sslocal://profile?uid=57015909540&refer=video
         * living_count : 0
         */

        private String avatar_url;
        private String name;
        private String description;
        private long user_id;
        private boolean user_verified;
        private String verified_content;
        private boolean follow;
        private int follower_count;
        private String user_auth_info;
        private String schema;
        private int living_count;

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public long getUser_id() {
            return user_id;
        }

        public void setUser_id(long user_id) {
            this.user_id = user_id;
        }

        public boolean isUser_verified() {
            return user_verified;
        }

        public void setUser_verified(boolean user_verified) {
            this.user_verified = user_verified;
        }

        public String getVerified_content() {
            return verified_content;
        }

        public void setVerified_content(String verified_content) {
            this.verified_content = verified_content;
        }

        public boolean isFollow() {
            return follow;
        }

        public void setFollow(boolean follow) {
            this.follow = follow;
        }

        public int getFollower_count() {
            return follower_count;
        }

        public void setFollower_count(int follower_count) {
            this.follower_count = follower_count;
        }

        public String getUser_auth_info() {
            return user_auth_info;
        }

        public void setUser_auth_info(String user_auth_info) {
            this.user_auth_info = user_auth_info;
        }

        public String getSchema() {
            return schema;
        }

        public void setSchema(String schema) {
            this.schema = schema;
        }

        public int getLiving_count() {
            return living_count;
        }

        public void setLiving_count(int living_count) {
            this.living_count = living_count;
        }
    }

    public static class VideoDetailInfoBean {
        /**
         * group_flags : 32832
         * detail_video_large_image : {"url":"http://p6-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:1920:1080.jpg","width":580,"url_list":[{"url":"http://p6-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:1920:1080.jpg"},{"url":"http://p9-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:1920:1080.jpg"},{"url":"http://p1-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:1920:1080.jpg"}],"uri":"video1609/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a","height":327}
         * video_id : v03004b40000bur0jgn7ptql8dj90b70
         * direct_play : 1
         * show_pgc_subscribe : 1
         * video_watch_count : 487
         * video_type : 0
         * video_watching_count : 0
         * video_preloading_flag : 1
         */

        private int group_flags;
        private DetailVideoLargeImageBean detail_video_large_image;
        private String video_id;
        private int direct_play;
        private int show_pgc_subscribe;
        private int video_watch_count;
        private int video_type;
        private int video_watching_count;
        private int video_preloading_flag;

        public int getGroup_flags() {
            return group_flags;
        }

        public void setGroup_flags(int group_flags) {
            this.group_flags = group_flags;
        }

        public DetailVideoLargeImageBean getDetail_video_large_image() {
            return detail_video_large_image;
        }

        public void setDetail_video_large_image(DetailVideoLargeImageBean detail_video_large_image) {
            this.detail_video_large_image = detail_video_large_image;
        }

        public String getVideo_id() {
            return video_id;
        }

        public void setVideo_id(String video_id) {
            this.video_id = video_id;
        }

        public int getDirect_play() {
            return direct_play;
        }

        public void setDirect_play(int direct_play) {
            this.direct_play = direct_play;
        }

        public int getShow_pgc_subscribe() {
            return show_pgc_subscribe;
        }

        public void setShow_pgc_subscribe(int show_pgc_subscribe) {
            this.show_pgc_subscribe = show_pgc_subscribe;
        }

        public int getVideo_watch_count() {
            return video_watch_count;
        }

        public void setVideo_watch_count(int video_watch_count) {
            this.video_watch_count = video_watch_count;
        }

        public int getVideo_type() {
            return video_type;
        }

        public void setVideo_type(int video_type) {
            this.video_type = video_type;
        }

        public int getVideo_watching_count() {
            return video_watching_count;
        }

        public void setVideo_watching_count(int video_watching_count) {
            this.video_watching_count = video_watching_count;
        }

        public int getVideo_preloading_flag() {
            return video_preloading_flag;
        }

        public void setVideo_preloading_flag(int video_preloading_flag) {
            this.video_preloading_flag = video_preloading_flag;
        }

        public static class DetailVideoLargeImageBean {
            /**
             * url : http://p6-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:1920:1080.jpg
             * width : 580
             * url_list : [{"url":"http://p6-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:1920:1080.jpg"},{"url":"http://p9-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:1920:1080.jpg"},{"url":"http://p1-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:1920:1080.jpg"}]
             * uri : video1609/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a
             * height : 327
             */

            private String url;
            private int width;
            private String uri;
            private int height;
            private List<UrlListBeanXXX> url_list;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public String getUri() {
                return uri;
            }

            public void setUri(String uri) {
                this.uri = uri;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public List<UrlListBeanXXX> getUrl_list() {
                return url_list;
            }

            public void setUrl_list(List<UrlListBeanXXX> url_list) {
                this.url_list = url_list;
            }

            public static class UrlListBeanXXX {
                /**
                 * url : http://p6-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:1920:1080.jpg
                 */

                private String url;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }
        }
    }

    public static class ActionListBean {
        /**
         * action : 1
         * desc :
         * extra : {}
         */

        private int action;
        private String desc;
        private ExtraBean extra;

        public int getAction() {
            return action;
        }

        public void setAction(int action) {
            this.action = action;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public ExtraBean getExtra() {
            return extra;
        }

        public void setExtra(ExtraBean extra) {
            this.extra = extra;
        }

        public static class ExtraBean {
        }
    }

    public static class FilterWordsBean {
        /**
         * id : 8:0
         * name : 看过了
         * is_selected : false
         */

        private String id;
        private String name;
        private boolean is_selected;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isIs_selected() {
            return is_selected;
        }

        public void setIs_selected(boolean is_selected) {
            this.is_selected = is_selected;
        }
    }

    public static class LargeImageListBean {
        /**
         * url : http://p6-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:1920:1080.jpg
         * width : 580
         * url_list : [{"url":"http://p6-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:1920:1080.jpg"},{"url":"http://p3-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:1920:1080.jpg"},{"url":"http://p26-tt.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:1920:1080.jpg"}]
         * uri : video1609/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a
         * height : 327
         */

        private String url;
        private int width;
        private String uri;
        private int height;
        private List<UrlListBeanXXXX> url_list;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public List<UrlListBeanXXXX> getUrl_list() {
            return url_list;
        }

        public void setUrl_list(List<UrlListBeanXXXX> url_list) {
            this.url_list = url_list;
        }

        public static class UrlListBeanXXXX {
            /**
             * url : http://p6-tt-ipv6.byteimg.com/img/tos-cn-i-0004/2d4546f9555547c6869ca4302929e27a~tplv-tt-cs0:1920:1080.jpg
             */

            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
