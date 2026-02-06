package com.loancalculator.finance.manager.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.loancalculator.finance.manager.activity.PlfRootActivity
import com.loancalculator.finance.manager.activity.utils.PlfToolsExchangeRateActivity
import com.loancalculator.finance.manager.activity.utils.PlfToolsLengthConvertActivity
import com.loancalculator.finance.manager.activity.utils.PlfToolsMassConvertActivity
import com.loancalculator.finance.manager.activity.utils.PlfToolsSpeedConvertActivity
import com.loancalculator.finance.manager.activity.utils.PlfToolsTemperatureActivity
import com.loancalculator.finance.manager.activity.utils.PlfToolsWorldTimeActivity
import com.loancalculator.finance.manager.databinding.FragmentToolsPlfBinding
import com.loancalculator.finance.manager.setSafeListener

class FragmentToolsPlf : RootPlfFragment<FragmentToolsPlfBinding>() {
    companion object {
        private const val TIL_PAGE = "til_page"

        fun newInstance(page: Int): FragmentToolsPlf {
            val fragment = FragmentToolsPlf()
            fragment.arguments = Bundle().apply {
                putInt(TIL_PAGE, page)
            }
            return fragment
        }
    }

    override fun startCreateContent(
        rootActivity: PlfRootActivity,
        view: View,
        bundle: Bundle?
    ) {
        mPlfBinding.clExchangeRate.setSafeListener {
            startActivity(Intent(rootActivity, PlfToolsExchangeRateActivity::class.java))
        }
        mPlfBinding.clTemperature.setSafeListener {
            startActivity(Intent(rootActivity, PlfToolsTemperatureActivity::class.java))
        }
        mPlfBinding.clMassConvert.setSafeListener {
            startActivity(Intent(rootActivity, PlfToolsMassConvertActivity::class.java))
        }
        mPlfBinding.clSpeedConvert.setSafeListener {
            startActivity(Intent(rootActivity, PlfToolsSpeedConvertActivity::class.java))
        }
        mPlfBinding.clLengthConvert.setSafeListener {
            startActivity(Intent(rootActivity, PlfToolsLengthConvertActivity::class.java))
        }
        mPlfBinding.clWorldClock.setSafeListener {
            startActivity(Intent(rootActivity, PlfToolsWorldTimeActivity::class.java))
        }
    }

    override fun getLayoutValue(): FragmentToolsPlfBinding {
        return FragmentToolsPlfBinding.inflate(layoutInflater)
    }
}