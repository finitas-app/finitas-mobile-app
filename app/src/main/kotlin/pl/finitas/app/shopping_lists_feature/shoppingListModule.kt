package pl.finitas.app.shopping_lists_feature

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.finitas.app.core.getDatabase
import pl.finitas.app.shopping_lists_feature.data.data_sourse.ShoppingListRepositoryImpl
import pl.finitas.app.shopping_lists_feature.domain.ShoppingListRepository
import pl.finitas.app.shopping_lists_feature.domain.ShoppingListService
import pl.finitas.app.shopping_lists_feature.presentation.read_shopping_list.ShoppingListViewModel
import pl.finitas.app.shopping_lists_feature.presentation.write_shopping_list.UpsertShoppingListViewModel

val shoppingListModule = module {
    single<ShoppingListRepository> {
        ShoppingListRepositoryImpl(getDatabase().shoppingListDao)
    }
    single {
        ShoppingListService(get(), get(), get(), get())
    }
    viewModel {
        UpsertShoppingListViewModel(get(), get())
    }
    viewModel {
        ShoppingListViewModel(get())
    }
}