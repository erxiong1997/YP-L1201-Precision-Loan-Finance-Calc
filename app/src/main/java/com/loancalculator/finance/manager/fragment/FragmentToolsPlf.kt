package com.loancalculator.finance.manager.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.loancalculator.finance.manager.activity.PlfRootActivity
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

        }
        mPlfBinding.clTemperature.setSafeListener {
            startActivity(Intent(rootActivity, PlfToolsTemperatureActivity::class.java))
        }
        mPlfBinding.clMassConvert.setSafeListener {

        }
        mPlfBinding.clSpeedConvert.setSafeListener {

        }
        mPlfBinding.clLengthConvert.setSafeListener {

        }
        mPlfBinding.clWorldClock.setSafeListener {
            startActivity(Intent(rootActivity, PlfToolsWorldTimeActivity::class.java))
        }
    }

    override fun getLayoutValue(): FragmentToolsPlfBinding {
        return FragmentToolsPlfBinding.inflate(layoutInflater)
    }
}