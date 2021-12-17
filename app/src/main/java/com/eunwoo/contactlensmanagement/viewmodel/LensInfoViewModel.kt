package com.eunwoo.contactlensmanagement.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.eunwoo.contactlensmanagement.database.Lens
import com.eunwoo.contactlensmanagement.database.LensDatabase
import com.eunwoo.contactlensmanagement.dataclass.LensInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


// 뷰모델을 사용할 때 주의점
// 뷰모델에는 반드시
// 액티비티나 프래그먼트(Fragment), 컨텍스트(Context)에 대한 참조를 저장하면 안됩니다.
// 뷰(Views)도 컨텍스트를 가지기 때문에 뷰에 대한 참조 역시 저장하면 안됩니다.
// 예외로 ApplicationContext는 사용해도됨

class LensInfoViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val TAG: String = "LensInfoViewModel"
    }
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

        }
    }
    fun save() {
        if (nameEditTextContent.toString().isNotEmpty() and
                initialdContent.toString().isNotEmpty() and
                expirationdContent.toString().isNotEmpty()) {
            CoroutineScope(Default).launch {
                db.lensDao().insert(Lens(null,
                    nameEditTextContent.value.toString(),
                    leftsContent.value.toString().toDouble(),
                    rightsContent.value.toString().toDouble(),
                    productnContent.value.toString(),
                    initialdContent.value.toString(),
                    expirationdContent.value.toString(),
                    pushCheck.value,
                    memoContent.value.toString()))
            }
        }
    }
    // Rest of the ViewModel...
}