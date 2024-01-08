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
import pl.finitas.app.core.domain.exceptions.InputValidationException
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

    var errors by mutableStateOf(mapOf<String?, List<String>?>())
        private set

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
        }.onEach {  authorities ->
            if (Authority.MODIFY_ROOM !in authorities) {
                closeChangeRoomNameDialog()
                closeRoleDialog()
            }
            if (Authority.READ_USERS_DATA !in authorities && Authority.MODIFY_ROOM !in authorities) {
                closeUserDialog()
            }
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
            try {
                roomService.upsertRole(idRoom, upsertRoleState)
                closeRoleDialog()
            } catch (e: InputValidationException) {
                errors = e.errors
            }
        }
    }

    fun deleteRole(idRole: UUID, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                roomService.deleteRole(idRoom, idRole)
                onSuccess()
            } catch (e: InputValidationException) {
                errors = e.errors
            }
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
            try {
                if (selectedUser != null) {
                    roomService.deleteUserFromRoom(
                        idRoom = idRoom,
                        idUser = selectedUser!!
                    )
                }
                closeUserDialog()
            } catch (e: InputValidationException) {
                errors = e.errors
            }
        }
    }

    fun assignRoleToSelectedUser(idRole: UUID?, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                if (selectedUser != null) roomService.assignRoleToUser(
                    idRoom = idRoom,
                    idUser = selectedUser!!,
                    idRole = idRole
                )
                onSuccess()
            } catch (e: InputValidationException) {
                errors = e.errors
            }
        }
    }

    fun regenerateLink() {
        viewModelScope.launch {
            try {
                roomService.regenerateLink(idRoom)
            } catch (e: InputValidationException) {
                errors = e.errors
            }
        }
    }

    fun changeRoomName() {
        viewModelScope.launch {
            try {
                roomService.changeRoomName(idRoom, newRoomName)
                closeChangeRoomNameDialog()
            } catch (e: InputValidationException) {
                errors = e.errors
            }
        }
    }

    fun openChangeRoomNameDialog(currentName: String) {
        this.newRoomName = currentName
        isChangeRoomNameDialogOpen = true
    }

    fun closeChangeRoomNameDialog() {
        this.newRoomName = ""
        errors = mapOf()
        isChangeRoomNameDialogOpen = false
    }

    fun setNewRoomNameValue(newRoomName: String) {
        this.newRoomName = newRoomName
    }
}
