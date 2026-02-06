package com.loancalculator.finance.manager.activity.other

import android.annotation.SuppressLint
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.activity.PlfMainToolActivity
import com.loancalculator.finance.manager.adapter.AdapterAmortizationTablePlf
import com.loancalculator.finance.manager.databinding.ActivityAmortizationTablePlfBinding
import com.loancalculator.finance.manager.formatToFixString
import com.loancalculator.finance.manager.room.mPlfLoanRoom
import com.loancalculator.finance.manager.setSafeListener
import com.loancalculator.finance.manager.showToastIDPlf
import com.loancalculator.finance.manager.utils.LoanMonthDetail
import com.loancalculator.finance.manager.utils.ShareResultPdfPlfUtil
import com.loancalculator.finance.manager.utils.TimeDatePlfUtils
import com.loancalculator.finance.manager.utils.ToolsLoanMonthDetailUtils
import com.loancalculator.finance.manager.utils.ToolsLoanMonthDetailUtils.mDataPersonalLoanPlf
import com.loancalculator.finance.manager.utils.dialog.DialogAddCompareName

class PlfAmortizationTableActivity : PlfBindingActivity<ActivityAmortizationTablePlfBinding>() {
    private lateinit var mAdapterAmortizationTablePlf: AdapterAmortizationTablePlf
    private var mListData = mutableListOf<LoanMonthDetail>()

    private var mTilPersonalLoanDao = mPlfLoanRoom.mTilPersonalLoanDao()

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

            mPlcBinding.tvTotalPayment2.text =
                "${totalPay.formatToFixString()}${data.currencySymbol}"
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
            }
            mListData.clear()
            data.mLoanMonthDetailList?.let {
                mListData.addAll(it)
            }
            setPlfRecyclerView()
        }

        mPlcBinding.tvGoHome.setSafeListener {
            startActivity(Intent(this, PlfMainToolActivity::class.java))
        }
        mPlcBinding.tvSharePDF.setSafeListener {

        }
        mPlcBinding.tvShareXLS.setSafeListener {
            mDataPersonalLoanPlf?.let { data ->

                val totalPay = (data.monthlyPayment * if (data.loanTermUnit == "month") {
                    data.loanTerm
                } else {
                    data.loanTerm * 12
                })
                val totalInterestPay = (totalPay - data.loanAmount).formatToFixString()
                ShareResultPdfPlfUtil.generateAndShareExcel(
                    this,
                    data.loanAmount,
                    data.interestRate,
                    mPlcBinding.tvLoanTerm2.text.toString(),
                    mPlcBinding.tvStartDate2.text.toString(),
                    data.monthlyPayment,
                    totalPay.formatToFixString(),
                    totalInterestPay,
                    mPlcBinding.tvPayingOffDate2.text.toString(),
                    data.mLoanMonthDetailList
                )
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