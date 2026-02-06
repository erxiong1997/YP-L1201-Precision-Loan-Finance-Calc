package com.loancalculator.finance.manager.activity.other

import androidx.recyclerview.widget.LinearLayoutManager
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.adapter.AdapterCurrencyUnitItemPlf
import com.loancalculator.finance.manager.data.DataCurrencyUnitPlf
import com.loancalculator.finance.manager.databinding.ActivityCurrencyUnitPlfBinding
import com.loancalculator.finance.manager.setSafeListener
import com.loancalculator.finance.manager.utils.value.ParamsLtdUtils.mCurrencyListData
import com.loancalculator.finance.manager.utils.value.ParamsLtdUtils.mDataCurrencyUnitPlf
import com.loancalculator.finance.manager.utils.value.ParamsLtdUtils.mRateCurrencyPlf

class PlfCurrencyUnitActivity : PlfBindingActivity<ActivityCurrencyUnitPlfBinding>(
    mBarTextWhite = false
) {
    private lateinit var mAdapterCurrencyUnitItemPlf: AdapterCurrencyUnitItemPlf
    private var mListData = mutableListOf<DataCurrencyUnitPlf>()
    private var mUnitClass = ""
    override fun beginViewAndDoLtd() {
        intent?.let {
            mUnitClass = it.getStringExtra("unitClass") ?: ""
        }
        mPlcBinding.topSetPlf.tvTitleAll.text = getString(R.string.plf_currency_unit)
        mDataCurrencyUnitPlf?.let {
            for (d in mListData) {
                if (d.currencyUnit == it.currencyUnit) {
                    d.fingerSelect = true
                    break
                }
            }
        }
        setPlfRecyclerView()

        mPlcBinding.topSetPlf.ivSelect.setSafeListener {
            for (data in mListData) {
                if (data.fingerSelect) {
                    if (mUnitClass == "convert") {
                        mRateCurrencyPlf = data
                    } else {
                        mDataCurrencyUnitPlf = data
                    }
                    setResult(RESULT_OK)
                    finish()
                    break
                }
            }
        }
    }

    override fun setPlfRecyclerView() {
        mListData.clear()
        mListData.addAll(mCurrencyListData)
        mAdapterCurrencyUnitItemPlf = AdapterCurrencyUnitItemPlf(this, mListData) {
            val data = mListData[it]
            if (data.fingerSelect) return@AdapterCurrencyUnitItemPlf
            for (i in mListData.indices) {
                if (mListData[i].fingerSelect) {
                    mListData[i].fingerSelect = false
                    mAdapterCurrencyUnitItemPlf.notifyItemChanged(i)
                    break
                }
            }
            data.fingerSelect = true
            mAdapterCurrencyUnitItemPlf.notifyItemChanged(it)
        }
        mPlcBinding.rvRvView.layoutManager = LinearLayoutManager(this)
        mPlcBinding.rvRvView.adapter = mAdapterCurrencyUnitItemPlf
    }

    override fun getLayoutValue(): ActivityCurrencyUnitPlfBinding {
        return ActivityCurrencyUnitPlfBinding.inflate(layoutInflater)
    }
}