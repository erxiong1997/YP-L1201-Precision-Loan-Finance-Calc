package com.loancalculator.finance.manager.fragment

import android.os.Bundle
import android.view.View
import com.loancalculator.finance.manager.activity.PlfRootActivity
import com.loancalculator.finance.manager.databinding.FragmentMinePlfBinding

class FragmentMinePlf : RootPlfFragment<FragmentMinePlfBinding>() {
    companion object {
        private const val TIL_PAGE = "til_page"

        fun newInstance(page: Int): FragmentMinePlf {
            val fragment = FragmentMinePlf()
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

    override fun getLayoutValue(): FragmentMinePlfBinding {
        return FragmentMinePlfBinding.inflate(layoutInflater)
    }
}