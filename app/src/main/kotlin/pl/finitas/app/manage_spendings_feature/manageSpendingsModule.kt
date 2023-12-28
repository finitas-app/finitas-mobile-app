package pl.finitas.app.manage_spendings_feature

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.finitas.app.core.data.data_source.repository.SpendingCategoryRepositoryImpl
import pl.finitas.app.core.domain.repository.SpendingCategoryRepository
import pl.finitas.app.core.domain.services.SpendingCategoryService
import pl.finitas.app.core.getDatabase
import pl.finitas.app.manage_spendings_feature.data.data_source.ChartRepositoryImpl
import pl.finitas.app.manage_spendings_feature.data.data_source.FinishedSpendingRepositoryImpl
import pl.finitas.app.manage_spendings_feature.domain.repository.ChartRepository
import pl.finitas.app.manage_spendings_feature.domain.repository.TotalSpendingRepository
import pl.finitas.app.manage_spendings_feature.domain.service.ChartService
import pl.finitas.app.manage_spendings_feature.domain.service.FinishedSpendingService
import pl.finitas.app.manage_spendings_feature.presentation.add_spending.AddSpendingViewModel
import pl.finitas.app.manage_spendings_feature.presentation.charts.ChartConstructorViewModel
import pl.finitas.app.manage_spendings_feature.presentation.charts.ChartDisplayViewModel
import pl.finitas.app.manage_spendings_feature.presentation.spendings.TotalSpendingViewModel

val manageSpendingsModule = module {
    single<TotalSpendingRepository> {
        FinishedSpendingRepositoryImpl(getDatabase().finishedSpendingDao)
    }
    single<SpendingCategoryRepository> {
        SpendingCategoryRepositoryImpl(getDatabase().spendingCategoryDao)
    }
    single<ChartRepository> {
        ChartRepositoryImpl(getDatabase().chartDao)
    }
    single {
        FinishedSpendingService(get(), get())
    }
    single {
        SpendingCategoryService(get())
    }
    single {
        ChartService(get(), get())
    }
    viewModel { TotalSpendingViewModel(get(), get()) }
    viewModel { AddSpendingViewModel(get(), get()) }
    viewModel { ChartDisplayViewModel(get()) }
    viewModel { ChartConstructorViewModel(get()) }
}