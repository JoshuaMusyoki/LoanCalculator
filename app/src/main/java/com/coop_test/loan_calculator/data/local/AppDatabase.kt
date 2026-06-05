package com.coop_test.loan_calculator.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        LoanCalculationEntity::class
               ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun loanDao(): LoanDao
}
