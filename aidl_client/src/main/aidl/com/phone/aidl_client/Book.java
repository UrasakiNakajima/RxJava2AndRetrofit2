package com.phone.aidl_client;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    
    private String name;
    private String content;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    @Override
    public String toString() {
        return "Book{" +
                   "name='" + name + '\'' +
                   ", content='" + content + '\'' +
                   '}';
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.content);
    }
    
    public void readFromParcel(Parcel source) {
        this.name = source.readString();
        this.content = source.readString();
    }
    
    public Book() {
    }
    
    protected Book(Parcel in) {
        this.name = in.readString();
        this.content = in.readString();
    }
    
    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }
        
        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
