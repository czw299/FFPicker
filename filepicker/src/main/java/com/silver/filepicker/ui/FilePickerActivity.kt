package com.silver.filepicker.ui

import android.app.Activity
import android.content.Intent
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
import androidx.recyclerview.widget.RecyclerView
import com.silver.filepicker.FilePicker.Companion.CHOOSE_FOLDER
import com.silver.filepicker.R
import com.silver.filepicker.adapter.FileRecycleViewAdapter
import com.silver.filepicker.bean.ParamBean
import com.silver.filepicker.filter.MyFileFilter
import com.silver.filepicker.utils.FileUtils
import com.silver.filepicker.utils.NotificationBarSetter
import java.io.File
import java.lang.reflect.Field

class FilePickerActivity: AppCompatActivity() {
    var toolbar: Toolbar? = null
    val context = this
    var adapter: FileRecycleViewAdapter? = null
    val rootPath = Environment.getExternalStorageDirectory().absolutePath
    var fileFolder: File = File(rootPath)//当前目录
    var mDataList: ArrayList<File>? = null//当前列表
    var recyclerView: RecyclerView? = null
    var paramBean: ParamBean? = null
    var lastOffset: ArrayList<Int> = ArrayList()
    var lastPosition: ArrayList<Int> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.filepicker_activity)
        initView()
        initData()
    }

    fun initView(){
        NotificationBarSetter.setNotificationBarDarkFont(this,true,true)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.setNavigationOnClickListener{_ -> finish()}
        try {
            val field: Field = Toolbar::class.java.getDeclaredField("mTitleTextView")
            field.setAccessible(true)
            (field.get(toolbar) as TextView).ellipsize = TextUtils.TruncateAt.START
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    private fun initData(){
        val intent = intent
        paramBean = intent.getParcelableExtra<ParamBean>("ddd")

        recyclerView = findViewById(R.id.rcv_filelist)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView?.layoutManager = linearLayoutManager

        adapter = FileRecycleViewAdapter(context, paramBean!!.chooseMode, paramBean!!.maxNum, paramBean!!.stringType)
        recyclerView?.adapter = adapter
        mDataList = FileUtils.getFileList(fileFolder.absolutePath, MyFileFilter(paramBean!!.stringType))
        if(mDataList != null){
            adapter?.add(mDataList!!)
            adapter?.onItemClickListener = OnClick()
            setToolBarTitle(Environment.getExternalStorageDirectory().absolutePath)
        }
    }

    inner class OnClick : FileRecycleViewAdapter.OnItemClickListener {
        override fun onClick(view: View, file: File) {
            if (file.isDirectory){      //点击文件夹
                checkInFolder(file)
            }else{                      //点击文件
                if(paramBean!!.maxNum == 1){        //单选模式 直接返回
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

    fun checkInFolder(file: File){
        if(file.isDirectory){
            if(recyclerView!!.layoutManager != null) {//记录位置
                val layoutManager = recyclerView!!.layoutManager
                //获取可视的第一个view
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
            mDataList = FileUtils.getFileList(file.absolutePath, MyFileFilter(paramBean!!.stringType))
            adapter?.clear()
            adapter!!.add(mDataList!!)
            recyclerView?.scrollToPosition(0)
            setToolBarTitle(file.absolutePath)
        }
    }

    fun checkBack(){
        if(fileFolder.absolutePath == rootPath){
            finish()
        }else{
            mDataList = FileUtils.getFileList(fileFolder.parentFile.absolutePath, MyFileFilter(paramBean!!.stringType))
            adapter?.clear()
            adapter!!.add(mDataList!!)
            recyclerView?.scrollToPosition(0)
            fileFolder = fileFolder.parentFile
            setToolBarTitle(fileFolder.absolutePath)

            if (recyclerView?.layoutManager != null && lastPosition.size >= 0) {
                (recyclerView?.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(lastPosition.last(), lastOffset.last())
                lastPosition.remove(lastPosition.last())
                lastOffset.remove(lastOffset.last())
            }
        }

    }

    private fun setToolBarTitle(text: String){
        supportActionBar?.title = text
    }

    fun setSingleModeUI(){

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(paramBean!!.maxNum == 1){
            if(paramBean!!.chooseMode == CHOOSE_FOLDER){
                menuInflater.inflate(R.menu.toolbar_menu, menu)
            }
        }else{
            menuInflater.inflate(R.menu.toolbar_menu, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId.equals(R.id.finish)){
            if(paramBean!!.chooseMode == CHOOSE_FOLDER){//目录模式
                var stringPath = ArrayList<String>()
                stringPath.add(fileFolder.absolutePath)
                val intent= Intent()
                intent.putExtra("paths",stringPath)
                setResult(Activity.RESULT_OK,intent)
                finish()
            }else{                                      //文件模式
                val checkList = adapter?.getCheckList()
                var stringPath = ArrayList<String>()
                if (checkList != null) {
                    for(f in checkList){
                        stringPath.add(f.absolutePath)
                    }
                    val intent= Intent()
                    intent.putExtra("paths",stringPath)
                    setResult(Activity.RESULT_OK,intent)
                    finish()
                }
            }

        }
        return true
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        checkBack()
    }
}