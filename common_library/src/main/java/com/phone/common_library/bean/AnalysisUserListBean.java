package com.phone.common_library.bean;

import java.util.ArrayList;
import java.util.List;

public class AnalysisUserListBean {

    private Integer code;
    private String message;
    private List<AnalysisUserBean> userBeanList = new ArrayList<>();

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

    public List<AnalysisUserBean> getUserBeanList() {
        return userBeanList;
    }

    public void setUserBeanList(List<AnalysisUserBean> userBeanList) {
        this.userBeanList = userBeanList;
    }

    @Override
    public String toString() {
        return "AnalysisUserListBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", userBeanList=" + userBeanList +
                '}';
    }
}
