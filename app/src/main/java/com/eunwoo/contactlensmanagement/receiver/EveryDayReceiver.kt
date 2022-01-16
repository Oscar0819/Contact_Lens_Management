package com.eunwoo.contactlensmanagement.receiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.eunwoo.contactlensmanagement.R
import com.eunwoo.contactlensmanagement.database.Lens
import com.eunwoo.contactlensmanagement.database.LensDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EveryDayReceiver: BroadcastReceiver() {

    lateinit var db: LensDatabase

    lateinit var manager: NotificationManager
    lateinit var builder: NotificationCompat.Builder

    companion object {
        const val TAG = "EveryDayReceiver"

        private val D_CHANNEL_ID: String = "Expiration Date Notification Channel"
        private val D_CHANNEL_NAME: String = "Expiration Date Notification Channel"

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive")
        if (context != null) {
            CoroutineScope(Dispatchers.Default).launch {
                db = LensDatabase.getInstance(context)
                if (db.lensDao().getList().isNotEmpty()) { // db가 비어있지않을경우...
                    db.lensDao().getList().forEach {
//                        val expirationDate: Long? = it.expirationDate2
                        if (it.expirationDate2!! > 2) {
                            updateExpirationDate2(it)
                        } else if (it.expirationDate2!! == 2L) {
                            if (it.pushCheck == true) {
                                exeAlarm(context, it.expirationDate2!!)
                                updateExpirationDate2(it)
                            } else {
                                updateExpirationDate2(it)
                            }
                        } else if (it.expirationDate2!! == 1L) {
                            if (it.pushCheck == true) {
                                exeAlarm(context, it.expirationDate2!!)
                                updateExpirationDate2AndPushCheck(it)
                            } else {
                                updateExpirationDate2(it)
                            }
                        }
                    }
                }
            }
        }

    }

    private fun updateExpirationDate2(lens: Lens) {
        db.lensDao().update(Lens(lens.id,
            lens.name,
            lens.leftSight,
            lens.rightSight,
            lens.productName,
            lens.initialDate,
            lens.expirationDate,
            lens.pushCheck,
            lens.memo,
            lens.expirationDate2!! - 1
        ))
    }

    private fun updateExpirationDate2AndPushCheck(lens: Lens) {
        db.lensDao().update(Lens(lens.id,
            lens.name,
            lens.leftSight,
            lens.rightSight,
            lens.productName,
            lens.initialDate,
            lens.expirationDate,
            false,
            lens.memo,
            lens.expirationDate2!! - 1
        ))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun exeAlarm(context: Context, expirationDate2: Long) {
        manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

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

        if (expirationDate2 == 2L) {
            builder.apply {
                // 노티 제목
                setContentTitle("aboutLens")
                setContentText("렌즈 사용기한이 임박했습니다.")
//            setContentIntent(pendingIntent)
                setSmallIcon(R.drawable.noti_icon)
                setPriority(NotificationCompat.PRIORITY_HIGH)
//            setFullScreenIntent(pendingIntent, true)
                setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE)
                // 노티 터치시 자동 삭제
                setAutoCancel(true)
            }

            val notification: Notification = builder.build()
            manager.notify(2, notification)
        } else if (expirationDate2 == 1L) {
            builder.apply {
                // 노티 제목
                setContentTitle("aboutLens")
                setContentText("렌즈를 바꿔주세요.")
//            setContentIntent(pendingIntent)
                setSmallIcon(R.drawable.noti_icon)
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
}