package pl.finitas.app.di

import android.content.Context
import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.Scope
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import pl.finitas.app.core.data.data_source.FinitasDatabase
import pl.finitas.app.manage_spendings_feature.manageSpendingsModule

fun koinApplication(context: Context): KoinAppDeclaration = {
    androidContext(context)
    modules(
        applicationModule,
        manageSpendingsModule,
    )
}

val applicationModule = module {
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
}

fun Scope.getDatabase() = get<FinitasDatabase>()