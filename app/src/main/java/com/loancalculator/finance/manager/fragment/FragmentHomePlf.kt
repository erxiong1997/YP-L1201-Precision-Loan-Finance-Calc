package com.loancalculator.finance.manager.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.loancalculator.finance.manager.activity.PlfRootActivity
import com.loancalculator.finance.manager.activity.calc.PlfBusinessLoanActivity
import com.loancalculator.finance.manager.activity.calc.PlfPersonalLoanActivity
import com.loancalculator.finance.manager.activity.compare.PlfHistoryCalculateActivity
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
        mPlfBinding.tvBusinessLoan.setSafeListener {
            startActivity(Intent(rootActivity, PlfBusinessLoanActivity::class.java))
        }
        mPlfBinding.tvMore.setSafeListener {
            startActivity(Intent(rootActivity, PlfHistoryCalculateActivity::class.java))
        }
        mPlfBinding.ivMore.setSafeListener {
            startActivity(Intent(rootActivity, PlfHistoryCalculateActivity::class.java))
        }
    }

    override fun getLayoutValue(): FragmentHomePlfBinding {
        return FragmentHomePlfBinding.inflate(layoutInflater)
    }
}