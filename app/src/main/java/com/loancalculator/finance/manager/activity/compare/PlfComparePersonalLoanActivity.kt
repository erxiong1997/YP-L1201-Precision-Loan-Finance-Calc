package com.loancalculator.finance.manager.activity.compare

import androidx.recyclerview.widget.LinearLayoutManager
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.adapter.AdapterComparePersonalLoanPlf
import com.loancalculator.finance.manager.data.DataPersonalLoanPlf
import com.loancalculator.finance.manager.databinding.ActivityComparePersonalLoanPlfBinding
import com.loancalculator.finance.manager.room.mPlfLoanRoom
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
                }

                "mortgages" -> {
                    mPlcBinding.topSetPlf.tvTitleAll.text = String.format(
                        getString(R.string.plf_compare_2),
                        getString(R.string.plf_personal_loan)
                    )
                }

                "autoLoan" -> {
                    mPlcBinding.topSetPlf.tvTitleAll.text = String.format(
                        getString(R.string.plf_compare_2),
                        getString(R.string.plf_business_loan)
                    )
                }
            }
        }
        setPlfRecyclerView()
    }

    override fun setPlfRecyclerView() {
        mAdapterComparePersonalLoanPlf = AdapterComparePersonalLoanPlf(this, mListData) {

        }
        mPlcBinding.rvRvView.layoutManager = LinearLayoutManager(this)
        mPlcBinding.rvRvView.adapter = mAdapterComparePersonalLoanPlf
    }

    override fun getLayoutValue(): ActivityComparePersonalLoanPlfBinding {
        return ActivityComparePersonalLoanPlfBinding.inflate(layoutInflater)
    }
}