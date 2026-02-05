package com.loancalculator.finance.manager.utils

import com.loancalculator.finance.manager.formatToFixString
import kotlin.math.roundToInt

/**
 * 长度单位枚举（公制）
 */
enum class LengthUnit(val symbol: String, val displayName: String, val toMeterFactor: Double) {
    KILOMETER("km", "Kilometer", 1000.0),
    HECTOMETER("hm", "Hectometer", 100.0),
    DECAMETER("dam", "Decameter", 10.0),
    METER("m", "Meter", 1.0),
    DECIMETER("dm", "Decimeter", 0.1),
    CENTIMETER("cm", "Centimeter", 0.01),
    MILLIMETER("mm", "Millimeter", 0.001)
}

/**
 * 长度转换工具类
 */
object ToolsLengthConvertUtils {

    /**
     * 将输入值从 fromUnit 转换为 toUnit
     * @param value 输入数值
     * @param fromUnit 源单位
     * @param toUnit 目标单位
     * @param decimalPlaces 小数位数（默认2位，适合大多数场景）
     * @return 转换后的值（String格式，已保留指定小数位）
     */
    fun convert(
        value: Double,
        fromUnit: LengthUnit?,
        toUnit: LengthUnit?,
        decimalPlaces: Int = 2
    ): String {
        if (fromUnit == null) return ""
        if (toUnit == null) return ""
        if (fromUnit == toUnit) {
            return format(value, decimalPlaces)
        }

        // 先转换为米 (m)
        val meters = value * fromUnit.toMeterFactor

        // 再转换为目标单位
        val result = meters / toUnit.toMeterFactor

        return format(result, decimalPlaces)
    }

    /**
     * 将输入值转换为所有单位（适合列表显示，如你的 app 界面）
     */
    fun convertToAllUnits(
        value: Double,
        fromUnit: LengthUnit,
        decimalPlaces: Int = 2
    ): Map<LengthUnit, String> {
        return LengthUnit.entries.associateWith { unit ->
            convert(value, fromUnit, unit, decimalPlaces)
        }
    }

    private fun format(value: Double, decimalPlaces: Int): String {
        return if (decimalPlaces <= 0) {
            value.roundToInt().toString()
        } else {
            "%.${decimalPlaces}f".format(value)
            value.toString()
        }
    }
}