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
import com.loancalculator.finance.manager.showToastIDPlf
import com.loancalculator.finance.manager.utils.DealRecentPlfUtils
import com.loancalculator.finance.manager.utils.TimeDatePlfUtils
import com.loancalculator.finance.manager.utils.dialog.DialogDeleteConfirmPlf

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
                                if (mShowEmpty) {
                                    mShowEmpty = false
                                    mListData.clear()
                                }
                                mListData.add(0, DataUtcSelectPlf(data, b).apply {
                                    mCurTime = a
                                    amOrPm = if (s > 12) {
                                        "PM"
                                    } else {
                                        "AM"
                                    }
                                })
                                if (mListData.size == 1) {
                                    mAdapterUtcSaveItemPlf.notifyDataSetChanged()
                                } else {
                                    mAdapterUtcSaveItemPlf.notifyItemInserted(0)
                                }
                            }
                        }
                        DealRecentPlfUtils.addWorldTimeUtcRecent(mListData)
                    }
                }
            }
        }

    private lateinit var mAdapterUtcSaveItemPlf: AdapterUtcSaveItemPlf
    private val mListData = mutableListOf<DataUtcSelectPlf>()

    private var mShowEmpty = false

    override fun beginViewAndDoPlf() {
        mPlcBinding.topSetPlf.tvTitleAll.text = getString(R.string.plf_world_clock)
        setPlfRecyclerView()
        mPlcBinding.tvAddClock.setSafeListener {
            mUtcSelectLauncher.launch(Intent(this, PlfWorldTimeSelectActivity::class.java))
        }
    }

    override fun setPlfRecyclerView() {
        mListData.clear()
        DealRecentPlfUtils.getWorldTimeUtcRecent(mListData)
        if (mListData.isEmpty()) {
            mShowEmpty = true
            fillEmptyList(true)
        } else {
            mShowEmpty = false
        }
        mAdapterUtcSaveItemPlf = AdapterUtcSaveItemPlf(this, mListData, {
            //long click
            val data = mListData[it]
            if (mShowEmpty) {
                showToastIDPlf(R.string.plf_the_example_content_not_delete)
                return@AdapterUtcSaveItemPlf
            }
            DialogDeleteConfirmPlf(
                this, String.format(
                    getString(R.string.plf_please_confirm_whether_delete_clock),
                    getString(R.string.plf_add_clock)
                )
            ) {
                mListData.removeAt(it)
                if (mListData.isEmpty()) {
                    mShowEmpty = true
                    fillEmptyList(false)
                } else {
                    mShowEmpty = false
                    mAdapterUtcSaveItemPlf.notifyItemRemoved(it)
                }
                DealRecentPlfUtils.addWorldTimeUtcRecent(mListData)
            }.show()
        }) {

        }
        mPlcBinding.rvRvView.layoutManager = LinearLayoutManager(this)
        mPlcBinding.rvRvView.adapter = mAdapterUtcSaveItemPlf
    }

    private val mEmptyList = mutableListOf(
        "Africa/Abidjan",
        "Africa/Accra",
        "Africa/Addis_Ababa",
    )

    private fun fillEmptyList(noUpdate: Boolean) {
        mListData.clear()
        for (data in mEmptyList) {
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
            }
        }
        if (!noUpdate) {
            mAdapterUtcSaveItemPlf.notifyDataSetChanged()
        }
    }

    override fun getLayoutValue(): ActivityToolsWorldTimePlfBinding {
        return ActivityToolsWorldTimePlfBinding.inflate(layoutInflater)
    }
}