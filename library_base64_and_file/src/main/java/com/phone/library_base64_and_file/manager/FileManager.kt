package com.phone.library_base64_and_file.manager

import android.util.Log
import java.io.*

object FileManager {

    private val TAG = FileManager::class.java.simpleName

    // 将字符串写入到文本文件中
    fun writeStrToTextFile(
        strContent: String,
        filePath: String,
        fileName: String
    ): String {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName)
        val strFilePath = filePath + fileName
        // 每次写入时，都换行写
        val strContent2 = """
             $strContent
             
             """.trimIndent()
        var filePath2: String? = null
        val file: File
        try {
            file = File(strFilePath)
            if (!file.exists()) {
                Log.i(TAG, "Create the file:$strFilePath")
                file.parentFile?.mkdirs()
                file.createNewFile()
            }
            val raf = RandomAccessFile(file, "rwd")
            raf.seek(file.length())
            raf.write(strContent2.toByteArray())
            raf.close()
            filePath2 = file.absolutePath
        } catch (e: Exception) {
            Log.i(TAG, "Error on write File:$e")
        }
        return filePath2!!
    }

    //生成文件
    fun makeFilePath(filePath: String, fileName: String): File? {
        var file: File? = null
        makeRootDirectory(filePath)
        try {
            file = File(filePath + fileName)
            if (!file.exists()) {
                file.createNewFile()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file
    }

    //生成文件夹
    fun makeRootDirectory(filePath: String) {
        var file: File? = null
        try {
            file = File(filePath)
            if (!file.exists()) {
                file.mkdir()
            }
        } catch (e: Exception) {
            Log.i(TAG, e.toString() + "")
        }
    }

    //读取指定目录下的所有TXT文件的文件内容
    fun getFileContent(file: File): String {
        var content = ""
        if (!file.isDirectory) { //检查此路径名的文件是否是一个目录(文件夹)
            if (file.name.endsWith("txt")) { //文件格式为""文件
                try {
                    val instream: InputStream = FileInputStream(file)
                    val inputreader = InputStreamReader(instream, "UTF-8")
                    val buffreader = BufferedReader(inputreader)
                    var line = ""
                    //分行读取
                    while (buffreader.readLine().also { line = it } != null) {
                        content += """
                        $line
                        
                        """.trimIndent()
                    }
                    instream.close() //关闭输入流
                } catch (e: FileNotFoundException) {
                    Log.i(TAG, "The File doesn't not exist.")
                } catch (e: IOException) {
                    Log.i(TAG, e.message!!)
                }
            }
        }
        return content
    }

}