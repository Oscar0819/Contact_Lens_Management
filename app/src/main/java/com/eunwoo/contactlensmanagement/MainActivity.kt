package com.eunwoo.contactlensmanagement

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
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

    private lateinit var fragmentManager: FragmentManager

    private var lensManagementFragment: LensManagementFragment? = null
    private var mapFragment: MapFragment? = null
    private var profileFragment: ProfileFragment? = null
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

        fragmentManager = supportFragmentManager
        // 시작 프래그먼트는 lensManagementFragment
        lensManagementFragment = LensManagementFragment.newInstance()
        fragmentManager.beginTransaction().replace(R.id.fl_container, lensManagementFragment!!).commit()

        Log.d(TAG, "MainActivity - onCreate() called")
        initBottomNavigationBar()
    }

    // 바텀네비바 초기
    private fun initBottomNavigationBar() {
        // 시작 프래그먼트는 lensManagementFragment
        binding.bnvMain.selectedItemId = R.id.lens_management_fragment_item
        binding.bnvMain.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.map_fragment_item -> {
                    Log.d(TAG, "MainActivity - 맵 버튼 클릭")
                    if (mapFragment == null) {
                        mapFragment = MapFragment.newInstance()
                        fragmentManager.beginTransaction().add(R.id.fl_container, mapFragment!!).commit()
                    }
                    if (lensManagementFragment != null) fragmentManager.beginTransaction().hide(lensManagementFragment!!).commit()
                    if (mapFragment != null) fragmentManager.beginTransaction().show(mapFragment!!).commit()
                    if (profileFragment != null) fragmentManager.beginTransaction().hide(profileFragment!!).commit()
                }
                R.id.lens_management_fragment_item -> {
                    Log.d(TAG, "MainActivity - 렌즈 관리 버튼 클릭")
                    if (lensManagementFragment == null) {
                        lensManagementFragment = LensManagementFragment.newInstance()
                        fragmentManager.beginTransaction().add(R.id.fl_container, lensManagementFragment!!).commit()
                    }

                    if (lensManagementFragment != null) fragmentManager.beginTransaction().show(lensManagementFragment!!).commit()
                    if (mapFragment != null) fragmentManager.beginTransaction().hide(mapFragment!!).commit()
                    if (profileFragment != null) fragmentManager.beginTransaction().hide(profileFragment!!).commit()
                }
                R.id.profile_fragment_item -> {
                    Log.d(TAG, "MainActivity - 프로필 버튼 클릭")
                    if (profileFragment == null) {
                        profileFragment = ProfileFragment.newInstance()
                        fragmentManager.beginTransaction().add(R.id.fl_container, profileFragment!!).commit()
                    }

                    if (lensManagementFragment != null) fragmentManager.beginTransaction().hide(lensManagementFragment!!).commit()
                    if (mapFragment != null) fragmentManager.beginTransaction().hide(mapFragment!!).commit()
                    if (profileFragment != null) fragmentManager.beginTransaction().show(profileFragment!!).commit()
                }

            }
            true
        }
    }

}