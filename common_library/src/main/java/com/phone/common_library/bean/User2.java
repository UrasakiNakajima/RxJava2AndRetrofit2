package com.phone.common_library.bean;

public class User2 extends User {

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
