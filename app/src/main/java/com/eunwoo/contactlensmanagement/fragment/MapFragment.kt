package com.eunwoo.contactlensmanagement.fragment

import android.content.Context
import android.graphics.PointF
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.*

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.eunwoo.contactlensmanagement.BuildConfig
import com.eunwoo.contactlensmanagement.MainActivity
import com.eunwoo.contactlensmanagement.R

import com.eunwoo.contactlensmanagement.ResultSearchKeyword
import com.eunwoo.contactlensmanagement.databinding.MapFragmentBinding
import com.eunwoo.contactlensmanagement.restapi.KakaoAPI
import com.eunwoo.contactlensmanagement.viewmodel.MainViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*

import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MapFragment: Fragment(), OnMapReadyCallback {
    // 싱글톤
    companion object {
        const val TAG: String = "MapFragment"

        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000

        private const val BASE_URL = "https://dapi.kakao.com/"
        private const val REST_API_KEY = BuildConfig.KAKAO_REST_API_KEY

        fun newInstance(): MapFragment {
            return MapFragment()
        }

    }

    lateinit var binding: MapFragmentBinding
    private lateinit var mapView: MapView

    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap

    private var initCnt: Int = 0
    val markers = mutableListOf<Marker>()

    private var mapPersistBottomFragment: MapPersistBottomFragment? = null

    private val viewModel: MainViewModel by activityViewModels()

    val mainActivity: MainActivity by lazy {
        (activity as MainActivity)
    }

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

        binding.animationView.playAnimation()
//        binding.animationView.loop(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mapView.visibility = View.INVISIBLE

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

            // GPS가 제대로된 위치를 탐색 했을 때 API를 호출
            naverMap.addOnLocationChangeListener {
                if (initCnt == 0) {
                    initCnt++
                    val x = it.longitude
                    val y = it.latitude
                    Log.d(TAG, "x : ${x}")
                    Log.d(TAG, "y : ${y}")
                    if (x != null && y != null) {
                        searchKeyword("렌즈", x, y, 5000)
                    }
                }
            }

        } else {
            // GPS가 꺼져있을경우 트래킹 모드 설정 및 토스트 메시지
            naverMap.locationTrackingMode = LocationTrackingMode.None
            shortToastMassege("GPS를 켜주세요")
        }
    }

    // 키워드 검색 함수
    private fun searchKeyword(keyword: String, x: Double, y: Double, radius: Long) {
        val retrofit = Retrofit.Builder()   // Retrofit 구성
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(KakaoAPI::class.java)   // 통신 인터페이스를 객체로 생성
        val call = api.getSearchKeyword("KakaoAK " + REST_API_KEY, keyword, x, y, radius)   // 검색 조건 입력

        // API 서버에 요청 (비동기로 실행됨...)
        call.enqueue(object: Callback<ResultSearchKeyword> {
            override fun onResponse(
                call: Call<ResultSearchKeyword>,
                response: Response<ResultSearchKeyword>
            ) {
                // 통신 성공 (검색 결과는 response.body()에 담겨있음)
//                Log.d("Test", "Raw: ${response.raw()}")
//                Log.d("Test", "Body: ${response.body()}")
//                shortToastMassege("통신 성공!")
                addItemsAndMarkers(response.body())
            }

            override fun onFailure(call: Call<ResultSearchKeyword>, t: Throwable) {
                // 통신 실패
                Log.w("MainActivity", "통신 실패: ${t.message}")
                longToastMassege("통신에 실패했습니다.")
            }
        })
    }

    // 검색 결과 처리 함수
    private fun addItemsAndMarkers(searchResult: ResultSearchKeyword?) {
        // 데이터가 null이 아닌 경우에만 작동..
        if (!searchResult?.documents.isNullOrEmpty()) {
            // 검색 결과 있음
            markers.clear()
            // 마커 생성 후 저장
            for (document in searchResult!!.documents) {
                val marker = Marker()
                marker.position = LatLng(document.y.toDouble(), document.x.toDouble())
                marker.tag = document.address_name + "\n" +
                        document.place_name + "\n" +
                        document.phone
                marker.onClickListener = Overlay.OnClickListener {
                    // 마커 리스너
                    mainActivity.showBottomFragment(document)

                    true
                }

                markers.add(marker)
            }

            markers.forEach {
                it.map = naverMap
            }

            binding.mapView.visibility = View.VISIBLE
            binding.animationView.pauseAnimation()
            binding.animationView.visibility = View.GONE


        } else {
            // 검색 결과 없음
            shortToastMassege("검색 결과가 없습니다.")
        }
    }

    private fun shortToastMassege(s: String) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }

    private fun longToastMassege(s: String) {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show()
    }



    override fun onMapReady(p0: NaverMap) {
        Log.d(TAG, "onMapReady")
        naverMap = p0

        // 지도를 클릭하면 정보 창을 닫음
        naverMap.setOnMapClickListener { pointF, latLng ->
            mainActivity.hideBottomFragment()
        }

        startTracking()
    }
}