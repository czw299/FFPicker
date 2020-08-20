package com.silver.ffpicker.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.silver.ffpicker.FFPicker.Companion.CHOOSE_FOLDER
import com.silver.ffpicker.R
import com.silver.ffpicker.adapter.FileRecycleViewAdapter
import com.silver.ffpicker.bean.ParamBean
import com.silver.ffpicker.filter.MyFileFilter
import com.silver.ffpicker.utils.FileUtils
import com.silver.ffpicker.utils.NotificationBarSetter
import kotlinx.android.synthetic.main.filepicker_activity.*
import java.io.File
import java.lang.reflect.Field

class FFPickerActivity: AppCompatActivity() {
    private val context = this
    private lateinit var adapter: FileRecycleViewAdapter
    private val rootPath: String = Environment.getExternalStorageDirectory().absolutePath
    private var fileFolder: File = File(rootPath)//当前目录
    private var mDataList: ArrayList<File>? = null//当前列表
    private lateinit var paramBean: ParamBean
    private var lastOffset: ArrayList<Int> = ArrayList()
    private var lastPosition: ArrayList<Int> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.filepicker_activity)
        initView()
        initData()
    }

    private fun getDarkModeStatus(context: Context): Boolean {
        val mode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return mode == Configuration.UI_MODE_NIGHT_YES
    }

    private fun initView(){
        if(!getDarkModeStatus(context)){
            NotificationBarSetter.setNotificationBar(this,true,true,true)
        }else{
            NotificationBarSetter.setNotificationBar(this,true,false,false)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            window.navigationBarColor = getColor(R.color.activityBg)
        }
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.setNavigationOnClickListener{_ -> finish()}

        try {
            val field: Field = Toolbar::class.java.getDeclaredField("mTitleTextView")
            field.isAccessible = true
            (field.get(toolbar) as TextView).ellipsize = TextUtils.TruncateAt.START
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    private fun initData(){
        paramBean = intent.getParcelableExtra("ddd")
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rcv_filelist.layoutManager = linearLayoutManager

        adapter = FileRecycleViewAdapter(context, paramBean.chooseMode, paramBean.maxNum, paramBean.stringType)
        rcv_filelist.adapter = adapter
        mDataList = FileUtils.getFileList(fileFolder.absolutePath, MyFileFilter(paramBean.stringType))
        mDataList?.let { adapter.add(it) }
        adapter.onItemClickListener = object: FileRecycleViewAdapter.OnItemClickListener{
            override fun onClick(view: View, file: File) {
                if (file.isDirectory){      //点击文件夹
                    checkInFolder(file)
                }else{                      //点击文件
                    if(paramBean.maxNum == 1){        //单选模式 直接返回
                        val stringPath = ArrayList<String>()
                        stringPath.add(file.absolutePath)
                        val intent = Intent()
                        intent.putExtra("paths",stringPath)
                        setResult(Activity.RESULT_OK,intent)
                        finish()
                    }
                }
            }
        }
        setToolBarTitle(Environment.getExternalStorageDirectory().absolutePath)
    }

    fun checkInFolder(file: File){
        if(file.isDirectory){
            if(rcv_filelist.layoutManager != null) {//记录位置
                val layoutManager = rcv_filelist.layoutManager
                //获取可视的第一个view
                val topView = layoutManager?.getChildAt(0)
                if (topView != null) {
                    //获取与该view的顶部的偏移量
                    lastOffset.add(topView.top)
                    //得到该View的数组位置
                    lastPosition.add(layoutManager.getPosition(topView))
                }
            }

            fileFolder = file
            mDataList = FileUtils.getFileList(file.absolutePath, MyFileFilter(paramBean.stringType))
            adapter.clear()
            mDataList?.let { adapter.add(it) }
            rcv_filelist.scrollToPosition(0)
            setToolBarTitle(file.absolutePath)
        }
    }

    private fun checkBack(){
        if(fileFolder.absolutePath == rootPath){
            finish()
        }else{
            mDataList = FileUtils.getFileList(fileFolder.parentFile.absolutePath, MyFileFilter(paramBean.stringType))
            adapter.clear()
            mDataList?.let { adapter.add(it) }
            rcv_filelist.scrollToPosition(0)
            fileFolder = fileFolder.parentFile
            setToolBarTitle(fileFolder.absolutePath)

            if (rcv_filelist.layoutManager != null && lastPosition.size >= 0) {
                (rcv_filelist.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(lastPosition.last(), lastOffset.last())
                lastPosition.remove(lastPosition.last())
                lastOffset.remove(lastOffset.last())
            }
        }

    }

    private fun setToolBarTitle(text: String){
        supportActionBar?.title = text
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(paramBean.maxNum == 1){
            if(paramBean.chooseMode == CHOOSE_FOLDER){
                menuInflater.inflate(R.menu.toolbar_menu, menu)
            }
        }else{
            menuInflater.inflate(R.menu.toolbar_menu, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.finish){
            if(paramBean.chooseMode == CHOOSE_FOLDER) {
                val stringPath = ArrayList<String>()
                stringPath.add(fileFolder.absolutePath)
                val intent = Intent()
                intent.putExtra("paths", stringPath)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
        return true
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        checkBack()
    }
}