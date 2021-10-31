package com.eunwoo.contactlensmanagement.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eunwoo.contactlensmanagement.Lens
import com.eunwoo.contactlensmanagement.LensDatabase
import com.eunwoo.contactlensmanagement.LensRecordAdapter
import com.eunwoo.contactlensmanagement.R
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
            // title 입력 다이얼로그를 호출한다.
            // title 입력하여 리사이클러뷰 addItem
            val edittext = EditText(_context)
            val builder: AlertDialog.Builder = AlertDialog.Builder(_context)
            builder.setTitle("Item 추가")
            builder.setMessage("제목을 입력해 주세요.")
            builder.setView(edittext)
            builder.setPositiveButton("입력"
            ) { dialog, which ->
                //제목 입력, DB추가
                if (!edittext.text.toString().isEmpty()) {
                    Thread(Runnable {
                        db!!.lensDao().insert( Lens(null, edittext.text.toString(), null))
                    }).start()
                }
            }
            builder.setNegativeButton("취소"
            ) { dialog, which ->
                //취소
            }
            builder.show()
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