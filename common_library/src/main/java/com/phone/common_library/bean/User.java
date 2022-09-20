package com.phone.common_library.bean;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.ArrayList;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;

@Entity
public class User {

    @Id(autoincrement = true)
    private Long id;
    private String userId;
    private String userName;
    private String date;
    private int age;
    @Convert(columnType = String.class, converter = AddressListConverter.class)
    private List<Address> addressList = new ArrayList<>();

    @Generated(hash = 2130539253)
    public User(Long id, String userId, String userName, String date, int age,
                List<Address> addressList) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.date = date;
        this.age = age;
        this.addressList = addressList;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Address> getAddressList() {
        return this.addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

}
