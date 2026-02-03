package com.loancalculator.finance.manager.utils

import android.annotation.SuppressLint
import com.loancalculator.finance.manager.data.DataPersonalLoanPlf
import kotlin.math.pow

data class LoanMonthDetail(
    val month: Int,             // 月份（从1开始）
    val payment: Float,         // 当月还款额
    val principalPart: Float,   // 当月偿还本金
    val interestPart: Float,    // 当月利息
    val remainingPrincipal: Float // 剩余本金
)

object ToolsLoanMonthDetailUtils {
    var mDataPersonalLoanPlf: DataPersonalLoanPlf? = null

    /**
     * 计算等额本息贷款
     * @param principal 本金（元）
     * @param annualRate 年利率（如 0.12 表示12%）
     * @param months 贷款总月数
     * @return Pair(每月固定还款额, 每月明细列表)
     */
    @SuppressLint("DefaultLocale")
    fun calculateAmortization(
        amount: Float,
        annualRate: Float,
        months: Int
    ): Pair<Float, List<LoanMonthDetail>> {
        if (amount <= 0 || months <= 0 || annualRate < 0) {
            return 0f to emptyList()
        }

        val monthlyRate = annualRate / 12f           // 月利率
        val power = (1 + monthlyRate).pow(months)    // (1 + r)^n

        // 等额本息每月还款公式
        val monthlyPayment = amount * (monthlyRate * power) / (power - 1)

        // 保留两位小数（四舍五入）
        val fixedPayment = String.format("%.2f", monthlyPayment).toFloat()

        val details = mutableListOf<LoanMonthDetail>()
        var remaining = amount

        for (month in 1..months) {
            // 当月利息 = 剩余本金 × 月利率
            val interest = remaining * monthlyRate

            // 当月本金 = 每月还款 - 当月利息
            val principalPart = fixedPayment - interest

            // 更新剩余本金
            remaining -= principalPart

            // 最后一期做微调（防止小数累积误差导致剩余不为0）
            if (month == months) {
                remaining = 0f
            }

            details.add(
                LoanMonthDetail(
                    month = month,
                    payment = fixedPayment,
                    principalPart = String.format("%.2f", principalPart).toFloat(),
                    interestPart = String.format("%.2f", interest).toFloat(),
                    remainingPrincipal = String.format("%.2f", remaining.coerceAtLeast(0f))
                        .toFloat()
                )
            )
        }

        return fixedPayment to details
    }
}

// 使用示例
fun main() {
    val principal = 120000f      // 本金
    val annualRate = 0.12f       // 年利率 12%
    val months = 12              // 12个月

    val (monthlyPayment, details) = ToolsLoanMonthDetailUtils.calculateAmortization(
        principal, annualRate, months
    )

    println("每月固定还款额: $monthlyPayment 元")
    println("总还款额: ${monthlyPayment * months} 元")
    println("总利息: ${monthlyPayment * months - principal} 元\n")

    println("月份 | 还款额    | 本金部分  | 利息部分  | 剩余本金")
    println("-".repeat(50))
    details.forEach { d ->
        println(
            "%2d   | %-9.2f | %-9.2f | %-9.2f | %-9.2f".format(
                d.month, d.payment, d.principalPart, d.interestPart, d.remainingPrincipal
            )
        )
    }
}