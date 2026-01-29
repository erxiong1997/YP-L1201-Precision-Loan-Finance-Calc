package com.loancalculator.finance.manager.utils.value

import com.loancalculator.finance.manager.PlfDealApplication
import com.loancalculator.finance.manager.data.DataToolsTemperature
import com.loancalculator.finance.manager.utils.TemperatureUnit
import java.io.File

object ParamsLtdUtils {

    val mSaveFilePlf by lazy {
        File(
            PlfDealApplication.mPlfContext.filesDir,
            "PrecisionLoanFiles"
        ).apply {
            if (!exists()) {
                mkdir()
            }
        }
    }
    //温度的单位和名称
    val mTemperatureUnitList by lazy {
        mutableListOf(
            TemperatureUnit.CELSIUS,
            TemperatureUnit.REAUMUR,
            TemperatureUnit.FAHRENHEIT,
            TemperatureUnit.KELVIN,
            TemperatureUnit.RANKINE,
        )
    }
}