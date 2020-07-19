package com.silver.filepicker

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import com.silver.filepicker.bean.ParamBean
import com.silver.filepicker.ui.FilePickerActivity

class FilePicker{
    companion object{
        val CHOOSE_FILE = 0
        val CHOOSE_FOLDER = 1
    }

    private var mActivity: Activity? = null
    private var mFragment: Fragment? = null
    private lateinit var mTitle: String
    private var mTheme: Int = R.style.FilePickerTheme
    private var mRequestCode: Int = 526
    private var mMaxNum = 1
    private var mStringType :Array<String?> = arrayOfNulls(0)
    private lateinit var mStartPath: String
    private var chooseMode: Int = 0

    fun withActivity(activity: Activity): FilePicker {
        this.mActivity = activity
        return this
    }

    fun withFragment(fragment: Fragment): FilePicker {
        this.mFragment = fragment
        return this
    }

    fun withTheme(theme: Int): FilePicker {
        this.mTheme = theme
        return this
    }

    fun withRequestCode(requsetCode: Int): FilePicker {
        this.mRequestCode = requsetCode
        return this
    }

    fun withMaxNum(num: Int): FilePicker {
        this.mMaxNum = num
        return this
    }

    fun withStartPath(startPath: String): FilePicker{
        this.mStartPath = startPath
        return this
    }

    fun withFileType(type: Array<String?>): FilePicker{
        this.mStringType = type
        return this
    }

    fun withChooseFolderMode(i: Int): FilePicker{
        this.chooseMode = i
        return this
    }

    fun start(){
        val paramBean: ParamBean = ParamBean()
        paramBean.maxNum = mMaxNum
        paramBean.chooseMode = chooseMode
        paramBean.stringType = mStringType
        if(mActivity == null && mFragment == null){
            throw RuntimeException("You must pass Activity or Fragment by withActivity or withFragment method")
        }
        val intent = initIntent()
        intent?.putExtra("ddd", paramBean)
        mActivity?.startActivityForResult(intent,mRequestCode) ?: mFragment?.startActivityForResult(intent, mRequestCode)
    }

    private fun initIntent(): Intent? {
        lateinit var intent: Intent
        if (mActivity != null) {
            intent = Intent(mActivity, FilePickerActivity::class.java)
        } else if (mFragment != null) {
            intent = Intent(mFragment?.activity, FilePickerActivity::class.java)
        }
        return intent
    }
}