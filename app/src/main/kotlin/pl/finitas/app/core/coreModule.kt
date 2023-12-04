package pl.finitas.app.core

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.core.scope.Scope
import org.koin.dsl.module
import pl.finitas.app.core.data.ProfileRepositoryImpl
import pl.finitas.app.core.data.data_source.FinitasDatabase
import pl.finitas.app.core.domain.ProfileRepository
import pl.finitas.app.core.http.httpClient


val coreModule = module {
    httpClient()
    single {
        Room
            .databaseBuilder(
                androidApplication(),
                FinitasDatabase::class.java,
                FinitasDatabase.databaseName,
            )
            .createFromAsset("sqlite.db")
            .build()
    }

    single<ProfileRepository> {
        ProfileRepositoryImpl(androidApplication())
    }
}

fun Scope.getDatabase() = get<FinitasDatabase>()