package com.loancalculator.finance.manager.activity.calc

import android.annotation.SuppressLint
import android.content.Intent
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.activity.PlfMainToolActivity
import com.loancalculator.finance.manager.data.EventManagerHome
import com.loancalculator.finance.manager.databinding.ActivityCalculateMortgagesResultPlfBinding
import com.loancalculator.finance.manager.formatToFixString
import com.loancalculator.finance.manager.room.mPlfLoanRoom
import com.loancalculator.finance.manager.setSafeListener
import com.loancalculator.finance.manager.showToastIDPlf
import com.loancalculator.finance.manager.utils.ToolsLoanMonthDetailUtils.mDataPersonalLoanPlf
import com.loancalculator.finance.manager.utils.dialog.DialogAddCompareName
import org.greenrobot.eventbus.EventBus

class PlfCalculateMortgagesResultActivity :
    PlfBindingActivity<ActivityCalculateMortgagesResultPlfBinding>(
        mBarTextWhite = false
    ) {
    private var mTilPersonalLoanDao = mPlfLoanRoom.mTilPersonalLoanDao()
    private var mViewModel = ""

    @SuppressLint("SetTextI18n")
    override fun beginViewAndDoPlf() {
        intent?.let {
            mViewModel = it.getStringExtra("model") ?: ""
        }
        mPlcBinding.topSetPlf.tvTitleAll.text = getString(R.string.plf_calculate_result)
        mDataPersonalLoanPlf?.let { data ->
            mPlcBinding.tvHomePrice2.text =
                "${data.loanAmount}${data.currencySymbol}"
            mPlcBinding.tvIntersetRate2.text = "${data.interestRate}%"
            mPlcBinding.tvLoanTerm2.text = if (data.loanTermUnit == "month") {
                "${data.loanTerm} ${getString(R.string.plf_month)}"
            } else {
                "${data.loanTerm} ${getString(R.string.plf_year)}"
            }


            mPlcBinding.tvDownPayment2.text = "${data.firstAmount}${data.currencySymbol}"
            mPlcBinding.tvPropertyTax2.text = "${data.propertyTax}${data.currencySymbol}"
            mPlcBinding.tvPMI2.text = "${data.pmiMoney}${data.currencySymbol}"
            mPlcBinding.tvHomeInsurance2.text = "${data.homeownersInsurance}${data.currencySymbol}"
            mPlcBinding.tvHOAFees2.text = "${data.hoaMoney}${data.currencySymbol}"

            val totalPay = data.monthlyPayment * if (data.loanTermUnit == "month") {
                data.loanTerm
            } else {
                data.loanTerm * 12
            }

            val principalInterest =
                data.monthlyPayment - data.pmiMoney - data.homeownersInsurance - data.hoaMoney - data.propertyTax

            mPlcBinding.mcrvManager.setValues(
                doubleArrayOf(
                    principalInterest,
                    data.propertyTax * 1.0,
                    data.hoaMoney * 1.0,
                    data.pmiMoney * 1.0,
                    data.homeownersInsurance * 1.0
                )
            )
            mPlcBinding.tvMonthlyPayment.text = "${data.monthlyPayment}${data.currencySymbol}"
            mPlcBinding.tvPrincipalInterest.text =
                "${principalInterest.formatToFixString()}${data.currencySymbol}"
            mPlcBinding.tvPrincipalInterestPercent.text =
                "(${((principalInterest / data.monthlyPayment) * 100).formatToFixString()}%)"
            mPlcBinding.tvPropertyTaxMoney.text = "${data.propertyTax}${data.currencySymbol}"
            mPlcBinding.tvPropertyTaxPercent.text =
                "(${((data.propertyTax / data.monthlyPayment) * 100).formatToFixString()}%)"
            mPlcBinding.tvHOAFeesMoney.text = "${data.hoaMoney}${data.currencySymbol}"
            mPlcBinding.tvHOAFeesPercent.text =
                "(${((data.hoaMoney / data.monthlyPayment) * 100).formatToFixString()}%)"
            mPlcBinding.tvPMIMoney.text = "${data.pmiMoney}${data.currencySymbol}"
            mPlcBinding.tvPMIPercent.text =
                "(${((data.pmiMoney / data.monthlyPayment) * 100).formatToFixString()}%)"
            mPlcBinding.tvHomeInsuranceMoney.text =
                "${data.homeownersInsurance}${data.currencySymbol}"
            mPlcBinding.tvHomeInsurancePercent.text =
                "(${((data.homeownersInsurance / data.monthlyPayment) * 100).formatToFixString()}%)"

            mPlcBinding.tvTotalPayment.text =
                "${totalPay.formatToFixString()}${data.currencySymbol}"

            if (mViewModel != "details") {
                val dataIndexId = mTilPersonalLoanDao.insertContent(data)
                if (dataIndexId != -1L) {
                    data.dataIndexId = dataIndexId
                } else {
                    data.dataIndexId = -1L
                }
                mPlcBinding.root.postDelayed({
                    EventBus.getDefault().post(EventManagerHome("updateHistory").apply {
                        this.mDataPersonalLoanPlf = data
                    })
                }, 240)
            }
        }

        mPlcBinding.tvAddCompareList.setSafeListener {
            DialogAddCompareName(this) { name ->
                mDataPersonalLoanPlf?.let {
                    if (it.dataIndexId > -1L) {
                        it.addCompareTable = "yes"
                        it.aTableName = name
                        val re = mTilPersonalLoanDao.updateContent(it)
                        if (re > 0) {
                            showToastIDPlf(R.string.plf_added_successfully)
                        } else {
                            showToastIDPlf(R.string.plf_add_failed)
                        }
                    }
                }
            }.show()
        }
        mPlcBinding.tvSharePDF.setSafeListener {

        }
        mPlcBinding.tvGoHome.setSafeListener {
            startActivity(Intent(this, PlfMainToolActivity::class.java))
        }
    }

    override fun getLayoutValue(): ActivityCalculateMortgagesResultPlfBinding {
        return ActivityCalculateMortgagesResultPlfBinding.inflate(layoutInflater)
    }
}