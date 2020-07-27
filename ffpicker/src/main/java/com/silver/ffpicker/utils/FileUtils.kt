package com.silver.ffpicker.utils

import java.io.File
import java.io.FileFilter
import java.util.*

class FileUtils {
    companion object{
        fun getFileList(path: String?, filter: FileFilter?): ArrayList<File>? {
            val directory = File(path)
            val files = directory.listFiles(filter)
            val result: ArrayList<File> = ArrayList()
            if (files == null) {
                return ArrayList()
            }
            for (i in files.indices) {
                result.add(files[i])
            }
            Collections.sort(result, FileComparator())
            return result
        }


    }
}