package com.phone.common_library.bean;

public class AddressBean implements Cloneable {

    private String county;
    private String city;

    public AddressBean() {
    }

    public AddressBean(String county, String city) {
        this.county = county;
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    protected AddressBean clone() throws CloneNotSupportedException {
        return (AddressBean) super.clone();
    }
}
