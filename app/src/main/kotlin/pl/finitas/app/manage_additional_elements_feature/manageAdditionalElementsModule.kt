package pl.finitas.app.manage_additional_elements_feature

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.finitas.app.manage_additional_elements_feature.presentation.spending_category.SpendingCategoryViewModel

val manageAdditionalElementsModule = module {
    viewModel { SpendingCategoryViewModel(get()) }
}