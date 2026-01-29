package com.loancalculator.finance.manager.activity.set

import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.databinding.ActivitySetPolicyPlfBinding

class PlfSetPolicyActivity : PlfBindingActivity<ActivitySetPolicyPlfBinding>() {
    override fun beginViewAndDoLtd() {
        mPlcBinding.topSetPlf.tvTitleAll.text = getString(R.string.ltd_policy_message_plf)
    }

    override fun getLayoutValue(): ActivitySetPolicyPlfBinding {
        return ActivitySetPolicyPlfBinding.inflate(layoutInflater)
    }
}