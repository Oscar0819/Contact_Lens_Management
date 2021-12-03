package com.eunwoo.contactlensmanagement.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// 뷰모델을 사용할 때 주의점
// 뷰모델에는 반드시
// 액티비티나 프래그먼트(Fragment), 컨텍스트(Context)에 대한 참조를 저장하면 안됩니다.
// 뷰(Views)도 컨텍스트를 가지기 때문에 뷰에 대한 참조 역시 저장하면 안됩니다.
// 예외로 ApplicationContext는 사용해도됨

class MainViewModel:  ViewModel(){
    // Create a LiveData with a String
    val currentName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    companion object {
        const val TAG: String = "MainViewModel"
    }
    // Rest of the ViewModel...


}