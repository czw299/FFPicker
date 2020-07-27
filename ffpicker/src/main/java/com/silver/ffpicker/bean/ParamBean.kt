package com.silver.ffpicker.bean

import android.os.Parcel
import android.os.Parcelable

class ParamBean() : Parcelable {
    var chooseMode: Int = 0
    var maxNum: Int = 1
    var stringType: Array<String?> = arrayOf()

    constructor(parcel: Parcel) : this() {
        chooseMode = parcel.readInt()
        maxNum = parcel.readInt()

        val size = parcel.readInt()
        if(size > 0){
            stringType = arrayOfNulls(size)
            parcel.readStringArray(stringType)
        }
    }

    override fun writeToParcel(parcel: Parcel?, p1: Int) {
        parcel?.writeInt(chooseMode)
        parcel?.writeInt(maxNum)

        parcel?.writeInt(stringType.size)
        parcel?.writeStringArray(stringType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParamBean> {
        override fun createFromParcel(parcel: Parcel): ParamBean {
            return ParamBean(parcel)
        }

        override fun newArray(size: Int): Array<ParamBean?> {
            return arrayOfNulls(size)
        }
    }

}