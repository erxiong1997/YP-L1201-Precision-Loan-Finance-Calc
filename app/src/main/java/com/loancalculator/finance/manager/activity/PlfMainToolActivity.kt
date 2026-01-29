package com.loancalculator.finance.manager.activity

import com.loancalculator.finance.manager.databinding.ActivityMainPlfBinding
import com.loancalculator.finance.manager.setSafeListener

class PlfMainToolActivity : PlfBindingActivity<ActivityMainPlfBinding>() {
    override fun beginViewAndDoLtd() {
        mPlcBinding.tvLoanOne.setSafeListener {

        }
    }

    override fun getLayoutValue(): ActivityMainPlfBinding {
        return ActivityMainPlfBinding.inflate(layoutInflater)
    }
}