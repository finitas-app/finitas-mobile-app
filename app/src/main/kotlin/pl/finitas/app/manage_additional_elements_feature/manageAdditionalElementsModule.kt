package pl.finitas.app.manage_additional_elements_feature

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.finitas.app.core.data.data_source.repository.SpendingCategoryRepositoryImpl
import pl.finitas.app.core.domain.repository.SpendingCategoryRepository
import pl.finitas.app.core.domain.services.SpendingCategoryService
import pl.finitas.app.core.getDatabase
import pl.finitas.app.manage_additional_elements_feature.data.data_source.RegularSpendingRepositoryImpl
import pl.finitas.app.manage_additional_elements_feature.domain.repositories.RegularSpendingRepository
import pl.finitas.app.manage_additional_elements_feature.domain.services.RegularSpendingService
import pl.finitas.app.manage_additional_elements_feature.presentation.regular_spending.RegularSpendingsViewModel
import pl.finitas.app.manage_additional_elements_feature.presentation.spending_category.SpendingCategoryViewModel

val manageAdditionalElementsModule = module {
    single<RegularSpendingRepository> {
        RegularSpendingRepositoryImpl(
            getDatabase().regularSpendingDao,
            getDatabase().finishedSpendingDao
        )
    }
    single {
        RegularSpendingService(get())
    }
    single<SpendingCategoryRepository> {
        SpendingCategoryRepositoryImpl(getDatabase().spendingCategoryDao, getDatabase().userDao)
    }
    single {
        SpendingCategoryService(get(), get(), get())
    }
    viewModel { SpendingCategoryViewModel(get()) }
    viewModel { RegularSpendingsViewModel(get(), get(), get()) }
}