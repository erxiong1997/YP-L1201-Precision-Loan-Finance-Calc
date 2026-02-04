package com.loancalculator.finance.manager.room.tildao

import androidx.room.Dao
import androidx.room.Query
import com.loancalculator.finance.manager.data.DataPersonalLoanPlf

@Dao
interface TilPersonalLoanDao : TilBaseDao<DataPersonalLoanPlf> {
    //ASC 升序 DESC = 降序
    // 根据 loanType 查询
    @Query("SELECT * FROM data_personal_loan_plf WHERE loanType = :loanType AND addAmortizationTable =:addAmortizationTable  ORDER BY dataIndexId ASC")
    fun getListByLoanType(
        loanType: String,
        addAmortizationTable: String = "yes"
    ): List<DataPersonalLoanPlf>

    // 获取全部
    @Query("SELECT * FROM data_personal_loan_plf ORDER BY dataIndexId ASC")
    fun getAllList(): List<DataPersonalLoanPlf>
}