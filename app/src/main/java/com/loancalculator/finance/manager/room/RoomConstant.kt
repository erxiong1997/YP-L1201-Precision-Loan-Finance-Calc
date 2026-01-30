package com.loancalculator.finance.manager.room

import androidx.room.Room
import com.loancalculator.finance.manager.PlfDealApplication.Companion.mPlfContext

const val PRECISION_LOAN_FINANCE_CALC = "precisionLoanFinanceCalc.db"

val mPlfLoanRoom by lazy {
    Room.databaseBuilder(
        mPlfContext, RoomDealTool::class.java, PRECISION_LOAN_FINANCE_CALC
    ).allowMainThreadQueries().addMigrations().build()
}