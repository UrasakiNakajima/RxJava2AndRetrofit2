package com.mobile.main_module.login.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * author : Urasaki
 * e-mail : 1164688204@qq.com
 * date   : 2021/5/1913:41
 * desc   :
 * version: 1.0
 */
public class GetVerificationCode {
	
	/**
	 * code : 0
	 * message :
	 * data : {"content":"验证码已发送至您的手机135****3214上，请注意查收！"}
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
		 * content : 验证码已发送至您的手机135****3214上，请注意查收！
		 */
		
		@JSONField(name = "content")
		private String content;
		
		public String getContent() {
			return content;
		}
		
		public void setContent(String content) {
			this.content = content;
		}
	}
}
