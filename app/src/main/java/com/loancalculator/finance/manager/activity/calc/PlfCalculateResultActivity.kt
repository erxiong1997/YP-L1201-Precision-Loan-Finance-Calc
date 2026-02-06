package com.loancalculator.finance.manager.activity.calc

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.activity.PlfMainToolActivity
import com.loancalculator.finance.manager.activity.other.PlfAmortizationTableActivity
import com.loancalculator.finance.manager.data.EventManagerHome
import com.loancalculator.finance.manager.databinding.ActivityCalculateResultPlfBinding
import com.loancalculator.finance.manager.formatToFixString
import com.loancalculator.finance.manager.room.mPlfLoanRoom
import com.loancalculator.finance.manager.setSafeListener
import com.loancalculator.finance.manager.showToastIDPlf
import com.loancalculator.finance.manager.utils.ShareResultPdfPlfUtil
import com.loancalculator.finance.manager.utils.TimeDatePlfUtils
import com.loancalculator.finance.manager.utils.ToolsLoanMonthDetailUtils.mDataPersonalLoanPlf
import com.loancalculator.finance.manager.utils.dialog.DialogAddCompareName
import com.loancalculator.finance.manager.utils.value.LoanTypePlf
import org.greenrobot.eventbus.EventBus

class PlfCalculateResultActivity : PlfBindingActivity<ActivityCalculateResultPlfBinding>(
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
            if (data.loanType == LoanTypePlf.PERSONAL) {
                mPlcBinding.tvAmortizationTable.visibility = View.VISIBLE
                mPlcBinding.tvAmortizationTable2.visibility = View.INVISIBLE
            }
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

            val totalPay = data.monthlyPayment * if (data.loanTermUnit == "month") {
                data.loanTerm
            } else {
                data.loanTerm * 12
            }

            mPlcBinding.tvTotalPayment2.text = "${totalPay.formatToFixString()}${data.currencySymbol}"
            mPlcBinding.tvTotalInterestPayable2.text =
                "${(totalPay - data.loanAmount).formatToFixString()}${data.currencySymbol}"
            mPlcBinding.tvPayingOffDate2.text =
                TimeDatePlfUtils.getTimeDateOnePlf(
                    TimeDatePlfUtils.getOverDatePlf(
                        if (data.loanTermUnit == "month") {
                            data.loanTerm
                        } else {
                            data.loanTerm * 12
                        }.toLong(), data.startDate
                    )
                )
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
        mPlcBinding.tvAmortizationTable.setSafeListener {
            startActivity(Intent(this, PlfAmortizationTableActivity::class.java))
        }
        mPlcBinding.tvSharePDF.setSafeListener {

        }
        mPlcBinding.tvGoHome.setSafeListener {
            startActivity(Intent(this, PlfMainToolActivity::class.java))
        }
    }

    override fun getLayoutValue(): ActivityCalculateResultPlfBinding {
        return ActivityCalculateResultPlfBinding.inflate(layoutInflater)
    }
}