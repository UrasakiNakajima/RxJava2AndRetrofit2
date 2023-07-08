package com.phone.library_room

import androidx.room.*

@Dao
abstract class BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(book: Book)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(books: List<Book>)

    @Delete
    abstract fun delete(book: Book)

    @Update
    abstract fun update(book: Book)

    @Query("select * from Book where id =:id")
    abstract fun queryById(id: Int): Book

    @Query("select * from Book")
    abstract fun queryAll(): List<Book>

    @Query("select count(*) from Book")
    abstract fun bookCount(): Int

    @Query("delete from Book")
    abstract fun deleteAll();

}