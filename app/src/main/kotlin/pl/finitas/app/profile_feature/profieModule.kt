package pl.finitas.app.profile_feature

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.finitas.app.core.data.data_source.repository.UserStoreRepositoryImpl
import pl.finitas.app.core.domain.repository.UserStoreRepository
import pl.finitas.app.profile_feature.domain.services.NotificationSettingsService
import pl.finitas.app.profile_feature.domain.services.ProfileService
import pl.finitas.app.profile_feature.presentation.NotificationPushViewModel
import pl.finitas.app.profile_feature.presentation.ProfileViewModel

val profileModule = module {
    single<UserStoreRepository> {
        UserStoreRepositoryImpl(get())
    }
    single {
        ProfileService(get(), get())
    }
    single {
        NotificationSettingsService(get())
    }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { NotificationPushViewModel(get()) }
}