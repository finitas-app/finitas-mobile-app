package pl.finitas.app.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.KoinAppDeclaration
import pl.finitas.app.auth_feature.profileModule
import pl.finitas.app.core.coreModule
import pl.finitas.app.manage_additional_elements_feature.manageAdditionalElementsModule
import pl.finitas.app.manage_spendings_feature.manageSpendingsModule
import pl.finitas.app.room_feature.roomModule
import pl.finitas.app.shopping_lists_feature.shoppingListModule
import pl.finitas.app.sync_feature.synchronizationModule

fun koinApplication(context: Context): KoinAppDeclaration = {
    androidContext(context)
    modules(
        coreModule,
        profileModule,
        synchronizationModule,
        manageSpendingsModule,
        manageAdditionalElementsModule,
        shoppingListModule,
        roomModule,
    )
}
