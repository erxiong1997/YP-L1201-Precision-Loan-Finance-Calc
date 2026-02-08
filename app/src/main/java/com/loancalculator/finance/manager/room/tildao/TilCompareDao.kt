package com.loancalculator.finance.manager.room.tildao

import androidx.room.Dao
import androidx.room.Query
import com.loancalculator.finance.manager.data.DataComparePlf
import com.loancalculator.finance.manager.data.DataPersonalLoanPlf

@Dao
interface TilCompareDao : TilBaseDao<DataComparePlf> {
    @Query("SELECT * FROM data_compare_plf WHERE loanType = :loanType AND addCompareTable =:addCompareTable  ORDER BY dataIndexId DESC")
    fun getListByLoanType(
        loanType: String,
        addCompareTable: String = "yes"
    ): List<DataPersonalLoanPlf>
}