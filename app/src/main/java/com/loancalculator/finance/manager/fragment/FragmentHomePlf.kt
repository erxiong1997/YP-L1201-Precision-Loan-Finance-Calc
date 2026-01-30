package com.loancalculator.finance.manager.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.loancalculator.finance.manager.activity.PlfRootActivity
import com.loancalculator.finance.manager.activity.calc.PlfPersonalLoanActivity
import com.loancalculator.finance.manager.databinding.FragmentHomePlfBinding
import com.loancalculator.finance.manager.setSafeListener

class FragmentHomePlf : RootPlfFragment<FragmentHomePlfBinding>() {
    companion object {
        private const val TIL_PAGE = "til_page"

        fun newInstance(page: Int): FragmentHomePlf {
            val fragment = FragmentHomePlf()
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
        mPlfBinding.tvPersonalLoan.setSafeListener {
            startActivity(Intent(rootActivity, PlfPersonalLoanActivity::class.java))
        }
    }

    override fun getLayoutValue(): FragmentHomePlfBinding {
        return FragmentHomePlfBinding.inflate(layoutInflater)
    }
}