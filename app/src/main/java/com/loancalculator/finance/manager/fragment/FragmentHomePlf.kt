package com.loancalculator.finance.manager.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.loancalculator.finance.manager.activity.PlfRootActivity
import com.loancalculator.finance.manager.activity.calc.PlfAutoLoanActivity
import com.loancalculator.finance.manager.activity.calc.PlfBusinessLoanActivity
import com.loancalculator.finance.manager.activity.calc.PlfCalculateMortgagesResultActivity
import com.loancalculator.finance.manager.activity.calc.PlfCalculateResultActivity
import com.loancalculator.finance.manager.activity.calc.PlfCalculateResultTwoActivity
import com.loancalculator.finance.manager.activity.calc.PlfFixedDepositActivity
import com.loancalculator.finance.manager.activity.calc.PlfInvestmentResultActivity
import com.loancalculator.finance.manager.activity.calc.PlfMortgagesActivity
import com.loancalculator.finance.manager.activity.calc.PlfPersonalLoanActivity
import com.loancalculator.finance.manager.activity.calc.PlfRecurringDepositActivity
import com.loancalculator.finance.manager.activity.compare.PlfHistoryCalculateActivity
import com.loancalculator.finance.manager.adapter.AdapterHistoryCalculatorPlf
import com.loancalculator.finance.manager.data.DataPersonalLoanPlf
import com.loancalculator.finance.manager.data.EventManagerHome
import com.loancalculator.finance.manager.databinding.FragmentHomePlfBinding
import com.loancalculator.finance.manager.room.mPlfLoanRoom
import com.loancalculator.finance.manager.setSafeListener
import com.loancalculator.finance.manager.utils.ToolsLoanMonthDetailUtils.mDataPersonalLoanPlf
import com.loancalculator.finance.manager.utils.value.LoanTypePlf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class FragmentHomePlf : RootPlfFragment<FragmentHomePlfBinding>() {
    companion object {
        private const val TIL_PAGE = "til_page"

        fun newInstance(page: Int): FragmentHomePlf {
            val fragment = FragmentHomePlf()
            fragment.arguments = Bundle().apply {
                putInt(TIL_PAGE, page)
            }
            return fragment
        }
    }

    private lateinit var mAdapterHistoryCalculatorPlf: AdapterHistoryCalculatorPlf
    private val mListData = mutableListOf<DataPersonalLoanPlf>()

    private var mTilPersonalLoanDao = mPlfLoanRoom.mTilPersonalLoanDao()

    override fun startCreateContent(
        rootActivity: PlfRootActivity,
        view: View,
        bundle: Bundle?
    ) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        mPlfBinding.tvPersonalLoan.setSafeListener {
            startActivity(Intent(rootActivity, PlfPersonalLoanActivity::class.java))
        }
        mPlfBinding.tvBusinessLoan.setSafeListener {
            startActivity(Intent(rootActivity, PlfBusinessLoanActivity::class.java))
        }
        mPlfBinding.tvMortgages.setSafeListener {
            startActivity(Intent(rootActivity, PlfMortgagesActivity::class.java))
        }
        mPlfBinding.tvAutoLoan.setSafeListener {
            startActivity(Intent(rootActivity, PlfAutoLoanActivity::class.java))
        }
        mPlfBinding.llRecurringDeposit.setSafeListener {
            startActivity(Intent(rootActivity, PlfRecurringDepositActivity::class.java))
        }
        mPlfBinding.llFixedDeposit.setSafeListener {
            startActivity(Intent(rootActivity, PlfFixedDepositActivity::class.java))
        }
        mPlfBinding.tvMore.setSafeListener {
            startActivity(Intent(rootActivity, PlfHistoryCalculateActivity::class.java))
        }
        mPlfBinding.ivMore.setSafeListener {
            startActivity(Intent(rootActivity, PlfHistoryCalculateActivity::class.java))
        }
        setPlfRecyclerView(rootActivity)
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch(Dispatchers.Main) {
            delay(64)
            (activity as? PlfRootActivity)?.let {

            }
        }
    }

    override fun setPlfRecyclerView(rootActivity: PlfRootActivity) {
        if (::mAdapterHistoryCalculatorPlf.isInitialized) return
        val list = mTilPersonalLoanDao.getLatestItems(4)
        mListData.clear()
        mListData.addAll(list)
        mAdapterHistoryCalculatorPlf = AdapterHistoryCalculatorPlf(false, rootActivity, mListData) {
            val data = mListData[it]
            mDataPersonalLoanPlf = data
            when (data.loanType) {
                LoanTypePlf.PERSONAL, LoanTypePlf.AUTO -> {
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
                        ).apply { putExtra("model", "details") }
                    )
                }

                LoanTypePlf.MORTGAGES -> {
                    mDataPersonalLoanPlf = data
                    startActivity(
                        Intent(
                            rootActivity, PlfCalculateMortgagesResultActivity::class.java
                        ).apply { putExtra("model", "details") }
                    )
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
        mPlfBinding.rvRvView.layoutManager = LinearLayoutManager(rootActivity)
        mPlfBinding.rvRvView.adapter = mAdapterHistoryCalculatorPlf
        if (mListData.isNotEmpty()) {
            mPlfBinding.llHistory.visibility = View.VISIBLE
            mPlfBinding.rvRvView.visibility = View.VISIBLE
        }
    }

    private fun getListDataPlf() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun managerHome(eventManagerHome: EventManagerHome) {
        when (eventManagerHome.managerType) {
            "updateHistory" -> {
                if (!::mAdapterHistoryCalculatorPlf.isInitialized) return
                val list = mTilPersonalLoanDao.getLatestItems(4)
                mListData.clear()
                mListData.addAll(list)
                mAdapterHistoryCalculatorPlf.notifyDataSetChanged()
                if (mListData.isNotEmpty()) {
                    mPlfBinding.llHistory.visibility = View.VISIBLE
                    mPlfBinding.rvRvView.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun getLayoutValue(): FragmentHomePlfBinding {
        return FragmentHomePlfBinding.inflate(layoutInflater)
    }
}