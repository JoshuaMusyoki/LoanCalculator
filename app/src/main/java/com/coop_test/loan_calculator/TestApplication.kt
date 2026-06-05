package com.coop_test.loan_calculator

import android.app.Application
import androidx.work.*
import com.coop_test.loan_calculator.data.worker.LoanSyncWorker
import com.coop_test.loan_calculator.di.appModules
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class TestApplication : Application(), Configuration.Provider {
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(getKoin().get())
            .build()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@TestApplication)
            workManagerFactory()
            modules(appModules)
        }

        setupSyncWorker()
    }

    private fun setupSyncWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = PeriodicWorkRequestBuilder<LoanSyncWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "TransactionSync",
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )
    }
}
