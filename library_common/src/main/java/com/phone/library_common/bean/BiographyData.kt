package com.phone.library_common.bean

import android.os.Parcel
import android.os.Parcelable

data class BiographyData(
    val name: String,
    val content: String,
    val briefIntroduction: String,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(content)
        parcel.writeString(briefIntroduction)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BiographyData> {
        override fun createFromParcel(parcel: Parcel): BiographyData {
            return BiographyData(parcel)
        }

        override fun newArray(size: Int): Array<BiographyData?> {
            return arrayOfNulls(size)
        }
    }
}