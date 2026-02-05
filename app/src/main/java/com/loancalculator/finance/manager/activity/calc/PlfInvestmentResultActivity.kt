package com.loancalculator.finance.manager.activity.calc

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.activity.PlfMainToolActivity
import com.loancalculator.finance.manager.databinding.ActivityCalculateResultPlfBinding
import com.loancalculator.finance.manager.databinding.ActivityInvestmentResultPlfBinding
import com.loancalculator.finance.manager.formatToFixString
import com.loancalculator.finance.manager.room.mPlfLoanRoom
import com.loancalculator.finance.manager.setSafeListener
import com.loancalculator.finance.manager.showToastIDPlf
import com.loancalculator.finance.manager.utils.ShareResultPdfPlfUtil
import com.loancalculator.finance.manager.utils.TimeDatePlfUtils
import com.loancalculator.finance.manager.utils.ToolsLoanMonthDetailUtils.mDataPersonalLoanPlf
import com.loancalculator.finance.manager.utils.dialog.DialogAddCompareName
import com.loancalculator.finance.manager.utils.value.ParamsLtdUtils.mDataCurrencyUnitPlf

class PlfInvestmentResultActivity : PlfBindingActivity<ActivityInvestmentResultPlfBinding>(
    mBarTextWhite = false
) {
    private var mTilPersonalLoanDao = mPlfLoanRoom.mTilPersonalLoanDao()
    private var mViewModel = ""

    @SuppressLint("SetTextI18n")
    override fun beginViewAndDoLtd() {
        intent?.let {
            mViewModel = it.getStringExtra("model") ?: ""
        }
        mPlcBinding.topSetPlf.tvTitleAll.text = getString(R.string.plf_calculate_result)
        mDataPersonalLoanPlf?.let { data ->
            mPlcBinding.tvInvestmentAmount2.text =
                "${data.loanAmount}${data.currencySymbol}"
            mPlcBinding.tvIntersetRate2.text = "${data.interestRate}%"
            mPlcBinding.tvTenure2.text = if (data.loanTermUnit == "month") {
                "${data.loanTerm} ${getString(R.string.plf_month)}"
            } else {
                "${data.loanTerm} ${getString(R.string.plf_year)}"
            }
            mPlcBinding.tvStartDate2.text = TimeDatePlfUtils.getTimeDateOnePlf(data.startDate)

            mPlcBinding.tvTotalInvestment2.text = "${
                if (data.loanTermUnit == "month") {
                    data.loanTerm
                } else {
                    data.loanTerm * 12
                } * data.loanAmount
            }${data.currencySymbol}"

            mPlcBinding.tvTotalInterest2.text = "${data.totalInterest}${data.currencySymbol}"
            mPlcBinding.tvTotalAmount2.text =
                "${data.totalInvestmentInterest}${data.currencySymbol}"

            mPlcBinding.tvEndDate2.text =
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
            }
        }

        mPlcBinding.tvSharePDF.setSafeListener {
            ShareResultPdfPlfUtil.generateInvestmentPdf(
                this, mutableListOf(
                    getString(R.string.plf_investment_information),
                    getString(R.string.plf_investment_amount),
                    mPlcBinding.tvInvestmentAmount2.text.toString(),
                    getString(R.string.plf_interest_rate),
                    mPlcBinding.tvIntersetRate2.text.toString(),
                    getString(R.string.plf_tenure),
                    mPlcBinding.tvTenure2.text.toString(),
                    getString(R.string.plf_start_date),
                    mPlcBinding.tvStartDate2.text.toString(),
                    getString(R.string.plf_result_after_calculation),
                    getString(R.string.plf_total_investment),
                    mPlcBinding.tvTotalInvestment2.text.toString(),
                    getString(R.string.plf_total_interest),
                    mPlcBinding.tvTotalInterest2.text.toString(),
                    getString(R.string.plf_total_amount),
                    mPlcBinding.tvTotalAmount2.text.toString(),
                    getString(R.string.plf_end_date),
                    mPlcBinding.tvEndDate2.text.toString()
                ), mutableListOf(0, 9)
            )
//            ShareResultPdfPlfUtil.createBitmapFromView(mPlcBinding.svContent)?.let {
//                ShareResultPdfPlfUtil.shareBitmapAsSinglePagePdf(this, it)
//            }
        }
        mPlcBinding.tvGoHome.setSafeListener {
            startActivity(Intent(this, PlfMainToolActivity::class.java))
        }
    }

    override fun getLayoutValue(): ActivityInvestmentResultPlfBinding {
        return ActivityInvestmentResultPlfBinding.inflate(layoutInflater)
    }
}