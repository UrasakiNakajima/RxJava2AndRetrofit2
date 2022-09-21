package com.phone.common_library.bean;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import org.greenrobot.greendao.annotation.Convert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class UserCloneBean {

    private Long id;
    private String userId;
    private String userName;
    private String date;
    private int age;
    private Double salary;
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal salaryBigDecimal;
    @Convert(columnType = String.class, converter = AddressBeanListConverter.class)
    private List<AddressBean> addressBeanList = new ArrayList<>();

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

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public BigDecimal getSalaryBigDecimal() {
        return salaryBigDecimal;
    }

//    public void setSalaryBigDecimal(Double salary) {
//        this.salaryBigDecimal = new BigDecimal(salary);
//    }

    public void setSalaryBigDecimal(BigDecimal salaryBigDecimal) {
        this.salaryBigDecimal = salaryBigDecimal;
    }

    public List<AddressBean> getAddressBeanList() {
        return addressBeanList;
    }

    public void setAddressBeanList(List<AddressBean> addressBeanList) {
        this.addressBeanList = addressBeanList;
    }
}
