package com.phone.library_network.bean

import java.io.File

sealed class DownloadState<T> {

    class ProgressState<T>(val progress: T, val total: Long, val speed: Long) : DownloadState<T>()

    class CompletedState<T>(val completed: File) : DownloadState<T>()

    class ErrorState<T>(val errorMsg: String) : DownloadState<T>()
}