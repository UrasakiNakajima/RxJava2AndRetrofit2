package com.phone.library_common.bean

import com.alibaba.fastjson.annotation.JSONField

class EventScheduleListBean {

    /**
     * code : 0
     * message :
     * data : {"rows":[{"companyCode":"300911","addressOther":null,"activeTitle":"亿田智能新股发行上市仪式","activeEndTime":"2020-12-03 09:30:00","auditStatusAppName":"待审核","addressType":1,"activeStartTime":"2020-12-03 09:00:00","activeTime":"2020-12-03 09:00-09:30","companyName":"亿田智能","businessName":"覃业波","activeTypeName":"IPO","pid":"f4186a0dd7894527af0f5e9c6f872fab","applyUser":"覃业波","auditReason":null,"activeType":1,"auditTime":null,"addressTypeName":"深圳18楼","companyPlateName":"创业板","auditUser":null,"auditStatus":0,"companyPlate":5,"auditStatusName":"待审核","applyTime":"2020-12-02 15:19:18"},{"companyCode":null,"addressOther":null,"activeTitle":"南凌科技新股发行上市仪式","activeEndTime":"2020-12-22 09:30:00","auditStatusAppName":"待审核","addressType":1,"activeStartTime":"2020-12-22 09:00:00","activeTime":"2020-12-22 09:00-09:30","companyName":"南凌科技","businessName":"覃业波","activeTypeName":"IPO","pid":"1660f40e4f2345489cea33eb1b1fa4f9","applyUser":"覃业波","auditReason":null,"activeType":1,"auditTime":null,"addressTypeName":"深圳18楼","companyPlateName":null,"auditUser":null,"auditStatus":0,"companyPlate":null,"auditStatusName":"待审核","applyTime":"2020-12-22 10:31:42"},{"companyCode":"","addressOther":"","activeTitle":"南凌科技新股发行上市仪式","activeEndTime":"2020-12-24 10:00:00","auditStatusAppName":"待审核","addressType":1,"activeStartTime":"2020-12-24 09:00:00","activeTime":"2020-12-24 09:00-10:00","companyName":"南凌科技","businessName":"覃业波","activeTypeName":"IPO","pid":"9820e808930f4459bf8d519f0b032901","applyUser":"覃业波","auditReason":null,"activeType":1,"auditTime":null,"addressTypeName":"深圳18楼","companyPlateName":null,"auditUser":null,"auditStatus":0,"companyPlate":null,"auditStatusName":"待审核","applyTime":"2020-12-24 16:51:09"},{"companyCode":"000004","addressOther":"","activeTitle":"再测试申请","activeEndTime":"2020-12-30 13:05:00","auditStatusAppName":"待审核","addressType":1,"activeStartTime":"2020-12-30 13:05:00","activeTime":"2020-12-30 13:05-13:05","companyName":"国农科技","businessName":"丁美煌","activeTypeName":"IPO","pid":"981464990c634653ab666ee2c7a43a29","applyUser":"丁美煌","auditReason":null,"activeType":1,"auditTime":null,"addressTypeName":"深圳18楼","companyPlateName":"深市主板","auditUser":null,"auditStatus":0,"companyPlate":3,"auditStatusName":"待审核","applyTime":"2020-12-30 16:33:19"},{"companyCode":"000005","addressOther":"","activeTitle":"测试提交申请","activeEndTime":"2020-12-30 19:00:00","auditStatusAppName":"待审核","addressType":1,"activeStartTime":"2020-12-30 17:00:00","activeTime":"2020-12-30 17:00-19:00","companyName":"世纪星源","businessName":"丁美煌","activeTypeName":"IPO","pid":"3c5d2e9878554805b9f5d690cf827376","applyUser":"丁美煌","auditReason":null,"activeType":1,"auditTime":null,"addressTypeName":"深圳18楼","companyPlateName":"深市主板","auditUser":null,"auditStatus":0,"companyPlate":3,"auditStatusName":"待审核","applyTime":"2020-12-31 08:35:55"},{"companyCode":"834877","addressOther":"","activeTitle":"测试申请66666666666666666666666666666","activeEndTime":"2020-12-30 08:00:00","auditStatusAppName":"待审核","addressType":1,"activeStartTime":"2020-12-30 00:00:00","activeTime":"2020-12-30 00:00-08:00","companyName":"全景网络","businessName":"丁美煌","activeTypeName":"IPO","pid":"965c009832a746bcaef8550cfdbb827d","applyUser":"丁美煌","auditReason":null,"activeType":1,"auditTime":null,"addressTypeName":"深圳18楼","companyPlateName":"科创板","auditUser":null,"auditStatus":0,"companyPlate":1,"auditStatusName":"待审核","applyTime":"2020-12-31 10:29:34"},{"companyCode":"","addressOther":"","activeTitle":"再测试提交一次9999999999999999999999","activeEndTime":"2020-12-30 11:45:0║ 0","auditStatusAppName":"待审核","addressType":1,"activeStartTime":"2020-12-30 11:05:00","activeTime":"2020-12-30 11:05-11:45","companyName":"全景网直播厅","businessName":"丁美煌","activeTypeName":"IPO","pid":"7bf4a99611b14a14a313f9e813a2c01e","applyUser":"丁美煌","auditReason":"","activeType":1,"auditTime":"2020-12-31 15:48:24","addressTypeName":"深圳18楼","companyPlateName":null,"auditUser":"丁美煌","auditStatus":0,"companyPlate":null,"auditStatusName":"待审核","applyTime":"2020-12-31 15:49:02"},{"companyCode":"008990","addressOther":"","activeTitle":"测试活动","activeEndTime":"2020-11-21 15:00:00","auditStatusAppName":"已拒绝","addressType":1,"activeStartTime":"2020-11-21 14:00:00","activeTime":"2020-11-21 14:00-15:00","companyName":"强哥集团1","businessName":"覃业波1","activeTypeName":"IPO","pid":"8c4de479db294ad699f50ef9daa94783","applyUser":"覃业波","auditReason":"信息填写不完整","activeType":1,"auditTime":"2020-12-23 11:29:34","addressTypeName":"深圳18楼","companyPlateName":null,"auditUser":"覃业波","auditStatus":1,"companyPlate":null,"auditStatusName":"审核不通过","applyTime":"2020-11-26 17:18:55"},{"companyCode":"300916","addressOther":"","activeTitle":"朗特智能新股发行上市仪式","activeEndTime":"2020-12-03 10:30:00","auditStatusAppName":"已拒绝","addressType":1,"activeStartTime":"2020-12-03 10:00:00","activeTime":"2020-12-03 10:00-10:30","companyName":"朗特智能","businessName":"覃业波","activeTypeName":"IPO","pid":"f9ea736e977549a28e6cf21d74d5983d","applyUser":"覃业波","auditReason":"活动提交重复","activeType":1,"auditTime":"2020-12-23 15:21:35","addressTypeName":"深圳18楼","companyPlateName":"创业板","auditUser":"覃业波","auditStatus":1,"companyPlate":5,"auditStatusName":"审核不通过","applyTime":"2020-12-03 16:28:13"},{"companyCode":"000001","addressOther":"","activeTitle":"同惠电子股票向不特定合格投资者公开发行并在精选层挂牌网上路演","activeEndTime":"2020-12-23 13:45:00","auditStatusAppName":"已拒绝","addressType":1,"activeStartTime":"2020-12-23 08:00:00","activeTime":"2020-12-23 08:00-13:45","companyName":"平安银行","businessName":"胡维","activeTypeName":"IPO","pid":"9fdd9029777446218cfd38fd08d6b474","applyUser":"胡维","auditReason":"","activeType":1,"auditTime":"2020-12-23 16:05:18","addressTypeName":"深圳18楼","companyPlateName":"深市主板","auditUser":"胡维","auditStatus":1,"companyPlate":3,"auditStatusName":"审核不通过","applyTime":"2020-12-23 16:04:09"}],"total":25}
     */
    @JSONField(name = "code")
    private var code = 0

    @JSONField(name = "message")
    private var message: String? = null

    @JSONField(name = "data")
    private var data: DataDTO? = null

    class DataDTO {
        /**
         * rows : [{"companyCode":"300911","addressOther":null,"activeTitle":"亿田智能新股发行上市仪式","activeEndTime":"2020-12-03 09:30:00","auditStatusAppName":"待审核","addressType":1,"activeStartTime":"2020-12-03 09:00:00","activeTime":"2020-12-03 09:00-09:30","companyName":"亿田智能","businessName":"覃业波","activeTypeName":"IPO","pid":"f4186a0dd7894527af0f5e9c6f872fab","applyUser":"覃业波","auditReason":null,"activeType":1,"auditTime":null,"addressTypeName":"深圳18楼","companyPlateName":"创业板","auditUser":null,"auditStatus":0,"companyPlate":5,"auditStatusName":"待审核","applyTime":"2020-12-02 15:19:18"},{"companyCode":null,"addressOther":null,"activeTitle":"南凌科技新股发行上市仪式","activeEndTime":"2020-12-22 09:30:00","auditStatusAppName":"待审核","addressType":1,"activeStartTime":"2020-12-22 09:00:00","activeTime":"2020-12-22 09:00-09:30","companyName":"南凌科技","businessName":"覃业波","activeTypeName":"IPO","pid":"1660f40e4f2345489cea33eb1b1fa4f9","applyUser":"覃业波","auditReason":null,"activeType":1,"auditTime":null,"addressTypeName":"深圳18楼","companyPlateName":null,"auditUser":null,"auditStatus":0,"companyPlate":null,"auditStatusName":"待审核","applyTime":"2020-12-22 10:31:42"},{"companyCode":"","addressOther":"","activeTitle":"南凌科技新股发行上市仪式","activeEndTime":"2020-12-24 10:00:00","auditStatusAppName":"待审核","addressType":1,"activeStartTime":"2020-12-24 09:00:00","activeTime":"2020-12-24 09:00-10:00","companyName":"南凌科技","businessName":"覃业波","activeTypeName":"IPO","pid":"9820e808930f4459bf8d519f0b032901","applyUser":"覃业波","auditReason":null,"activeType":1,"auditTime":null,"addressTypeName":"深圳18楼","companyPlateName":null,"auditUser":null,"auditStatus":0,"companyPlate":null,"auditStatusName":"待审核","applyTime":"2020-12-24 16:51:09"},{"companyCode":"000004","addressOther":"","activeTitle":"再测试申请","activeEndTime":"2020-12-30 13:05:00","auditStatusAppName":"待审核","addressType":1,"activeStartTime":"2020-12-30 13:05:00","activeTime":"2020-12-30 13:05-13:05","companyName":"国农科技","businessName":"丁美煌","activeTypeName":"IPO","pid":"981464990c634653ab666ee2c7a43a29","applyUser":"丁美煌","auditReason":null,"activeType":1,"auditTime":null,"addressTypeName":"深圳18楼","companyPlateName":"深市主板","auditUser":null,"auditStatus":0,"companyPlate":3,"auditStatusName":"待审核","applyTime":"2020-12-30 16:33:19"},{"companyCode":"000005","addressOther":"","activeTitle":"测试提交申请","activeEndTime":"2020-12-30 19:00:00","auditStatusAppName":"待审核","addressType":1,"activeStartTime":"2020-12-30 17:00:00","activeTime":"2020-12-30 17:00-19:00","companyName":"世纪星源","businessName":"丁美煌","activeTypeName":"IPO","pid":"3c5d2e9878554805b9f5d690cf827376","applyUser":"丁美煌","auditReason":null,"activeType":1,"auditTime":null,"addressTypeName":"深圳18楼","companyPlateName":"深市主板","auditUser":null,"auditStatus":0,"companyPlate":3,"auditStatusName":"待审核","applyTime":"2020-12-31 08:35:55"},{"companyCode":"834877","addressOther":"","activeTitle":"测试申请66666666666666666666666666666","activeEndTime":"2020-12-30 08:00:00","auditStatusAppName":"待审核","addressType":1,"activeStartTime":"2020-12-30 00:00:00","activeTime":"2020-12-30 00:00-08:00","companyName":"全景网络","businessName":"丁美煌","activeTypeName":"IPO","pid":"965c009832a746bcaef8550cfdbb827d","applyUser":"丁美煌","auditReason":null,"activeType":1,"auditTime":null,"addressTypeName":"深圳18楼","companyPlateName":"科创板","auditUser":null,"auditStatus":0,"companyPlate":1,"auditStatusName":"待审核","applyTime":"2020-12-31 10:29:34"},{"companyCode":"","addressOther":"","activeTitle":"再测试提交一次9999999999999999999999","activeEndTime":"2020-12-30 11:45:0║ 0","auditStatusAppName":"待审核","addressType":1,"activeStartTime":"2020-12-30 11:05:00","activeTime":"2020-12-30 11:05-11:45","companyName":"全景网直播厅","businessName":"丁美煌","activeTypeName":"IPO","pid":"7bf4a99611b14a14a313f9e813a2c01e","applyUser":"丁美煌","auditReason":"","activeType":1,"auditTime":"2020-12-31 15:48:24","addressTypeName":"深圳18楼","companyPlateName":null,"auditUser":"丁美煌","auditStatus":0,"companyPlate":null,"auditStatusName":"待审核","applyTime":"2020-12-31 15:49:02"},{"companyCode":"008990","addressOther":"","activeTitle":"测试活动","activeEndTime":"2020-11-21 15:00:00","auditStatusAppName":"已拒绝","addressType":1,"activeStartTime":"2020-11-21 14:00:00","activeTime":"2020-11-21 14:00-15:00","companyName":"强哥集团1","businessName":"覃业波1","activeTypeName":"IPO","pid":"8c4de479db294ad699f50ef9daa94783","applyUser":"覃业波","auditReason":"信息填写不完整","activeType":1,"auditTime":"2020-12-23 11:29:34","addressTypeName":"深圳18楼","companyPlateName":null,"auditUser":"覃业波","auditStatus":1,"companyPlate":null,"auditStatusName":"审核不通过","applyTime":"2020-11-26 17:18:55"},{"companyCode":"300916","addressOther":"","activeTitle":"朗特智能新股发行上市仪式","activeEndTime":"2020-12-03 10:30:00","auditStatusAppName":"已拒绝","addressType":1,"activeStartTime":"2020-12-03 10:00:00","activeTime":"2020-12-03 10:00-10:30","companyName":"朗特智能","businessName":"覃业波","activeTypeName":"IPO","pid":"f9ea736e977549a28e6cf21d74d5983d","applyUser":"覃业波","auditReason":"活动提交重复","activeType":1,"auditTime":"2020-12-23 15:21:35","addressTypeName":"深圳18楼","companyPlateName":"创业板","auditUser":"覃业波","auditStatus":1,"companyPlate":5,"auditStatusName":"审核不通过","applyTime":"2020-12-03 16:28:13"},{"companyCode":"000001","addressOther":"","activeTitle":"同惠电子股票向不特定合格投资者公开发行并在精选层挂牌网上路演","activeEndTime":"2020-12-23 13:45:00","auditStatusAppName":"已拒绝","addressType":1,"activeStartTime":"2020-12-23 08:00:00","activeTime":"2020-12-23 08:00-13:45","companyName":"平安银行","businessName":"胡维","activeTypeName":"IPO","pid":"9fdd9029777446218cfd38fd08d6b474","applyUser":"胡维","auditReason":"","activeType":1,"auditTime":"2020-12-23 16:05:18","addressTypeName":"深圳18楼","companyPlateName":"深市主板","auditUser":"胡维","auditStatus":1,"companyPlate":3,"auditStatusName":"审核不通过","applyTime":"2020-12-23 16:04:09"}]
         * total : 25
         */
        @JSONField(name = "rows")
        var rows: List<RowsDTO>? = null

        @JSONField(name = "total")
        var total = 0

        class RowsDTO {
            /**
             * companyCode : 300911
             * addressOther : null
             * activeTitle : 亿田智能新股发行上市仪式
             * activeEndTime : 2020-12-03 09:30:00
             * auditStatusAppName : 待审核
             * addressType : 1
             * activeStartTime : 2020-12-03 09:00:00
             * activeTime : 2020-12-03 09:00-09:30
             * companyName : 亿田智能
             * businessName : 覃业波
             * activeTypeName : IPO
             * pid : f4186a0dd7894527af0f5e9c6f872fab
             * applyUser : 覃业波
             * auditReason : null
             * activeType : 1
             * auditTime : null
             * addressTypeName : 深圳18楼
             * companyPlateName : 创业板
             * auditUser : null
             * auditStatus : 0
             * companyPlate : 5
             * auditStatusName : 待审核
             * applyTime : 2020-12-02 15:19:18
             */
            @JSONField(name = "companyCode")
            var companyCode: String? = null

            @JSONField(name = "addressOther")
            var addressOther: Any? = null

            @JSONField(name = "activeTitle")
            var activeTitle: String? = null

            @JSONField(name = "activeEndTime")
            var activeEndTime: String? = null

            @JSONField(name = "auditStatusAppName")
            var auditStatusAppName: String? = null

            @JSONField(name = "addressType")
            var addressType = 0

            @JSONField(name = "activeStartTime")
            var activeStartTime: String? = null

            @JSONField(name = "activeTime")
            var activeTime: String? = null

            @JSONField(name = "companyName")
            var companyName: String? = null

            @JSONField(name = "businessName")
            var businessName: String? = null

            @JSONField(name = "activeTypeName")
            var activeTypeName: String? = null

            @JSONField(name = "pid")
            var pid: String? = null

            @JSONField(name = "applyUser")
            var applyUser: String? = null

            @JSONField(name = "auditReason")
            var auditReason: Any? = null

            @JSONField(name = "activeType")
            var activeType = 0

            @JSONField(name = "auditTime")
            var auditTime: Any? = null

            @JSONField(name = "addressTypeName")
            var addressTypeName: String? = null

            @JSONField(name = "companyPlateName")
            var companyPlateName: String? = null

            @JSONField(name = "auditUser")
            var auditUser: Any? = null

            @JSONField(name = "auditStatus")
            var auditStatus = 0

            @JSONField(name = "companyPlate")
            var companyPlate = 0

            @JSONField(name = "auditStatusName")
            var auditStatusName: String? = null

            @JSONField(name = "applyTime")
            var applyTime: String? = null
        }
    }
}