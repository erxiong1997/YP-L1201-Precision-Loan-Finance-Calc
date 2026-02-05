package com.loancalculator.finance.manager.activity.compare

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.adapter.AdapterComparePersonalLoanPlf
import com.loancalculator.finance.manager.data.DataPersonalLoanPlf
import com.loancalculator.finance.manager.databinding.ActivityComparePersonalLoanPlfBinding
import com.loancalculator.finance.manager.room.mPlfLoanRoom
import com.loancalculator.finance.manager.utils.dialog.DialogDeleteConfirmPlf
import com.loancalculator.finance.manager.utils.value.LoanTypePlf

class PlfComparePersonalLoanActivity : PlfBindingActivity<ActivityComparePersonalLoanPlfBinding>() {
    private lateinit var mAdapterComparePersonalLoanPlf: AdapterComparePersonalLoanPlf
    private val mListData = mutableListOf<DataPersonalLoanPlf>()
    private var mCompareType = ""
    private var mTilPersonalLoanDao = mPlfLoanRoom.mTilPersonalLoanDao()

    override fun beginViewAndDoLtd() {
        intent?.let {
            mCompareType = it.getStringExtra("compareType") ?: ""
            when (mCompareType) {
                "personalLoan" -> {
                    mPlcBinding.topSetPlf.tvTitleAll.text = String.format(
                        getString(R.string.plf_compare_2),
                        getString(R.string.plf_personal_loan)
                    )
                    mTilPersonalLoanDao.getListByLoanType(LoanTypePlf.PERSONAL).let {
                        mListData.addAll(it)
                    }
                }

                "businessLoan" -> {
                    mPlcBinding.topSetPlf.tvTitleAll.text = String.format(
                        getString(R.string.plf_compare_2),
                        getString(R.string.plf_business_loan)
                    )
                    mTilPersonalLoanDao.getListByLoanType(LoanTypePlf.BUSINESS).let {
                        mListData.addAll(it)
                    }
                }

                "mortgages" -> {
                    mPlcBinding.topSetPlf.tvTitleAll.text = String.format(
                        getString(R.string.plf_compare_2),
                        getString(R.string.plf_mortgages)
                    )
                    mTilPersonalLoanDao.getListByLoanType(LoanTypePlf.MORTGAGES).let {
                        mListData.addAll(it)
                    }
                }

                "autoLoan" -> {
                    mPlcBinding.topSetPlf.tvTitleAll.text = String.format(
                        getString(R.string.plf_compare_2),
                        getString(R.string.plf_auto_loan)
                    )
                    mTilPersonalLoanDao.getListByLoanType(LoanTypePlf.AUTO).let {
                        mListData.addAll(it)
                    }
                }
            }
        }
        setPlfRecyclerView()
    }

    override fun setPlfRecyclerView() {
        mAdapterComparePersonalLoanPlf = AdapterComparePersonalLoanPlf(this, mListData, {
            //delete
            DialogDeleteConfirmPlf(this, getString(R.string.plf_deleteed_no_recovered)) {
                mListData.removeAt(it)
                if (mListData.isEmpty()) {
                    mPlcBinding.rvRvView.visibility = View.GONE
                    mPlcBinding.clNoData.visibility = View.VISIBLE
                }
                mAdapterComparePersonalLoanPlf.notifyItemRemoved(it)
            }.show()

        }) {//item

        }
        mPlcBinding.rvRvView.layoutManager = LinearLayoutManager(this)
        mPlcBinding.rvRvView.adapter = mAdapterComparePersonalLoanPlf
        if (mListData.isEmpty()) {
            mPlcBinding.rvRvView.visibility = View.GONE
            mPlcBinding.clNoData.visibility = View.VISIBLE
        } else {
            mPlcBinding.rvRvView.visibility = View.VISIBLE
            mPlcBinding.clNoData.visibility = View.GONE
        }
    }

    override fun getLayoutValue(): ActivityComparePersonalLoanPlfBinding {
        return ActivityComparePersonalLoanPlfBinding.inflate(layoutInflater)
    }
}