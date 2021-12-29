package com.eunwoo.contactlensmanagement

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.eunwoo.contactlensmanagement.databinding.ActivityMainBinding
import com.eunwoo.contactlensmanagement.fragment.LensManagementFragment
import com.eunwoo.contactlensmanagement.fragment.MapFragment
import com.eunwoo.contactlensmanagement.fragment.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var  lensManagementFragment: LensManagementFragment
    private lateinit var  mapFragment: MapFragment
    private lateinit var  profileFragment: ProfileFragment

    companion object {
        const val TAG: String = "로그"
        val MY_PERMISSION_ACCESS_ALL = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Log.d(TAG, "MainActivity - onCreate() called")
        initBottomNavigationBar(binding)
        checkPermission()
    }

    // 바텀네비바 초기
    private fun initBottomNavigationBar(binding: ActivityMainBinding) {
        binding.bnvMain.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.map_fragment_item -> {
                    Log.d(TAG, "MainActivity - 맵 버튼 클릭")
                    mapFragment = MapFragment.newInstance()
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, mapFragment).commit()
                }
                R.id.lens_management_fragment_item -> {
                    Log.d(TAG, "MainActivity - 렌즈 관리 버튼 클릭")
                    lensManagementFragment = LensManagementFragment.newInstance()
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, lensManagementFragment).commit()
                }
                R.id.profile_fragment_item -> {
                    Log.d(TAG, "MainActivity - 프로필 버튼 클릭")
                    profileFragment = ProfileFragment.newInstance()
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, profileFragment).commit()
                }

            }
            true
        }
        // 초기 값 세팅
        binding.bnvMain.selectedItemId = R.id.lens_management_fragment_item
    }

    // 권한 확
    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            var permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
            ActivityCompat.requestPermissions(this, permissions, MY_PERMISSION_ACCESS_ALL)
        }
    }

    // 권한 요청 결과 반환
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            MY_PERMISSION_ACCESS_ALL -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this@MainActivity, "위치권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "위치권한을 거절하셨습니다. 관련 서비스를 제공 받으시려면 설정에 가셔서 위치권한을 허용해주세요.", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }
}