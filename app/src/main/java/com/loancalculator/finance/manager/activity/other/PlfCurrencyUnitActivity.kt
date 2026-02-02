package com.loancalculator.finance.manager.activity.other

import androidx.recyclerview.widget.LinearLayoutManager
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.adapter.AdapterCurrencyUnitItemPlf
import com.loancalculator.finance.manager.data.DataCurrencyUnitPlf
import com.loancalculator.finance.manager.databinding.ActivityCurrencyUnitPlfBinding

class PlfCurrencyUnitActivity : PlfBindingActivity<ActivityCurrencyUnitPlfBinding>() {
    private val mListData = mutableListOf(
        DataCurrencyUnitPlf(R.drawable.plf_til_home_more, "GBP", "UK Pound", "£"),
        DataCurrencyUnitPlf(R.drawable.plf_til_home_tools_icon_1, "EUR", "Euro", "€"),
        DataCurrencyUnitPlf(R.drawable.plf_til_home_more, "AUD", "Australian Dollar", "$"),
        DataCurrencyUnitPlf(R.drawable.plf_til_home_tools_icon_1, "USD", "US Dollar", "$"),
        DataCurrencyUnitPlf(R.drawable.plf_til_home_more, "CNY", "Chinese Yuan", "¥"),
        DataCurrencyUnitPlf(R.drawable.plf_til_home_tools_icon_1, "INR", "Indian Rupee", "₹"),
        DataCurrencyUnitPlf(R.drawable.plf_til_home_more, "VND", "Vietnamese Dong", "₫"),
        DataCurrencyUnitPlf(R.drawable.plf_til_home_tools_icon_1, "THB", "Thai Baht", "฿"),
        DataCurrencyUnitPlf(R.drawable.plf_til_home_more, "IDR", "Indonesian Rupiah", "Rp"),
    )
    private lateinit var mAdapterCurrencyUnitItemPlf: AdapterCurrencyUnitItemPlf

    override fun beginViewAndDoLtd() {
        setPlfRecyclerView()
    }

    override fun setPlfRecyclerView() {
        mAdapterCurrencyUnitItemPlf = AdapterCurrencyUnitItemPlf(this, mListData) {
            val data = mListData[it]
//            mSelectCurrencyUnit = data.currencyUnit
        }
        mPlcBinding.rvRvView.layoutManager = LinearLayoutManager(this)
        mPlcBinding.rvRvView.adapter = mAdapterCurrencyUnitItemPlf
    }

    override fun getLayoutValue(): ActivityCurrencyUnitPlfBinding {
        return ActivityCurrencyUnitPlfBinding.inflate(layoutInflater)
    }
}