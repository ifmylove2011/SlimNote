package com.xter.slimnotek.extension

import android.text.TextUtils
import androidx.room.util.FileUtil
import java.io.*


/**
 * 删除文件或目录
 *
 * @param path 文件或目录。
 * @return true 表示删除成功，否则为失败
 */
@Synchronized
fun FileUtil.delete(path: File?): Boolean {
    if (null == path) {
        return true
    }
    if (path.isDirectory()) {
        val files: Array<File> = path.listFiles()
        for (file in files) {
            if (!delete(file)) {
                return false
            }
        }
    }
    return !path.exists() || path.delete()
}

@Throws(IOException::class)
fun FileUtil.copyfile(url1: String?, url2: String?) {
    val fis = FileInputStream(url1)
    val bufis = BufferedInputStream(fis)
    val fos = FileOutputStream(url2)
    val bufos = BufferedOutputStream(fos)
    var len = 0
    while (bufis.read().also({ len = it }) != -1) {
        bufos.write(len)
    }
    bufis.close()
    bufos.close()
}

/**
 * 备份文件
 *
 * @param path 原路径
 */
@Synchronized
fun FileUtil.backupFile(path: String) {
    try {
        copyfile(path, "$path.bak")
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * 备份文件
 *
 * @param path 原路径
 */
@Synchronized
fun FileUtil.revertFile(path: String) {
    try {
        copyfile("$path.bak", path)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@Synchronized
@Throws(IOException::class)
fun FileUtil.createFile(path: String) {
    if (TextUtils.isEmpty(path)) {
        return
    }
    val file = File(path)
    if (!file.exists()) if (path.endsWith("/")) {
        file.mkdirs()
    } else {
        if (file.getParentFile().exists()) {
            file.createNewFile()
        } else {
            file.getParentFile().mkdirs()
            file.createNewFile()
        }
    }
}

@Synchronized
fun FileUtil.writeToFile(bytes: ByteArray?, file: File?, append: Boolean) {
    var fo: FileOutputStream? = null
    try {
        fo = FileOutputStream(file, append)
        fo.write(bytes)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        fo?.close();
    }
}