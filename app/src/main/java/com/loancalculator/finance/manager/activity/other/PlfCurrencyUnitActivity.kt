package com.loancalculator.finance.manager.activity.other

import androidx.recyclerview.widget.LinearLayoutManager
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.adapter.AdapterCurrencyUnitItemPlf
import com.loancalculator.finance.manager.data.DataCurrencyUnitPlf
import com.loancalculator.finance.manager.databinding.ActivityCurrencyUnitPlfBinding
import com.loancalculator.finance.manager.setSafeListener
import com.loancalculator.finance.manager.utils.value.ParamsPlfUtils.mCurrencyListData
import com.loancalculator.finance.manager.utils.value.ParamsPlfUtils.mDataCurrencyUnitPlf
import com.loancalculator.finance.manager.utils.value.ParamsPlfUtils.mRateCurrencyPlf

/**
 * 选择当前货币符号
 */
class PlfCurrencyUnitActivity : PlfBindingActivity<ActivityCurrencyUnitPlfBinding>(
    mBarTextWhite = false
) {
    private lateinit var mAdapterCurrencyUnitItemPlf: AdapterCurrencyUnitItemPlf
    private var mListData = mutableListOf<DataCurrencyUnitPlf>()
    private var mUnitClass = ""
    private var mMovePosition = 0
    override fun beginViewAndDoPlf() {
        intent?.let {
            mUnitClass = it.getStringExtra("unitClass") ?: ""
        }
        mPlcBinding.topSetPlf.tvTitleAll.text = getString(R.string.plf_currency_unit)
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
        mCurrencyListData.forEach {
            it.fingerSelect = false
        }
        mListData.clear()
        mListData.addAll(mCurrencyListData)
        if (mUnitClass == "convert") {
            mRateCurrencyPlf?.let {
                for (i in mListData.indices) {
                    if (mListData[i].currencyUnit == it.currencyUnit) {
                        mMovePosition = i
                        mListData[i].fingerSelect = true
                        break
                    }
                }
            }
        } else {
            mDataCurrencyUnitPlf?.let {
                for (i in mListData.indices) {
                    if (mListData[i].currencyUnit == it.currencyUnit) {
                        mMovePosition = i
                        mListData[i].fingerSelect = true
                        break
                    }
                }
            }
        }
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

        if (mMovePosition > 4) {
            mPlcBinding.rvRvView.postDelayed(
                {
                    mPlcBinding.rvRvView.smoothScrollToPosition(mMovePosition)
                }, 636
            )
        }
    }

    override fun getLayoutValue(): ActivityCurrencyUnitPlfBinding {
        return ActivityCurrencyUnitPlfBinding.inflate(layoutInflater)
    }
}