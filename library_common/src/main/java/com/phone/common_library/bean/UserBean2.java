package com.phone.common_library.bean;

public class UserBean2 extends UserBean {

    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "User2{" +
                "userId='" + userId + '\'' +
                '}';
    }
}
