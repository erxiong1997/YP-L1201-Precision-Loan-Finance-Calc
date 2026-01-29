package com.loancalculator.finance.manager.activity.utils

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.adapter.AdapterWorldTimeSelectPlf
import com.loancalculator.finance.manager.data.DataUtcSelectPlf
import com.loancalculator.finance.manager.databinding.ActivityWorldTimeSelectPlfBinding
import com.loancalculator.finance.manager.utils.value.ParamsLtdUtils.mWorldTimeUtcList

class PlfWorldTimeSelectActivity : PlfBindingActivity<ActivityWorldTimeSelectPlfBinding>() {
    private lateinit var mAdapterWorldTimeSelectPlf: AdapterWorldTimeSelectPlf
    private val mListDoData = mutableListOf<DataUtcSelectPlf>()

    override fun beginViewAndDoLtd() {
        mPlcBinding.topSetPlf
        setPlfRecyclerView()
    }

    override fun setPlfRecyclerView() {
        mListDoData.clear()
        mWorldTimeUtcList.forEach {
            mListDoData.add(DataUtcSelectPlf(it))
        }
        mAdapterWorldTimeSelectPlf = AdapterWorldTimeSelectPlf(this, mListDoData) {
            val data = mListDoData[it]
            setResult(RESULT_OK, Intent().apply {
                putExtra("utcValue", data.utcPlf)
            })
            finish()
        }
        mPlcBinding.rvRvView.layoutManager = LinearLayoutManager(this)
        mPlcBinding.rvRvView.adapter = mAdapterWorldTimeSelectPlf
    }

    override fun getLayoutValue(): ActivityWorldTimeSelectPlfBinding {
        return ActivityWorldTimeSelectPlfBinding.inflate(layoutInflater)
    }
}