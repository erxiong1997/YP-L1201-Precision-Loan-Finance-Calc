package com.loancalculator.finance.manager.activity.calc

import android.annotation.SuppressLint
import android.content.Intent
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.activity.PlfMainToolActivity
import com.loancalculator.finance.manager.activity.compare.PlfComparePersonalLoanActivity
import com.loancalculator.finance.manager.data.DataComparePlf
import com.loancalculator.finance.manager.data.EventManagerHome
import com.loancalculator.finance.manager.databinding.ActivityCalculateResultTwoPlfBinding
import com.loancalculator.finance.manager.room.mPlfLoanRoom
import com.loancalculator.finance.manager.setSafeListener
import com.loancalculator.finance.manager.showToastIDPlf
import com.loancalculator.finance.manager.utils.ShareResultPdfPlfUtil
import com.loancalculator.finance.manager.utils.TimeDatePlfUtils
import com.loancalculator.finance.manager.utils.ToolsLoanMonthDetailUtils.mDataPersonalLoanPlf
import com.loancalculator.finance.manager.utils.dialog.DialogAddCompareName
import com.loancalculator.finance.manager.utils.value.LoanTypePlf
import org.greenrobot.eventbus.EventBus

class PlfCalculateResultTwoActivity : PlfBindingActivity<ActivityCalculateResultTwoPlfBinding>(
    mBarTextWhite = false
) {
    private var mTilPersonalLoanDao = mPlfLoanRoom.mTilPersonalLoanDao()
    private var mViewModel = ""

    private var mTilCompareDao = mPlfLoanRoom.mTilCompareDao()

    @SuppressLint("SetTextI18n")
    override fun beginViewAndDoPlf() {
        intent?.let {
            mViewModel = it.getStringExtra("model") ?: ""
        }
        mPlcBinding.topSetPlf.tvTitleAll.text = getString(R.string.plf_calculate_result)
        mDataPersonalLoanPlf?.let { data ->
            mPlcBinding.tvLoanAmount2.text =
                "${data.loanAmount}${data.currencySymbol}"
            mPlcBinding.tvIntersetRate2.text = "${data.interestRate}%"
            mPlcBinding.tvLoanTerm2.text = if (data.loanTermUnit == "month") {
                "${data.loanTerm} ${getString(R.string.plf_month)}"
            } else {
                "${data.loanTerm} ${getString(R.string.plf_year)}"
            }
            mPlcBinding.tvStartDate2.text = TimeDatePlfUtils.getTimeDateOnePlf(data.startDate)
            mPlcBinding.tvMonthPayment2.text = "${data.monthlyPayment}${data.currencySymbol}"

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

                        val data = DataComparePlf()
                        data.loanType = it.loanType
                        data.loanTerm = it.loanTerm
                        data.currencySymbol = it.currencySymbol
                        data.loanAmount = it.loanAmount
                        data.firstAmount = it.firstAmount
                        data.interestRate = it.interestRate
                        data.loanTermUnit = it.loanTermUnit
                        data.numberInvestment = it.numberInvestment
                        data.startDate = it.startDate
                        data.monthlyPayment = it.monthlyPayment
                        data.addDate = it.addDate
                        data.propertyTax = it.propertyTax
                        data.pmiMoney = it.pmiMoney
                        data.homeownersInsurance = it.homeownersInsurance
                        data.hoaMoney = it.hoaMoney
                        data.tempLong1 = it.tempLong1
                        data.tempLong2 = it.tempLong2
                        data.tempString1 = it.tempString1
                        data.tempString2 = it.tempString2
                        data.mLoanMonthDetailList = it.mLoanMonthDetailList
                        data.fingerSelect = it.fingerSelect
                        data.totalInvestmentInterest = it.totalInvestmentInterest
                        data.totalInterest = it.totalInterest

                        data.addCompareTable = "yes"
                        data.aTableName = name

                        val re = mTilCompareDao.insertContent(data)
                        if (re > 0) {
                            showToastIDPlf(R.string.plf_added_successfully)
                            startActivity(
                                Intent(this, PlfComparePersonalLoanActivity::class.java).apply {
                                    putExtra("compareType", "businessLoan")
                                })
                        } else {
                            showToastIDPlf(R.string.plf_add_failed)
                        }
                    }
                }
            }.show()
        }
        mPlcBinding.tvShare.setSafeListener {
            ShareResultPdfPlfUtil.generateInvestmentPdf(
                this, mutableListOf(
                    getString(R.string.plf_loan_information),
                    getString(R.string.plf_loan_amount),
                    mPlcBinding.tvLoanAmount2.text.toString(),
                    getString(R.string.plf_interest_rate),
                    mPlcBinding.tvIntersetRate2.text.toString(),
                    getString(R.string.plf_loan_term),
                    mPlcBinding.tvLoanTerm2.text.toString(),
                    getString(R.string.plf_start_date),
                    mPlcBinding.tvStartDate2.text.toString(),
                    getString(R.string.plf_result_after_calculation),
                    getString(R.string.plf_monthly_payment),
                    mPlcBinding.tvMonthPayment2.text.toString()
                ), mutableListOf(0, 9)
            )
        }
        mPlcBinding.tvGoHome.setSafeListener {
            startActivity(Intent(this, PlfMainToolActivity::class.java))
        }
    }

    override fun getLayoutValue(): ActivityCalculateResultTwoPlfBinding {
        return ActivityCalculateResultTwoPlfBinding.inflate(layoutInflater)
    }
}