package pl.finitas.app.room_feature.presentation.rooms

import androidx.lifecycle.ViewModel
import pl.finitas.app.room_feature.domain.service.RoomService

class RoomViewModel(
    private val roomService: RoomService,
): ViewModel() {
    val roomsState = roomService.getRoomsPreview()
}