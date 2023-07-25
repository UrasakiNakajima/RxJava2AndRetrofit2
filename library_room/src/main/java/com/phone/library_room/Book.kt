package com.phone.library_room

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*

//@Entity(indices = [Index(value = ["bookName"], unique = true)])
@Entity(tableName = "Book")
class Book {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "bId")
    var bId: Long = 0

    @ColumnInfo(name = "bookName")
    var bookName: String = ""

    @ColumnInfo(name = "anchor")
    var anchor: String = ""

    @ColumnInfo(name = "price")
    var price: Int = 0

//    @ColumnInfo(name = "briefIntroduction")
//    var briefIntroduction: String = ""

}