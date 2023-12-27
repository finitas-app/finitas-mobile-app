package pl.finitas.app.room_feature

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.finitas.app.core.getDatabase
import pl.finitas.app.room_feature.data.data_sorce.MessageRepositoryImpl
import pl.finitas.app.room_feature.data.data_sorce.RoomRepositoryImpl
import pl.finitas.app.room_feature.domain.repository.MessageRepository
import pl.finitas.app.room_feature.domain.repository.RoomRepository
import pl.finitas.app.room_feature.domain.service.MessageService
import pl.finitas.app.room_feature.domain.service.RoomService
import pl.finitas.app.room_feature.presentation.messanger.MessengerViewModel
import pl.finitas.app.room_feature.presentation.room_settings.RoomSettingsViewModel
import pl.finitas.app.room_feature.presentation.rooms.RoomViewModel

val roomModule = module {
    single<MessageRepository> { MessageRepositoryImpl(getDatabase().messageDao) }
    single<RoomRepository> { RoomRepositoryImpl(getDatabase().roomDao, get()) }
    single { RoomService(get(), get()) }
    single { MessageService(get(), get(), get(), get()) }
    viewModel { RoomViewModel(get()) }
    viewModel { MessengerViewModel(get(),get(),get()) }
    viewModel { RoomSettingsViewModel(get(), get()) }
}