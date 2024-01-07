package pl.finitas.app.room_feature.presentation.rooms

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.finitas.app.core.domain.exceptions.InputValidationException
import pl.finitas.app.core.domain.services.AuthorizedUserService
import pl.finitas.app.room_feature.domain.service.RoomService
import pl.finitas.app.room_feature.presentation.rooms.AddRoomOption.Create
import pl.finitas.app.room_feature.presentation.rooms.AddRoomOption.Join

class RoomViewModel(
    private val roomService: RoomService,
    private val authorizedUserService: AuthorizedUserService,
) : ViewModel() {
    var addRoomErrors by mutableStateOf<List<String>?>(null)
    val roomsState = roomService.getRoomsPreview()
    val authorizedUserId = authorizedUserService.getAuthorizedIdUser()
    var addRoomState by mutableStateOf(AddRoomState(Join, "", ""))
        private set

    var isAddRoomDialogOpen by mutableStateOf(false)
        private set

    fun openAddDialog() {
        isAddRoomDialogOpen = true
    }

    fun closeAddDialog() {
        isAddRoomDialogOpen = false
        addRoomState = AddRoomState(Join, "", "")
        addRoomErrors = null
    }

    fun addRoom() {
        viewModelScope.launch {
            try {
                roomService.addRoom(addRoomState)
                closeAddDialog()
            } catch (e: InputValidationException) {
                addRoomErrors = e.errors[null]
            } catch (e: Exception) {
                val action = when (addRoomState.addRoomOption) {
                    Create -> "create"

                    Join -> "join"
                }
                addRoomErrors = listOf("Failed to $action room, check your internet connection.")
            }
        }
    }

    fun onTitleChange(newTitle: String) {
        addRoomState = addRoomState.copy(title = newTitle)
    }

    fun onInvitationLinkChange(newInvitationLink: String) {
        addRoomState = addRoomState.copy(invitationLink = newInvitationLink)
    }

    fun onAddRoomOptionChange(addRoomOption: AddRoomOption) {
        addRoomState = AddRoomState(addRoomOption, "", "")
        addRoomErrors = null
    }
}