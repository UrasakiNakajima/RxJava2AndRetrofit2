package com.phone.common_library.bean;

import java.util.ArrayList;
import java.util.List;

public class UserCloneListBean {

    private Integer code;
    private String message;
    private List<UserCloneBean> userCloneBeanList = new ArrayList<>();

    public UserCloneListBean() {
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

    public List<UserCloneBean> getUserCloneBeanList() {
        return userCloneBeanList;
    }

    public void setUserCloneBeanList(List<UserCloneBean> userCloneBeanList) {
        this.userCloneBeanList = userCloneBeanList;
    }
}
