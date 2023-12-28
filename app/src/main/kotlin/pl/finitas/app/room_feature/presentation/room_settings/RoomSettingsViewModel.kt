package pl.finitas.app.room_feature.presentation.room_settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import pl.finitas.app.core.data.model.Authority
import pl.finitas.app.room_feature.domain.RoomWithAdditionalInfoView
import pl.finitas.app.room_feature.domain.service.RoomService
import pl.finitas.app.room_feature.presentation.messanger.IdRoomNotProvidedException
import pl.finitas.app.room_feature.presentation.room_settings.roles.UpsertRoleState
import java.util.UUID

class RoomSettingsViewModel(
    private val roomService: RoomService,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private var _idRoom by mutableStateOf<UUID?>(null)
    var idRoom: UUID
        get() = _idRoom ?: throw IdRoomNotProvidedException()
        set(value) { _idRoom = value }

    var upsertRoleState by mutableStateOf(UpsertRoleState.empty)
        private set

    var selectedUser by mutableStateOf<UUID?>(null)
        private set

    val room: Flow<RoomWithAdditionalInfoView>

    var isRoomRoleDialogOpen by mutableStateOf(false)
        private set


    init {
        val idRoom = savedStateHandle.get<String>("idRoom")?.let(UUID::fromString) ?: throw IdRoomNotProvidedException()
        this.idRoom = idRoom

        room = roomService.getRoomWithAdditionalInfo(idRoom).onEach { room ->
            if (upsertRoleState.idRole != null && room.roomRoles.find { it.idRole == upsertRoleState.idRole } == null)
                closeRoleDialog()
            if (selectedUser != null && room.roomMembers.find { it.idUser == selectedUser } == null)
                closeUserDialog()
        }
    }

    fun openRoleDialog(roomRole: UpsertRoleState = UpsertRoleState.empty) {
        this.upsertRoleState = roomRole
        isRoomRoleDialogOpen = true
    }

    fun closeRoleDialog() {
        this.upsertRoleState = UpsertRoleState.empty
        isRoomRoleDialogOpen = false
    }

    fun closeUserDialog() {
        selectedUser = null
    }

    fun upsertRole() {
        viewModelScope.launch {
            roomService.upsertRole(idRoom, upsertRoleState)
            closeRoleDialog()
        }
    }

    fun deleteRole(idRole: UUID, onSuccess: () -> Unit) {
        viewModelScope.launch {
            roomService.deleteRole(idRoom, idRole)
            onSuccess()
        }
    }

    fun setRoleName(name: String) {
        upsertRoleState = upsertRoleState.copy(name = name)
    }

    fun addAuthority(authority: Authority) {
        upsertRoleState = upsertRoleState.copy(authorities = upsertRoleState.authorities + authority)
    }

    fun removeAuthority(authority: Authority) {
        upsertRoleState = upsertRoleState.copy(authorities = upsertRoleState.authorities - authority)
    }

    fun selectUser(idUser: UUID) {
        selectedUser = idUser
    }

    fun removeSelectedUserFromRoom() {
        viewModelScope.launch {
            if (selectedUser != null) roomService.deleteUserFromRoom(
                idRoom = idRoom,
                idUser = selectedUser!!
            )
            closeUserDialog()
        }
    }

    fun assignRoleToSelectedUser(idRole: UUID?, onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (selectedUser != null) roomService.assignRoleToUser(
                idRoom = idRoom,
                idUser = selectedUser!!,
                idRole = idRole
            )
            onSuccess()
        }
    }
}
