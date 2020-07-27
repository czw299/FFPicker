package com.silver.ffpicker.utils

import java.io.File
import java.util.Comparator as javaComparator

class FileComparator: javaComparator<File>{
    override fun compare(f1: File?, f2: File?): Int {
        if (f1 != null && f2 != null) {
            if (f1 == f2){
                return 0
            }
            if(f1.isDirectory && f2.isFile){
                return -1
            }
            if(f1.isFile && f2.isDirectory){
                return 1
            }
            return f1.name.compareTo(f2.name,true)
        }
        return 0

    }
}