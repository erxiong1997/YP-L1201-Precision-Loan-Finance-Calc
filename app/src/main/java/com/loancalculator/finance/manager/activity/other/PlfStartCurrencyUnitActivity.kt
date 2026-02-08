package com.loancalculator.finance.manager.activity.other

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.activity.PlfMainToolActivity
import com.loancalculator.finance.manager.adapter.AdapterCurrencyUnitItemPlf
import com.loancalculator.finance.manager.data.DataCurrencyUnitPlf
import com.loancalculator.finance.manager.databinding.ActivityCurrencyUnitPlfBinding
import com.loancalculator.finance.manager.setSafeListener
import com.loancalculator.finance.manager.utils.DealRecentPlfUtils
import com.loancalculator.finance.manager.utils.value.ParamsPlfUtils.mCurrencyListData
import com.loancalculator.finance.manager.utils.value.ParamsPlfUtils.mDataCurrencyUnitPlf
import com.loancalculator.finance.manager.utils.value.ParamsPlfUtils.mRateCurrencyPlf

class PlfStartCurrencyUnitActivity : PlfBindingActivity<ActivityCurrencyUnitPlfBinding>(
    mBarTextWhite = false
) {
    private lateinit var mAdapterCurrencyUnitItemPlf: AdapterCurrencyUnitItemPlf
    private var mListData = mutableListOf<DataCurrencyUnitPlf>()

    override fun beginViewAndDoPlf() {
        mPlcBinding.topSetPlf.ivBackAll.visibility = View.INVISIBLE
        mPlcBinding.topSetPlf.tvTitleAll.text = getString(R.string.plf_currency_unit)
        setPlfRecyclerView()

        mPlcBinding.topSetPlf.ivSelect.setSafeListener {
            for (data in mListData) {
                if (data.fingerSelect) {
                    mDataCurrencyUnitPlf = data
                    DealRecentPlfUtils.addCurrencyUnitRecent(data)
                    startActivity(Intent(this, PlfMainToolActivity::class.java))
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