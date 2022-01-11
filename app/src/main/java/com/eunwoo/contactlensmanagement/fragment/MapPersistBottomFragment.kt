package com.eunwoo.contactlensmanagement.fragment

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import com.eunwoo.contactlensmanagement.R
import com.eunwoo.contactlensmanagement.databinding.LayoutCollapseBinding
import com.eunwoo.contactlensmanagement.databinding.LayoutExpandBinding
import kr.co.prnd.persistbottomsheetfragment.PersistBottomSheetFragment

class MapPersistBottomFragment :
    PersistBottomSheetFragment<LayoutCollapseBinding, LayoutExpandBinding>(
        R.layout.layout_collapse,
        R.layout.layout_expand
    ) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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