package com.phone.library_greendao.bean

class AddressBean(county: String, city: String) : Cloneable {

    var county: String? = county
    var city: String? = city

    @Throws(CloneNotSupportedException::class)
    public override fun clone(): AddressBean {
        return super.clone() as AddressBean
    }

    override fun toString(): String {
        return "AddressBean{" +
                "county='" + county + '\'' +
                ", city='" + city + '\'' +
                '}'
    }
}