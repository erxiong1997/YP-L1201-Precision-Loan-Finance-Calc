package com.loancalculator.finance.manager.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.activity.PlfRootActivity
import com.loancalculator.finance.manager.activity.calc.PlfCalculateResultActivity
import com.loancalculator.finance.manager.activity.calc.PlfCalculateResultTwoActivity
import com.loancalculator.finance.manager.activity.calc.PlfInvestmentResultActivity
import com.loancalculator.finance.manager.adapter.AdapterHistoryCalculatorPlf
import com.loancalculator.finance.manager.data.DataPersonalLoanPlf
import com.loancalculator.finance.manager.databinding.FragmentHistoryInvestmentPlfBinding
import com.loancalculator.finance.manager.room.mPlfLoanRoom
import com.loancalculator.finance.manager.setSafeListener
import com.loancalculator.finance.manager.utils.ToolsLoanMonthDetailUtils.mDataPersonalLoanPlf
import com.loancalculator.finance.manager.utils.dialog.DialogDeleteConfirmPlf
import com.loancalculator.finance.manager.utils.value.LoanTypePlf

class FragmentHistoryInvestmentPlf : RootPlfFragment<FragmentHistoryInvestmentPlfBinding>() {
    companion object {
        private const val TIL_PAGE = "til_page"

        fun newInstance(page: Int): FragmentHistoryInvestmentPlf {
            val fragment = FragmentHistoryInvestmentPlf()
            fragment.arguments = Bundle().apply {
                putInt(TIL_PAGE, page)
            }
            return fragment
        }
    }

    private var mTilPersonalLoanDao = mPlfLoanRoom.mTilPersonalLoanDao()

    private lateinit var mAdapterHistoryCalculatorPlf: AdapterHistoryCalculatorPlf
    private val mListData = mutableListOf<DataPersonalLoanPlf>()

    private var mCurDeleteModel = false

    override fun startCreateContent(
        rootActivity: PlfRootActivity,
        view: View,
        bundle: Bundle?
    ) {
        setPlfRecyclerView(rootActivity)
        getListDataPlf()

        mPlfBinding.tvDelete.setSafeListener {
            DialogDeleteConfirmPlf(rootActivity, getString(R.string.plf_deleteed_no_recovered)) {
                for ((index, data) in mListData.withIndex().reversed()) {
                    if (data.fingerSelect) {
                        val deleteRows = mTilPersonalLoanDao.deleteContent(data)
                        if (deleteRows > 0) {
                            mListData.removeAt(index)
                            mSelectorCount--
                            mAdapterHistoryCalculatorPlf.notifyItemRemoved(index)
                        }
                    }
                }
                changeDeleteButton()
            }.show()
        }
    }

    private var mSelectorCount = 0

    fun initSelectStatus(openDelete: Boolean) {
        if (openDelete == mCurDeleteModel) {
            return
        }
        mCurDeleteModel = openDelete
        if (mListData.isEmpty()) return
        mListData.forEach {
            it.fingerSelect = false
        }
        mSelectorCount = 0
        mAdapterHistoryCalculatorPlf.mDeleteModel = openDelete
        mAdapterHistoryCalculatorPlf.notifyItemRangeChanged(0, mListData.size, "updateStatus")
        mPlfBinding.tvDelete.visibility = View.GONE
    }

    fun changeSelectStatus(fingerSelect: Boolean) {
        if (mListData.isEmpty()) return
        mListData.forEach {
            it.fingerSelect = fingerSelect
        }
        if (fingerSelect) {
            mSelectorCount = mListData.size
        } else {
            mSelectorCount = 0
        }
        mAdapterHistoryCalculatorPlf.mDeleteModel = true
        mAdapterHistoryCalculatorPlf.notifyItemRangeChanged(0, mListData.size, "updateStatus")
        changeDeleteButton()
    }

    override fun setPlfRecyclerView(rootActivity: PlfRootActivity) {
        val list = mTilPersonalLoanDao.getAllListInvestment(LoanTypePlf.RD, LoanTypePlf.FD)
        mListData.clear()
        mListData.addAll(list)
        mAdapterHistoryCalculatorPlf = AdapterHistoryCalculatorPlf(false, rootActivity, mListData, {

        }) {
            val data = mListData[it]
            if (mCurDeleteModel) {
                data.fingerSelect = !data.fingerSelect
                if (data.fingerSelect) {
                    mSelectorCount++
                } else {
                    mSelectorCount--
                }
                mAdapterHistoryCalculatorPlf.notifyItemChanged(it)
                changeDeleteButton()
            } else {
                when (data.loanType) {
                    LoanTypePlf.PERSONAL, LoanTypePlf.AUTO -> {
                        mDataPersonalLoanPlf = data
                        startActivity(
                            Intent(
                                rootActivity,
                                PlfCalculateResultActivity::class.java
                            ).apply {
                                putExtra("model", "details")
                            })
                    }

                    LoanTypePlf.BUSINESS -> {
                        mDataPersonalLoanPlf = data
                        startActivity(
                            Intent(
                                rootActivity, PlfCalculateResultTwoActivity::class.java
                            ).apply {
                                putExtra("model", "details")
                            }
                        )
                    }

                    LoanTypePlf.MORTGAGES -> {

                    }

                    LoanTypePlf.RD, LoanTypePlf.FD -> {
                        startActivity(
                            Intent(
                                rootActivity, PlfInvestmentResultActivity::class.java
                            ).apply {
                                putExtra("model", "details")
                            }
                        )
                    }
                }
            }
        }
        mPlfBinding.rvRvView.layoutManager = LinearLayoutManager(rootActivity)
        mPlfBinding.rvRvView.adapter = mAdapterHistoryCalculatorPlf
        if (mListData.isEmpty()) {
            mPlfBinding.rvRvView.visibility = View.GONE
            mPlfBinding.clNoData.visibility = View.VISIBLE
        } else {
            mPlfBinding.rvRvView.visibility = View.VISIBLE
            mPlfBinding.clNoData.visibility = View.GONE
        }
    }

    private fun getListDataPlf() {

    }

    private fun changeDeleteButton() {
        if (mListData.isEmpty()) {
            mPlfBinding.tvDelete.visibility = View.GONE
            mPlfBinding.rvRvView.visibility = View.GONE
            mPlfBinding.clNoData.visibility = View.VISIBLE
            return
        }
        mPlfBinding.tvDelete.visibility = View.VISIBLE
        if (mSelectorCount == 0) {
            mPlfBinding.tvDelete.isEnabled = false
        } else {
            mPlfBinding.tvDelete.isEnabled = true
        }
    }


    override fun getLayoutValue(): FragmentHistoryInvestmentPlfBinding {
        return FragmentHistoryInvestmentPlfBinding.inflate(layoutInflater)

    }
}