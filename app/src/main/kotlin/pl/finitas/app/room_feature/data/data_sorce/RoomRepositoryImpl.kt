package pl.finitas.app.room_feature.data.data_sorce

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import pl.finitas.app.core.data.data_source.dao.RoomDao
import pl.finitas.app.core.data.model.Room
import pl.finitas.app.core.data.model.RoomRole
import pl.finitas.app.core.http.HttpUrls
import pl.finitas.app.core.http.frontendApiUrl
import pl.finitas.app.room_feature.domain.AddRoomDto
import pl.finitas.app.room_feature.domain.RoomMemberView
import pl.finitas.app.room_feature.domain.RoomRoleView
import pl.finitas.app.room_feature.domain.RoomWithAdditionalInfoView
import pl.finitas.app.room_feature.domain.repository.AddRoleRequest
import pl.finitas.app.room_feature.domain.repository.AssignRoleToUserRequest
import pl.finitas.app.room_feature.domain.repository.DeleteRoleRequest
import pl.finitas.app.room_feature.domain.repository.DeleteUserRequest
import pl.finitas.app.room_feature.domain.repository.RoomRepository
import pl.finitas.app.room_feature.domain.repository.UpdateRoleRequest
import pl.finitas.app.sync_feature.domain.RoomDto
import java.util.UUID

class RoomRepositoryImpl(
    private val roomDao: RoomDao,
    private val httpClient: HttpClient,
): RoomRepository {
    override fun getRooms(): Flow<List<Room>> {
        return roomDao.getRooms()
    }

    override suspend fun getRoomById(idRoom: UUID): Room {
        return roomDao.getRoomById(idRoom)
    }

    override suspend fun addRoomRepository(addRoomDto: AddRoomDto) {
        val response: RoomDto = httpClient.post(HttpUrls.addRoom) {
            setBody(addRoomDto)
        }.body()
        roomDao.upsertRoomsWithMembersAndRoles(listOf(response))
    }

    override fun getRoomWithAdditionalInfo(idRoom: UUID): Flow<RoomWithAdditionalInfoView> {
        // TODO: Split to 3 independent flow
        return combine(
            roomDao.getRoomByIdFlow(idRoom),
            roomDao.getRoomRolesByIdRoom(idRoom),
            roomDao.getRoomMembers(idRoom),
        ) { room, roles, members ->
            val rolesById = roles
                .map { it.toView() }
                .associateBy { it.idRole }

            RoomWithAdditionalInfoView(
                idRoom = room.idRoom,
                title = room.title,
                invitationLinkUUID = room.invitationLinkUUID,
                roomRoles = rolesById.values.toList(),
                roomMembers = members.map {
                    RoomMemberView(
                        idUser = it.idUser,
                        username = it.username,
                        roomRole = rolesById[it.idRole],
                    )
                }
            )
        }
    }

    override suspend fun addRole(roomRole: AddRoleRequest) {
        httpClient.post("$frontendApiUrl/rooms/roles") {
            setBody(roomRole)
        }
    }

    override suspend fun updateRole(roomRole: UpdateRoleRequest) {
        httpClient.put("$frontendApiUrl/rooms/roles") {
            setBody(roomRole)
        }
    }

    override suspend fun deleteRole(roomRole: DeleteRoleRequest) {
        httpClient.delete("$frontendApiUrl/rooms/roles") {
            setBody(roomRole)
        }
    }

    override suspend fun deleteUserFromRoom(deleteUserRequest: DeleteUserRequest) {
        httpClient.delete("$frontendApiUrl/rooms/users") {
            setBody(deleteUserRequest)
        }
    }

    override suspend fun assignRoleToUser(assignRoleToUserRequest: AssignRoleToUserRequest) {
        httpClient.put("$frontendApiUrl/rooms/users/roles") {
            setBody(assignRoleToUserRequest)
        }
    }
}

private fun RoomRole.toView() = RoomRoleView(idRole, name, authorities)
