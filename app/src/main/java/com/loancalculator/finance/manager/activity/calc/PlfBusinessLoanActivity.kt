package com.loancalculator.finance.manager.activity.calc

import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toDrawable
import coil.load
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.activity.other.PlfCurrencyUnitActivity
import com.loancalculator.finance.manager.data.DataPersonalLoanPlf
import com.loancalculator.finance.manager.databinding.ActivityBusinessLoanPlfBinding
import com.loancalculator.finance.manager.plfPxDp
import com.loancalculator.finance.manager.setSafeListener
import com.loancalculator.finance.manager.utils.DealRecentPlfUtils
import com.loancalculator.finance.manager.utils.ToolsLoanMonthDetailUtils
import com.loancalculator.finance.manager.utils.ToolsLoanMonthDetailUtils.mDataPersonalLoanPlf
import com.loancalculator.finance.manager.utils.dialog.DialogExitToHomePlf
import com.loancalculator.finance.manager.utils.value.LoanTypePlf
import com.loancalculator.finance.manager.utils.value.ParamsPlfUtils.mDataCurrencyUnitPlf

class PlfBusinessLoanActivity : PlfBindingActivity<ActivityBusinessLoanPlfBinding>() {
    private val mCurrencySelectLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                mPlcBinding.tvCurrencySymbol.text = mDataCurrencyUnitPlf?.currencySymbol
                mPlcBinding.ivCurrencyFlag.load(mDataCurrencyUnitPlf?.currencyDrawable)
                mPlcBinding.tvCurrencyName.text = mDataCurrencyUnitPlf?.currencyUnit
                DealRecentPlfUtils.addCurrencyUnitRecent(mDataCurrencyUnitPlf)
            }
        }

    override fun doBackPressed() {
        DialogExitToHomePlf(this) {
            super.doBackPressed()
        }.show()
    }

    //month year
    private var mMonthYear = "month"
    override fun beginViewAndDoPlf() {
        mPlcBinding.topSetPlf.tvTitleAll.text = getString(R.string.plf_business_loan)

        mDataCurrencyUnitPlf = DealRecentPlfUtils.getCurrencyUnitRecent()
        mDataCurrencyUnitPlf?.let {
            mPlcBinding.tvCurrencySymbol.text = mDataCurrencyUnitPlf?.currencySymbol
            mPlcBinding.ivCurrencyFlag.load(mDataCurrencyUnitPlf?.currencyDrawable)
            mPlcBinding.tvCurrencyName.text = mDataCurrencyUnitPlf?.currencyUnit
        }
        mPlcBinding.llCurrencyUnit.setSafeListener {
            mCurrencySelectLauncher.launch(Intent(this, PlfCurrencyUnitActivity::class.java))
        }
        mPlcBinding.ivDeleteLoanAmount.setSafeListener {
            mPlcBinding.etLoanAmount.setText(null)
        }
        mPlcBinding.tvMonthYear.setSafeListener {
            val popView = PopupWindow(this).apply {
                width = 84.plfPxDp()
                height = ViewGroup.LayoutParams.WRAP_CONTENT
                val view = LayoutInflater.from(this@PlfBusinessLoanActivity)
                    .inflate(R.layout.item_pop_month_year_plf, null, false)
                val month = view.findViewById<TextView>(R.id.tvMonth)
                month.setOnClickListener {
                    mMonthYear = "month"
                    dismiss()
                    mPlcBinding.tvMonthYear.text = getString(R.string.plf_month)
                }
                val year = view.findViewById<TextView>(R.id.tvYear)
                year.setOnClickListener {
                    mMonthYear = "year"
                    dismiss()
                    mPlcBinding.tvMonthYear.text = getString(R.string.plf_year)
                }
                if (mMonthYear == "month") {
                    month.isSelected = true
                    year.isSelected = false
                } else {
                    month.isSelected = false
                    year.isSelected = true
                }
                contentView = view
                isFocusable = true
                setBackgroundDrawable(0x00000000.toDrawable())
                isOutsideTouchable = true
            }
            popView.showAsDropDown(it, 0, 0, Gravity.CENTER)
        }
        mPlcBinding.tvCalculate.setSafeListener {
            calculatorResult()
        }
        mPlcBinding.tvResetFields.setSafeListener {
            clearAllValue()
        }
    }

    /**
     * 第1个月利息 = 当前剩余本金 × 月利率 = 120000 × 0.01 = 1,200 元
     * 第1个月本金 = 月还款 - 当月利息 = 10,661.85 - 1,200 = 9,461.85 元
     * 剩余本金 = 上期剩余 - 本期本金 = 120000 - 9,461.85 = 110,538.15 元
     * 第2个月利息 = 110,538.15 × 0.01 ≈ 1,105.38 元
     * 第2个月本金 = 10,661.85 - 1,105.38 ≈ 9,556.47 元
     * 剩余本金 = 110,538.15 - 9,556.47 ≈ 100,981.67 元
     */

    //等额本息 方式 每月还款额 = 本金 × [月利率 × (1 + 月利率)^期数] / [(1 + 月利率)^期数 - 1]
    private fun calculatorResult() {
        var hasAllValue = 3
        if (mPlcBinding.etLoanAmount.text.isNullOrEmpty()) {
            hasAllValue--
            mPlcBinding.tvLoanAmountError.visibility = View.VISIBLE
        } else {
            mPlcBinding.tvLoanAmountError.visibility = View.GONE
        }
        if (mPlcBinding.etInterestRate.text.isNullOrEmpty()) {
            hasAllValue--
            mPlcBinding.tvInterestRateError.visibility = View.VISIBLE
        } else {
            mPlcBinding.tvInterestRateError.visibility = View.GONE
        }
        if (mPlcBinding.etLoanTerm.text.isNullOrEmpty()) {
            hasAllValue--
            mPlcBinding.tvLoanTermError.visibility = View.VISIBLE
        } else {
            mPlcBinding.tvLoanTermError.visibility = View.GONE
        }
        if (hasAllValue != 3) {
            return
        }
        val amount = mPlcBinding.etLoanAmount.text.toString().trim().toIntOrNull() ?: 0
        if (amount <= 0) {
            mPlcBinding.tvLoanAmountError.visibility = View.VISIBLE
            return
        } else {
            mPlcBinding.tvLoanAmountError.visibility = View.GONE
        }
        val rate = mPlcBinding.etInterestRate.text.toString().trim().toDoubleOrNull() ?: 0.0
        if (rate <= 0 || rate > 100) {
            mPlcBinding.etInterestRate.setText(null)
            mPlcBinding.tvInterestRateError.visibility = View.VISIBLE
            return
        } else {
            mPlcBinding.tvInterestRateError.visibility = View.GONE
        }
        var term = mPlcBinding.etLoanTerm.text.toString().trim().toIntOrNull() ?: 0
        val term2 = term
        if (term <= 0) {
            mPlcBinding.tvLoanTermError.visibility = View.VISIBLE
            return
        } else {
            mPlcBinding.tvLoanTermError.visibility = View.GONE
        }
        if (mMonthYear == "year") {
            term *= 12
        }
        val dataPersonalLoanPlf = DataPersonalLoanPlf().apply {
            loanType = LoanTypePlf.BUSINESS
            loanAmount = amount
            interestRate = rate
            loanTerm = term2
            loanTermUnit = mMonthYear
            startDate = System.currentTimeMillis()
            currencySymbol = mDataCurrencyUnitPlf?.currencySymbol ?: "$"
        }
        val (a, b) = ToolsLoanMonthDetailUtils.calculateAmortization(amount, rate / 100, term)
        dataPersonalLoanPlf.monthlyPayment = a
        dataPersonalLoanPlf.mLoanMonthDetailList = b
        mDataPersonalLoanPlf = dataPersonalLoanPlf
        startActivity(Intent(this, PlfCalculateResultTwoActivity::class.java))
    }

    private fun clearAllValue() {
        mPlcBinding.etLoanAmount.text = null
        mPlcBinding.etInterestRate.text = null
        mPlcBinding.etLoanTerm.text = null
    }

    override fun getLayoutValue(): ActivityBusinessLoanPlfBinding {
        return ActivityBusinessLoanPlfBinding.inflate(layoutInflater)
    }
}