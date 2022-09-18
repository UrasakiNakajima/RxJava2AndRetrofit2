package com.phone.base64_and_file.bean;

import android.graphics.Bitmap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Base64AndFileBean {

    private String dirsPath;
    private String dirsPathCompressed;
    private String dirsPathCompressedRecover;
    private File file;
    private File fileCompressed;
    private File fileCompressedRecover;
    private Bitmap bitmap;
    private Bitmap bitmapCompressed;
    private Bitmap bitmapCompressedRecover;
    public String base64Str;
    private String txtFilePath;
    private List<String> base64StrList = new ArrayList<>();

    public String getDirsPath() {
        return dirsPath;
    }

    public void setDirsPath(String dirsPath) {
        this.dirsPath = dirsPath;
    }

    public String getDirsPathCompressed() {
        return dirsPathCompressed;
    }

    public void setDirsPathCompressed(String dirsPathCompressed) {
        this.dirsPathCompressed = dirsPathCompressed;
    }

    public String getDirsPathCompressedRecover() {
        return dirsPathCompressedRecover;
    }

    public void setDirsPathCompressedRecover(String dirsPathCompressedRecover) {
        this.dirsPathCompressedRecover = dirsPathCompressedRecover;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getFileCompressed() {
        return fileCompressed;
    }

    public void setFileCompressed(File fileCompressed) {
        this.fileCompressed = fileCompressed;
    }

    public File getFileCompressedRecover() {
        return fileCompressedRecover;
    }

    public void setFileCompressedRecover(File fileCompressedRecover) {
        this.fileCompressedRecover = fileCompressedRecover;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmapCompressed() {
        return bitmapCompressed;
    }

    public void setBitmapCompressed(Bitmap bitmapCompressed) {
        this.bitmapCompressed = bitmapCompressed;
    }

    public Bitmap getBitmapCompressedRecover() {
        return bitmapCompressedRecover;
    }

    public void setBitmapCompressedRecover(Bitmap bitmapCompressedRecover) {
        this.bitmapCompressedRecover = bitmapCompressedRecover;
    }

    public String getBase64Str() {
        return base64Str;
    }

    public void setBase64Str(String base64Str) {
        this.base64Str = base64Str;
    }

    public String getTxtFilePath() {
        return txtFilePath;
    }

    public void setTxtFilePath(String txtFilePath) {
        this.txtFilePath = txtFilePath;
    }

    public List<String> getBase64StrList() {
        return base64StrList;
    }

    public void setBase64StrList(List<String> base64StrList) {
        this.base64StrList.clear();
        this.base64StrList.addAll(base64StrList);
    }
}
