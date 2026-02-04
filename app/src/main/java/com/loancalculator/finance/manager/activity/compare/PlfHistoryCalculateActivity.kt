package com.loancalculator.finance.manager.activity.compare

import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.databinding.ActivityHistoryCalculatePlfBinding

class PlfHistoryCalculateActivity: PlfBindingActivity<ActivityHistoryCalculatePlfBinding>() {
    override fun beginViewAndDoLtd() {
        
    }

    override fun getLayoutValue(): ActivityHistoryCalculatePlfBinding {
        return ActivityHistoryCalculatePlfBinding.inflate(layoutInflater)
    }
}