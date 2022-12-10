package com.phone.library_common.bean

class AddressBean(county: String?, city: String?) : Cloneable {

    var county: String? = null
    var city: String? = null

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