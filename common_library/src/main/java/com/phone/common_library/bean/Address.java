package com.phone.common_library.bean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Address {

    private String county;
    private String city;

    public Address() {
    }

    public Address(String county, String city) {
        this.county = county;
        this.city = city;
    }
}
