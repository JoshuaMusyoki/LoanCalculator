package com.coop_test.loan_calculator.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.coop_test.loan_calculator.domain.repository.LoanRepository
import kotlinx.coroutines.flow.first
import java.io.IOException

class LoanSyncWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val loanRepository: LoanRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // Simplified sync logic for demo
            val savedCalculations = loanRepository.getSavedCalculations().first()
            // In a real banking app, you would sync these to the server here
            Result.success()
        } catch (e: IOException) {
            Result.retry()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
