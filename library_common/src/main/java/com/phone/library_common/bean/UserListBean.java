package com.phone.library_common.bean;

import java.util.ArrayList;
import java.util.List;

public class UserListBean {

    private Integer code;
    private String message;
    private List<UserBean> userBeanList = new ArrayList<>();

    public UserListBean() {
    }

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

    public List<UserBean> getUserBeanList() {
        return userBeanList;
    }

    public void setUserBeanList(List<UserBean> userBeanList) {
        this.userBeanList = userBeanList;
    }
}
