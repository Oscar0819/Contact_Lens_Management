package com.eunwoo.contactlensmanagement.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.work.*
import com.eunwoo.contactlensmanagement.EveryDayWorker
import com.eunwoo.contactlensmanagement.R
import com.eunwoo.contactlensmanagement.receiver.EveryDayReceiver
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {

    lateinit var alarmManager: AlarmManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        checkPermission()

    }

    fun startMainActivity() {
        CoroutineScope(Dispatchers.Main).launch {
            checkFirst()
//            setAlarm()
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
    private fun checkFirst() {
        val sp: SharedPreferences = getSharedPreferences("checkTheFirst", Context.MODE_PRIVATE)
        val check: Boolean = sp.getBoolean("checkTheFirst", false)

        if (!check) {
            doWorkManager()
            val spEdit: SharedPreferences.Editor = sp.edit()
            spEdit.putBoolean("checkTheFirst", true)
            spEdit.commit()
        }

    }

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
        calendar.set(Calendar.MINUTE, 0)

        alarmManager.setInexactRepeating(
            AlarmManager.RTC,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    private fun doWorkManager() {
        val calendar = Calendar.getInstance()
        val delay = calculateDelay(calendar)

        if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
            val periodicWorkRequest = PeriodicWorkRequestBuilder<EveryDayWorker>(15, TimeUnit.MINUTES)
                .setInitialDelay(delay, TimeUnit.SECONDS)
                .addTag("EveryDayWork")
                .build()
            WorkManager.getInstance(this).enqueue(periodicWorkRequest)
        }


    }

    private fun calculateDelay(startCalendar: Calendar): Long {
        // 오늘 알람이 울릴 시각 설정
        val todayCalendar = Calendar.getInstance()
        todayCalendar.set(Calendar.HOUR_OF_DAY, 4)
//        todayCalendar.set(Calendar.MINUTE, 0)
        todayCalendar.add(Calendar.DAY_OF_YEAR, 0)

        var delay = (todayCalendar.time.time - startCalendar.time.time) / 1000

        // 만약에 시작한 시점의 시간의 값을 뺐을 때 음수가 나오면 시간이 제대로 설정되지 않으므로
        // todayCalendar에 24시간을 추가해서 현재 시각(startCalendar)의 값을 빼더라도
        // 알림을 등록된 시각에 맞춰서 설정.
        // 현재 시각이 13시, 알림으로 지정된 시각이 4시라면 (알림시각) = 4 + 24 = 28
        // 28 - 13 = 15시간 후에 알림.. 현재 시각은 13시니 13 + 15 = 28 = 새벽 4시
        return if (delay < 0) {
            todayCalendar.add(Calendar.DAY_OF_YEAR, 1)
            (todayCalendar.time.time - startCalendar.time.time) / 1000
        } else {
            delay
        }

    }

}