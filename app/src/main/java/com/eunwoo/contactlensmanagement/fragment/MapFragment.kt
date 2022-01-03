package com.eunwoo.contactlensmanagement.fragment

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.eunwoo.contactlensmanagement.databinding.MapFragmentBinding

class MapFragment: Fragment() {
    // 싱글톤
    companion object {
        const val TAG: String = "MapFragment"

        fun newInstance(): MapFragment {
            return MapFragment()
        }
    }

    lateinit var binding: MapFragmentBinding


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


        return binding.root
//------------------------------------------------------
//        val v = inflater.inflate(R.layout.map_fragment, container, false)
//
//        mapView = MapView(activity)
//        val mapViewContainer: ViewGroup = v.findViewById(R.id.kakaoMapView)
//        mapViewContainer.addView(mapView)
//
//        startTracking()
//
//        return v
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart called")
    }

    // GPS가 켜져있는지 확인
    private fun checkLocationService(): Boolean {
        val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    // 위치추적 시작
    private fun startTracking() {
        Log.d(TAG, "startTracking")
        if (checkLocationService()) {
            onLog("checkLocationService : ${checkLocationService()}")


        } else {
                // GPS가 꺼져있을 경우
                Toast.makeText(context, "GPS를 켜주세요", Toast.LENGTH_SHORT).show()
        }
    }

    fun onLog(s: String) {
        Log.d(TAG, s)
    }

}