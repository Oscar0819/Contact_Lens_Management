package com.eunwoo.contactlensmanagement.receiver

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.eunwoo.contactlensmanagement.R
import java.text.SimpleDateFormat

class AlarmReceiver: BroadcastReceiver() {

    companion object {
        val TAG: String = "AlarmReceiver"

        private val B_CHANNEL_ID: String = "Before Expiration Date Notification Channel"
        private val B_CHANNEL_NAME: String = "Before Expiration Date Notification Channel"
        private val D_CHANNEL_ID: String = "Expiration Date Notification Channel"
        private val D_CHANNEL_NAME: String = "Expiration Date Notification Channel"
    }

    lateinit var manager: NotificationManager
    lateinit var builder: NotificationCompat.Builder

//    init {
//        val manager: NotificationManager
//        val builder: NotificationCompat.Builder
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive called : ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())}")
        val am: AlarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (context != null) {
            manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        manager.createNotificationChannel(
            NotificationChannel(
                D_CHANNEL_ID, D_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
            )
        )
        builder = NotificationCompat.Builder(context, D_CHANNEL_ID)

        // 알림창 클릭시 activity call
//        val intent2: Intent = Intent(context, MainActivity::class.java)
//        val pendingIntent: PendingIntent = PendingIntent.getActivity(
//            context, 101, intent2, PendingIntent.FLAG_MUTABLE
//        )

        builder.apply {
            // 노티 제목
            setContentTitle("제목")
            setContentText("렌즈 사용기한이 임박했습니다.")
//            setContentIntent(pendingIntent)
            setSmallIcon(R.drawable.ic_icon)
            setPriority(NotificationCompat.PRIORITY_HIGH)
//            setFullScreenIntent(pendingIntent, true)
            setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE)
            // 노티 터치시 자동 삭제
            setAutoCancel(true)
        }

        val notification: Notification = builder.build()
        manager.notify(1, notification)
    }
}