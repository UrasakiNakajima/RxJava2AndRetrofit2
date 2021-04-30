package com.mobile.main_module.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * author : Urasaki
 * e-mail : 1164688204@qq.com
 * date   : 2021/4/14 14:35
 * desc   : 活动日程
 * version: 1.0
 */
public class EventScheduleListBean {
	
	/**
	 * code : 0
	 * message :
	 * data : {"rows":[{"companyCode":"300911","addressOther":null,"activeTitle":"亿田智能新股发行上市仪式","activeEndTime":"2020-12-03 09:30:00","auditStatusAppName":"待审核","addressType":1,"activeStartTime":"2020-12-03 09:00:00","activeTime":"2020-12-03 09:00-09:30","companyName":"亿田智能","businessName":"覃业波","activeTypeName":"IPO","pid":"f4186a0dd7894527af0f5e9c6f872fab","applyUser":"覃业波","auditReason":null,"activeType":1,"auditTime":null,"addressTypeName":"深圳18楼","companyPlateName":"创业板","auditUser":null,"auditStatus":0,"companyPlate":5,"auditStatusName":"待审核","applyTime":"2020-12-02 15:19:18"},{"companyCode":null,"addressOther":null,"activeTitle":"南凌科技新股发行上市仪式","activeEndTime":"2020-12-22 09:30:00","auditStatusAppName":"待审核","addressType":1,"activeStartTime":"2020-12-22 09:00:00","activeTime":"2020-12-22 09:00-09:30","companyName":"南凌科技","businessName":"覃业波","activeTypeName":"IPO","pid":"1660f40e4f2345489cea33eb1b1fa4f9","applyUser":"覃业波","auditReason":null,"activeType":1,"auditTime":null,"addressTypeName":"深圳18楼","companyPlateName":null,"auditUser":null,"auditStatus":0,"companyPlate":null,"auditStatusName":"待审核","applyTime":"2020-12-22 10:31:42"},{"companyCode":"","addressOther":"","activeTitle":"南凌科技新股发行上市仪式","activeEndTime":"2020-12-24 10:00:00","auditStatusAppName":"待审核","addressType":1,"activeStartTime":"2020-12-24 09:00:00","activeTime":"2020-12-24 09:00-10:00","companyName":"南凌科技","businessName":"覃业波","activeTypeName":"IPO","pid":"9820e808930f4459bf8d519f0b032901","applyUser":"覃业波","auditReason":null,"activeType":1,"auditTime":null,"addressTypeName":"深圳18楼","companyPlateName":null,"auditUser":null,"auditStatus":0,"companyPlate":null,"auditStatusName":"待审核","applyTime":"2020-12-24 16:51:09"},{"companyCode":"000004","addressOther":"","activeTitle":"再测试申请","activeEndTime":"2020-12-30 13:05:00","auditStatusAppName":"待审核","addressType":1,"activeStartTime":"2020-12-30 13:05:00","activeTime":"2020-12-30 13:05-13:05","companyName":"国农科技","businessName":"丁美煌","activeTypeName":"IPO","pid":"981464990c634653ab666ee2c7a43a29","applyUser":"丁美煌","auditReason":null,"activeType":1,"auditTime":null,"addressTypeName":"深圳18楼","companyPlateName":"深市主板","auditUser":null,"auditStatus":0,"companyPlate":3,"auditStatusName":"待审核","applyTime":"2020-12-30 16:33:19"},{"companyCode":"000005","addressOther":"","activeTitle":"测试提交申请","activeEndTime":"2020-12-30 19:00:00","auditStatusAppName":"待审核","addressType":1,"activeStartTime":"2020-12-30 17:00:00","activeTime":"2020-12-30 17:00-19:00","companyName":"世纪星源","businessName":"丁美煌","activeTypeName":"IPO","pid":"3c5d2e9878554805b9f5d690cf827376","applyUser":"丁美煌","auditReason":null,"activeType":1,"auditTime":null,"addressTypeName":"深圳18楼","companyPlateName":"深市主板","auditUser":null,"auditStatus":0,"companyPlate":3,"auditStatusName":"待审核","applyTime":"2020-12-31 08:35:55"},{"companyCode":"834877","addressOther":"","activeTitle":"测试申请66666666666666666666666666666","activeEndTime":"2020-12-30 08:00:00","auditStatusAppName":"待审核","addressType":1,"activeStartTime":"2020-12-30 00:00:00","activeTime":"2020-12-30 00:00-08:00","companyName":"全景网络","businessName":"丁美煌","activeTypeName":"IPO","pid":"965c009832a746bcaef8550cfdbb827d","applyUser":"丁美煌","auditReason":null,"activeType":1,"auditTime":null,"addressTypeName":"深圳18楼","companyPlateName":"科创板","auditUser":null,"auditStatus":0,"companyPlate":1,"auditStatusName":"待审核","applyTime":"2020-12-31 10:29:34"},{"companyCode":"","addressOther":"","activeTitle":"再测试提交一次9999999999999999999999","activeEndTime":"2020-12-30 11:45:0║ 0","auditStatusAppName":"待审核","addressType":1,"activeStartTime":"2020-12-30 11:05:00","activeTime":"2020-12-30 11:05-11:45","companyName":"全景网直播厅","businessName":"丁美煌","activeTypeName":"IPO","pid":"7bf4a99611b14a14a313f9e813a2c01e","applyUser":"丁美煌","auditReason":"","activeType":1,"auditTime":"2020-12-31 15:48:24","addressTypeName":"深圳18楼","companyPlateName":null,"auditUser":"丁美煌","auditStatus":0,"companyPlate":null,"auditStatusName":"待审核","applyTime":"2020-12-31 15:49:02"},{"companyCode":"008990","addressOther":"","activeTitle":"测试活动","activeEndTime":"2020-11-21 15:00:00","auditStatusAppName":"已拒绝","addressType":1,"activeStartTime":"2020-11-21 14:00:00","activeTime":"2020-11-21 14:00-15:00","companyName":"强哥集团1","businessName":"覃业波1","activeTypeName":"IPO","pid":"8c4de479db294ad699f50ef9daa94783","applyUser":"覃业波","auditReason":"信息填写不完整","activeType":1,"auditTime":"2020-12-23 11:29:34","addressTypeName":"深圳18楼","companyPlateName":null,"auditUser":"覃业波","auditStatus":1,"companyPlate":null,"auditStatusName":"审核不通过","applyTime":"2020-11-26 17:18:55"},{"companyCode":"300916","addressOther":"","activeTitle":"朗特智能新股发行上市仪式","activeEndTime":"2020-12-03 10:30:00","auditStatusAppName":"已拒绝","addressType":1,"activeStartTime":"2020-12-03 10:00:00","activeTime":"2020-12-03 10:00-10:30","companyName":"朗特智能","businessName":"覃业波","activeTypeName":"IPO","pid":"f9ea736e977549a28e6cf21d74d5983d","applyUser":"覃业波","auditReason":"活动提交重复","activeType":1,"auditTime":"2020-12-23 15:21:35","addressTypeName":"深圳18楼","companyPlateName":"创业板","auditUser":"覃业波","auditStatus":1,"companyPlate":5,"auditStatusName":"审核不通过","applyTime":"2020-12-03 16:28:13"},{"companyCode":"000001","addressOther":"","activeTitle":"同惠电子股票向不特定合格投资者公开发行并在精选层挂牌网上路演","activeEndTime":"2020-12-23 13:45:00","auditStatusAppName":"已拒绝","addressType":1,"activeStartTime":"2020-12-23 08:00:00","activeTime":"2020-12-23 08:00-13:45","companyName":"平安银行","businessName":"胡维","activeTypeName":"IPO","pid":"9fdd9029777446218cfd38fd08d6b474","applyUser":"胡维","auditReason":"","activeType":1,"auditTime":"2020-12-23 16:05:18","addressTypeName":"深圳18楼","companyPlateName":"深市主板","auditUser":"胡维","auditStatus":1,"companyPlate":3,"auditStatusName":"审核不通过","applyTime":"2020-12-23 16:04:09"}],"total":25}
	 */
	
	@JSONField(name = "code")
	private int     code;
	@JSONField(name = "message")
	private String  message;
	@JSONField(name = "data")
	private DataDTO data;
	
	public int getCode() {
		return code;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public DataDTO getData() {
		return data;
	}
	
	public void setData(DataDTO data) {
		this.data = data;
	}
	
	public static class DataDTO {
		
		/**
		 * rows : [{"companyCode":"300911","addressOther":null,"activeTitle":"亿田智能新股发行上市仪式","activeEndTime":"2020-12-03 09:30:00","auditStatusAppName":"待审核","addressType":1,"activeStartTime":"2020-12-03 09:00:00","activeTime":"2020-12-03 09:00-09:30","companyName":"亿田智能","businessName":"覃业波","activeTypeName":"IPO","pid":"f4186a0dd7894527af0f5e9c6f872fab","applyUser":"覃业波","auditReason":null,"activeType":1,"auditTime":null,"addressTypeName":"深圳18楼","companyPlateName":"创业板","auditUser":null,"auditStatus":0,"companyPlate":5,"auditStatusName":"待审核","applyTime":"2020-12-02 15:19:18"},{"companyCode":null,"addressOther":null,"activeTitle":"南凌科技新股发行上市仪式","activeEndTime":"2020-12-22 09:30:00","auditStatusAppName":"待审核","addressType":1,"activeStartTime":"2020-12-22 09:00:00","activeTime":"2020-12-22 09:00-09:30","companyName":"南凌科技","businessName":"覃业波","activeTypeName":"IPO","pid":"1660f40e4f2345489cea33eb1b1fa4f9","applyUser":"覃业波","auditReason":null,"activeType":1,"auditTime":null,"addressTypeName":"深圳18楼","companyPlateName":null,"auditUser":null,"auditStatus":0,"companyPlate":null,"auditStatusName":"待审核","applyTime":"2020-12-22 10:31:42"},{"companyCode":"","addressOther":"","activeTitle":"南凌科技新股发行上市仪式","activeEndTime":"2020-12-24 10:00:00","auditStatusAppName":"待审核","addressType":1,"activeStartTime":"2020-12-24 09:00:00","activeTime":"2020-12-24 09:00-10:00","companyName":"南凌科技","businessName":"覃业波","activeTypeName":"IPO","pid":"9820e808930f4459bf8d519f0b032901","applyUser":"覃业波","auditReason":null,"activeType":1,"auditTime":null,"addressTypeName":"深圳18楼","companyPlateName":null,"auditUser":null,"auditStatus":0,"companyPlate":null,"auditStatusName":"待审核","applyTime":"2020-12-24 16:51:09"},{"companyCode":"000004","addressOther":"","activeTitle":"再测试申请","activeEndTime":"2020-12-30 13:05:00","auditStatusAppName":"待审核","addressType":1,"activeStartTime":"2020-12-30 13:05:00","activeTime":"2020-12-30 13:05-13:05","companyName":"国农科技","businessName":"丁美煌","activeTypeName":"IPO","pid":"981464990c634653ab666ee2c7a43a29","applyUser":"丁美煌","auditReason":null,"activeType":1,"auditTime":null,"addressTypeName":"深圳18楼","companyPlateName":"深市主板","auditUser":null,"auditStatus":0,"companyPlate":3,"auditStatusName":"待审核","applyTime":"2020-12-30 16:33:19"},{"companyCode":"000005","addressOther":"","activeTitle":"测试提交申请","activeEndTime":"2020-12-30 19:00:00","auditStatusAppName":"待审核","addressType":1,"activeStartTime":"2020-12-30 17:00:00","activeTime":"2020-12-30 17:00-19:00","companyName":"世纪星源","businessName":"丁美煌","activeTypeName":"IPO","pid":"3c5d2e9878554805b9f5d690cf827376","applyUser":"丁美煌","auditReason":null,"activeType":1,"auditTime":null,"addressTypeName":"深圳18楼","companyPlateName":"深市主板","auditUser":null,"auditStatus":0,"companyPlate":3,"auditStatusName":"待审核","applyTime":"2020-12-31 08:35:55"},{"companyCode":"834877","addressOther":"","activeTitle":"测试申请66666666666666666666666666666","activeEndTime":"2020-12-30 08:00:00","auditStatusAppName":"待审核","addressType":1,"activeStartTime":"2020-12-30 00:00:00","activeTime":"2020-12-30 00:00-08:00","companyName":"全景网络","businessName":"丁美煌","activeTypeName":"IPO","pid":"965c009832a746bcaef8550cfdbb827d","applyUser":"丁美煌","auditReason":null,"activeType":1,"auditTime":null,"addressTypeName":"深圳18楼","companyPlateName":"科创板","auditUser":null,"auditStatus":0,"companyPlate":1,"auditStatusName":"待审核","applyTime":"2020-12-31 10:29:34"},{"companyCode":"","addressOther":"","activeTitle":"再测试提交一次9999999999999999999999","activeEndTime":"2020-12-30 11:45:0║ 0","auditStatusAppName":"待审核","addressType":1,"activeStartTime":"2020-12-30 11:05:00","activeTime":"2020-12-30 11:05-11:45","companyName":"全景网直播厅","businessName":"丁美煌","activeTypeName":"IPO","pid":"7bf4a99611b14a14a313f9e813a2c01e","applyUser":"丁美煌","auditReason":"","activeType":1,"auditTime":"2020-12-31 15:48:24","addressTypeName":"深圳18楼","companyPlateName":null,"auditUser":"丁美煌","auditStatus":0,"companyPlate":null,"auditStatusName":"待审核","applyTime":"2020-12-31 15:49:02"},{"companyCode":"008990","addressOther":"","activeTitle":"测试活动","activeEndTime":"2020-11-21 15:00:00","auditStatusAppName":"已拒绝","addressType":1,"activeStartTime":"2020-11-21 14:00:00","activeTime":"2020-11-21 14:00-15:00","companyName":"强哥集团1","businessName":"覃业波1","activeTypeName":"IPO","pid":"8c4de479db294ad699f50ef9daa94783","applyUser":"覃业波","auditReason":"信息填写不完整","activeType":1,"auditTime":"2020-12-23 11:29:34","addressTypeName":"深圳18楼","companyPlateName":null,"auditUser":"覃业波","auditStatus":1,"companyPlate":null,"auditStatusName":"审核不通过","applyTime":"2020-11-26 17:18:55"},{"companyCode":"300916","addressOther":"","activeTitle":"朗特智能新股发行上市仪式","activeEndTime":"2020-12-03 10:30:00","auditStatusAppName":"已拒绝","addressType":1,"activeStartTime":"2020-12-03 10:00:00","activeTime":"2020-12-03 10:00-10:30","companyName":"朗特智能","businessName":"覃业波","activeTypeName":"IPO","pid":"f9ea736e977549a28e6cf21d74d5983d","applyUser":"覃业波","auditReason":"活动提交重复","activeType":1,"auditTime":"2020-12-23 15:21:35","addressTypeName":"深圳18楼","companyPlateName":"创业板","auditUser":"覃业波","auditStatus":1,"companyPlate":5,"auditStatusName":"审核不通过","applyTime":"2020-12-03 16:28:13"},{"companyCode":"000001","addressOther":"","activeTitle":"同惠电子股票向不特定合格投资者公开发行并在精选层挂牌网上路演","activeEndTime":"2020-12-23 13:45:00","auditStatusAppName":"已拒绝","addressType":1,"activeStartTime":"2020-12-23 08:00:00","activeTime":"2020-12-23 08:00-13:45","companyName":"平安银行","businessName":"胡维","activeTypeName":"IPO","pid":"9fdd9029777446218cfd38fd08d6b474","applyUser":"胡维","auditReason":"","activeType":1,"auditTime":"2020-12-23 16:05:18","addressTypeName":"深圳18楼","companyPlateName":"深市主板","auditUser":"胡维","auditStatus":1,"companyPlate":3,"auditStatusName":"审核不通过","applyTime":"2020-12-23 16:04:09"}]
		 * total : 25
		 */
		
		@JSONField(name = "rows")
		private List<RowsDTO> rows;
		@JSONField(name = "total")
		private int           total;
		
		public List<RowsDTO> getRows() {
			return rows;
		}
		
		public void setRows(List<RowsDTO> rows) {
			this.rows = rows;
		}
		
		public int getTotal() {
			return total;
		}
		
		public void setTotal(int total) {
			this.total = total;
		}
		
		public static class RowsDTO {
			
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
			private String companyCode;
			@JSONField(name = "addressOther")
			private Object addressOther;
			@JSONField(name = "activeTitle")
			private String activeTitle;
			@JSONField(name = "activeEndTime")
			private String activeEndTime;
			@JSONField(name = "auditStatusAppName")
			private String auditStatusAppName;
			@JSONField(name = "addressType")
			private int    addressType;
			@JSONField(name = "activeStartTime")
			private String activeStartTime;
			@JSONField(name = "activeTime")
			private String activeTime;
			@JSONField(name = "companyName")
			private String companyName;
			@JSONField(name = "businessName")
			private String businessName;
			@JSONField(name = "activeTypeName")
			private String activeTypeName;
			@JSONField(name = "pid")
			private String pid;
			@JSONField(name = "applyUser")
			private String applyUser;
			@JSONField(name = "auditReason")
			private Object auditReason;
			@JSONField(name = "activeType")
			private int    activeType;
			@JSONField(name = "auditTime")
			private Object auditTime;
			@JSONField(name = "addressTypeName")
			private String addressTypeName;
			@JSONField(name = "companyPlateName")
			private String companyPlateName;
			@JSONField(name = "auditUser")
			private Object auditUser;
			@JSONField(name = "auditStatus")
			private int    auditStatus;
			@JSONField(name = "companyPlate")
			private int    companyPlate;
			@JSONField(name = "auditStatusName")
			private String auditStatusName;
			@JSONField(name = "applyTime")
			private String applyTime;
			
			public String getCompanyCode() {
				return companyCode;
			}
			
			public void setCompanyCode(String companyCode) {
				this.companyCode = companyCode;
			}
			
			public Object getAddressOther() {
				return addressOther;
			}
			
			public void setAddressOther(Object addressOther) {
				this.addressOther = addressOther;
			}
			
			public String getActiveTitle() {
				return activeTitle;
			}
			
			public void setActiveTitle(String activeTitle) {
				this.activeTitle = activeTitle;
			}
			
			public String getActiveEndTime() {
				return activeEndTime;
			}
			
			public void setActiveEndTime(String activeEndTime) {
				this.activeEndTime = activeEndTime;
			}
			
			public String getAuditStatusAppName() {
				return auditStatusAppName;
			}
			
			public void setAuditStatusAppName(String auditStatusAppName) {
				this.auditStatusAppName = auditStatusAppName;
			}
			
			public int getAddressType() {
				return addressType;
			}
			
			public void setAddressType(int addressType) {
				this.addressType = addressType;
			}
			
			public String getActiveStartTime() {
				return activeStartTime;
			}
			
			public void setActiveStartTime(String activeStartTime) {
				this.activeStartTime = activeStartTime;
			}
			
			public String getActiveTime() {
				return activeTime;
			}
			
			public void setActiveTime(String activeTime) {
				this.activeTime = activeTime;
			}
			
			public String getCompanyName() {
				return companyName;
			}
			
			public void setCompanyName(String companyName) {
				this.companyName = companyName;
			}
			
			public String getBusinessName() {
				return businessName;
			}
			
			public void setBusinessName(String businessName) {
				this.businessName = businessName;
			}
			
			public String getActiveTypeName() {
				return activeTypeName;
			}
			
			public void setActiveTypeName(String activeTypeName) {
				this.activeTypeName = activeTypeName;
			}
			
			public String getPid() {
				return pid;
			}
			
			public void setPid(String pid) {
				this.pid = pid;
			}
			
			public String getApplyUser() {
				return applyUser;
			}
			
			public void setApplyUser(String applyUser) {
				this.applyUser = applyUser;
			}
			
			public Object getAuditReason() {
				return auditReason;
			}
			
			public void setAuditReason(Object auditReason) {
				this.auditReason = auditReason;
			}
			
			public int getActiveType() {
				return activeType;
			}
			
			public void setActiveType(int activeType) {
				this.activeType = activeType;
			}
			
			public Object getAuditTime() {
				return auditTime;
			}
			
			public void setAuditTime(Object auditTime) {
				this.auditTime = auditTime;
			}
			
			public String getAddressTypeName() {
				return addressTypeName;
			}
			
			public void setAddressTypeName(String addressTypeName) {
				this.addressTypeName = addressTypeName;
			}
			
			public String getCompanyPlateName() {
				return companyPlateName;
			}
			
			public void setCompanyPlateName(String companyPlateName) {
				this.companyPlateName = companyPlateName;
			}
			
			public Object getAuditUser() {
				return auditUser;
			}
			
			public void setAuditUser(Object auditUser) {
				this.auditUser = auditUser;
			}
			
			public int getAuditStatus() {
				return auditStatus;
			}
			
			public void setAuditStatus(int auditStatus) {
				this.auditStatus = auditStatus;
			}
			
			public int getCompanyPlate() {
				return companyPlate;
			}
			
			public void setCompanyPlate(int companyPlate) {
				this.companyPlate = companyPlate;
			}
			
			public String getAuditStatusName() {
				return auditStatusName;
			}
			
			public void setAuditStatusName(String auditStatusName) {
				this.auditStatusName = auditStatusName;
			}
			
			public String getApplyTime() {
				return applyTime;
			}
			
			public void setApplyTime(String applyTime) {
				this.applyTime = applyTime;
			}
		}
	}
}
