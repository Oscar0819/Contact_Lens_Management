package com.eunwoo.contactlensmanagement

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        checkPermission()

    }

    fun startMainActivity() {
        CoroutineScope(Dispatchers.Main).launch {
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
}