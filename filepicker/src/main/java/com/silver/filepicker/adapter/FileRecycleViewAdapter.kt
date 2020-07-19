package com.silver.filepicker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.silver.filepicker.FilePicker
import com.silver.filepicker.R
import java.io.File

class FileRecycleViewAdapter(var context: Context, var chooseMode: Int, var mMaxNum: Int, var fileType: Array<String?>): RecyclerView.Adapter<FileRecycleViewAdapter.MyHolder>(),View.OnClickListener{
    var onItemClickListener:OnItemClickListener? = null
    private val mData: ArrayList<File> = ArrayList()
    var mCheckFlag: BooleanArray = BooleanArray(0)
    private val mListNumbers = ArrayList<String>() //存放选中条目的数据地址


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_filelist_rcv, parent, false)
        val myHolder = MyHolder(view)
        return myHolder
    }

    override fun getItemCount(): Int {
        return mData.size ?: 0
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val file: File = mData[position]
        if(file.isDirectory){
            holder.imageView.setImageDrawable(context.getDrawable(R.drawable.icon_folder))
            holder.checkBox.visibility = View.GONE
        }else if(file.isFile){
            holder.imageView.setImageDrawable(context.getDrawable(R.drawable.icon_file))
            if(mMaxNum == 1){
                holder.checkBox.visibility = View.GONE
            }else{
                holder.checkBox.visibility = View.VISIBLE
            }
        }
        holder.checkBox.setOnCheckedChangeListener(onCheck(position))
        holder.checkBox.isChecked = false
        holder.itemView.setOnClickListener(this)
        holder.textView.text = file.name
        holder.itemView.tag = mData[position]
    }

    inner class onCheck(var position: Int): CompoundButton.OnCheckedChangeListener{
        override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
            mCheckFlag[position] = p1
            if(p1){
                if(mListNumbers.size < mMaxNum){
                    mListNumbers.add(mData[position].absolutePath)
                }else{
                    (p0 as CheckBox).isChecked = false
                    Toast.makeText(context, "已达最大数量", Toast.LENGTH_LONG).show()
                }
            }else{
                mListNumbers.remove(mData[position].absolutePath)
            }

        }
    }

    private fun add(file: File){
        if(chooseMode == FilePicker.CHOOSE_FILE){
            mData.add(file)
            notifyDataSetChanged()
        }else{
            if(file.isDirectory){
                mData.add(file)
                notifyDataSetChanged()
            }
        }
    }

    fun add(fileList: ArrayList<File>){
        for(file in fileList){
            add(file)
        }
        mCheckFlag = BooleanArray(mData.size)
        mListNumbers.clear()
    }

    fun clear(){
        mData.clear()
        notifyDataSetChanged()
        mCheckFlag = BooleanArray(mData.size)
        mListNumbers.clear()
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tv_filename)
        val imageView: ImageView = itemView.findViewById(R.id.iv_fileicon)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
    }

    fun getCheckList(): ArrayList<File> {
        val checkFileList: ArrayList<File> = ArrayList()
        for((index, b) in mCheckFlag.withIndex()){
            if(b){
                checkFileList.add(mData[index])
            }
        }
        return checkFileList
    }



    abstract inner class OnFileItemClick(val holder: MyHolder): View.OnClickListener{
        /*override fun onClick(p0: View?) {
            if(mMaxNum == 1){

            }
            if(mListNumbers.size < mMaxNum){
                holder.checkBox.isChecked = !holder.checkBox.isChecked
            }else{
                if(holder.checkBox.isChecked){
                    holder.checkBox.isChecked = !holder.checkBox.isChecked
                }else{
                    Toast.makeText(context,"已达最大数量",Toast.LENGTH_LONG).show()
                }
            }

        }*/
    }

    interface OnItemClickListener{
        fun onClick(view: View, file: File)
    }

    override fun onClick(p0: View?) {
        if (p0 != null) {
            onItemClickListener?.onClick(p0, p0.tag as File)
        }
    }
}