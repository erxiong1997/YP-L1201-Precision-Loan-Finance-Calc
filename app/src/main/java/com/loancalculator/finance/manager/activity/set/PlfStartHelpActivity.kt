package com.loancalculator.finance.manager.activity.set

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.activity.other.PlfStartCurrencyUnitActivity
import com.loancalculator.finance.manager.adapter.AdapterStartHelpPlf
import com.loancalculator.finance.manager.data.DataStartHelpPlf
import com.loancalculator.finance.manager.databinding.ActivityStartHelpPlfBinding
import com.loancalculator.finance.manager.utils.DataManagerPlfUtils
import com.loancalculator.finance.manager.utils.value.ConstantNextPlf.PLF_ENTER_MAIN_RESULT

class PlfStartHelpActivity : PlfBindingActivity<ActivityStartHelpPlfBinding>() {

    private val mHelpDownloadList = mutableListOf<DataStartHelpPlf>()
    private lateinit var mAdapterStartHelpPlf: AdapterStartHelpPlf
    private var mNextCount = 0
    override fun beginViewAndDoPlf() {
        DataManagerPlfUtils.setDataKeyPlf(PLF_ENTER_MAIN_RESULT, "remind")
        setPlfRecyclerView()
        mPlcBinding.tvGoHome.setOnClickListener {
            if (mNextCount < 3) {
                mNextCount++
                mPlcBinding.rvHelpContent.smoothScrollToPosition(mNextCount)
                changeHelpPosition(mNextCount)
            } else {
                startActivity(Intent(this, PlfStartCurrencyUnitActivity::class.java))
                finish()
            }
        }
    }

    override fun setPlfRecyclerView() {
        mHelpDownloadList.add(
            DataStartHelpPlf(
                getString(R.string.plf_help_title1),
                getString(R.string.plf_help_des1),
                R.drawable.plf_til_help_img_1
            )
        )
        mHelpDownloadList.add(
            DataStartHelpPlf(
                getString(R.string.plf_help_title2),
                getString(R.string.plf_help_des2),
                R.drawable.plf_til_help_img_2
            )
        )
        mHelpDownloadList.add(
            DataStartHelpPlf(
                getString(R.string.plf_help_title3),
                getString(R.string.plf_help_des3),
                R.drawable.plf_til_help_img_3
            )
        )
        mHelpDownloadList.add(
            DataStartHelpPlf(
                getString(R.string.plf_help_title4),
                getString(R.string.plf_help_des4),
                R.drawable.plf_til_help_img_4
            )
        )
        mAdapterStartHelpPlf = AdapterStartHelpPlf(this, mHelpDownloadList)
        mPlcBinding.rvHelpContent.apply {
            layoutManager = LinearLayoutManager(
                this@PlfStartHelpActivity, LinearLayoutManager.HORIZONTAL, false
            )
            adapter = mAdapterStartHelpPlf
        }
        val onePageHelp = PagerSnapHelper()
        onePageHelp.attachToRecyclerView(mPlcBinding.rvHelpContent)
        mPlcBinding.rvHelpContent.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val snapView = onePageHelp.findSnapView(recyclerView.layoutManager)
                    snapView?.let {
                        val position = recyclerView.getChildAdapterPosition(it)
                        changeHelpPosition(position)
                    }
                }
            }
        })
    }

    private fun changeHelpPosition(position: Int) {
        mNextCount = position
        mPlcBinding.pointView.changePlfPosition(mNextCount)
        if (mNextCount == 3) {
            mPlcBinding.tvGoHome.text = getString(R.string.plf_start)
        } else {
            mPlcBinding.tvGoHome.text = getString(R.string.plf_next)
        }
    }

    override fun getLayoutValue(): ActivityStartHelpPlfBinding {
        return ActivityStartHelpPlfBinding.inflate(layoutInflater)
    }
}