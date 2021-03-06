package com.eunwoo.contactlensmanagement

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eunwoo.contactlensmanagement.activity.LensInfoActivity
import com.eunwoo.contactlensmanagement.database.Lens
import com.eunwoo.contactlensmanagement.database.LensDatabase
import com.eunwoo.contactlensmanagement.databinding.LensRecordItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

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

    fun removeData(position: Int) {
        CoroutineScope(IO).launch {
            db.lensDao().delete(items!!.get(position))
        }
    }

    inner class ViewHolder(private val binding: LensRecordItemBinding): RecyclerView.ViewHolder(binding.root){
        var index: Int? = null
        fun bind(lens: Lens, position: Int) {
            index = position
            Log.d("LRA", "INDEX : $index")
            binding.textView.text = lens.id.toString()
            binding.textView2.text = lens.name
            binding.textView3.text = lens.initialDate
            binding.textView4.text = lens.expirationDate2.toString()

            // 리사이클러뷰의 아이템 클릭시 아이템 수정 창이 뜨는 코드...
            itemView.setOnClickListener {
                Log.d("LRA", "ClickINDEX : $index")
                CoroutineScope(IO).launch {
                Intent(mContext, LensInfoActivity::class.java).apply {
                    putExtra("code", 1)
                    putExtra("index", index)
                    putExtra("id", db.lensDao().getList()[index!!].id)
                    }.run { mContext.startActivity(this) }
                }
            }

//            binding.itemDelete.setOnClickListener {
//                removeData(position)
//            }
        }

    }
}