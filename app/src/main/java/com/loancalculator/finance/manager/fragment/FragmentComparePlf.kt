package com.loancalculator.finance.manager.fragment

import android.os.Bundle
import android.view.View
import com.loancalculator.finance.manager.activity.PlfRootActivity
import com.loancalculator.finance.manager.databinding.FragmentComparePlfBinding

class FragmentComparePlf: RootPlfFragment<FragmentComparePlfBinding>() {
    companion object {
        private const val TIL_PAGE = "til_page"

        fun newInstance(page: Int): FragmentComparePlf {
            val fragment = FragmentComparePlf()
            fragment.arguments = Bundle().apply {
                putInt(TIL_PAGE, page)
            }
            return fragment
        }
    }
    override fun startCreateContent(
        rootActivity: PlfRootActivity,
        view: View,
        bundle: Bundle?
    ) {
        
    }

    override fun getLayoutValue(): FragmentComparePlfBinding {
        return FragmentComparePlfBinding.inflate(layoutInflater)
    }
}