package com.loancalculator.finance.manager.utils

import kotlin.math.roundToInt

/**
 * 质量单位枚举
 */
enum class MassUnit(val symbol: String, val displayName: String, val toKgFactor: Double) {
    QUINTAL("q", "Quintal", 100.0),
    CARAT("ct", "Carat", 0.0002),
    TON("t", "Ton", 1000.0),
    MILLIGRAM("mg", "Milligram", 0.000001),
    KILOGRAM("kg", "Kilogram", 1.0),
    GRAM("g", "Gram", 0.001)
}

/**
 * 质量转换工具类
 */
object ToolsMassUnitUtils {

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
        fromUnit: MassUnit,
        toUnit: MassUnit,
        decimalPlaces: Int = 2
    ): String {
        if (fromUnit == toUnit) {
            return format(value, decimalPlaces)
        }

        // 先转换为 kg
        val kg = value * fromUnit.toKgFactor

        // 再转换为目标单位
        val result = kg / toUnit.toKgFactor

        return format(result, decimalPlaces)
    }

    /**
     * 将输入值转换为所有单位（适合列表显示，如你的 app 界面）
     */
    fun convertToAllUnits(
        value: Double,
        fromUnit: MassUnit,
        decimalPlaces: Int = 2
    ): Map<MassUnit, String> {
        return MassUnit.entries.associateWith { unit ->
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
    // 示例1：输入 1 kg，转换为所有单位
    val kgValue = 1.0
    println("输入: $kgValue kg")
    ToolsMassUnitUtils.convertToAllUnits(kgValue, MassUnit.KILOGRAM).forEach { (unit, result) ->
        println("${unit.displayName.padEnd(12)} (${unit.symbol}): $result ${unit.symbol}")
    }

    println("\n-------------------\n")

    // 示例2：输入 500 ct (克拉)，转换为其他单位
    val caratValue = 500.0
    println("输入: $caratValue ct (相当于 100g)")
    ToolsMassUnitUtils.convertToAllUnits(caratValue, MassUnit.CARAT).forEach { (unit, result) ->
        println("${unit.displayName.padEnd(12)} (${unit.symbol}): $result ${unit.symbol}")
    }

    println("\n-------------------\n")

    // 示例3：单次转换 - 1 吨 = ? 千克
    val tonValue = 1.0
    val toKg = ToolsMassUnitUtils.convert(tonValue, MassUnit.TON, MassUnit.KILOGRAM)
    println("$tonValue ${MassUnit.TON.symbol} = $toKg ${MassUnit.KILOGRAM.symbol}")
}