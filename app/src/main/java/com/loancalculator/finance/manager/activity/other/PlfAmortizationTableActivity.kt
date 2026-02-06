package com.loancalculator.finance.manager.activity.other

import android.annotation.SuppressLint
import androidx.recyclerview.widget.LinearLayoutManager
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.adapter.AdapterAmortizationTablePlf
import com.loancalculator.finance.manager.databinding.ActivityAmortizationTablePlfBinding
import com.loancalculator.finance.manager.formatToFixString
import com.loancalculator.finance.manager.utils.LoanMonthDetail
import com.loancalculator.finance.manager.utils.TimeDatePlfUtils
import com.loancalculator.finance.manager.utils.ToolsLoanMonthDetailUtils
import com.loancalculator.finance.manager.utils.ToolsLoanMonthDetailUtils.mDataPersonalLoanPlf

class PlfAmortizationTableActivity : PlfBindingActivity<ActivityAmortizationTablePlfBinding>() {
    private lateinit var mAdapterAmortizationTablePlf: AdapterAmortizationTablePlf
    private var mListData = mutableListOf<LoanMonthDetail>()

    @SuppressLint("SetTextI18n")
    override fun beginViewAndDoPlf() {
        mPlcBinding.topSetPlf.tvTitleAll.text = getString(R.string.plf_amortization_table)

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

            val totalPay = data.monthlyPayment * if (data.loanTermUnit == "month") {
                data.loanTerm
            } else {
                data.loanTerm * 12
            }

            mPlcBinding.tvTotalPayment2.text = totalPay.formatToFixString()
            mPlcBinding.tvTotalInterestPayable2.text =
                (totalPay - data.loanAmount).formatToFixString()
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
            if (data.mLoanMonthDetailList == null) {
                val amount = data.loanAmount
                val rate = data.interestRate
                val term = if (data.loanTermUnit == "month") {
                    data.loanTerm
                } else {
                    data.loanTerm * 12
                }
                val (a, b) = ToolsLoanMonthDetailUtils.calculateAmortization(
                    amount,
                    rate / 100,
                    term
                )
                data.mLoanMonthDetailList = b
                mListData.clear()
                data.mLoanMonthDetailList?.let {
                    mListData.addAll(it)
                }
                setPlfRecyclerView()
            }
        }
    }

    override fun setPlfRecyclerView() {
        mAdapterAmortizationTablePlf = AdapterAmortizationTablePlf(this, mListData) {

        }
        mPlcBinding.rvRvView.layoutManager = LinearLayoutManager(this)
        mPlcBinding.rvRvView.adapter = mAdapterAmortizationTablePlf
    }

    override fun getLayoutValue(): ActivityAmortizationTablePlfBinding {
        return ActivityAmortizationTablePlfBinding.inflate(layoutInflater)
    }
}