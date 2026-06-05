package com.coop_test.loan_calculator.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LoanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalculation(calculation: LoanCalculationEntity)

    @Query("SELECT * FROM loan_calculations ORDER BY timestamp DESC")
    fun getAllCalculations(): Flow<List<LoanCalculationEntity>>

    @Delete
    suspend fun deleteCalculation(calculation: LoanCalculationEntity)
}
