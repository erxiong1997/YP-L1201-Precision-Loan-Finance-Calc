package com.loancalculator.finance.manager.activity.calc

import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.databinding.ActivityPersonalLoanPlfBinding

class PlfPersonalLoanActivity : PlfBindingActivity<ActivityPersonalLoanPlfBinding>() {
    override fun beginViewAndDoLtd() {

    }

    override fun getLayoutValue(): ActivityPersonalLoanPlfBinding {
        return ActivityPersonalLoanPlfBinding.inflate(layoutInflater)
    }
}