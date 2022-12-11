package com.phone.library_common.manager

import android.annotation.TargetApi
import android.net.Uri
import android.os.Build
import android.preference.PreferenceManager
import android.provider.DocumentsContract
import androidx.documentfile.provider.DocumentFile
import com.phone.library_common.BaseApplication
import com.phone.library_common.manager.LogManager.i
import java.io.*

object DocumentManager {

    private val TAG = DocumentManager::class.java.simpleName
    val OPEN_DOCUMENT_TREE_CODE = 8000
    private val sExtSdCardPaths: MutableList<String> = ArrayList()

    @JvmStatic
    fun cleanCache() {
        sExtSdCardPaths.clear()
    }

    /**
     * Get a list of external SD card paths. (Kitkat or higher.)
     *
     * @return A list of external SD card paths.
     */
    @JvmStatic
    private fun getExtSdCardPaths(): Array<String> {
        if (sExtSdCardPaths.size > 0) {
            return sExtSdCardPaths.toTypedArray()
        }
        for (file in BaseApplication.get().getExternalFilesDirs("external")) {
            if (file != null && file != BaseApplication.get().getExternalFilesDir("external")) {
                val index = file.absolutePath.lastIndexOf("/Android/data")
                if (index < 0) {
                    i(TAG, "Unexpected external file dir: " + file.absolutePath)
                } else {
                    var path = file.absolutePath.substring(0, index)
                    try {
                        path = File(path).canonicalPath
                    } catch (e: IOException) {
                        // Keep non-canonical path.
                    }
                    sExtSdCardPaths.add(path)
                }
            }
        }
        if (sExtSdCardPaths.isEmpty()) sExtSdCardPaths.add("/storage/sdcard1")
        return sExtSdCardPaths.toTypedArray()
    }

    /**
     * Determine the main folder of the external SD card containing the given file.
     *
     * @param file the file.
     * @return The main folder of the external SD card containing this file, if the file is on an SD
     * card. Otherwise,
     * null is returned.
     */
    @JvmStatic
    private fun getExtSdCardFolder(file: File): String? {
        val extSdPaths = getExtSdCardPaths()
        try {
            for (i in extSdPaths.indices) {
                if (file.canonicalPath.startsWith(extSdPaths[i])) {
                    return extSdPaths[i]
                }
            }
        } catch (e: IOException) {
            return null
        }
        return null
    }

    /**
     * Determine if a file is on external sd card. (Kitkat or higher.)
     *
     * @param file The file.
     * @return true if on external sd card.
     */
    @JvmStatic
    fun isOnExtSdCard(file: File): Boolean {
        return getExtSdCardFolder(file) != null
    }

    /**
     * Get a DocumentFile corresponding to the given file (for writing on ExtSdCard on Android 5).
     * If the file is not
     * existing, it is created.
     *
     * @param file        The file.
     * @param isDirectory flag indicating if the file should be a directory.
     * @return The DocumentFile
     */
    @JvmStatic
    fun getDocumentFile(
        file: File, isDirectory: Boolean
    ): DocumentFile? {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            return DocumentFile.fromFile(file)
        }
        val baseFolder = getExtSdCardFolder(file)
        var originalDirectory = false
        if (baseFolder == null) {
            return null
        }
        var relativePath: String? = null
        try {
            val fullPath = file.canonicalPath
            if (baseFolder != fullPath) {
                relativePath = fullPath.substring(baseFolder.length + 1)
            } else {
                originalDirectory = true
            }
        } catch (e: IOException) {
            return null
        } catch (f: Exception) {
            originalDirectory = true
            //continue
        }
        val `as` = PreferenceManager.getDefaultSharedPreferences(BaseApplication.get()).getString(
            baseFolder,
            null
        )
        var treeUri: Uri? = null
        if (`as` != null) treeUri = Uri.parse(`as`)
        if (treeUri == null) {
            return null
        }

        // start with root of SD card and then parse through document tree.
        var document = DocumentFile.fromTreeUri(BaseApplication.get(), treeUri)
        if (originalDirectory) return document
        relativePath?.let {
            val parts = relativePath.split("/").toTypedArray()
            for (i in parts.indices) {
                var nextDocument = document?.findFile(parts[i])
                if (nextDocument == null) {
                    nextDocument = if (i < parts.size - 1 || isDirectory) {
                        document?.createDirectory(parts[i])
                    } else {
                        document?.createFile("image", parts[i])
                    }
                }
                document = nextDocument
            }
        }
        return document
    }

    @JvmStatic
    fun mkdirs(dir: File): Boolean {
        var res = dir.mkdirs()
        if (!res) {
            if (isOnExtSdCard(dir)) {
                val documentFile =
                    getDocumentFile(dir, true)
                res = documentFile != null && documentFile.canWrite()
            }
        }
        return res
    }

    @JvmStatic
    fun delete(file: File): Boolean {
        var ret = file.delete()
        if (!ret && isOnExtSdCard(file)) {
            val f = getDocumentFile(file, false)
            if (f != null) {
                ret = f.delete()
            }
        }
        return ret
    }

    @JvmStatic
    fun canWrite(file: File): Boolean {
        var res = file.exists() && file.canWrite()
        if (!res && !file.exists()) {
            try {
                res = if (!file.isDirectory) {
                    file.createNewFile() && file.delete()
                } else {
                    file.mkdirs() && file.delete()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return res
    }

    @JvmStatic
    fun canWrite2(file: File): Boolean {
        var res = canWrite(file)
        if (!res && isOnExtSdCard(file)) {
            val documentFile = getDocumentFile(file, true)
            res = documentFile != null && documentFile.canWrite()
        }
        return res
    }

    @JvmStatic
    fun renameTo(src: File, dest: File): Boolean {
        var res = src.renameTo(dest)
        if (!res && isOnExtSdCard(dest)) {
            val srcDoc: DocumentFile?
            srcDoc = if (isOnExtSdCard(src)) {
                getDocumentFile(src, false)
            } else {
                DocumentFile.fromFile(src)
            }
            val destDoc = getDocumentFile(dest.parentFile, true)
            srcDoc?.let {
                try {
                    if (src.parent == dest.parent) {
                        res = it.renameTo(dest.name)
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        res = DocumentsContract.moveDocument(
                            BaseApplication.get().contentResolver,
                            srcDoc.uri,
                            srcDoc.parentFile!!.uri,
                            destDoc!!.uri
                        ) != null
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return res
    }

    @JvmStatic
    fun getInputStream(destFile: File): InputStream? {
        var `in`: InputStream? = null
        try {
            if (!canWrite2(destFile) && isOnExtSdCard(destFile)) {
                val file = getDocumentFile(destFile, false)
                if (file != null && file.canWrite()) {
                    `in` = BaseApplication.get().contentResolver.openInputStream(file.uri)
                }
            } else {
                `in` = FileInputStream(destFile)
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return `in`
    }

    @JvmStatic
    fun getOutputStream(destFile: File): OutputStream? {
        var out: OutputStream? = null
        try {
            if (!canWrite2(destFile) && isOnExtSdCard(destFile)) {
                val file = getDocumentFile(destFile, false)
                if (file != null && file.canWrite()) {
                    out = BaseApplication.get().contentResolver.openOutputStream(file.uri)
                }
            } else {
                out = FileOutputStream(destFile)
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return out
    }

    @JvmStatic
    fun saveTreeUri(rootPath: String, uri: Uri): Boolean {
        val file = DocumentFile.fromTreeUri(BaseApplication.get(), uri)
        if (file != null && file.canWrite()) {
            val perf = PreferenceManager.getDefaultSharedPreferences(BaseApplication.get())
            perf.edit().putString(rootPath, uri.toString()).apply()
            return true
        } else {
            i(TAG, "no write permission: $rootPath")
        }
        return false
    }

    @JvmStatic
    fun checkWritableRootPath(rootPath: String): Boolean {
        val root = File(rootPath)
        return if (!root.canWrite()) {
            if (isOnExtSdCard(root)) {
                val documentFile = getDocumentFile(root, true)
                documentFile == null || !documentFile.canWrite()
            } else {
                val perf = PreferenceManager.getDefaultSharedPreferences(BaseApplication.get())
                val documentUri = perf.getString(rootPath, "")
                if (documentUri == null || documentUri.isEmpty()) {
                    true
                } else {
                    val file =
                        DocumentFile.fromTreeUri(BaseApplication.get(), Uri.parse(documentUri))
                    !(file != null && file.canWrite())
                }
            }
        } else false
    }

}