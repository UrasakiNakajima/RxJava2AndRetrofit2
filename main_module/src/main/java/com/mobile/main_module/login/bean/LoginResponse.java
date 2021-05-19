package com.mobile.main_module.login.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class LoginResponse {
	
	/**
	 * code : 0
	 * message :
	 * data : {"validTime":604800,"userId":"418","token":"ce8e19c9f3994a9093e9694d96dd2835"}
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
		 * validTime : 604800
		 * userId : 418
		 * token : ce8e19c9f3994a9093e9694d96dd2835
		 */
		
		@JSONField(name = "validTime")
		private int    validTime;
		@JSONField(name = "userId")
		private String userId;
		@JSONField(name = "token")
		private String token;
		
		public int getValidTime() {
			return validTime;
		}
		
		public void setValidTime(int validTime) {
			this.validTime = validTime;
		}
		
		public String getUserId() {
			return userId;
		}
		
		public void setUserId(String userId) {
			this.userId = userId;
		}
		
		public String getToken() {
			return token;
		}
		
		public void setToken(String token) {
			this.token = token;
		}
	}
}
