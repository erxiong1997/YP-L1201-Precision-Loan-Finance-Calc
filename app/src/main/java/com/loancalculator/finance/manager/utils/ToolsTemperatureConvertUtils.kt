package com.loancalculator.finance.manager.utils

import com.loancalculator.finance.manager.formatToFixString
import kotlin.math.roundToInt

/**
 * 温度单位枚举
 */
enum class TemperatureUnit(val symbol: String, val displayName: String) {
    CELSIUS("°C", "Celsius"),
    FAHRENHEIT("°F", "Fahrenheit"),
    KELVIN("K", "Kelvin"),          // 正式不带°，但显示时可加
    RANKINE("°R", "Rankine"),
    REAUMUR("°Re", "Réaumur")
}

/**
 * 温度转换工具类
 */
object ToolsTemperatureUnitUtils {

    /**
     * 将输入值从 fromUnit 转换为 toUnit
     * @param value 输入数值
     * @param fromUnit 源单位
     * @param toUnit 目标单位
     * @param decimalPlaces 小数位数（默认2位）
     * @return 转换后的值（String格式，已保留指定小数位）
     */
    fun convert(
        value: Double = 1.0,
        fromUnit: TemperatureUnit? = null,
        toUnit: TemperatureUnit? = null,
        decimalPlaces: Int = 2
    ): String {
        if (fromUnit == null) return ""
        if (toUnit == null) return ""
        if (fromUnit == toUnit) {
            return format(1.0, decimalPlaces)
        }

        // 先全部转换为摄氏度
        val celsius = when (fromUnit) {
            TemperatureUnit.CELSIUS -> value
            TemperatureUnit.FAHRENHEIT -> (value - 32) * 5 / 9
            TemperatureUnit.KELVIN -> value - 273.15
            TemperatureUnit.RANKINE -> (value - 491.67) * 5 / 9
            TemperatureUnit.REAUMUR -> value * 5 / 4
        }

        // 再从摄氏度转换为目标单位
        val result = when (toUnit) {
            TemperatureUnit.CELSIUS -> celsius
            TemperatureUnit.FAHRENHEIT -> celsius * 9 / 5 + 32
            TemperatureUnit.KELVIN -> celsius + 273.15
            TemperatureUnit.RANKINE -> (celsius + 273.15) * 9 / 5
            TemperatureUnit.REAUMUR -> celsius * 4 / 5
        }

        return format(result, decimalPlaces)
    }

    /**
     * 将所有单位的值一次性计算出来（适合显示列表）
     */
    fun convertToAllUnits(
        value: Double = 1.0,
        fromUnit: TemperatureUnit,
        decimalPlaces: Int = 2
    ): Map<TemperatureUnit, String> {
        return TemperatureUnit.entries.associateWith { unit ->
            convert(value, fromUnit, unit, decimalPlaces)
        }
    }

    private fun format(value: Double, decimalPlaces: Int): String {
        return if (decimalPlaces <= 0) {
            value.roundToInt().toString()
        } else {
            "%.${decimalPlaces}f".format(value)
            value.formatToFixString(12)
        }
    }
}