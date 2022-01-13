package com.eunwoo.contactlensmanagement

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.eunwoo.contactlensmanagement.databinding.ActivityMainBinding
import com.eunwoo.contactlensmanagement.enums.PageType
import com.eunwoo.contactlensmanagement.fragment.LensManagementFragment
import com.eunwoo.contactlensmanagement.fragment.MapFragment
import com.eunwoo.contactlensmanagement.fragment.MapPersistBottomFragment
import com.eunwoo.contactlensmanagement.fragment.ProfileFragment
import com.eunwoo.contactlensmanagement.receiver.AlarmReceiver
import com.eunwoo.contactlensmanagement.receiver.EveryDayReceiver
import com.eunwoo.contactlensmanagement.viewmodel.MainViewModel
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var fragmentManager: FragmentManager

    private var lensManagementFragment: LensManagementFragment? = null
    private var mapFragment: MapFragment? = null
    private var profileFragment: ProfileFragment? = null
    private var mapPersistBottomFragment: MapPersistBottomFragment? = null
    private var bottomFragmentCnt = 0

    private val viewModel: MainViewModel by viewModels()

    lateinit var alarmManager: AlarmManager

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

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fragmentManager = supportFragmentManager
        // 시작 프래그먼트는 lensManagementFragment
        lensManagementFragment = LensManagementFragment.newInstance()
        fragmentManager.beginTransaction().replace(R.id.fl_container, lensManagementFragment!!)
            .commit()

        Log.d(TAG, "MainActivity - onCreate() called")
        initBottomNavigationBar()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onOptionsItemSelected")
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val receiverIntent = Intent(this, EveryDayReceiver::class.java)
        //receiverIntent.extras.putInt("id", id)
        // requestCode를 통해 인텐트를 식별. 취소할 때 참고..
        val pendingIntent = PendingIntent.getBroadcast(
            this, 1000, receiverIntent, PendingIntent.FLAG_MUTABLE
        )
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, 4)
//        calendar.set(Calendar.MINUTE, 0)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        return super.onOptionsItemSelected(item)
    }

    // 바텀네비바 초기
    private fun initBottomNavigationBar() {
        binding.bnvMain.selectedItemId =
            R.id.lens_management_fragment_item // 바텀 네비의 버튼 clicked 초기화..
        binding.bnvMain.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.map_fragment_item -> {
                    Log.d(TAG, "MainActivity - 맵 버튼 클릭")
                    if (mapFragment == null) {
                        mapFragment = MapFragment.newInstance()
                        fragmentManager.beginTransaction().add(R.id.fl_container, mapFragment!!)
                            .commit()
                    }
                    if (lensManagementFragment != null) fragmentManager.beginTransaction()
                        .hide(lensManagementFragment!!).commit()
                    if (mapFragment != null) fragmentManager.beginTransaction().show(mapFragment!!)
                        .commit()
                    if (profileFragment != null) fragmentManager.beginTransaction()
                        .hide(profileFragment!!).commit()
                    hideBottomFragment()
                }
                R.id.lens_management_fragment_item -> {
                    Log.d(TAG, "MainActivity - 렌즈 관리 버튼 클릭")
                    if (lensManagementFragment == null) {
                        lensManagementFragment = LensManagementFragment.newInstance()
                        fragmentManager.beginTransaction()
                            .add(R.id.fl_container, lensManagementFragment!!).commit()
                    }

                    if (lensManagementFragment != null) fragmentManager.beginTransaction()
                        .show(lensManagementFragment!!).commit()
                    if (mapFragment != null) fragmentManager.beginTransaction().hide(mapFragment!!)
                        .commit()
                    if (profileFragment != null) fragmentManager.beginTransaction()
                        .hide(profileFragment!!).commit()
                    hideBottomFragment()
                }
                R.id.profile_fragment_item -> {
                    Log.d(TAG, "MainActivity - 프로필 버튼 클릭")
                    if (profileFragment == null) {
                        profileFragment = ProfileFragment.newInstance()
                        fragmentManager.beginTransaction().add(R.id.fl_container, profileFragment!!)
                            .commit()
                    }

                    if (lensManagementFragment != null) fragmentManager.beginTransaction()
                        .hide(lensManagementFragment!!).commit()
                    if (mapFragment != null) fragmentManager.beginTransaction().hide(mapFragment!!)
                        .commit()
                    if (profileFragment != null) fragmentManager.beginTransaction()
                        .show(profileFragment!!).commit()
                    hideBottomFragment()
                }
            }
            true
        }
    }

    fun showBottomFragment(place: Place) {

        if (bottomFragmentCnt == 0) {
            Log.d(TAG, "Show")
            bottomFragmentCnt++
            mapPersistBottomFragment = MapPersistBottomFragment.show(fragmentManager, R.id.view_bottom_sheet)
        } else {
            Log.d(TAG, "Visible")
            mapPersistBottomFragment!!.view?.visibility = View.VISIBLE
            binding.viewBottomSheet.visibility = View.VISIBLE
        }
        viewModel.setPlaceData(place)
    }

    fun hideBottomFragment() {
//      if  (mapPersistBottomFragment?.isVisible == true)
        if (mapPersistBottomFragment?.isVisible == true) {
            Log.d(TAG, "inVisible")
            mapPersistBottomFragment!!.view?.visibility = View.INVISIBLE
            binding.viewBottomSheet.visibility = View.INVISIBLE
        }
    }

    override fun onBackPressed() {
        // handleBackKeyEvent : 백스택을 조회하여 있을 경우 childFragmentManager.popBackStackImmediate() 실행..
        if (mapPersistBottomFragment?.handleBackKeyEvent() == true) {
            // no-op
        } else {
            super.onBackPressed()
        }
    }

}
