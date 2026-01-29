package com.loancalculator.finance.manager.activity.utils

import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.databinding.ActivityToolsWorldTimePlfBinding
import com.loancalculator.finance.manager.setSafeListener
import com.loancalculator.finance.manager.utils.LtdTotalUtils

class PlfToolsWorldTimeActivity : PlfBindingActivity<ActivityToolsWorldTimePlfBinding>() {

    private val mUtcSelectLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.let {
                    val utcValue = it.getStringExtra("utcValue")
                    if (!utcValue.isNullOrEmpty()) {
                        val re = LtdTotalUtils.getCurrentTimeInZone(utcValue)
                        mPlcBinding.tvCurTime.text = re
                    }
                }
            }
        }

    override fun beginViewAndDoLtd() {
        mPlcBinding.btn1.setSafeListener {
            mUtcSelectLauncher.launch(Intent(this, PlfWorldTimeSelectActivity::class.java))
        }
    }

    override fun getLayoutValue(): ActivityToolsWorldTimePlfBinding {
        return ActivityToolsWorldTimePlfBinding.inflate(layoutInflater)
    }
}