package pl.finitas.app.manage_spendings_feature

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.finitas.app.core.getDatabase
import pl.finitas.app.manage_spendings_feature.data.repository.SpendingCategoryRepositoryImpl
import pl.finitas.app.manage_spendings_feature.data.repository.TotalSpendingRepositoryImpl
import pl.finitas.app.manage_spendings_feature.domain.repository.SpendingCategoryRepository
import pl.finitas.app.manage_spendings_feature.domain.repository.TotalSpendingRepository
import pl.finitas.app.manage_spendings_feature.domain.services.SpendingCategoryService
import pl.finitas.app.manage_spendings_feature.domain.services.TotalSpendingService
import pl.finitas.app.manage_spendings_feature.presentation.add_spending.AddSpendingViewModel
import pl.finitas.app.manage_spendings_feature.presentation.spendings.TotalSpendingViewModel

val manageSpendingsModule = module {
    single<TotalSpendingRepository> {
        TotalSpendingRepositoryImpl(getDatabase().totalSpendingDao)
    }
    single<SpendingCategoryRepository> {
        SpendingCategoryRepositoryImpl(getDatabase().spendingCategoryDao)
    }
    single {
        TotalSpendingService(get(), get())
    }
    single {
        SpendingCategoryService(get())
    }
    viewModel { TotalSpendingViewModel(get(), get()) }
    viewModel { AddSpendingViewModel(get(), get()) }
}