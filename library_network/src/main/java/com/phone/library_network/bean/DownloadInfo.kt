package com.phone.library_network.bean

import java.io.File

data class DownloadInfo(
    var file: File?,
    var fileName: String?,
    var fileSize: Long?,
    var currentSize: Long?,
    var progress: Int?,
    var speed: Long?,
    var error: Throwable?
)