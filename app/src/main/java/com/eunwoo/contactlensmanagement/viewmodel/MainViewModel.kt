package com.eunwoo.contactlensmanagement.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eunwoo.contactlensmanagement.R
import com.eunwoo.contactlensmanagement.enums.PageType
import java.lang.IllegalArgumentException

// 뷰모델을 사용할 때 주의점
// 뷰모델에는 반드시
// 액티비티나 프래그먼트(Fragment), 컨텍스트(Context)에 대한 참조를 저장하면 안됩니다.
// 뷰(Views)도 컨텍스트를 가지기 때문에 뷰에 대한 참조 역시 저장하면 안됩니다.
// 예외로 ApplicationContext는 사용해도됨

class MainViewModel: ViewModel(){
    // Create a LiveData with a String

    val signal: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    private val _placeName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val placeName: LiveData<String> = _placeName

    private val _addressName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val addressName: LiveData<String> = _addressName

    private val _phone: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val phone: LiveData<String> = _phone

    private val _shopURL: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val shopURL: LiveData<String> = _shopURL

    companion object {
        const val TAG: String = "MainViewModel"
    }
    // Rest of the ViewModel...



}