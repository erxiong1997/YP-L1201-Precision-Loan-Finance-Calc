package com.loancalculator.finance.manager.activity.utils

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.adapter.AdapterToolsUnitItemPlf
import com.loancalculator.finance.manager.data.DataUnitSelectPlf
import com.loancalculator.finance.manager.databinding.ActivityToolsUnitSelectPlfBinding
import com.loancalculator.finance.manager.setSafeListener
import com.loancalculator.finance.manager.utils.value.ParamsPlfUtils.mLengthUnitList
import com.loancalculator.finance.manager.utils.value.ParamsPlfUtils.mMassUnitList
import com.loancalculator.finance.manager.utils.value.ParamsPlfUtils.mSpeedUnitList
import com.loancalculator.finance.manager.utils.value.ParamsPlfUtils.mTemperatureUnitList

class PlfToolsUnitSelectActivity : PlfBindingActivity<ActivityToolsUnitSelectPlfBinding>() {
    private var mUnitClass: String? = null
    private var mPrePosition: Int = 0
    private val mListData = mutableListOf<DataUnitSelectPlf>()
    private lateinit var mAdapterToolsUnitItemPlf: AdapterToolsUnitItemPlf

    private var mSelectPosition = 0

    override fun beginViewAndDoPlf() {
        intent?.let {
            mUnitClass = it.getStringExtra("unitClass") ?: ""
            mPrePosition = it.getIntExtra("prePosition", -1)
        }
        if (mPrePosition > -1) {
            mSelectPosition = mPrePosition
        }
        getUnitClassList()
        setPlfRecyclerView()

        mPlcBinding.topSetPlf.ivSelect.setSafeListener {
            setResult(RESULT_OK, Intent().apply {
                putExtra("position", mSelectPosition)
            })
            finish()
        }
    }

    override fun setPlfRecyclerView() {
        mAdapterToolsUnitItemPlf = AdapterToolsUnitItemPlf(this, mListData) {
            val data = mListData[it]
            if (data.fingerSelect) return@AdapterToolsUnitItemPlf
            for (i in mListData.indices) {
                if (mListData[i].fingerSelect) {
                    mListData[i].fingerSelect = false
                    mAdapterToolsUnitItemPlf.notifyItemChanged(i)
                    break
                }
            }
            data.fingerSelect = true
            mSelectPosition = it
            mAdapterToolsUnitItemPlf.notifyItemChanged(it)
        }
        mPlcBinding.rvRvView.layoutManager = LinearLayoutManager(this)
        mPlcBinding.rvRvView.adapter = mAdapterToolsUnitItemPlf
    }

    private fun getUnitClassList() {
        when (mUnitClass) {
            "temperature" -> {
                mPlcBinding.topSetPlf.tvTitleAll.text = getString(R.string.plf_temperature)
                mTemperatureUnitList.forEachIndexed { index, data ->
                    mListData.add(DataUnitSelectPlf(data.symbol, data.displayName).apply {
                        if (index == mSelectPosition) {
                            fingerSelect = true
                        }
                    })
                }
            }

            "speed" -> {
                mPlcBinding.topSetPlf.tvTitleAll.text = getString(R.string.plf_speed_convert)
                mSpeedUnitList.forEachIndexed { index, data ->
                    mListData.add(DataUnitSelectPlf(data.symbol, data.displayName).apply {
                        if (index == mSelectPosition) {
                            fingerSelect = true
                        }
                    })
                }
            }

            "mass" -> {
                mPlcBinding.topSetPlf.tvTitleAll.text = getString(R.string.plf_mass_convert)
                mMassUnitList.forEachIndexed { index, data ->
                    mListData.add(DataUnitSelectPlf(data.symbol, data.displayName).apply {
                        if (index == mSelectPosition) {
                            fingerSelect = true
                        }
                    })
                }
            }

            "length" -> {
                mPlcBinding.topSetPlf.tvTitleAll.text = getString(R.string.plf_length_convert)
                mLengthUnitList.forEachIndexed { index, data ->
                    mListData.add(DataUnitSelectPlf(data.symbol, data.displayName).apply {
                        if (index == mSelectPosition) {
                            fingerSelect = true
                        }
                    })
                }
            }
        }
    }

    override fun getLayoutValue(): ActivityToolsUnitSelectPlfBinding {
        return ActivityToolsUnitSelectPlfBinding.inflate(layoutInflater)
    }
}