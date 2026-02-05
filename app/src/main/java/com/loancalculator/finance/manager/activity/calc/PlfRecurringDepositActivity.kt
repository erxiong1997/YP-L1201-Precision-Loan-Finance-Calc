package com.loancalculator.finance.manager.activity.calc

import android.app.DatePickerDialog
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
import com.loancalculator.finance.manager.data.EventManagerHome
import com.loancalculator.finance.manager.databinding.ActivityPersonalLoanPlfBinding
import com.loancalculator.finance.manager.databinding.ActivityRecurringDepositPlfBinding
import com.loancalculator.finance.manager.plfPxDp
import com.loancalculator.finance.manager.setSafeListener
import com.loancalculator.finance.manager.utils.DealRecentPlfUtils
import com.loancalculator.finance.manager.utils.TimeDatePlfUtils
import com.loancalculator.finance.manager.utils.ToolsLoanMonthDetailUtils
import com.loancalculator.finance.manager.utils.ToolsLoanMonthDetailUtils.mDataPersonalLoanPlf
import com.loancalculator.finance.manager.utils.value.LoanTypePlf
import com.loancalculator.finance.manager.utils.value.ParamsLtdUtils.mDataCurrencyUnitPlf
import org.greenrobot.eventbus.EventBus
import java.util.Calendar

class PlfRecurringDepositActivity : PlfBindingActivity<ActivityRecurringDepositPlfBinding>(
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
    override fun beginViewAndDoLtd() {
        mPlcBinding.topSetPlf.tvTitleAll.text = getString(R.string.plf_recurring_deposit)

        mPlcBinding.tvStartDate.text =
            TimeDatePlfUtils.getTimeDateOnePlf(System.currentTimeMillis())
        mDataCurrencyUnitPlf = DealRecentPlfUtils.getCurrencyUnitRecent()
        mDataCurrencyUnitPlf?.let {
            mPlcBinding.tvCurrencySymbol.text = mDataCurrencyUnitPlf?.currencySymbol
            mPlcBinding.ivCurrencyFlag.load(mDataCurrencyUnitPlf?.currencyDrawable)
            mPlcBinding.tvCurrencyName.text = mDataCurrencyUnitPlf?.currencyUnit
        }
        mPlcBinding.llCurrencyUnit.setSafeListener {
            mCurrencySelectLauncher.launch(Intent(this, PlfCurrencyUnitActivity::class.java))
        }
        mPlcBinding.ivDeleteInvestmentAmount.setSafeListener {
            mPlcBinding.etInvestmentAmount.setText(null)
        }
        mPlcBinding.tvMonthYear.setSafeListener {
            val popView = PopupWindow(this).apply {
                width = 84.plfPxDp()
                height = ViewGroup.LayoutParams.WRAP_CONTENT
                val view = LayoutInflater.from(this@PlfRecurringDepositActivity)
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
        mPlcBinding.clStartDate.setSafeListener {
            showStartDateDialog()
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
        if (mPlcBinding.etInvestmentAmount.text.isNullOrEmpty()) {
            hasAllValue--
            mPlcBinding.tvInvestmentAmountError.visibility = View.VISIBLE
        } else {
            mPlcBinding.tvInvestmentAmountError.visibility = View.GONE
        }
        if (mPlcBinding.etInterestRate.text.isNullOrEmpty()) {
            hasAllValue--
            mPlcBinding.tvInterestRateError.visibility = View.VISIBLE
        } else {
            mPlcBinding.tvInterestRateError.visibility = View.GONE
        }
        if (mPlcBinding.etTenure.text.isNullOrEmpty()) {
            hasAllValue--
            mPlcBinding.tvTenureError.visibility = View.VISIBLE
        } else {
            mPlcBinding.tvTenureError.visibility = View.GONE
        }
        if (hasAllValue != 3) {
            return
        }
        val amount = mPlcBinding.etInvestmentAmount.text.toString().trim().toIntOrNull() ?: 0
        if (amount <= 0) {
            mPlcBinding.tvInvestmentAmountError.visibility = View.VISIBLE
            return
        } else {
            mPlcBinding.tvInvestmentAmountError.visibility = View.GONE
        }
        val rate = mPlcBinding.etInterestRate.text.toString().trim().toDoubleOrNull() ?: 0.0
        if (rate <= 0) {
            mPlcBinding.tvInterestRateError.visibility = View.VISIBLE
            return
        } else {
            mPlcBinding.tvInterestRateError.visibility = View.GONE
        }
        val term = mPlcBinding.etTenure.text.toString().trim().toIntOrNull() ?: 0
        val term2 = term
        if (term <= 0) {
            mPlcBinding.tvTenureError.visibility = View.VISIBLE
            return
        } else {
            mPlcBinding.tvTenureError.visibility = View.GONE
        }
        val dataPersonalLoanPlf = DataPersonalLoanPlf().apply {
            loanType = LoanTypePlf.RD
            loanAmount = amount
            interestRate = rate
            loanTerm = term2
            loanTermUnit = mMonthYear
            startDate = if (mCurStartDateTime == 0L) {
                System.currentTimeMillis()
            } else {
                mCurStartDateTime
            }
            currencySymbol = mDataCurrencyUnitPlf?.currencySymbol ?: "$"
        }

        mDataPersonalLoanPlf = dataPersonalLoanPlf
        startActivity(Intent(this, PlfInvestmentResultActivity::class.java))
    }

    private fun clearAllValue() {
        mPlcBinding.etInvestmentAmount.text = null
        mPlcBinding.etInterestRate.text = null
        mPlcBinding.etTenure.text = null

        val calendar = Calendar.getInstance()
        mYearValue = calendar.get(Calendar.YEAR)
        mMonthValue = calendar.get(Calendar.MONTH)
        mDayValue = calendar.get(Calendar.DAY_OF_MONTH)
        val selectedDate = Calendar.getInstance().apply {
            set(mYearValue, mMonthValue, mDayValue)
        }
        mCurStartDateTime = selectedDate.time.time
        mPlcBinding.tvStartDate.text = TimeDatePlfUtils.getTimeDateOnePlf(mCurStartDateTime)
    }

    private var mYearValue = 0
    private var mMonthValue = 0
    private var mDayValue = 0
    private var mCurStartDateTime = 0L

    private fun showStartDateDialog() {
        val calendar = Calendar.getInstance()
        mYearValue = calendar.get(Calendar.YEAR)
        mMonthValue = calendar.get(Calendar.MONTH)
        mDayValue = calendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(
            this,
            R.style.MyDatePickerDialogTheme,
            { view, year, month, dayOfMonth ->
                mYearValue = year
                mMonthValue = month
                mDayValue = dayOfMonth
                val selectedDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                mCurStartDateTime = selectedDate.time.time

                mPlcBinding.tvStartDate.text = TimeDatePlfUtils.getTimeDateOnePlf(mCurStartDateTime)
            },
            mYearValue,
            mMonthValue,
            mDayValue
        ).show()
    }

    override fun getLayoutValue(): ActivityRecurringDepositPlfBinding {
        return ActivityRecurringDepositPlfBinding.inflate(layoutInflater)
    }
}