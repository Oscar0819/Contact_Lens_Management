package com.eunwoo.contactlensmanagement.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.eunwoo.contactlensmanagement.MainActivity
import com.eunwoo.contactlensmanagement.R
import com.eunwoo.contactlensmanagement.databinding.MapFragmentBinding
import com.eunwoo.contactlensmanagement.viewmodel.MainViewModel
import net.daum.mf.map.api.MapView

class MapFragment: Fragment() {
    // 싱글톤
    companion object {
        const val TAG: String = "로그"

        fun newInstance(): MapFragment {
            return MapFragment()
        }
    }

    lateinit var binding: MapFragmentBinding
    lateinit var mapView: MapView

    // 메모리 올라갔을 때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "MapFragment - onCreate() called")
    }

    // 프레그먼트를 안고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "MapFragment - onAttach() called")
    }

    // 뷰가 생성되었을 때
    // 프래그먼트와 레이아웃을 연결시켜주는 부분
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "MapFragment - onCreateView() called")

        binding = MapFragmentBinding.inflate(inflater, container, false)

        // val view = inflater.inflate(R.layout.map_fragment, container, false)

        mapView = MapView(activity)
        binding.exploreMapView.addView(mapView)

        return binding.root
        //return super.onCreateView(inflater, container, savedInstanceState)
    }
}