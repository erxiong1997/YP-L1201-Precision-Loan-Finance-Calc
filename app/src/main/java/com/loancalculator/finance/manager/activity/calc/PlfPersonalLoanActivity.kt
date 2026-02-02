package com.loancalculator.finance.manager.activity.calc

import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import coil.load
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.activity.other.PlfCurrencyUnitActivity
import com.loancalculator.finance.manager.databinding.ActivityPersonalLoanPlfBinding
import com.loancalculator.finance.manager.setSafeListener
import com.loancalculator.finance.manager.utils.value.ParamsLtdUtils.mDataCurrencyUnitPlf

class PlfPersonalLoanActivity : PlfBindingActivity<ActivityPersonalLoanPlfBinding>(
    mBarTextWhite = false
) {
    private val mCurrencySelectLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                mPlcBinding.tvCurrencySymbol.text = mDataCurrencyUnitPlf?.currencySymbol
                mPlcBinding.ivCurrencyFlag.load(mDataCurrencyUnitPlf?.currencyDrawable)
                mPlcBinding.tvCurrencyName.text = mDataCurrencyUnitPlf?.currencyUnit
            }
        }

    override fun beginViewAndDoLtd() {
        mPlcBinding.topSetPlf.tvTitleAll.text = getString(R.string.plf_personal_loan)

        mPlcBinding.llCurrencyUnit.setSafeListener {
            mCurrencySelectLauncher.launch(Intent(this, PlfCurrencyUnitActivity::class.java))
        }
        mPlcBinding.ivDeleteLoanAmount.setSafeListener {
            mPlcBinding.etLoanAmount.setText(null)
        }
        mPlcBinding.clStartDate.setSafeListener {

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
        }
        if (mPlcBinding.etInterestRate.text.isNullOrEmpty()) {
            hasAllValue--
        }
        if (mPlcBinding.etLoanTerm.text.isNullOrEmpty()) {
            hasAllValue--
        }
        if (hasAllValue != 3) {
            return
        }

        val amount = mPlcBinding.etLoanAmount.toString().trim().toIntOrNull() ?: 0
        val rate = mPlcBinding.etInterestRate.toString().trim().toDoubleOrNull() ?: 0
        val term = mPlcBinding.etLoanTerm.toString().trim().toIntOrNull() ?: 0


    }

    private fun clearAllValue() {
        mPlcBinding.etLoanAmount.text = null
        mPlcBinding.etInterestRate.text = null
        mPlcBinding.etLoanTerm.text = null
    }

    override fun getLayoutValue(): ActivityPersonalLoanPlfBinding {
        return ActivityPersonalLoanPlfBinding.inflate(layoutInflater)
    }
}