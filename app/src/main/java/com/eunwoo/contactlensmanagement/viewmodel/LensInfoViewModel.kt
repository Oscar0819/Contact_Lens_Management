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

    private val currentName: String = ""
    private val currentLeftSight: Double = 0.0
    private val currentRightSight: Double = 0.0
    private val currentProductName: String = ""
    private val currentInitialDate: String = ""
    private val currentExpirationDate: String = ""
    private val currentPushCheck: Boolean = false
    private val currentMemo: String = ""


    fun getLensInfo(): LiveData<LensInfo> {
        return lensInfo
    }

    fun updateLensInfo(info: LensInfo) {
        lensInfo.value = info
    }


    // Rest of the ViewModel...
}