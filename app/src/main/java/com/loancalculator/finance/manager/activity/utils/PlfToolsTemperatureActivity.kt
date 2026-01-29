package com.loancalculator.finance.manager.activity.utils

import androidx.lifecycle.lifecycleScope
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.databinding.ActivityToolsTemperaturePlfBinding
import com.loancalculator.finance.manager.utils.ToolsTemperatureUnitUtils
import com.loancalculator.finance.manager.utils.value.ParamsLtdUtils.mTemperatureUnitList
import kotlinx.coroutines.launch

class PlfToolsTemperatureActivity : PlfBindingActivity<ActivityToolsTemperaturePlfBinding>() {
    override fun beginViewAndDoLtd() {
        lifecycleScope.launch {

        }
        mPlcBinding.tvValue.text = ToolsTemperatureUnitUtils.convert(
            fromUnit = mTemperatureUnitList[0],
            toUnit = mTemperatureUnitList[2]
        ) + "\n" + ToolsTemperatureUnitUtils.convert(
            fromUnit = mTemperatureUnitList[2],
            toUnit = mTemperatureUnitList[0]
        )
    }

    override fun getLayoutValue(): ActivityToolsTemperaturePlfBinding {
        return ActivityToolsTemperaturePlfBinding.inflate(layoutInflater)
    }

}