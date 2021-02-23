package com.mobile.rxjava2andretrofit2.manager

import android.content.Context
import android.media.MediaMetadataRetriever
import android.graphics.Bitmap
import android.net.Uri
import java.io.File


class VideoImageManager {

    companion object {
        fun getVideoImage(context: Context, url: String, isSD: Boolean): Bitmap? {
            var bitmap: Bitmap? = null
            //MediaMetadataRetriever 是android中定义好的一个类，提供了统一
            //的接口，用于从输入的媒体文件中取得帧和元数据；
            val retriever = MediaMetadataRetriever()
            try {
                if (isSD) {
                    //（）根据文件路径获取缩略图
                    retriever.setDataSource(context, Uri.fromFile(File(url)))
                } else {
                    //根据网络路径获取缩略图
                    retriever.setDataSource(url, HashMap())
                }
                //获得第一帧图片
                bitmap = retriever.frameAtTime
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } finally {
                retriever.release()
            }
            return bitmap
        }
    }

}