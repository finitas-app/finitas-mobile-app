package pl.finitas.app.manage_spendings_feature

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.finitas.app.core.data.repository.FinishedSpendingRepositoryImpl
import pl.finitas.app.core.data.repository.SpendingCategoryRepositoryImpl
import pl.finitas.app.core.domain.repository.SpendingCategoryRepository
import pl.finitas.app.core.domain.repository.TotalSpendingRepository
import pl.finitas.app.core.getDatabase
import pl.finitas.app.manage_spendings_feature.domain.services.FinishedSpendingService
import pl.finitas.app.core.domain.services.SpendingCategoryService
import pl.finitas.app.manage_spendings_feature.presentation.add_spending.AddSpendingViewModel
import pl.finitas.app.manage_spendings_feature.presentation.spendings.TotalSpendingViewModel

val manageSpendingsModule = module {
    single<TotalSpendingRepository> {
        FinishedSpendingRepositoryImpl(getDatabase().finishedSpendingDao)
    }
    single<SpendingCategoryRepository> {
        SpendingCategoryRepositoryImpl(getDatabase().spendingCategoryDao)
    }
    single {
        FinishedSpendingService(get(), get())
    }
    single {
        SpendingCategoryService(get())
    }
    viewModel { TotalSpendingViewModel(get(), get()) }
    viewModel { AddSpendingViewModel(get(), get()) }
}