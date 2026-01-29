package com.loancalculator.finance.manager.activity

import android.content.Intent
import com.loancalculator.finance.manager.activity.calc.PlfPersonalLoanActivity
import com.loancalculator.finance.manager.activity.utils.PlfToolsTemperatureActivity
import com.loancalculator.finance.manager.databinding.ActivityMainPlfBinding
import com.loancalculator.finance.manager.setSafeListener

class PlfMainToolActivity : PlfBindingActivity<ActivityMainPlfBinding>() {
    override fun beginViewAndDoLtd() {
        mPlcBinding.tvLoanOne.setSafeListener {
            startActivity(Intent(this, PlfPersonalLoanActivity::class.java))
        }
        mPlcBinding.tvLoanTwo.setSafeListener {
            startActivity(Intent(this, PlfToolsTemperatureActivity::class.java))
        }
    }

    override fun getLayoutValue(): ActivityMainPlfBinding {
        return ActivityMainPlfBinding.inflate(layoutInflater)
    }
}