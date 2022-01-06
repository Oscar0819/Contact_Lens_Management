package com.eunwoo.contactlensmanagement.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import com.eunwoo.contactlensmanagement.R
import com.eunwoo.contactlensmanagement.databinding.ActivitySplashBinding
import com.eunwoo.contactlensmanagement.databinding.MapBottomSheetDialogFrargmentBinding
import com.eunwoo.contactlensmanagement.databinding.MapFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MapBottomDialogFragment() : BottomSheetDialogFragment() {
    lateinit var binding: MapBottomSheetDialogFrargmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MapBottomSheetDialogFrargmentBinding.inflate(inflater, container, false)

//        val contextThemeWrapper: Context = ContextThemeWrapper(activity, R.style.AppModalStyle)
//
//        val localInflater: LayoutInflater = inflater.cloneInContext(contextThemeWrapper)
//
//        return localInflater.inflate(R.layout.map_bottom_sheet_dialog_frargment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}