package com.eunwoo.contactlensmanagement.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebViewClient
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.eunwoo.contactlensmanagement.Place
import com.eunwoo.contactlensmanagement.R
import com.eunwoo.contactlensmanagement.databinding.LayoutCollapseBinding
import com.eunwoo.contactlensmanagement.databinding.LayoutExpandBinding
import com.eunwoo.contactlensmanagement.viewmodel.MainViewModel
import kr.co.prnd.persistbottomsheetfragment.PersistBottomSheetFragment

class MapPersistBottomFragment :
    PersistBottomSheetFragment<LayoutCollapseBinding, LayoutExpandBinding>(
        R.layout.layout_collapse,
        R.layout.layout_expand
    ) {
    val viewModel: MainViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("BF", "onViewCreated")
        viewModel.id.observe(this, Observer {
            collapseBinding.apply {
                colPlaceName.text = viewModel.placeName.value
                colAddressName.text = viewModel.addressName.value
                colDistance.text = viewModel.distance.value
            }
            expandBinding.apply {
                expPlaceName.text = viewModel.placeName.value
                expAddressName.text = viewModel.addressName.value
                expPhone.text = viewModel.phone.value
                expDistance.text = viewModel.distance.value
            }
        })

        collapseBinding.viewSelect.setOnClickListener {
            expand()
        }
    }
    companion object {
        private val TAG = MapPersistBottomFragment::class.simpleName
        fun show(
            fragmentManager: FragmentManager,
            @IdRes containerViewId: Int,
        ): MapPersistBottomFragment =
            fragmentManager.findFragmentByTag(TAG) as? MapPersistBottomFragment
                ?: MapPersistBottomFragment().apply {
                    fragmentManager.beginTransaction()
                        .replace(containerViewId, this, TAG)
                        .commitAllowingStateLoss()
                }
    }

//    fun setPlaceData(place: Place) {
//        var distanceMeter: String = place.distance
//        if (distanceMeter.toInt() < 1000) {
//            distanceMeter = (distanceMeter.toInt() / 1000).toString() + "km"
//        } else {
//            distanceMeter += "m"
//        }
//        collapseBinding.apply {
//            colAddressName.text = place.address_name
//            colPlaceName.text = place.place_name
//            colDistance.text = distanceMeter
//        }
//        expandBinding.apply {
//            expPlaceName.text = place.place_name
//            expAddressName.text = place.address_name
//            expPhone.text = place.phone
//            expDistance.text = distanceMeter
//        }
//    }



}