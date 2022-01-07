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
import com.eunwoo.contactlensmanagement.fragment.MapPersistBottomFragment
import com.eunwoo.contactlensmanagement.fragment.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kr.co.prnd.persistbottomsheetfragment.PersistBottomSheetFragment
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var  lensManagementFragment: LensManagementFragment
    private lateinit var  mapFragment: MapFragment
    private lateinit var  profileFragment: ProfileFragment
    private var mapPersistBottomFragment: MapPersistBottomFragment? = null

    companion object {
        const val TAG: String = "로그"
        val MY_PERMISSION_ACCESS_ALL = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
//        setTheme(R.style.Theme_ContactLensManagement)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Log.d(TAG, "MainActivity - onCreate() called")
        initBottomNavigationBar(binding)
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
//                    profileFragment = ProfileFragment.newInstance()
//                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, profileFragment).commit()
                    mapPersistBottomFragment = MapPersistBottomFragment.show(supportFragmentManager, R.id.view_bottom_sheet)
                }

            }
            true
        }
        // 초기 값 세팅
        binding.bnvMain.selectedItemId = R.id.lens_management_fragment_item
    }
}