package com.loancalculator.finance.manager.activity

import android.content.Intent
import com.loancalculator.finance.manager.activity.calc.PlfPersonalLoanActivity
import com.loancalculator.finance.manager.activity.utils.PlfToolsTemperatureActivity
import com.loancalculator.finance.manager.activity.utils.PlfToolsWorldTimeActivity
import com.loancalculator.finance.manager.databinding.ActivityMainPlfBinding
import com.loancalculator.finance.manager.setSafeListener
import com.loancalculator.finance.manager.utils.LtdTotalUtils

class PlfMainToolActivity : PlfBindingActivity<ActivityMainPlfBinding>() {
    override fun beginViewAndDoLtd() {
        mPlcBinding.tvLoanOne.setSafeListener {
            startActivity(Intent(this, PlfPersonalLoanActivity::class.java))
        }
        mPlcBinding.tvLoanTwo.setSafeListener {
            startActivity(Intent(this, PlfToolsTemperatureActivity::class.java))
        }
        mPlcBinding.tvLoanThree.setSafeListener {
            startActivity(Intent(this, PlfToolsWorldTimeActivity::class.java))
        }
        mPlcBinding.tvLoanThree.text =
            mPlcBinding.tvLoanThree.text.toString() + "=" + LtdTotalUtils.getCurrentTimeInZone("PST8PDT")
    }

    override fun getLayoutValue(): ActivityMainPlfBinding {
        return ActivityMainPlfBinding.inflate(layoutInflater)
    }
}