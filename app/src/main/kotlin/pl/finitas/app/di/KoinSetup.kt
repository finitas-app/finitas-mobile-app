package pl.finitas.app.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.KoinAppDeclaration
import pl.finitas.app.core.coreModule
import pl.finitas.app.manage_spendings_feature.manageSpendingsModule
import pl.finitas.app.profile_feature.profileModule

fun koinApplication(context: Context): KoinAppDeclaration = {
    androidContext(context)
    modules(
        coreModule,
        manageSpendingsModule,
        profileModule,
    )
}