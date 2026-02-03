package com.loancalculator.finance.manager.activity.utils

import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.adapter.AdapterUtcSaveItemPlf
import com.loancalculator.finance.manager.data.DataUtcSelectPlf
import com.loancalculator.finance.manager.databinding.ActivityToolsWorldTimePlfBinding
import com.loancalculator.finance.manager.setSafeListener
import com.loancalculator.finance.manager.utils.DealRecentPlfUtils
import com.loancalculator.finance.manager.utils.TimeDatePlfUtils

class PlfToolsWorldTimeActivity : PlfBindingActivity<ActivityToolsWorldTimePlfBinding>() {
    private val mUtcSelectLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.let {
                    val utcValue = it.getStringExtra("utcValue")
                    if (!utcValue.isNullOrEmpty()) {
                        val utcValueList = utcValue.split("::")
                        for (data in utcValueList) {
                            if (data.isNotEmpty()) {
                                val (a, b) = TimeDatePlfUtils.getCurrentTimeInZoneOffset(data)
                                val s = a.split(":")[0].toInt()
                                mListData.add(0, DataUtcSelectPlf(data, b).apply {
                                    mCurTime = a
                                    amOrPm = if (s > 12) {
                                        "PM"
                                    } else {
                                        "AM"
                                    }
                                })
                                mAdapterUtcSaveItemPlf.notifyItemInserted(0)
                            }
                        }
                        DealRecentPlfUtils.addWorldTimeUtcRecent(mListData)
                    }
                }
            }
        }

    private lateinit var mAdapterUtcSaveItemPlf: AdapterUtcSaveItemPlf
    private val mListData = mutableListOf<DataUtcSelectPlf>()

    override fun beginViewAndDoLtd() {
        mPlcBinding.topSetPlf.tvTitleAll.text = getString(R.string.plf_world_clock)
        setPlfRecyclerView()
        mPlcBinding.tvAddClock.setSafeListener {
            mUtcSelectLauncher.launch(Intent(this, PlfWorldTimeSelectActivity::class.java))
        }
    }

    override fun setPlfRecyclerView() {
        mListData.clear()
        DealRecentPlfUtils.getWorldTimeUtcRecent(mListData)
        mAdapterUtcSaveItemPlf = AdapterUtcSaveItemPlf(this, mListData) {

        }
        mPlcBinding.rvRvView.layoutManager = LinearLayoutManager(this)
        mPlcBinding.rvRvView.adapter = mAdapterUtcSaveItemPlf
    }

    override fun getLayoutValue(): ActivityToolsWorldTimePlfBinding {
        return ActivityToolsWorldTimePlfBinding.inflate(layoutInflater)
    }
}