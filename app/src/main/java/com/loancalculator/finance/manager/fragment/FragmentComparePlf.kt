package com.loancalculator.finance.manager.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.loancalculator.finance.manager.activity.PlfRootActivity
import com.loancalculator.finance.manager.activity.compare.PlfComparePersonalLoanActivity
import com.loancalculator.finance.manager.databinding.FragmentComparePlfBinding
import com.loancalculator.finance.manager.setSafeListener

class FragmentComparePlf : RootPlfFragment<FragmentComparePlfBinding>() {
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
        mPlfBinding.llPersonalLoan.setSafeListener {
            startActivity(Intent(rootActivity, PlfComparePersonalLoanActivity::class.java).apply {
                putExtra("compareType", "personalLoan")
            })
        }
        mPlfBinding.llBusinessLoan.setSafeListener {
            startActivity(Intent(rootActivity, PlfComparePersonalLoanActivity::class.java).apply {
                putExtra("compareType", "businessLoan")
            })
        }
        mPlfBinding.llMortgages.setSafeListener {
            startActivity(Intent(rootActivity, PlfComparePersonalLoanActivity::class.java).apply {
                putExtra("compareType", "mortgages")
            })
        }
        mPlfBinding.llAutoLoanLoan.setSafeListener {
            startActivity(Intent(rootActivity, PlfComparePersonalLoanActivity::class.java).apply {
                putExtra("compareType", "autoLoan")
            })
        }
    }

    override fun getLayoutValue(): FragmentComparePlfBinding {
        return FragmentComparePlfBinding.inflate(layoutInflater)
    }
}