package com.loancalculator.finance.manager.utils

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
object LengthConverter {

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
        fromUnit: LengthUnit,
        toUnit: LengthUnit,
        decimalPlaces: Int = 2
    ): String {
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
        return LengthUnit.values().associateWith { unit ->
            convert(value, fromUnit, unit, decimalPlaces)
        }
    }

    private fun format(value: Double, decimalPlaces: Int): String {
        return if (decimalPlaces <= 0) {
            value.roundToInt().toString()
        } else {
            "%.${decimalPlaces}f".format(value)
        }
    }
}

// 使用示例
fun main() {
    // 示例1：输入 1 km，转换为所有单位
    val kmValue = 1.0
    println("输入: $kmValue ${LengthUnit.KILOMETER.symbol}")
    LengthConverter.convertToAllUnits(kmValue, LengthUnit.KILOMETER).forEach { (unit, result) ->
        println("${unit.displayName.padEnd(15)} (${unit.symbol}): $result ${unit.symbol}")
    }

    println("\n-------------------\n")

    // 示例2：输入 250 cm，转换为其他单位
    val cmValue = 250.0
    println("输入: $cmValue ${LengthUnit.CENTIMETER.symbol} (相当于 2.5米)")
    LengthConverter.convertToAllUnits(cmValue, LengthUnit.CENTIMETER).forEach { (unit, result) ->
        println("${unit.displayName.padEnd(15)} (${unit.symbol}): $result ${unit.symbol}")
    }

    println("\n-------------------\n")

    // 示例3：单次转换 - 1 米 = ? 毫米
    val meterValue = 1.0
    val toMm = LengthConverter.convert(meterValue, LengthUnit.METER, LengthUnit.MILLIMETER, 0)
    println("$meterValue ${LengthUnit.METER.symbol} = $toMm ${LengthUnit.MILLIMETER.symbol}")
}