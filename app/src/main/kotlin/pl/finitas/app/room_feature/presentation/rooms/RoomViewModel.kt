package pl.finitas.app.room_feature.presentation.rooms

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.finitas.app.room_feature.domain.service.RoomService

class RoomViewModel(
    private val roomService: RoomService,
): ViewModel() {
    val roomsState = roomService.getRoomsPreview()

    var isAddRoomDialogOpen by mutableStateOf(false)
        private set

    fun openAddDialog() {
        isAddRoomDialogOpen = true
    }

    fun closeAddDialog() {
        isAddRoomDialogOpen = false
    }

    fun addRoom(addRoomState: AddRoomState) {
        viewModelScope.launch {
            roomService.addRoom(addRoomState)
            closeAddDialog()
        }
    }
}