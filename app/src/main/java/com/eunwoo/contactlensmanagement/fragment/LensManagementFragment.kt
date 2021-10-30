package com.eunwoo.contactlensmanagement.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
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

    // 메모리 올라갔을 때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "LensManagementFragment - onCreate() called")
    }

    // 프레그먼트를 안고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "LensManagementFragment - onAttach() called")
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



        return binding.root
    }
}