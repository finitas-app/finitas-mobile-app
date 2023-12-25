package pl.finitas.app.core

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.core.scope.Scope
import org.koin.dsl.module
import pl.finitas.app.core.data.data_source.FinitasDatabase
import pl.finitas.app.core.data.data_source.repository.FinishedSpendingStoreRepositoryImpl
import pl.finitas.app.core.data.data_source.repository.MessageSenderRepositoryImpl
import pl.finitas.app.core.data.data_source.repository.ProfileRepositoryImpl
import pl.finitas.app.core.data.data_source.repository.ShoppingListStoreRepositoryImpl
import pl.finitas.app.core.data.data_source.repository.UserStoreRepositoryImpl
import pl.finitas.app.core.domain.repository.FinishedSpendingStoreRepository
import pl.finitas.app.core.domain.repository.MessageSenderRepository
import pl.finitas.app.core.domain.repository.ProfileRepository
import pl.finitas.app.core.domain.repository.ShoppingListStoreRepository
import pl.finitas.app.core.domain.repository.UserStoreRepository
import pl.finitas.app.core.http.httpClient
import pl.finitas.app.sync_feature.data.data_source.UserRepositoryImpl
import pl.finitas.app.sync_feature.domain.repository.UserRepository


val coreModule = module {
    single<ProfileRepository> {
        ProfileRepositoryImpl(androidApplication())
    }
    single<FinishedSpendingStoreRepository> {
        FinishedSpendingStoreRepositoryImpl(get())
    }
    single<ShoppingListStoreRepository> {
        ShoppingListStoreRepositoryImpl(get())
    }
    single<UserStoreRepository> {
        UserStoreRepositoryImpl(get())
    }
    httpClient()
    single {
        Room
            .databaseBuilder(
                androidApplication(),
                FinitasDatabase::class.java,
                FinitasDatabase.databaseName,
            )
            //.createFromAsset("sqlite.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    single<UserRepository> { UserRepositoryImpl(getDatabase().userDao) }
    single<MessageSenderRepository> { MessageSenderRepositoryImpl(get()) }
}

fun Scope.getDatabase() = get<FinitasDatabase>()