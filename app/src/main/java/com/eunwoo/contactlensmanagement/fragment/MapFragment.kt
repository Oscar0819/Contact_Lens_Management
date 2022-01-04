package com.eunwoo.contactlensmanagement.fragment

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable

import androidx.fragment.app.Fragment
import com.eunwoo.contactlensmanagement.BuildConfig

import com.eunwoo.contactlensmanagement.R
import com.eunwoo.contactlensmanagement.ResultSearchKeyword
import com.eunwoo.contactlensmanagement.databinding.MapFragmentBinding
import com.eunwoo.contactlensmanagement.restapi.KakaoAPI
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MapFragment: Fragment(), OnMapReadyCallback{
    // 싱글톤
    companion object {
        const val TAG: String = "MapFragment"

        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000

        private const val BASE_URL = "https://dapi.kakao.com/"
        private const val API_KEY = BuildConfig.KAKAO_REST_API_KEY

        fun newInstance(): MapFragment {
            return MapFragment()
        }
    }

    lateinit var binding: MapFragmentBinding
    private lateinit var mapView: MapView

    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap

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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    // GPS가 켜져있는지 확인
    private fun checkLocationService(): Boolean {
        val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    // 위치 추적 시작
    private fun startTracking() {
        Log.d(TAG, "startTracking")
        if (checkLocationService()) {
            // GPS가 켜져있을경우 트래킹 모드 설정
            Log.d(TAG, "checkLocationService : ${checkLocationService()}")
            naverMap.locationSource = locationSource
            naverMap.locationTrackingMode = LocationTrackingMode.Follow

            // naverMap ui settings
            val uiSettings = naverMap.uiSettings
            uiSettings.isZoomControlEnabled = true
            uiSettings.isLocationButtonEnabled = true

            // 오버레이 setting
            val locationOverlay = naverMap.locationOverlay
            locationOverlay.isVisible = true

        } else {
            // GPS가 꺼져있을경우 트래킹 모드 설정 및 토스트 메시지
            naverMap.locationTrackingMode = LocationTrackingMode.None
            Toast.makeText(context, "GPS를 켜주세요", Toast.LENGTH_SHORT).show()
        }
    }

    // 키워드 검색 함수
    private fun searchKeyword(keyword: String) {
        val retrofit = Retrofit.Builder()   // Retrofit 구성
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(KakaoAPI::class.java)   // 통신 인터페이스를 객체로 생성
        val call = api.getSearchKeyword(API_KEY, keyword)   // 검색 조건 입력

        // API 서버에 요청
        call.enqueue(object: Callback<ResultSearchKeyword> {
            override fun onResponse(
                call: Call<ResultSearchKeyword>,
                response: Response<ResultSearchKeyword>
            ) {
                // 통신 성공 (검색 결과는 response.body()에 담겨있음)
                Log.d("Test", "Raw: ${response.raw()}")
                Log.d("Test", "Body: ${response.body()}")
            }

            override fun onFailure(call: Call<ResultSearchKeyword>, t: Throwable) {
                // 통신 실패
                Log.w("MainActivity", "통신 실패: ${t.message}")
            }
        })
    }

    override fun onMapReady(p0: NaverMap) {
        Log.d(TAG, "onMapReady")
        naverMap = p0
//        val locationOverlay = naverMap.locationOverlay
//        locationOverlay.isVisible = true

        startTracking()
    }
}