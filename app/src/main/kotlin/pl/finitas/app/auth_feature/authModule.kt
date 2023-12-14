package pl.finitas.app.auth_feature

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.finitas.app.auth_feature.data.AuthRepositoryImpl
import pl.finitas.app.auth_feature.domain.AuthRepository
import pl.finitas.app.auth_feature.domain.AuthService
import pl.finitas.app.auth_feature.presentation.AuthViewModel

val profileModule = module {
    single<AuthRepository> {
        AuthRepositoryImpl(get())
    }
    single {
        AuthService(get(), get())
    }
    viewModel { AuthViewModel(get()) }
}