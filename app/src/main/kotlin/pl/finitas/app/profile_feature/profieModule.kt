package pl.finitas.app.profile_feature

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.finitas.app.core.data.data_source.repository.ProfileRepositoryImpl
import pl.finitas.app.core.data.data_source.repository.UserStoreRepositoryImpl
import pl.finitas.app.core.domain.repository.ProfileRepository
import pl.finitas.app.core.domain.repository.UserStoreRepository
import pl.finitas.app.profile_feature.domain.services.ProfileService
import pl.finitas.app.profile_feature.presentation.ProfileViewModel

val profileModule = module {
    single<UserStoreRepository> {
        UserStoreRepositoryImpl(get())
    }
    single<ProfileRepository> {
        ProfileRepositoryImpl(get())
    }
    single {
        ProfileService(get(), get())
    }
    viewModel { ProfileViewModel(get()) }
}