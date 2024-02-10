package pl.finitas.app.manage_spendings_feature

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.finitas.app.core.data.data_source.repository.SpendingCategoryRepositoryImpl
import pl.finitas.app.core.domain.repository.SpendingCategoryRepository
import pl.finitas.app.core.domain.services.SpendingCategoryService
import pl.finitas.app.core.getDatabase
import pl.finitas.app.manage_spendings_feature.data.data_source.ChartRepositoryImpl
import pl.finitas.app.manage_spendings_feature.data.data_source.FinishedSpendingRepositoryImpl
import pl.finitas.app.manage_spendings_feature.data.data_source.ScanReceiptRepositoryImpl
import pl.finitas.app.manage_spendings_feature.domain.repository.ChartRepository
import pl.finitas.app.manage_spendings_feature.domain.repository.FinishedSpendingRepository
import pl.finitas.app.manage_spendings_feature.domain.repository.ScanReceiptRepository
import pl.finitas.app.manage_spendings_feature.domain.service.ChartService
import pl.finitas.app.manage_spendings_feature.domain.service.FinishedSpendingService
import pl.finitas.app.manage_spendings_feature.domain.service.ScanReceiptService
import pl.finitas.app.manage_spendings_feature.presentation.add_spending.AddSpendingViewModel
import pl.finitas.app.manage_spendings_feature.presentation.charts.ChartConstructorViewModel
import pl.finitas.app.manage_spendings_feature.presentation.charts.ChartDisplayViewModel
import pl.finitas.app.manage_spendings_feature.presentation.spendings.FinishedSpendingViewModel

val manageSpendingsModule = module {
    single<FinishedSpendingRepository> {
        FinishedSpendingRepositoryImpl(getDatabase().finishedSpendingDao)
    }
    single<SpendingCategoryRepository> {
        SpendingCategoryRepositoryImpl(getDatabase().spendingCategoryDao, getDatabase().userDao)
    }
    single<ChartRepository> {
        ChartRepositoryImpl(getDatabase().chartDao)
    }
    single<ScanReceiptRepository> {
        ScanReceiptRepositoryImpl(get())
    }
    single {
        FinishedSpendingService(get(), get(), get(), get())
    }
    single {
        SpendingCategoryService(get(), get(), get())
    }
    single {
        ChartService(get(), get())
    }
    single {
        ScanReceiptService(get())
    }
    viewModel { AddSpendingViewModel(get(), get(), get(), get(), get()) }
    viewModel { FinishedSpendingViewModel(get(), get(), get()) }
    viewModel { ChartDisplayViewModel(get(), get(), get()) }
    viewModel { ChartConstructorViewModel(get(), get(), get()) }
}