package com.eunwoo.contactlensmanagement.viewmodel

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.util.Log
import androidx.lifecycle.*
import androidx.work.impl.background.systemalarm.SystemAlarmService
import com.eunwoo.contactlensmanagement.AlarmReceiver
import com.eunwoo.contactlensmanagement.database.Lens
import com.eunwoo.contactlensmanagement.database.LensDatabase
import com.eunwoo.contactlensmanagement.dataclass.LensInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs


// 뷰모델을 사용할 때 주의점
// 뷰모델에는 반드시
// 액티비티나 프래그먼트(Fragment), 컨텍스트(Context)에 대한 참조를 저장하면 안됩니다.
// 뷰(Views)도 컨텍스트를 가지기 때문에 뷰에 대한 참조 역시 저장하면 안됩니다.
// 예외로 ApplicationContext는 사용해도됨

class LensInfoViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val TAG: String = "LensInfoViewModel"
    }
    private val context = getApplication<Application>().applicationContext
    // 화면이 돌아간 횟수...
    private var _currentCnt: Int = 0

    // 이전 액티비티에서 받은 데이터
    // code는 저장, 수정 관련 신호 0 = 저장, 1 = 수정
    // 기본 디폴트 값
    private var _code: Int = -1
    val code: Int
        get() = _code
    private var _index: Long = -1
    val index: Long
        get() = _index
    private var _id: Long = -1
    val id: Long
        get() = _id

    private lateinit var db: LensDatabase

    private val lensInfo: MutableLiveData<LensInfo> by lazy {
        MutableLiveData<LensInfo>()
    }

    val nameEditTextContent: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val leftsContent: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val rightsContent: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val productnContent: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val initialdContent: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val expirationdContent: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val pushCheck: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val memoContent: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun increaseCnt() {
        _currentCnt++
    }

    // set Code, Index, Id
    fun setCode(c: Int) {
        _code = c
    }

    fun setIndex(indx: Long) {
        _index = indx
    }

    fun setId(i: Long) {
        _id = i
    }

    // 무결성을 위한 getter
    val currentCnt: Int
        get() = _currentCnt

    init {
        db = LensDatabase.getInstance(application)
    }

    // Rest of the ViewModel...

    fun initModify() {
        // 처음 액티비티가 실행 되었을 때 데이터를 셋
        if (currentCnt == 1) {
            CoroutineScope(Dispatchers.Default).launch {
                val lensData: Lens = db.lensDao().getList()[index.toInt()]
                var lensInfo: LensInfo = LensInfo(lensData.name!!,
                    lensData.leftSight!!,
                    lensData.rightSight!!,
                    lensData.productName!!,
                    lensData.initialDate!!,
                    lensData.expirationDate!!,
                    lensData.pushCheck!!,
                    lensData.memo!!)
                CoroutineScope(Dispatchers.Main).launch {
                    nameEditTextContent.value = lensInfo.name
                    leftsContent.value = lensInfo.leftSight.toString()
                    rightsContent.value = lensInfo.rightSight.toString()
                    productnContent.value = lensInfo.productName
                    initialdContent.value = lensInfo.initialDate
                    expirationdContent.value = lensInfo.expirationDate
                    pushCheck.value = lensInfo.pushCheck
                    memoContent.value = lensInfo.memo
                }
            }
        }
    }

    fun modify() {
        CoroutineScope(IO).launch {
            val lens: Lens = Lens(
                id,
                nameEditTextContent.value.toString(),
                leftsContent.value.toString().toDouble(),
                rightsContent.value.toString().toDouble(),
                productnContent.value.toString(),
                initialdContent.value.toString(),
                expirationdContent.value.toString(),
                pushCheck.value,
                memoContent.value.toString()
            )
            db.lensDao().update(lens)

            // 푸시 체크 후 알람 등록 및 취소
            if (pushCheck.value == false) {
                // 알람 취소 코드.
                val intent: Intent = Intent(context, AlarmReceiver::class.java)
                val sender: PendingIntent = PendingIntent.getBroadcast(context,
                    _id.toInt(),
                    intent,
                    PendingIntent.FLAG_MUTABLE)
                if (sender != null) {
                    val am: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    am.cancel(sender)
                    sender.cancel()
                }
            } else { // true : id를 통해 알람을 확인 후 알람이 있으면 변화x 알람이 없으면 새로 추가.
                val intent: Intent = Intent(context, AlarmReceiver::class.java)
                val sender: PendingIntent? = PendingIntent.getBroadcast(context,
                    _id.toInt(),
                    intent,
                    PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_NO_CREATE)
                if (sender == null) { // sender가 null인 경우 아직 알람이 생성되지않아서 새로 생성해줘야함
                    setAlarm(_id.toInt())
                }
            }
        }
    }

    fun save() {
        if (nameEditTextContent.toString().isNotEmpty() and
                initialdContent.toString().isNotEmpty() and
                expirationdContent.toString().isNotEmpty()) {
            CoroutineScope(Default).launch {
                launch {
                    db.lensDao().insert(Lens(null,
                        nameEditTextContent.value.toString(),
                        leftsContent.value.toString().toDouble(),
                        rightsContent.value.toString().toDouble(),
                        productnContent.value.toString(),
                        initialdContent.value.toString(),
                        expirationdContent.value.toString(),
                        pushCheck.value,
                        memoContent.value.toString()))

                    // 데이터베이스 크기를 가져와서 추가될 데이터의 id를 구하는 코드...
                    val id = db.lensDao().getList().size

                    setAlarm(id)
                }
            }
        }
    }


    // viewModel 은 액티비티나 프래그먼트의 context를 참조하지 않게 구현하는것을 지향해야한다
    private fun setAlarm(requestCode: Int) {
        if (pushCheck.value == true) {
            Log.d(TAG, "setAlarm Called ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())}")
            val notificationManager: NotificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            val today = GregorianCalendar()

            val selectDate: String = initialdContent.value.toString()
            val todayDate: String = "${today.get(Calendar.YEAR)}-${today.get(Calendar.MONTH) + 1}-${today.get(Calendar.DATE)}"
            val expirationDate: String = expirationdContent.value.toString()

            Log.d(TAG, "선택 날짜 : ${selectDate}")
            Log.d(TAG, "오늘 날짜 : ${todayDate}")
            Log.d(TAG, "만료 날짜 : ${expirationDate}")

            val dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

            val FirstDate: Date = try {
                dateFormat.parse(todayDate)
            } catch (e: ParseException) {
                Date()
            }
            Log.d(TAG, "FirstDate : ${FirstDate}")
            val SecondDate: Date = try {
                dateFormat.parse(selectDate)
            } catch (e: ParseException) {
                Date()
            }
            Log.d(TAG, "SecondDate : ${SecondDate}")

            val calDate: Long = FirstDate.time - SecondDate.time
            Log.d(TAG, "calDate : ${calDate}")

            var calDateDays: Long = calDate / (24*60*60*1000)
            Log.d(TAG, "calDateDays : ${calDateDays}")

            // 두 날짜의 차이...
            calDateDays = abs(calDateDays)
            Log.d(TAG, "두 날짜의 차이 : ${calDateDays}")

            // ----------------
            val datetime: Long = (expirationDate.toLong() - calDateDays)
            Log.d(TAG, "datatime : ${datetime}")

            // triggerTime
            val beforeDay: Long = datetime - 1
            val dDay: Long = datetime

            // 24*60*60*1000(각 시간값에 따른 차이점) 을 나눠주면 일수
            val triggerTime: Long = System.currentTimeMillis() + (datetime * 24 * 60 * 60 * 1000)
            Log.d(TAG, "triggerTime : ${triggerTime}")
            //-----------------

            val receiverIntent = Intent(context, AlarmReceiver::class.java)
            // requestCode를 통해 인텐트를 식별. 취소할 때 참고..
            // requestCode는 각 아이템의 id 값과 동일
            val pendingIntent = PendingIntent.getBroadcast(
                context, requestCode, receiverIntent, PendingIntent.FLAG_MUTABLE
            )

            alarmManager.set(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 10 * 1000,
                pendingIntent
            )
        }

    }
}