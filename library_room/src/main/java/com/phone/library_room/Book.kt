package com.phone.library_room

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

//    @ColumnInfo(name = "brief_introduction")
//    var briefIntroduction: String? = null

}