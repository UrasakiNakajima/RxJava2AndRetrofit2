package com.phone.library_common.bean

import android.os.Parcel
import android.os.Parcelable

class FirstPageResponse(`in`: Parcel) {

    var reason: String? = null
    var result: ResultData? = null
    var error_code = 0

    init {
        reason = `in`.readString()
        result = `in`.readParcelable(ResultData::class.java.classLoader)
        error_code = `in`.readInt()
    }

    fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(reason)
        dest.writeParcelable(result, flags)
        dest.writeInt(error_code)
    }

    fun describeContents(): Int {
        return 0
    }

    val CREATOR: Parcelable.Creator<FirstPageResponse> =
        object : Parcelable.Creator<FirstPageResponse> {
            override fun createFromParcel(`in`: Parcel): FirstPageResponse {
                return FirstPageResponse(`in`)
            }

            override fun newArray(size: Int): Array<FirstPageResponse?> {
                return arrayOfNulls(size)
            }
        }

    class ResultData protected constructor(`in`: Parcel) : Parcelable {
        var stat: String? = null
        var data: List<JuheNewsBean>? = null

        class JuheNewsBean protected constructor(`in`: Parcel) : Parcelable {
            var uniquekey: String? = null
            var title: String? = null
            var date: String? = null
            var category: String? = null
            var author_name: String? = null
            var url: String? = null
            var thumbnail_pic_s: String? = null
            var thumbnail_pic_s02: String? = null
            var thumbnail_pic_s03: String? = null

            override fun describeContents(): Int {
                return 0
            }

            override fun writeToParcel(dest: Parcel, flags: Int) {
                dest.writeString(uniquekey)
                dest.writeString(title)
                dest.writeString(date)
                dest.writeString(this.category)
                dest.writeString(author_name)
                dest.writeString(url)
                dest.writeString(thumbnail_pic_s)
                dest.writeString(thumbnail_pic_s02)
                dest.writeString(thumbnail_pic_s03)
            }

            fun readFromParcel(source: Parcel) {
                uniquekey = source.readString()
                title = source.readString()
                date = source.readString()
                this.category = source.readString()
                author_name = source.readString()
                url = source.readString()
                thumbnail_pic_s = source.readString()
                thumbnail_pic_s02 = source.readString()
                thumbnail_pic_s03 = source.readString()
            }

            init {
                uniquekey = `in`.readString()
                title = `in`.readString()
                date = `in`.readString()
                this.category = `in`.readString()
                author_name = `in`.readString()
                url = `in`.readString()
                thumbnail_pic_s = `in`.readString()
                thumbnail_pic_s02 = `in`.readString()
                thumbnail_pic_s03 = `in`.readString()
            }

            override fun toString(): String {
                return "JuheNewsBean{" +
                        "uniquekey='" + uniquekey + '\'' +
                        ", title='" + title + '\'' +
                        ", date='" + date + '\'' +
                        ", category='" + category + '\'' +
                        ", author_name='" + author_name + '\'' +
                        ", url='" + url + '\'' +
                        ", thumbnail_pic_s='" + thumbnail_pic_s + '\'' +
                        ", thumbnail_pic_s02='" + thumbnail_pic_s02 + '\'' +
                        ", thumbnail_pic_s03='" + thumbnail_pic_s03 + '\'' +
                        '}'
            }

            companion object {
                @JvmField
                val CREATOR: Parcelable.Creator<JuheNewsBean> =
                    object : Parcelable.Creator<JuheNewsBean> {
                        override fun createFromParcel(source: Parcel): JuheNewsBean {
                            return JuheNewsBean(source)
                        }

                        override fun newArray(size: Int): Array<JuheNewsBean?> {
                            return arrayOfNulls(size)
                        }
                    }
            }
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeString(stat)
            dest.writeList(data)
        }

        fun readFromParcel(source: Parcel) {
            stat = source.readString()
            data = ArrayList()
            source.readList(data!!, JuheNewsBean::class.java.classLoader)
        }

        init {
            stat = `in`.readString()
            data = ArrayList()
            `in`.readList(data!!, JuheNewsBean::class.java.classLoader)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<ResultData> = object : Parcelable.Creator<ResultData> {
                override fun createFromParcel(source: Parcel): ResultData {
                    return ResultData(source)
                }

                override fun newArray(size: Int): Array<ResultData?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

}