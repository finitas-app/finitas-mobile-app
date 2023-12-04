package pl.finitas.app.profile_feature

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.finitas.app.profile_feature.data.AuthRepositoryImpl
import pl.finitas.app.profile_feature.domain.AuthRepository
import pl.finitas.app.profile_feature.domain.AuthService
import pl.finitas.app.profile_feature.presentation.AuthViewModel

val profileModule = module {
    single<AuthRepository> {
        AuthRepositoryImpl(get())
    }
    single {
        AuthService(get(), get())
    }
    viewModel { AuthViewModel(get()) }
}