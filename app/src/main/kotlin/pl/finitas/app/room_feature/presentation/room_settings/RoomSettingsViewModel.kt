package pl.finitas.app.room_feature.presentation.room_settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import pl.finitas.app.core.data.model.Authority
import pl.finitas.app.core.domain.services.AuthorizedUserService
import pl.finitas.app.room_feature.domain.RoomWithAdditionalInfoView
import pl.finitas.app.room_feature.domain.service.RoomService
import pl.finitas.app.room_feature.presentation.messanger.IdRoomNotProvidedException
import pl.finitas.app.room_feature.presentation.room_settings.roles.UpsertRoleState
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class RoomSettingsViewModel(
    private val roomService: RoomService,
    private val authorizedUserService: AuthorizedUserService,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private var _idRoom by mutableStateOf<UUID?>(null)
    var idRoom: UUID
        get() = _idRoom ?: throw IdRoomNotProvidedException()
        set(value) { _idRoom = value }

    val authorizedUserId = authorizedUserService.getAuthorizedIdUser()

    private val authorities: Flow<Set<Authority>>
    val hasModifyRoomAuthority: Flow<Boolean>
    val hasReadUsersDataAuthority: Flow<Boolean>

    var upsertRoleState by mutableStateOf(UpsertRoleState.empty)
        private set

    var selectedUser by mutableStateOf<UUID?>(null)
        private set

    val room: Flow<RoomWithAdditionalInfoView?>

    var isRoomRoleDialogOpen by mutableStateOf(false)
        private set

    var isChangeRoomNameDialogOpen by mutableStateOf(false)
        private set

    var newRoomName by mutableStateOf("")
        private set


    init {
        val idRoom = savedStateHandle.get<String>("idRoom")?.let(UUID::fromString) ?: throw IdRoomNotProvidedException()
        this.idRoom = idRoom

        room = roomService.getRoomWithAdditionalInfo(idRoom).onEach { room ->
            if (upsertRoleState.idRole != null && room.roomRoles.find { it.idRole == upsertRoleState.idRole } == null)
                closeRoleDialog()
            if (selectedUser != null && room.roomMembers.find { it.idUser == selectedUser } == null)
                closeUserDialog()
        }.catch<RoomWithAdditionalInfoView?> {
            emit(null)
        }
        authorities = authorizedUserId.flatMapMerge {
            roomService.getAuthoritiesForUser(it, idRoom)
        }
        hasModifyRoomAuthority = authorities.map { Authority.MODIFY_ROOM in it }
        hasReadUsersDataAuthority = authorities.map { Authority.READ_USERS_DATA in it }
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

    fun regenerateLink() {
        viewModelScope.launch {
            roomService.regenerateLink(idRoom)
        }
    }

    fun changeRoomName() {
        viewModelScope.launch {
            roomService.changeRoomName(idRoom, newRoomName)
            closeChangeRoomNameDialog()
        }
    }

    fun openChangeRoomNameDialog(currentName: String) {
        this.newRoomName = currentName
        isChangeRoomNameDialogOpen = true
    }

    fun closeChangeRoomNameDialog() {
        this.newRoomName = ""
        isChangeRoomNameDialogOpen = false
    }

    fun setNewRoomNameValue(newRoomName: String) {
        this.newRoomName = newRoomName
    }
}
