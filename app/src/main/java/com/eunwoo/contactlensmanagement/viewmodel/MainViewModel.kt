package com.eunwoo.contactlensmanagement.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eunwoo.contactlensmanagement.Place
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

    private val _id:  MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val id: LiveData<String>
        get() = _id

    private val _placeName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val placeName: LiveData<String>
        get() = _placeName

    private val _addressName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val addressName: LiveData<String>
        get() = _addressName

    private val _phone: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val phone: LiveData<String>
        get() = _phone

    private val _distance: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val distance: LiveData<String>
        get() = _distance

    companion object {
        const val TAG: String = "MainViewModel"
    }
    // Rest of the ViewModel...

    fun setPlaceData(place: Place) {
        var phoneCheck: String = place.phone
        var distanceMeter: String = place.distance
        if (phoneCheck == "") {
            phoneCheck = "연락처가 등록되지 않았습니다."
        }

        if (distanceMeter.toInt() >= 1000) {
            distanceMeter = (distanceMeter.toInt() / 1000).toString() + "km"

        } else {
            distanceMeter += "m"
        }
        _placeName.value = place.place_name
        _addressName.value = place.address_name
        _phone.value = phoneCheck
        _distance.value = distanceMeter
        _id.value = place.id
    }


}