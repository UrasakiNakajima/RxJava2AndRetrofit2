package com.phone.common_library.bean;

public class User3 extends User {

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
