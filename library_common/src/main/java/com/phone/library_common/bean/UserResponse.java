package com.phone.library_common.bean;

import org.greenrobot.greendao.annotation.Convert;

import java.util.ArrayList;
import java.util.List;

public class UserResponse implements Cloneable {

    private Long id;
    private String userId;
    private String userName;
    private String date;
    private int age;
    private String salary;
    @Convert(columnType = String.class, converter = AddressBeanListConverter.class)
    private List<AddressBean> addressBeanList = new ArrayList<>();

    public UserResponse() {
    }

    public UserResponse(Long id, String userId, String userName, String date, int age, String salary, List<AddressBean> addressBeanList) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.date = date;
        this.age = age;
        this.salary = salary;
        this.addressBeanList = addressBeanList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public List<AddressBean> getAddressBeanList() {
        return addressBeanList;
    }

    public void setAddressBeanList(List<AddressBean> addressBeanList) {
        this.addressBeanList = addressBeanList;
    }

    @Override
    protected UserResponse clone() throws CloneNotSupportedException {
        List<AddressBean> addressBeanList = new ArrayList<>();
        UserResponse userResponse = (UserResponse) super.clone();
        for (int i = 0; i < userResponse.getAddressBeanList().size(); i++) {
            addressBeanList.add(userResponse.getAddressBeanList().get(i).clone());
        }
        userResponse.setAddressBeanList(addressBeanList);
        return userResponse;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", date='" + date + '\'' +
                ", age=" + age +
                ", salary='" + salary + '\'' +
                ", addressBeanList=" + addressBeanList +
                '}';
    }
}
