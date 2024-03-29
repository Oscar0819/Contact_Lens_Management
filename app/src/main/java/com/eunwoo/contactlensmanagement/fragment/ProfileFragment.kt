package com.eunwoo.contactlensmanagement.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.eunwoo.contactlensmanagement.R
import com.eunwoo.contactlensmanagement.databinding.LensManagementFragmentBinding
import com.eunwoo.contactlensmanagement.databinding.ProfileFragmentBinding
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract

class ProfileFragment: Fragment() {
    // 싱글톤
    companion object {
        const val TAG: String = "로그"

        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }

    lateinit var binding: ProfileFragmentBinding

    // 메모리 올라갔을 때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "ProfileFragment - onCreate() called")
    }

    // 프레그먼트를 안고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "ProfileFragment - onAttach() called")
    }

    // 뷰가 생성되었을 때
    // 프래그먼트와 레이아웃을 연결시켜주는 부분
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d(TAG, "ProfileFragment - onCreateView() called")

        binding = ProfileFragmentBinding.inflate(inflater, container, false)

        binding.profileCardview.setOnClickListener {
            val intent = Intent()

        }

        return binding.root
        //return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}