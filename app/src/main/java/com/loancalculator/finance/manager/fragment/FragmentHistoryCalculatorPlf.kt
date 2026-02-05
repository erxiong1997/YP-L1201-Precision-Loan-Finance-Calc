package com.loancalculator.finance.manager.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.loancalculator.finance.manager.activity.PlfRootActivity
import com.loancalculator.finance.manager.activity.utils.PlfToolsSpeedConvertActivity
import com.loancalculator.finance.manager.activity.utils.PlfToolsTemperatureActivity
import com.loancalculator.finance.manager.activity.utils.PlfToolsWorldTimeActivity
import com.loancalculator.finance.manager.adapter.AdapterHistoryCalculatorPlf
import com.loancalculator.finance.manager.data.DataPersonalLoanPlf
import com.loancalculator.finance.manager.databinding.FragmentHistoryCalculatorPlfBinding
import com.loancalculator.finance.manager.databinding.FragmentToolsPlfBinding
import com.loancalculator.finance.manager.room.mPlfLoanRoom
import com.loancalculator.finance.manager.setSafeListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FragmentHistoryCalculatorPlf : RootPlfFragment<FragmentHistoryCalculatorPlfBinding>() {
    companion object {
        private const val TIL_PAGE = "til_page"

        fun newInstance(page: Int): FragmentHistoryCalculatorPlf {
            val fragment = FragmentHistoryCalculatorPlf()
            fragment.arguments = Bundle().apply {
                putInt(TIL_PAGE, page)
            }
            return fragment
        }
    }

    private var mTilPersonalLoanDao = mPlfLoanRoom.mTilPersonalLoanDao()

    private var mAdapterHistoryCalculatorPlf: AdapterHistoryCalculatorPlf? = null
    private val mListData = mutableListOf<DataPersonalLoanPlf>()

    override fun startCreateContent(
        rootActivity: PlfRootActivity,
        view: View,
        bundle: Bundle?
    ) {
        setPlfRecyclerView(rootActivity)
        getListDataPlf()
    }

    override fun setPlfRecyclerView(rootActivity: PlfRootActivity) {

        mAdapterHistoryCalculatorPlf = AdapterHistoryCalculatorPlf(rootActivity, mListData) {
            val data = mListData[it]
        }
        mPlfBinding.rvRvView.layoutManager = LinearLayoutManager(rootActivity)
        mPlfBinding.rvRvView.adapter = mAdapterHistoryCalculatorPlf
    }

    private fun getListDataPlf() {

    }

    override fun getLayoutValue(): FragmentHistoryCalculatorPlfBinding {
        return FragmentHistoryCalculatorPlfBinding.inflate(layoutInflater)

    }
}