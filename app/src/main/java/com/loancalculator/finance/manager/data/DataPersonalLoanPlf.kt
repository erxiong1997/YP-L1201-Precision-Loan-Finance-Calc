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

    //贷款金额
    var loanAmount: Int = 0

    //贷款年利率
    var interestRate: Double = 0.0

    //表示多少个月
    var loanTerm: Int = 0

    //开始日期
    var startDate: Long = 0

    //月供
    var monthlyPayment: Double = 0.0

    //添加日期
    var addDate: Long = System.currentTimeMillis()

    var tempLong1: Long = 0
    var tempLong2: Long = 0
    var tempString1: String = ""
    var tempString2: String = ""

    @Ignore
    var mLoanMonthDetailList: List<LoanMonthDetail>? = null
}