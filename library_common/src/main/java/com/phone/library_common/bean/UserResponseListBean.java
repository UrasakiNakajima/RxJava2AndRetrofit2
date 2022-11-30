package com.phone.library_common.bean;

import java.util.ArrayList;
import java.util.List;

public class UserResponseListBean {

    private Integer code;
    private String message;
    private List<UserResponse> userBeanList = new ArrayList<>();

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<UserResponse> getUserBeanList() {
        return userBeanList;
    }

    public void setUserBeanList(List<UserResponse> userBeanList) {
        this.userBeanList = userBeanList;
    }

    @Override
    public String toString() {
        return "UserResponseListBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", userBeanList=" + userBeanList +
                '}';
    }
}
