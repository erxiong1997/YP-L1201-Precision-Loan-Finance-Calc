package com.loancalculator.finance.manager.utils

import kotlin.math.roundToInt

/**
 * 速度单位枚举
 */
enum class SpeedUnit(val symbol: String, val displayName: String, val toMsFactor: Double) {
    LIGHT("c", "Speed of light", 299792458.0),
    KMS("km/s", "Kilometer per second", 1000.0),
    KMH("km/h", "Kilometer per hour", 1000.0 / 3600.0),  // ≈0.277778
    MS("m/s", "Meter per second", 1.0),
    MH("m/h", "Meter per hour", 1.0 / 3600.0),            // ≈0.000277778
    INS("in/s", "Inch per second", 0.0254),               // 1 inch = 0.0254 m
    MACH("ma", "Mach", 343.0)                             // 标准海平面音速 ≈343 m/s
}

/**
 * 速度转换工具类
 */
object ToolsSpeedConverterUtils {

    /**
     * 将输入值从 fromUnit 转换为 toUnit
     * @param value 输入数值
     * @param fromUnit 源单位
     * @param toUnit 目标单位
     * @param decimalPlaces 小数位数（默认2位，光速/大值可设更高）
     * @return 转换后的值（String格式，已保留指定小数位）
     */
    fun convert(
        value: Double,
        fromUnit: SpeedUnit? = null,
        toUnit: SpeedUnit? = null,
        decimalPlaces: Int = 2
    ): String {
        if (fromUnit == null) return ""
        if (toUnit == null) return ""
        if (fromUnit == toUnit) {
            return format(value, decimalPlaces)
        }

        // 先转换为 m/s
        val ms = value * fromUnit.toMsFactor

        // 再转换为目标单位
        val result = ms / toUnit.toMsFactor

        return format(result, decimalPlaces)
    }

    /**
     * 将输入值转换为所有单位（适合列表显示，如你的 app 界面）
     */
    fun convertToAllUnits(
        value: Double,
        fromUnit: SpeedUnit,
        decimalPlaces: Int = 2
    ): Map<SpeedUnit, String> {
        return SpeedUnit.values().associateWith { unit ->
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
    // 示例1：输入 1 c (光速)，转换为所有单位
    val lightValue = 1.0
    println("输入: $lightValue ${SpeedUnit.LIGHT.symbol} (光速)")
    ToolsSpeedConverterUtils.convertToAllUnits(lightValue, SpeedUnit.LIGHT)
        .forEach { (unit, result) ->
            println("${unit.displayName.padEnd(25)} (${unit.symbol}): $result ${unit.symbol}")
        }

    println("\n-------------------\n")

    // 示例2：输入 100 km/h，转换为其他单位
    val kmhValue = 100.0
    println("输入: $kmhValue ${SpeedUnit.KMH.symbol} (常见车速)")
    ToolsSpeedConverterUtils.convertToAllUnits(kmhValue, SpeedUnit.KMH).forEach { (unit, result) ->
        println("${unit.displayName.padEnd(25)} (${unit.symbol}): $result ${unit.symbol}")
    }

    println("\n-------------------\n")

    // 示例3：单次转换 - 1 Mach = ? km/s
    val machValue = 1.0
    val toKms = ToolsSpeedConverterUtils.convert(machValue, SpeedUnit.MACH, SpeedUnit.KMS, 6)
    println("1 ${SpeedUnit.MACH.symbol} ≈ $toKms ${SpeedUnit.KMS.symbol}")
}