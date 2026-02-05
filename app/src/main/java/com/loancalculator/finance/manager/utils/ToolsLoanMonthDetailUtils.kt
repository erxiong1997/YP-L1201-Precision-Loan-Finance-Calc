package com.loancalculator.finance.manager.utils

import android.annotation.SuppressLint
import com.loancalculator.finance.manager.data.DataPersonalLoanPlf
import kotlin.math.pow

data class LoanMonthDetail(
    val month: Int,             // 月份（从1开始）
    val payment: Double,         // 当月还款额
    val principalPart: Double,   // 当月偿还本金
    val interestPart: Double,    // 当月利息
    val remainingPrincipal: Double // 剩余本金
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
        amount: Int,
        annualRate: Double,
        months: Int
    ): Pair<Double, List<LoanMonthDetail>> {
        if (amount <= 0 || months <= 0 || annualRate < 0) {
            return 0.0 to emptyList()
        }

        val monthlyRate = annualRate / 12f           // 月利率
        val power = (1 + monthlyRate).pow(months)    // (1 + r)^n

        // 等额本息每月还款公式
        val monthlyPayment = amount * (monthlyRate * power) / (power - 1)

        // 保留两位小数（四舍五入）
        val fixedPayment = String.format("%.2f", monthlyPayment).toDouble()

        val details = mutableListOf<LoanMonthDetail>()
        var remaining = amount * 1.0

        for (month in 1..months) {
            // 当月利息 = 剩余本金 × 月利率
            val interest = remaining * monthlyRate

            // 当月本金 = 每月还款 - 当月利息
            val principalPart = fixedPayment - interest

            // 更新剩余本金
            remaining -= principalPart

            // 最后一期做微调（防止小数累积误差导致剩余不为0）
            if (month == months) {
                remaining = 0.0
            }

            details.add(
                LoanMonthDetail(
                    month = month,
                    payment = fixedPayment,
                    principalPart = String.format("%.2f", principalPart).toDouble(),
                    interestPart = String.format("%.2f", interest).toDouble(),
                    remainingPrincipal = String.format("%.2f", remaining.coerceAtLeast(0.0))
                        .toDouble()
                )
            )
        }

        return fixedPayment to details
    }

    @SuppressLint("DefaultLocale")
    fun calculateAmortization(
        amount: Double,
        annualRate: Double,
        months: Int
    ): Pair<Double, List<LoanMonthDetail>> {
        if (amount <= 0 || months <= 0 || annualRate < 0) {
            return 0.0 to emptyList()
        }

        val monthlyRate = annualRate / 12f           // 月利率
        val power = (1 + monthlyRate).pow(months)    // (1 + r)^n

        // 等额本息每月还款公式
        val monthlyPayment = amount * (monthlyRate * power) / (power - 1)

        // 保留两位小数（四舍五入）
        val fixedPayment = String.format("%.2f", monthlyPayment).toDouble()

        val details = mutableListOf<LoanMonthDetail>()
        var remaining = amount * 1.0

        for (month in 1..months) {
            // 当月利息 = 剩余本金 × 月利率
            val interest = remaining * monthlyRate

            // 当月本金 = 每月还款 - 当月利息
            val principalPart = fixedPayment - interest

            // 更新剩余本金
            remaining -= principalPart

            // 最后一期做微调（防止小数累积误差导致剩余不为0）
            if (month == months) {
                remaining = 0.0
            }

            details.add(
                LoanMonthDetail(
                    month = month,
                    payment = fixedPayment,
                    principalPart = String.format("%.2f", principalPart).toDouble(),
                    interestPart = String.format("%.2f", interest).toDouble(),
                    remainingPrincipal = String.format("%.2f", remaining.coerceAtLeast(0.0))
                        .toDouble()
                )
            )
        }

        return fixedPayment to details
    }


    /**
     * 计算定投终值（每月月初投入，当月即开始产生利息）
     *
     * @param monthlyAmount 每月定投金额
     * @param annualRate 年化利率（如 0.10 = 10%）
     * @param months 总共定投月数
     * @param scale 小数位精度（默认2位）
     * @return Pair<总金额, 总利息>
     */
    @SuppressLint("DefaultLocale")
    fun calculateMonthlyInvestment(
        monthlyAmount: Int,
        annualRate: Double,
        months: Int
    ): Pair<Double, Double> {
        if (months <= 0) return 0.0 to 0.0
        if (annualRate <= 0.0) {
            val totalInvested = monthlyAmount * 1.0 * months
            return totalInvested to 0.0
        }

        val monthlyRate = annualRate / 12.0
        var total = 0.0

        // 第一个月投的钱增长 months 期，最后一个月投的钱增长 1 期
        for (i in 0 until months) {
            val periods = months - i
            val growth = (1.0 + monthlyRate).pow(periods)
            total += monthlyAmount * growth
        }

        val totalInvested = monthlyAmount * months
        val interest = total - totalInvested

        return String.format("%.2f", total).toDouble() to String.format("%.2f", interest).toDouble()
    }

}