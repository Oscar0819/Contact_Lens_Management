package com.eunwoo.contactlensmanagement.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eunwoo.contactlensmanagement.*
import com.eunwoo.contactlensmanagement.database.LensDatabase
import com.eunwoo.contactlensmanagement.databinding.LensManagementFragmentBinding

class LensManagementFragment: Fragment() {
    // 싱글톤
    companion object {
        const val TAG: String = "로그"

        fun newInstance(): LensManagementFragment {
            return LensManagementFragment()
        }
    }

    private lateinit var binding: LensManagementFragmentBinding
    private lateinit var adapter: LensRecordAdapter

    private lateinit var db: LensDatabase

    private lateinit var _context: Context

    // 메모리 올라갔을 때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "LensManagementFragment - onCreate() called")
    }

    // 프레그먼트를 안고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "LensManagementFragment - onAttach() called")
        _context = context
    }

    // 뷰가 생성되었을 때
    // 프래그먼트와 레이아웃을 연결시켜주는 부분
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d(TAG, "LensManagementFragment - onCreateView() called")

        binding = LensManagementFragmentBinding.inflate(inflater, container, false)

//        initSwipe()

        binding.lensAddButton.setOnClickListener {
            val intent = Intent(_context, LensInfoActivity::class.java)
            startActivity(intent)
        }
        db = LensDatabase.getInstance(_context)
        binding.lensRecordRecyclerview.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(_context)
        binding.lensRecordRecyclerview.layoutManager = layoutManager

        //UI 갱신 (라이브데이터 Observer 이용, 해당 디비값이 변화가생기면 실행됨)
        db!!.lensDao().getAll().observe(viewLifecycleOwner, androidx.lifecycle.Observer{
            // update UI
            adapter = LensRecordAdapter(db!!,it)
            binding.lensRecordRecyclerview.adapter = adapter
        })

        return binding.root
    }

}