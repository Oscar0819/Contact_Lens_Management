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
        collapseBinding.vm = viewModel
        expandBinding.vm = viewModel

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

}