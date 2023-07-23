package com.phone.library_room

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*

//@Entity(indices = [Index(value = ["bookName"], unique = true)])
@Entity(tableName = "Book")
class Book {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "book_name")
    var bookName: String? = null

    var anchor: String? = null

    @Ignore
    var price: Int = 0

    @ColumnInfo(name = "brief_introduction")
    var briefIntroduction: String? = null

    var isSuccess: Boolean = true

    var message = ""

    override fun toString(): String {
        return "Book(id=$id, bookName=$bookName, anchor=$anchor, price=$price, briefIntroduction=$briefIntroduction, isSuccess=$isSuccess, message='$message')"
    }

}