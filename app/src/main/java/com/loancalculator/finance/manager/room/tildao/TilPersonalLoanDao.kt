package com.loancalculator.finance.manager.room.tildao

import androidx.room.Dao
import androidx.room.Query
import com.loancalculator.finance.manager.data.DataPersonalLoanPlf

@Dao
interface TilPersonalLoanDao : TilBaseDao<DataPersonalLoanPlf> {
    //DESC 升序 DESC = 降序
    // 根据 loanType 查询
    @Query("SELECT * FROM data_personal_loan_plf WHERE loanType = :loanType AND addCompareTable =:addCompareTable  ORDER BY dataIndexId DESC")
    fun getListByLoanType(
        loanType: String,
        addCompareTable: String = "yes"
    ): List<DataPersonalLoanPlf>

    // 获取全部
    @Query("SELECT * FROM data_personal_loan_plf ORDER BY dataIndexId DESC")
    fun getAllList(): List<DataPersonalLoanPlf>

    @Query("SELECT * FROM data_personal_loan_plf WHERE loanType != :loanType1 AND loanType != :loanType2 ORDER BY dataIndexId DESC")
    fun getAllListCalculator(
        loanType1: String,
        loanType2: String
    ): List<DataPersonalLoanPlf>

    @Query("SELECT * FROM data_personal_loan_plf WHERE loanType == :loanType1 OR loanType == :loanType2 ORDER BY dataIndexId DESC")
    fun getAllListInvestment(
        loanType1: String,
        loanType2: String
    ): List<DataPersonalLoanPlf>

    // 取最新的 N 条（不排除任何 loanType）
    @Query("SELECT * FROM data_personal_loan_plf ORDER BY dataIndexId DESC  LIMIT :itemCount")
    fun getLatestItems(itemCount: Int): List<DataPersonalLoanPlf>
}