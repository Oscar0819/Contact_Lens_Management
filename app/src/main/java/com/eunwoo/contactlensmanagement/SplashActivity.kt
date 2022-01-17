package com.eunwoo.contactlensmanagement

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.eunwoo.contactlensmanagement.receiver.EveryDayReceiver
import kotlinx.coroutines.*
import java.util.*

class SplashActivity : AppCompatActivity() {

    lateinit var alarmManager: AlarmManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        checkPermission()

    }

    fun startMainActivity() {
        CoroutineScope(Dispatchers.Main).launch {
//            checkFirst()
            setAlarm()
            delay(1000)
            val intent: Intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    // 권한 확인
    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            var permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
            ActivityCompat.requestPermissions(this, permissions,
                MainActivity.MY_PERMISSION_ACCESS_ALL
            )
        } else {
            startMainActivity()
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
            MainActivity.MY_PERMISSION_ACCESS_ALL -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this@SplashActivity, "위치권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
                    startMainActivity()
                } else {
                    Toast.makeText(this@SplashActivity, "위치권한을 거절하셨습니다. 관련 서비스를 제공 받으시려면 설정에 가셔서 위치권한을 허용해주세요.", Toast.LENGTH_LONG).show()
                    startMainActivity()
                }
                return
            }
        }
    }


    // 최초 실행에 알람 설정하는 코드
//    private fun checkFirst() {
//        val sp: SharedPreferences = getSharedPreferences("checkTheFirst", Context.MODE_PRIVATE)
//        val check: Boolean = sp.getBoolean("checkTheFirst", false)
//
//        if (!check) {
//            setAlarm()
//            val spEdit: SharedPreferences.Editor = sp.edit()
//            spEdit.putBoolean("checkTheFirst", true)
//            spEdit.commit()
//        }
//
//    }

    private fun setAlarm() {
        Log.d("splash", "setAlarm called!!")
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val receiverIntent = Intent(this, EveryDayReceiver::class.java)
        //receiverIntent.extras.putInt("id", id)
        // requestCode를 통해 인텐트를 식별. 취소할 때 참고..
        val pendingIntent = PendingIntent.getBroadcast(
            this, 9999, receiverIntent, PendingIntent.FLAG_MUTABLE
        )
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, 4)
//        calendar.set(Calendar.MINUTE, 0)

        alarmManager.setInexactRepeating(
            AlarmManager.RTC,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

}