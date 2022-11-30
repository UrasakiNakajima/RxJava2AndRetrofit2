package com.phone.library_common.bean;

public class UserBean3 extends UserBean {

    private int userNumber;

    public int getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(int userNumber) {
        this.userNumber = userNumber;
    }

    @Override
    public String toString() {
        return "User3{" +
                "userNumber=" + userNumber +
                '}';
    }
}
