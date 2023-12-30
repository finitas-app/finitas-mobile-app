package pl.finitas.app.sync_feature

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import pl.finitas.app.core.getDatabase
import pl.finitas.app.sync_feature.data.data_source.MessageSyncRepositoryImpl
import pl.finitas.app.sync_feature.data.data_source.RoomSyncRepositoryImpl
import pl.finitas.app.sync_feature.data.data_source.ShoppingListSyncRepositoryImpl
import pl.finitas.app.sync_feature.data.data_source.VersionRepositoryImpl
import pl.finitas.app.sync_feature.domain.SynchronizationService
import pl.finitas.app.sync_feature.domain.repository.MessageSyncRepository
import pl.finitas.app.sync_feature.domain.repository.RoomSyncRepository
import pl.finitas.app.sync_feature.domain.repository.ShoppingListSyncRepository
import pl.finitas.app.sync_feature.domain.repository.VersionsRepository

val synchronizationModule = module {
    single<VersionsRepository> { VersionRepositoryImpl(getDatabase().versionsDao) }
    single<MessageSyncRepository> { MessageSyncRepositoryImpl(getDatabase().messageDao, get()) }
    single<RoomSyncRepository> { RoomSyncRepositoryImpl(getDatabase().roomDao, get()) }
    single<ShoppingListSyncRepository> { ShoppingListSyncRepositoryImpl(getDatabase().shoppingListDao) }
    singleOf(::SynchronizationService)
    viewModel { SynchronizationViewModel(get(), get(), get()) }
}