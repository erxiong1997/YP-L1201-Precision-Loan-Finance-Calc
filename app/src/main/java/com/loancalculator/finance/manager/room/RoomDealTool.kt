package com.loancalculator.finance.manager.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.loancalculator.finance.manager.data.DataPersonalLoanPlf
import com.loancalculator.finance.manager.room.tildao.TilPersonalLoanDao

@Database(
    entities = [DataPersonalLoanPlf::class],
    version = 1,
    exportSchema = true
)
abstract class RoomDealTool : RoomDatabase() {
    abstract fun mTilPersonalLoanDao(): TilPersonalLoanDao
}
