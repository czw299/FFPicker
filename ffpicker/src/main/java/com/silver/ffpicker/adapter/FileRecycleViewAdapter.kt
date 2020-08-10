package com.silver.ffpicker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.silver.ffpicker.FFPicker
import com.silver.ffpicker.R
import kotlinx.android.synthetic.main.item_filelist_rcv.view.*
import java.io.File

class FileRecycleViewAdapter(var context: Context, var chooseMode: Int, var mMaxNum: Int, var fileType: Array<String?>): RecyclerView.Adapter<FileRecycleViewAdapter.MyHolder>(),View.OnClickListener{
    var onItemClickListener:OnItemClickListener? = null
    private val mData: ArrayList<File> = ArrayList()
    private val mListNumbers = ArrayList<String>() //存放选中条目的数据地址

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_filelist_rcv, parent, false)
        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val file: File = mData[position]
        if(file.isDirectory){
            holder.itemView.iv_fileicon.setImageDrawable(context.getDrawable(R.drawable.icon_folder))
        }else if(file.isFile){
            holder.itemView.iv_fileicon.setImageDrawable(context.getDrawable(R.drawable.icon_file))
        }
        holder.itemView.setOnClickListener(this)
        holder.itemView.tv_filename.text = file.name
        holder.itemView.tag = mData[position]
    }

    private fun add(file: File){
        if(chooseMode == FFPicker.CHOOSE_FILE){
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
        mListNumbers.clear()
    }

    fun clear(){
        mData.clear()
        notifyDataSetChanged()
        mListNumbers.clear()
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface OnItemClickListener{
        fun onClick(view: View, file: File)
    }

    override fun onClick(p0: View?) {
        if (p0 != null) {
            onItemClickListener?.onClick(p0, p0.tag as File)
        }
    }
}