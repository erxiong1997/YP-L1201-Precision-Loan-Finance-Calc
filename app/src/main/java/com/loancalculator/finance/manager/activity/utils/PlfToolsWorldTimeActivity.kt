package com.loancalculator.finance.manager.activity.utils

import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.adapter.AdapterUtcSaveItemPlf
import com.loancalculator.finance.manager.data.DataUtcSelectPlf
import com.loancalculator.finance.manager.databinding.ActivityToolsWorldTimePlfBinding
import com.loancalculator.finance.manager.setSafeListener
import com.loancalculator.finance.manager.utils.TimeDatePlfUtils

class PlfToolsWorldTimeActivity : PlfBindingActivity<ActivityToolsWorldTimePlfBinding>() {
    private val mUtcSelectLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.let {
                    val utcValue = it.getStringExtra("utcValue")
                    if (!utcValue.isNullOrEmpty()) {
                        val (a, b) = TimeDatePlfUtils.getCurrentTimeInZoneOffset(utcValue)
                        mPlcBinding.tvCurTime.text = a
                        mListData.add(0, DataUtcSelectPlf(utcValue, b).apply {
                            mCurTime = a
                        })
                        mAdapterUtcSaveItemPlf.notifyItemInserted(0)
                    }
                }
            }
        }

    private lateinit var mAdapterUtcSaveItemPlf: AdapterUtcSaveItemPlf
    private val mListData = mutableListOf<DataUtcSelectPlf>()

    override fun beginViewAndDoLtd() {
        setPlfRecyclerView()
        mPlcBinding.btn1.setSafeListener {
            mUtcSelectLauncher.launch(Intent(this, PlfWorldTimeSelectActivity::class.java))
        }
    }

    override fun setPlfRecyclerView() {
        mAdapterUtcSaveItemPlf = AdapterUtcSaveItemPlf(this, mListData) {

        }
        mPlcBinding.rvRvView.layoutManager = LinearLayoutManager(this)
        mPlcBinding.rvRvView.adapter = mAdapterUtcSaveItemPlf
    }

    override fun getLayoutValue(): ActivityToolsWorldTimePlfBinding {
        return ActivityToolsWorldTimePlfBinding.inflate(layoutInflater)
    }
}