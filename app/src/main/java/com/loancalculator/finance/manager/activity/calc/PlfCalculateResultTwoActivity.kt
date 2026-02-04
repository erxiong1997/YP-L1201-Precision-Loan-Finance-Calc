package com.loancalculator.finance.manager.activity.calc

import android.annotation.SuppressLint
import android.content.Intent
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.activity.PlfMainToolActivity
import com.loancalculator.finance.manager.databinding.ActivityCalculateResultPlfBinding
import com.loancalculator.finance.manager.databinding.ActivityCalculateResultTwoPlfBinding
import com.loancalculator.finance.manager.formatToSmartString
import com.loancalculator.finance.manager.room.mPlfLoanRoom
import com.loancalculator.finance.manager.setSafeListener
import com.loancalculator.finance.manager.utils.TimeDatePlfUtils
import com.loancalculator.finance.manager.utils.ToolsLoanMonthDetailUtils.mDataPersonalLoanPlf
import com.loancalculator.finance.manager.utils.value.ParamsLtdUtils.mDataCurrencyUnitPlf

class PlfCalculateResultTwoActivity : PlfBindingActivity<ActivityCalculateResultTwoPlfBinding>(
    mBarTextWhite = false
) {
    private var mTilPersonalLoanDao = mPlfLoanRoom.mTilPersonalLoanDao()

    @SuppressLint("SetTextI18n")
    override fun beginViewAndDoLtd() {
        mPlcBinding.topSetPlf.tvTitleAll.text = getString(R.string.plf_calculate_result)
        mDataPersonalLoanPlf?.let { data ->
            mPlcBinding.tvLoanAmount2.text =
                "${data.loanAmount}${data.currencySymbol}"
            mPlcBinding.tvIntersetRate2.text = "${data.interestRate}%"
            mPlcBinding.tvLoanTerm2.text = "${data.loanTerm} ${getString(R.string.plf_month)}"
            mPlcBinding.tvStartDate2.text = TimeDatePlfUtils.getTimeDateOnePlf(data.startDate)
            mPlcBinding.tvMonthPayment2.text = "${data.monthlyPayment}${data.currencySymbol}"

            val dataIndexId = mTilPersonalLoanDao.insertContent(data)
            if (dataIndexId != -1L) {
                data.dataIndexId = dataIndexId
            } else {
                data.dataIndexId = -1L
            }
        }

        mPlcBinding.tvAddCompareList.setSafeListener {
            mDataPersonalLoanPlf?.let {
                if (it.dataIndexId > -1L) {
                    it.addAmortizationTable = "yes"
                    mTilPersonalLoanDao.updateContent(it)
                }
            }
        }
        mPlcBinding.tvShare.setSafeListener {

        }
        mPlcBinding.tvGoHome.setSafeListener {
            startActivity(Intent(this, PlfMainToolActivity::class.java))
        }
    }

    override fun getLayoutValue(): ActivityCalculateResultTwoPlfBinding {
        return ActivityCalculateResultTwoPlfBinding.inflate(layoutInflater)
    }
}