package com.phone.library_greendao.bean;

import com.phone.library_greendao.AddressBeanListConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Entity
public class UserBean implements Cloneable {

    @Id(autoincrement = true)
    private Long id;
    private String userId;
    private String password;
    private String birthday;
    private Integer age;
    private Double salary;
    @Convert(columnType = String.class, converter = AddressBeanListConverter.class)
    private List<AddressBean> addressBeanList = new ArrayList<>();

    @Generated(hash = 703739529)
    public UserBean(Long id, String userId, String password, String birthday,
                    Integer age, Double salary, List<AddressBean> addressBeanList) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.birthday = birthday;
        this.age = age;
        this.salary = salary;
        this.addressBeanList = addressBeanList;
    }

    @Generated(hash = 1203313951)
    public UserBean() {
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

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Integer getAge() {
        return this.age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getSalary() {
        return this.salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public List<AddressBean> getAddressBeanList() {
        return this.addressBeanList;
    }

    public void setAddressBeanList(List<AddressBean> addressBeanList) {
        this.addressBeanList = addressBeanList;
    }

    @Override
    protected UserBean clone() throws CloneNotSupportedException {
        List<AddressBean> addressBeanList = new ArrayList<>();
        UserBean userBean = (UserBean) super.clone();
        for (int i = 0; i < userBean.getAddressBeanList().size(); i++) {
            addressBeanList.add(userBean.getAddressBeanList().get(i).clone());
        }
        userBean.setAddressBeanList(addressBeanList);
        return userBean;
    }

}
