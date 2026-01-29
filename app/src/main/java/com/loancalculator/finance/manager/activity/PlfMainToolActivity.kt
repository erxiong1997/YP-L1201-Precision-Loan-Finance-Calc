package com.loancalculator.finance.manager.activity

import com.loancalculator.finance.manager.databinding.ActivityMainPlfBinding

class PlfMainToolActivity : PlfBindingActivity<ActivityMainPlfBinding>() {
    override fun beginViewAndDoLtd() {

    }

    override fun getLayoutValue(): ActivityMainPlfBinding {
        return ActivityMainPlfBinding.inflate(layoutInflater)
    }
}