package com.eunwoo.contactlensmanagement

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.eunwoo.contactlensmanagement.database.Lens
import com.eunwoo.contactlensmanagement.database.LensDatabase
import com.eunwoo.contactlensmanagement.databinding.LensRecordItemBinding

class LensRecordAdapter(val db: LensDatabase, var items: List<Lens>?)
    : RecyclerView.Adapter<LensRecordAdapter.ViewHolder>() {

    lateinit var binding: LensRecordItemBinding
    lateinit var mContext: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : ViewHolder {
        binding = LensRecordItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        mContext = parent.context
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items!!.get(position),position)
    }

    override fun getItemCount(): Int {
        return items!!.size
    }
    fun getItem(): List<Lens>?{
        return items
    }


    inner class ViewHolder(private val binding: LensRecordItemBinding): RecyclerView.ViewHolder(binding.root){
        var index: Int? = null
        fun bind(lens: Lens, position: Int) {
            index = position
            binding.textView.setText(lens.id.toString())
            binding.textView2.setText(lens.name)
            binding.textView3.setText(lens.contents)
            binding.button.setOnClickListener { editData(binding.textView.text.toString()) }
        }
        fun editData(contents: String){
            Thread {
                index?.let { items!!.get(it).contents = contents };
                index?.let { items!!.get(it) }?.let { db.lensDao().update(it) };
            }.start()
            Toast.makeText(mContext,"저장완료", Toast.LENGTH_SHORT).show()
        }

    }
}