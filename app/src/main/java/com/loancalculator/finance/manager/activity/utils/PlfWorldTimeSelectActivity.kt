package com.loancalculator.finance.manager.activity.utils

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.adapter.AdapterWorldTimeSelectPlf
import com.loancalculator.finance.manager.data.DataUtcSelectPlf
import com.loancalculator.finance.manager.databinding.ActivityWorldTimeSelectPlfBinding
import com.loancalculator.finance.manager.setSafeListener
import com.loancalculator.finance.manager.utils.value.ParamsLtdUtils.mWorldTimeUtcList

class PlfWorldTimeSelectActivity : PlfBindingActivity<ActivityWorldTimeSelectPlfBinding>() {
    private lateinit var mAdapterWorldTimeSelectPlf: AdapterWorldTimeSelectPlf
    private val mListDoData = mutableListOf<DataUtcSelectPlf>()
    private val mTotalListDoData = mutableListOf<DataUtcSelectPlf>()

    private var mSelectCount = 0

    init {
        mHandlerLtd = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    1189 -> {
                        val text = mPlcBinding.etSearch.text.toString().trim()
                        val list = mTotalListDoData.filter {
                            it.utcPlf.contains(text, true)
                        }
                        mListDoData.clear()
                        mListDoData.addAll(list)
                        mAdapterWorldTimeSelectPlf.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun beginViewAndDoLtd() {
        mPlcBinding.topSetPlf.tvTitleAll.text = getString(R.string.plf_world_clock)
        setPlfRecyclerView()
        mPlcBinding.etSearch.doAfterTextChanged {
            if (it.isNullOrEmpty()) {
                mListDoData.clear()
                mListDoData.addAll(mTotalListDoData)
                mAdapterWorldTimeSelectPlf.notifyDataSetChanged()
            } else {
                mHandlerLtd?.removeMessages(1189)
                mHandlerLtd?.sendEmptyMessageDelayed(1189, 64)
            }
        }
        mPlcBinding.topSetPlf.ivSelect.visibility = View.INVISIBLE
        mPlcBinding.topSetPlf.ivSelect.setSafeListener {
            var result = ""
            for (data in mListDoData) {
                if (data.fingerSelect) {
                    result += data.utcPlf
                    result += "::"
                }
            }
            if (result.isNotEmpty()) {
                setResult(RESULT_OK, Intent().apply {
                    putExtra("utcValue", result)
                })
                finish()
            }
        }
    }

    override fun setPlfRecyclerView() {
        mListDoData.clear()
        mTotalListDoData.clear()
        mWorldTimeUtcList.forEach {
            mTotalListDoData.add(DataUtcSelectPlf(it, ""))
        }
        mListDoData.addAll(mTotalListDoData)
        mAdapterWorldTimeSelectPlf = AdapterWorldTimeSelectPlf(this, mListDoData) {
            val data = mListDoData[it]
            data.fingerSelect = !data.fingerSelect
            if (data.fingerSelect) {
                mSelectCount++
            } else {
                mSelectCount--
            }
            if (mSelectCount == 0) {
                mPlcBinding.topSetPlf.ivSelect.visibility = View.INVISIBLE
            } else {
                mPlcBinding.topSetPlf.ivSelect.visibility = View.VISIBLE
            }
            mAdapterWorldTimeSelectPlf.notifyItemChanged(it)
        }
        mPlcBinding.rvRvView.layoutManager = LinearLayoutManager(this)
        mPlcBinding.rvRvView.adapter = mAdapterWorldTimeSelectPlf
    }

    override fun getLayoutValue(): ActivityWorldTimeSelectPlfBinding {
        return ActivityWorldTimeSelectPlfBinding.inflate(layoutInflater)
    }
}