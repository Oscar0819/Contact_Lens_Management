package com.eunwoo.contactlensmanagement.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eunwoo.contactlensmanagement.dataclass.LensInfo


// 뷰모델을 사용할 때 주의점
// 뷰모델에는 반드시
// 액티비티나 프래그먼트(Fragment), 컨텍스트(Context)에 대한 참조를 저장하면 안됩니다.
// 뷰(Views)도 컨텍스트를 가지기 때문에 뷰에 대한 참조 역시 저장하면 안됩니다.
// 예외로 ApplicationContext는 사용해도됨

class LensInfoViewModel: ViewModel() {

    companion object {
        const val TAG: String = "LensInfoViewModel"
    }

    private val lensInfo: MutableLiveData<LensInfo> by lazy {
        MutableLiveData<LensInfo>()
    }
    private val currentName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    private val currentLeftSight: MutableLiveData<Double> by lazy {
        MutableLiveData<Double>()
    }
    private val currentRightSight: MutableLiveData<Double> by lazy {
        MutableLiveData<Double>()
    }
    private val currentProductName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    private val currentInitialDate: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    private val currentExpirationDate: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    private val currentPushCheck: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    private val currentMemo: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun getLensInfo(): LiveData<LensInfo> {
        return lensInfo
    }

    fun updateLensInfo(info: LensInfo) {
        currentName.value = info.name
        currentLeftSight.value = info.leftSight
        currentRightSight.value = info.rightSight
        currentProductName.value = info.productName
        currentInitialDate.value = info.initialDate
        currentExpirationDate.value = info.expirationDate
        currentPushCheck.value = info.pushCheck
        currentMemo.value = info.memo
    }


    // Rest of the ViewModel...
}