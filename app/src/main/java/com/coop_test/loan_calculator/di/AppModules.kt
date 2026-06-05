package com.coop_test.loan_calculator.di

import androidx.room.Room
import com.coop_test.loan_calculator.data.local.AppDatabase
import com.coop_test.loan_calculator.data.local.SecurePrefsManager
import com.coop_test.loan_calculator.data.remote.BankingApiService
import com.coop_test.loan_calculator.data.remote.KtorClientFactory
import com.coop_test.loan_calculator.data.repos.LoanRepositoryImpl
import com.coop_test.loan_calculator.data.worker.LoanSyncWorker
import com.coop_test.loan_calculator.domain.repository.LoanRepository
import com.coop_test.loan_calculator.domain.usecases.CalculateLoanUseCase
import com.coop_test.loan_calculator.ui.feature.loan.LoanViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "loan_calculator_db"
        ).build()
    }
    single { get<AppDatabase>().loanDao() }
    single { SecurePrefsManager(androidContext()) }
}

val networkModule = module {
    single { KtorClientFactory.create() }
    single { BankingApiService(get()) }
}

val repositoryModule = module {
    single<LoanRepository> { LoanRepositoryImpl(get()) }
}

val useCaseModule = module {
    single { CalculateLoanUseCase() }
}

val workerModule = module {
    worker { LoanSyncWorker(get(), get(), get()) }
}

val viewModelModule = module {
    viewModel { LoanViewModel(get(), get()) }
}

val appModules = listOf(
    databaseModule,
    networkModule,
    repositoryModule,
    useCaseModule,
    workerModule,
    viewModelModule
)
