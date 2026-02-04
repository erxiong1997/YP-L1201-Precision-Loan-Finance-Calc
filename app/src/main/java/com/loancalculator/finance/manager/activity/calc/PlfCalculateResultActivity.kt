package com.loancalculator.finance.manager.activity.calc

import android.annotation.SuppressLint
import android.content.Intent
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.activity.PlfMainToolActivity
import com.loancalculator.finance.manager.databinding.ActivityCalculateResultPlfBinding
import com.loancalculator.finance.manager.formatToSmartString
import com.loancalculator.finance.manager.setSafeListener
import com.loancalculator.finance.manager.utils.TimeDatePlfUtils
import com.loancalculator.finance.manager.utils.ToolsLoanMonthDetailUtils.mDataPersonalLoanPlf
import com.loancalculator.finance.manager.utils.value.ParamsLtdUtils.mDataCurrencyUnitPlf
import java.math.BigDecimal

class PlfCalculateResultActivity : PlfBindingActivity<ActivityCalculateResultPlfBinding>(
    mBarTextWhite = false
) {
    @SuppressLint("SetTextI18n")
    override fun beginViewAndDoLtd() {
        mPlcBinding.topSetPlf.tvTitleAll.text = getString(R.string.plf_calculate_result)
        mDataPersonalLoanPlf?.let { data ->
            val symbol = mDataCurrencyUnitPlf?.currencySymbol ?: "$"
            mPlcBinding.tvLoanAmount2.text =
                "${data.loanAmount}$symbol"
            mPlcBinding.tvIntersetRate2.text = "${data.interestRate}%"
            mPlcBinding.tvLoanTerm2.text = "${data.loanTerm} ${getString(R.string.plf_month)}"
            mPlcBinding.tvStartDate2.text = TimeDatePlfUtils.getTimeDateOnePlf(data.startDate)
            mPlcBinding.tvMonthPayment2.text = "${data.monthlyPayment}${symbol}"

            val totalPay = data.monthlyPayment * data.loanTerm

            mPlcBinding.tvTotalPayment2.text = totalPay.formatToSmartString()
            mPlcBinding.tvTotalInterestPayable2.text =
                (totalPay - data.loanAmount).formatToSmartString()
            mPlcBinding.tvPayingOffDate2.text =
                TimeDatePlfUtils.getTimeDateOnePlf(
                    TimeDatePlfUtils.getOverDatePlf(
                        data.loanTerm.toLong(), data.startDate
                    )
                )
        }

        mPlcBinding.tvAddCompareList.setSafeListener {

        }
        mPlcBinding.tvAmortizationTable.setSafeListener {

        }
        mPlcBinding.tvShare.setSafeListener {

        }
        mPlcBinding.tvGoHome.setSafeListener {
            startActivity(Intent(this, PlfMainToolActivity::class.java))
        }
    }

    override fun getLayoutValue(): ActivityCalculateResultPlfBinding {
        return ActivityCalculateResultPlfBinding.inflate(layoutInflater)
    }
}