package com.loancalculator.finance.manager.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.loancalculator.finance.manager.room.tildata.TilBaseData
import com.loancalculator.finance.manager.utils.LoanMonthDetail

@Entity(
    tableName = "data_personal_loan_plf",
    //添加索引 主键会自动创建
//    indices = [Index(value = ["dataIndexId"], unique = true)]
)
class DataPersonalLoanPlf() : TilBaseData() {
    @PrimaryKey(autoGenerate = true)
    var dataIndexId: Long = 0L

    //类型 personalLoan businessLoan mortgages autoLoan rd fd
    var loanType: String = ""

    //是否加入对比表 yes no
    var addCompareTable = "no"

    //对比表的名称
    var aTableName = ""

    //货币符号
    var currencySymbol = ""

    //贷款金额
    var loanAmount: Int = 0

    //首次支付的房款
    var firstAmount: Double = 0.0

    //贷款年利率
    var interestRate: Double = 0.0

    //表示多少个月
    var loanTerm: Int = 0

    //
    var loanTermUnit: String = ""

    //开始日期
    var startDate: Long = 0

    //月供
    var monthlyPayment: Double = 0.0

    //添加日期
    var addDate: Long = System.currentTimeMillis()

    //物业税
    var propertyTax: Int = 0

    //PMI
    var pmiMoney: Int = 0

    //房主保险
    var homeownersInsurance: Int = 0

    //业委会费用
    var hoaMoney: Int = 0

    var tempLong1: Long = 0
    var tempLong2: Long = 0
    var tempString1: String = ""
    var tempString2: String = ""

    @Ignore
    var mLoanMonthDetailList: List<LoanMonthDetail>? = null

    @Ignore
    var fingerSelect = false
}