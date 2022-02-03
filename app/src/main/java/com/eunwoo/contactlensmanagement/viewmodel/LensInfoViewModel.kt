package com.eunwoo.contactlensmanagement.viewmodel

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.eunwoo.contactlensmanagement.receiver.AlarmReceiver
import com.eunwoo.contactlensmanagement.database.Lens
import com.eunwoo.contactlensmanagement.database.LensDatabase
import com.eunwoo.contactlensmanagement.dataclass.LensInfo
import com.eunwoo.contactlensmanagement.repo.LensInfoRepository
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

//    private lateinit var db: LensDatabase

    private val lensInfoRepository = LensInfoRepository(application)

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

    fun setIndex(index: Long) {
        Log.d(TAG, "!!!!! index : $index")
        _index = index
    }

    fun setId(i: Long) {
        _id = i
    }

    // 무결성을 위한 getter
    val currentCnt: Int
        get() = _currentCnt

    init {
//        db = LensDatabase.getInstance(application)
    }

    // Rest of the ViewModel...

    fun initModify() {
        // 처음 액티비티가 실행 되었을 때 데이터를 셋
        if (currentCnt == 1) {
            CoroutineScope(Dispatchers.Default).launch {
                lensInfoRepository.getList(index.toInt())
                val lensData: Lens = lensInfoRepository.getList(index.toInt())// db.lensDao().getList()[index.toInt()]
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
        if (!nameEditTextContent.value.isNullOrEmpty() and
            !initialdContent.value.isNullOrEmpty() and
            !expirationdContent.value.isNullOrEmpty()) {
            CoroutineScope(IO).launch {
                val lens: Lens = Lens(
                    id,
                    nameEditTextContent.value.toString(),
                    if (leftsContent.value.isNullOrEmpty()) 0.0 else leftsContent.value.toString()
                        .toDouble(),
                    if (rightsContent.value.isNullOrEmpty()) 0.0 else rightsContent.value.toString()
                        .toDouble(),
                    if (productnContent.value.isNullOrEmpty()) "" else productnContent.value.toString(),
                    initialdContent.value.toString(),
                    expirationdContent.value.toString(),
                    if (dateTime().toInt() < 0) false else pushCheck.value == true, // dateTime이 0이하면 false반환 그게 아니면 pushCheck를 확인 후 값 반환..
                    if (memoContent.value == null) "" else memoContent.value.toString(),
                    dateTime()
                )
                lensInfoRepository.modify(lens)
//                db.lensDao().update(lens)
            }
        } else {
            Toast.makeText(context, "이름, 착용 시작일, 착용 기간은 반드시 입력해야합니다.", Toast.LENGTH_LONG).show()
        }
    }

    fun save() {
        if (!nameEditTextContent.value.isNullOrEmpty() and
                !initialdContent.value.isNullOrEmpty() and
                !expirationdContent.value.isNullOrEmpty()) {
            CoroutineScope(Default).launch {
                launch {
                    val lens = Lens(null,
                        nameEditTextContent.value.toString(),
                        if (leftsContent.value.isNullOrEmpty()) 0.0 else leftsContent.value.toString().toDouble(),
                        if (rightsContent.value.isNullOrEmpty()) 0.0 else rightsContent.value.toString().toDouble(),
                        if (productnContent.value.isNullOrEmpty()) "" else productnContent.value.toString(),
                        initialdContent.value.toString(),
                        expirationdContent.value.toString(),
                        if (dateTime().toInt() == 0) false else pushCheck.value == true, // dateTime이 0이하면 false반환 그게 아니면 pushCheck를 확인 후 값 반환..
                        if (memoContent.value == null) "" else memoContent.value.toString(),
                        dateTime()
                    )
                    lensInfoRepository.insert(lens)
                }
            }
        } else {
            Toast.makeText(context, "이름, 착용 시작일, 착용 기간은 반드시 입력해야합니다.", Toast.LENGTH_LONG).show()
        }
    }

    fun delete() {
        CoroutineScope(IO).launch {
            val lens = lensInfoRepository.getList(_index.toInt())
            lensInfoRepository.delete(lens)
//            db.lensDao().delete(db.lensDao().getList()[_index.toInt()])

            val size =  lensInfoRepository.getSize()// db.lensDao().getList().size
            // 위 코드로인해 사이즈가 감소한 상태의 사이즈를 가져옴.. 그래서 0부터 시작하는 인덱스와 맞춰서 계산
            if (_index.toInt() != size) { // 삭제한 인덱스 값이 List의 값과 같은지 확인했을 때 같으면
                for (i in index.toInt()..size) {// index = 1, size = 4 (..연산자는 1~4 until은 1~3)
                    val lens: Lens = lensInfoRepository.getList(i)// db.lensDao().getList()[i]
                    if (i == size) {
                        lensInfoRepository.delete(lens)// db.lensDao().delete(lens)
                    }
                    lens.id = lens.id?.minus(1)
                    // 동일한 id를 넣었을 때 덮어씌우기 설정되어있음
                    lensInfoRepository.insert(lens)
                    // db.lensDao().insert(lens)
                }
            } else {
                // 같은 경우엔 마지막 인덱스이므로 정렬을 할 필요가 없음
            }
        }
    }

    // viewModel 은 액티비티나 프래그먼트의 context를 참조하지 않게 구현하는것을 지향해야한다

    private fun dateTime(): Long {
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

        calDateDays = abs(calDateDays)
        Log.d(TAG, "calDateDays abs : ${calDateDays}")

        // 두 날짜의 차이...
        var datetime: Long = (expirationDate.toLong() - calDateDays)
        // 차이값이 -가 될 경우 0으로 셋
        if (datetime < 0) {
            datetime = 0
        }
        Log.d(TAG, "datatime : ${datetime}")

        // triggerTime
        val dDay: Long = datetime

        return dDay
    }

}