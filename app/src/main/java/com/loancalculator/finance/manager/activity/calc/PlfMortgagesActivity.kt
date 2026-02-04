package com.loancalculator.finance.manager.activity.calc

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toDrawable
import androidx.core.widget.doAfterTextChanged
import coil.load
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.activity.other.PlfCurrencyUnitActivity
import com.loancalculator.finance.manager.data.DataPersonalLoanPlf
import com.loancalculator.finance.manager.databinding.ActivityMortgagesPlfBinding
import com.loancalculator.finance.manager.formatToFixString
import com.loancalculator.finance.manager.plfPxDp
import com.loancalculator.finance.manager.setSafeListener
import com.loancalculator.finance.manager.utils.DealRecentPlfUtils
import com.loancalculator.finance.manager.utils.ToolsLoanMonthDetailUtils
import com.loancalculator.finance.manager.utils.ToolsLoanMonthDetailUtils.mDataPersonalLoanPlf
import com.loancalculator.finance.manager.utils.value.LoanTypePlf
import com.loancalculator.finance.manager.utils.value.ParamsLtdUtils.mDataCurrencyUnitPlf

class PlfMortgagesActivity : PlfBindingActivity<ActivityMortgagesPlfBinding>(
    mBarTextWhite = false
) {
    private val mCurrencySelectLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                mPlcBinding.tvCurrencySymbol.text = mDataCurrencyUnitPlf?.currencySymbol
                mPlcBinding.ivCurrencyFlag.load(mDataCurrencyUnitPlf?.currencyDrawable)
                mPlcBinding.tvCurrencyName.text = mDataCurrencyUnitPlf?.currencyUnit
                DealRecentPlfUtils.addCurrencyUnitRecent(mDataCurrencyUnitPlf)
            }
        }

    //month year
    private var mMonthYear = "month"
    private var mEditIng = false

    init {
        mHandlerLtd = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    1189 -> {
                        val total =
                            mPlcBinding.etHomePrice.text.toString().trim().toIntOrNull() ?: 0
                        val down =
                            mPlcBinding.etDownloadPayment.text.toString().trim().toDoubleOrNull()
                                ?: 0.0
                        if (total > 0 && down <= total) {
                            val re = (down * 1.0 / (total * 1.0) * 100.0).formatToFixString()
                            mPlcBinding.etPaymentRatio.setText(re)
                        }
                    }

                    1190 -> {
                        val total =
                            mPlcBinding.etHomePrice.text.toString().trim().toIntOrNull() ?: 0
                        val ratio =
                            (mPlcBinding.etPaymentRatio.text.toString().trim().toDoubleOrNull()
                                ?: 0.0) / 100
                        if (ratio < 1) {
                            val re = (total * 1.0 * ratio).formatToFixString()
                            mPlcBinding.etDownloadPayment.setText(re)
                        }
                    }
                }
            }
        }
    }

    override fun beginViewAndDoLtd() {
        mPlcBinding.topSetPlf.tvTitleAll.text = getString(R.string.plf_personal_loan)

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
            mPlcBinding.etHomePrice.setText(null)
        }
        mPlcBinding.tvMonthYear.setSafeListener {
            val popView = PopupWindow(this).apply {
                width = 84.plfPxDp()
                height = ViewGroup.LayoutParams.WRAP_CONTENT
                val view = LayoutInflater.from(this@PlfMortgagesActivity)
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

        mPlcBinding.etHomePrice.doAfterTextChanged {
            if (it.isNullOrEmpty()) {
                mPlcBinding.etDownloadPayment.setText(null)
                mPlcBinding.etPaymentRatio.setText(null)
                mPlcBinding.tvDownloadPayment1.visibility = View.GONE
                mPlcBinding.tvDownloadPayment2.visibility = View.GONE
                mPlcBinding.clDownloadPayment1.visibility = View.GONE
            } else {
                mPlcBinding.tvDownloadPayment1.visibility = View.VISIBLE
                mPlcBinding.tvDownloadPayment2.visibility = View.VISIBLE
                mPlcBinding.clDownloadPayment1.visibility = View.VISIBLE
            }
        }
        mPlcBinding.etDownloadPayment.doAfterTextChanged {
            if (it.isNullOrEmpty()) {
                mPlcBinding.etPaymentRatio.setText(null)
            } else {
                if (mEditIng) {
                    mEditIng = false
                } else {
                    mEditIng = true
                    mHandlerLtd?.removeCallbacksAndMessages(null)
                    mHandlerLtd?.sendEmptyMessageDelayed(1189, 64)
                }
            }
        }
        mPlcBinding.etDownloadPayment.doAfterTextChanged {
            if (it.isNullOrEmpty()) {
                mPlcBinding.etPaymentRatio.setText(null)
            } else {
                if (mEditIng) {
                    mEditIng = false
                } else {
                    mEditIng = true
                    mHandlerLtd?.removeCallbacksAndMessages(null)
                    mHandlerLtd?.sendEmptyMessageDelayed(1190, 64)
                }
            }
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
        var hasAllValue = 5
        if (mPlcBinding.etHomePrice.text.isNullOrEmpty()) {
            hasAllValue--
            mPlcBinding.tvHomePriceError.visibility = View.VISIBLE
        } else {
            mPlcBinding.tvHomePriceError.visibility = View.GONE
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
        if (mPlcBinding.etDownloadPayment.text.isNullOrEmpty()) {
            hasAllValue--
            mPlcBinding.tvDownloadPaymentError.visibility = View.VISIBLE
        } else {
            mPlcBinding.tvDownloadPaymentError.visibility = View.GONE
        }
        if (mPlcBinding.etPaymentRatio.text.isNullOrEmpty()) {
            hasAllValue--
            mPlcBinding.etPaymentRatio.visibility = View.VISIBLE
        } else {
            mPlcBinding.etPaymentRatio.visibility = View.GONE
        }
        if (hasAllValue != 5) {
            return
        }
        val amount = mPlcBinding.etHomePrice.text.toString().trim().toIntOrNull() ?: 0
        if (amount <= 0) {
            mPlcBinding.tvHomePriceError.visibility = View.VISIBLE
            return
        } else {
            mPlcBinding.tvHomePriceError.visibility = View.GONE
        }
        val rate = mPlcBinding.etInterestRate.text.toString().trim().toDoubleOrNull() ?: 0.0
        if (rate <= 0) {
            mPlcBinding.tvInterestRateError.visibility = View.VISIBLE
            return
        } else {
            mPlcBinding.tvInterestRateError.visibility = View.GONE
        }
        var term = mPlcBinding.etLoanTerm.text.toString().trim().toIntOrNull() ?: 0
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
            loanType = LoanTypePlf.MORTGAGES
            loanAmount = amount
            interestRate = rate
            loanTerm = term
            startDate = System.currentTimeMillis()
            currencySymbol = mDataCurrencyUnitPlf?.currencySymbol ?: "$"
        }
        val (a, b) = ToolsLoanMonthDetailUtils.calculateAmortization(amount, rate / 100, term)
        dataPersonalLoanPlf.monthlyPayment = a
        dataPersonalLoanPlf.mLoanMonthDetailList = b
        mDataPersonalLoanPlf = dataPersonalLoanPlf
        startActivity(Intent(this, PlfCalculateResultActivity::class.java))
    }

    private fun clearAllValue() {
        mPlcBinding.etHomePrice.text = null
        mPlcBinding.etInterestRate.text = null
        mPlcBinding.etLoanTerm.text = null
        mPlcBinding.etDownloadPayment.text = null
        mPlcBinding.etPaymentRatio.text = null
        mPlcBinding.etPropertyTax.text = null
        mPlcBinding.etPmiText.text = null
        mPlcBinding.etHomeInsurance.text = null
        mPlcBinding.etHOAFees.text = null
    }

    override fun getLayoutValue(): ActivityMortgagesPlfBinding {
        return ActivityMortgagesPlfBinding.inflate(layoutInflater)
    }
}