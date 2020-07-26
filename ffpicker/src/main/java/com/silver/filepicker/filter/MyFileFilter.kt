package com.silver.filepicker.filter

import java.io.File
import java.io.FileFilter

class MyFileFilter(var mTypes: Array<String?>): FileFilter{

    override fun accept(file: File?): Boolean {
        if(file!!.isDirectory){
            return true
        }
        if (mTypes.isNotEmpty()) {
            for (i in mTypes.indices) {
                if (file.name.endsWith(mTypes[i]!!.toLowerCase()) || file.name.endsWith(mTypes[i]!!.toUpperCase())) {
                    return true
                }
            }
        } else {
            return true
        }
        return false
    }
}