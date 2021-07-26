// BookManager.aidl
package com.phone.aidl_client;

// Declare any non-default types here with import statements
import com.phone.aidl_client.Book;

interface BookManager {

   List<Book> getBookList();

   void addBook(in Book book);
}
