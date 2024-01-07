package pl.finitas.app.room_feature.domain.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import pl.finitas.app.core.data.model.Authority
import pl.finitas.app.core.data.model.Room
import pl.finitas.app.core.data.model.RoomMember
import pl.finitas.app.core.domain.exceptions.InputValidationException
import pl.finitas.app.core.domain.validateBuilder
import pl.finitas.app.core.http.FrontendApiException
import pl.finitas.app.room_feature.domain.CreateRoomDto
import pl.finitas.app.room_feature.domain.RoomPreviewDto
import pl.finitas.app.room_feature.domain.RoomWithAdditionalInfoView
import pl.finitas.app.room_feature.domain.repository.AddRoleRequest
import pl.finitas.app.room_feature.domain.repository.AssignRoleToUserRequest
import pl.finitas.app.room_feature.domain.repository.DeleteRoleRequest
import pl.finitas.app.room_feature.domain.repository.DeleteUserRequest
import pl.finitas.app.room_feature.domain.repository.JoinRoomDto
import pl.finitas.app.room_feature.domain.repository.MessageRepository
import pl.finitas.app.room_feature.domain.repository.RoomRepository
import pl.finitas.app.room_feature.domain.repository.UpdateRoleRequest
import pl.finitas.app.room_feature.presentation.room_settings.roles.UpsertRoleState
import pl.finitas.app.room_feature.presentation.rooms.AddRoomOption
import pl.finitas.app.room_feature.presentation.rooms.AddRoomState
import java.util.UUID

class RoomService(
    private val roomRepository: RoomRepository,
    private val messageRepository: MessageRepository,
) {
    fun getRoomsPreview(): Flow<List<RoomPreviewDto>> = roomRepository.getRooms().combine(
        messageRepository
            .getLastMessage()
    ) { rooms, messages ->
        val roomIds = rooms.map { it.idRoom }
        val lastMessagesByRoom = messages
            .groupBy { it.idRoom }
            .mapValues { it.value[0] }
        val unreadCountByRoom = messageRepository
            .getUnreadMessageCountBy(roomIds)
            .groupBy { it.idRoom }
            .mapValues { it.value[0] }

        rooms.map {
            RoomPreviewDto(
                idRoom = it.idRoom,
                title = it.title,
                lastMessage = if (lastMessagesByRoom[it.idRoom] == null) {
                    ""
                } else if (lastMessagesByRoom[it.idRoom]?.content == null) {
                    "Shopping list"
                } else {
                    lastMessagesByRoom[it.idRoom]?.content!!
                },
                unreadMessagesNumber = unreadCountByRoom[it.idRoom]?.unreadCount ?: 0
            )
        }
    }

    fun findRoomById(idRoom: UUID): Flow<Room?> {
        return roomRepository.findRoomById(idRoom)
    }

    fun getRoomWithAdditionalInfo(idRoom: UUID): Flow<RoomWithAdditionalInfoView> {
        return roomRepository.getRoomWithAdditionalInfo(idRoom)
    }

    suspend fun getRoomMembers(idRoom: UUID): List<RoomMember> {
        return roomRepository.getRoomMembers(idRoom)
    }

    suspend fun addRoom(addRoomState: AddRoomState) {
        when (addRoomState.addRoomOption) {
            AddRoomOption.Join -> {
                validateBuilder {
                    validate(addRoomState.invitationLink.isNotBlank()) { "The invitation link cannot be blank." }
                }
                try {
                    roomRepository.joinRoom(addRoomState.toJoinDto())
                } catch (e: IllegalArgumentException) {
                    throw InputValidationException("The invitational link is wrong. Try another one.")
                } catch (e: FrontendApiException) {
                    throw InputValidationException("The invitational link is wrong. Try another one.")
                }
            }

            AddRoomOption.Create -> {
                validateBuilder {
                    validate(addRoomState.title.isNotBlank()) { "Room title cannot be blank." }
                }
                roomRepository.createRoom(addRoomState.toCreateDto())
            }
        }
    }

    suspend fun upsertRole(idRoom: UUID, upsertRoleState: UpsertRoleState) {
        validateBuilder {
            validate(upsertRoleState.name.isNotBlank(), "roleTitle") {
                "Role title cannot be empty."
            }
        }
        try {
            if (upsertRoleState.idRole == null)
                roomRepository.addRole(
                    AddRoleRequest(
                        idRoom = idRoom,
                        name = upsertRoleState.name,
                        authorities = upsertRoleState.authorities,
                    )
                )
            else
                roomRepository.updateRole(
                    UpdateRoleRequest(
                        idRole = upsertRoleState.idRole,
                        idRoom = idRoom,
                        name = upsertRoleState.name,
                        authorities = upsertRoleState.authorities,
                    )
                )
        } catch (e: FrontendApiException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
            throw InputValidationException(
                mapOf(
                    "roleSummary" to listOf(
                        "Failed to change roles, check your internet connection."
                    )
                )
            )
        }
    }

    suspend fun deleteRole(idRoom: UUID, idRole: UUID) {
        try {
            roomRepository.deleteRole(
                DeleteRoleRequest(idRoom, idRole)
            )
        } catch (e: Exception) {

            e.printStackTrace()
            throw InputValidationException(
                mapOf(
                    "deleteRole" to listOf(
                        "Failed to delete roles, check your internet connection."
                    )
                )
            )
        }
    }

    suspend fun deleteUserFromRoom(idRoom: UUID, idUser: UUID) {
        try {
            roomRepository.deleteUserFromRoom(
                DeleteUserRequest(
                    idRoom = idRoom,
                    idUser = idUser,
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            throw InputValidationException(
                mapOf(
                    "userSummary" to listOf(
                        "Failed to delete user from room, check your internet connection."
                    )
                )
            )
        }
    }

    suspend fun assignRoleToUser(idRoom: UUID, idUser: UUID, idRole: UUID?) {
        try {
            roomRepository.assignRoleToUser(
                AssignRoleToUserRequest(
                    idRoom = idRoom,
                    idRole = idRole,
                    idUser = idUser
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            throw InputValidationException(
                mapOf(
                    "assignRoleToUser" to listOf(
                        "Failed to assign role to user, check your internet connection."
                    )
                )
            )
        }
    }

    fun getAuthoritiesForUser(idUser: UUID?, idRoom: UUID): Flow<Set<Authority>> {
        return roomRepository.getAuthorizedUserAuthorities(idUser, idRoom)
    }

    suspend fun regenerateLink(idRoom: UUID) {
        try {
            roomRepository.regenerateLink(idRoom)
        } catch (e: Exception) {
            e.printStackTrace()
            throw InputValidationException(
                mapOf(
                    "regenerateLink" to listOf(
                        "Failed to regenerate link, check your internet connection."
                    )
                )
            )
        }
    }

    suspend fun changeRoomName(idRoom: UUID, newName: String) {
        validateBuilder {
            validate(newName.isNotBlank(), "roomName") {
                "Room name cannot be empty."
            }
        }
        try {
            roomRepository.changeRoomName(idRoom, newName)
        } catch (e: Exception) {
            e.printStackTrace()
            throw InputValidationException(
                mapOf(
                    "roomName" to listOf(
                        "Failed to change room name, check your internet connection."
                    )
                )
            )
        }
    }
}

private fun AddRoomState.toJoinDto(): JoinRoomDto {
    val idInvitationLink = invitationLink.trim().split("/").last()
    return JoinRoomDto(UUID.fromString(idInvitationLink))
}

private fun AddRoomState.toCreateDto() = CreateRoomDto(title)