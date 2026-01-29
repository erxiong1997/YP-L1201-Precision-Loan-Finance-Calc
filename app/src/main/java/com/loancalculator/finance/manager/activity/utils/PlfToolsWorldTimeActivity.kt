package com.loancalculator.finance.manager.activity.utils

import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.databinding.ActivityToolsWorldTimePlfBinding

class PlfToolsWorldTimeActivity: PlfBindingActivity<ActivityToolsWorldTimePlfBinding>() {
    override fun beginViewAndDoLtd() {
        
    }

    override fun getLayoutValue(): ActivityToolsWorldTimePlfBinding {
        return ActivityToolsWorldTimePlfBinding.inflate(layoutInflater)
    }
}