package pl.finitas.app.profile_feature

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.finitas.app.profile_feature.domain.services.ProfileService
import pl.finitas.app.profile_feature.presentation.ProfileViewModel

val profileModule = module {
    single {
        ProfileService(get())
    }
    viewModel { ProfileViewModel(get()) }
}