// BookManager.aidl
package com.mobile.aidl_client;

// Declare any non-default types here with import statements
import com.mobile.aidl_client.Book;

interface BookManager {

   List<Book> getBooks();

   void addBook(in Book book);
}
