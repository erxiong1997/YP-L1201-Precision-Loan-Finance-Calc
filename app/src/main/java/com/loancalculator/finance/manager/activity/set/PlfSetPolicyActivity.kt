package com.loancalculator.finance.manager.activity.set

import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.databinding.ActivitySetPolicyPlfBinding

class PlfSetPolicyActivity : PlfBindingActivity<ActivitySetPolicyPlfBinding>() {
    override fun beginViewAndDoPlf() {
        mPlcBinding.topSetPlf.tvTitleAll.text = getString(R.string.plf_privacy_policy)
    }

    override fun getLayoutValue(): ActivitySetPolicyPlfBinding {
        return ActivitySetPolicyPlfBinding.inflate(layoutInflater)
    }
}