package com.phone.module_main.main;

public class CommodityBean {

    private int id;
    private String name;
    private String describe;
    private double price;
    private int salesVolume;

    public CommodityBean(int id, String name, String describe, double price, int salesVolume) {
        this.id = id;
        this.name = name;
        this.describe = describe;
        this.price = price;
        this.salesVolume = salesVolume;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(int salesVolume) {
        this.salesVolume = salesVolume;
    }

    @Override
    public String toString() {
        return "CommodityBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", describe='" + describe + '\'' +
                ", price=" + price +
                ", salesVolume=" + salesVolume +
                '}';
    }
}
