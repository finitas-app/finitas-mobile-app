package pl.finitas.app.sync_feature.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pl.finitas.app.core.data.model.Authority
import pl.finitas.app.core.data.model.MessagesVersion
import pl.finitas.app.core.data.model.RoomMessage
import pl.finitas.app.core.data.model.RoomVersion
import pl.finitas.app.core.data.model.ShoppingListVersion
import pl.finitas.app.core.data.model.SpendingCategory
import pl.finitas.app.core.data.model.User
import pl.finitas.app.core.domain.dto.store.GetVisibleNamesRequest
import pl.finitas.app.core.domain.dto.store.RemoteFinishedSpendingDto
import pl.finitas.app.core.domain.dto.store.RemoteShoppingItemDto
import pl.finitas.app.core.domain.dto.store.RemoteShoppingListDto
import pl.finitas.app.core.domain.dto.store.RemoteSpendingRecordDto
import pl.finitas.app.core.domain.dto.store.UserIdValue
import pl.finitas.app.core.domain.emptyUUID
import pl.finitas.app.core.domain.repository.CategoryVersionDto
import pl.finitas.app.core.domain.repository.FinishedSpendingStoreRepository
import pl.finitas.app.core.domain.repository.FinishedSpendingVersion
import pl.finitas.app.core.domain.repository.MessageSenderRepository
import pl.finitas.app.core.domain.repository.SendMessageRequest
import pl.finitas.app.core.domain.repository.ShoppingListStoreRepository
import pl.finitas.app.core.domain.repository.SingleMessageDto
import pl.finitas.app.core.domain.repository.SpendingCategoryRepository
import pl.finitas.app.core.domain.repository.UserStoreRepository
import pl.finitas.app.manage_spendings_feature.domain.model.FinishedSpendingWithRecordsDto
import pl.finitas.app.shopping_lists_feature.domain.ShoppingItemDto
import pl.finitas.app.shopping_lists_feature.domain.ShoppingListDto
import pl.finitas.app.sync_feature.data.data_source.NewMessagesResponse
import pl.finitas.app.sync_feature.domain.repository.FinishedSpendingSyncRepository
import pl.finitas.app.sync_feature.domain.repository.MessageSyncRepository
import pl.finitas.app.sync_feature.domain.repository.RoomSyncRepository
import pl.finitas.app.sync_feature.domain.repository.ShoppingListSyncRepository
import pl.finitas.app.sync_feature.domain.repository.SyncFinishedSpendingWithRecordsDto
import pl.finitas.app.sync_feature.domain.repository.SyncSpendingRecordDto
import pl.finitas.app.sync_feature.domain.repository.UserRepository
import pl.finitas.app.sync_feature.domain.repository.VersionsRepository
import java.util.UUID

// TODO Split the whole file into use cases instead of methods since it will be very large in size
class SynchronizationService(
    private val messageSyncRepository: MessageSyncRepository,
    private val versionsRepository: VersionsRepository,
    private val roomSyncRepository: RoomSyncRepository,
    private val messageSenderRepository: MessageSenderRepository,
    private val userStoreRepository: UserStoreRepository,
    private val userRepository: UserRepository,
    private val shoppingListSyncRepository: ShoppingListSyncRepository,
    private val shoppingListStoreRepository: ShoppingListStoreRepository,
    private val categoriesRepository: SpendingCategoryRepository,
    private val finishedSpendingSyncRepository: FinishedSpendingSyncRepository,
    private val finishedSpendingStoreRepository: FinishedSpendingStoreRepository,
) {
    fun CoroutineScope.fullSync(authorizedUserId: UUID) = launch {
        fullSyncRooms(authorizedUserId)
        fullSyncNames(listOf())
        fullSyncMessages(authorizedUserId)
        fullSyncCategories(authorizedUserId)
        fullSyncShoppingLists(authorizedUserId)
        fullSyncFinishedSpendings(authorizedUserId) //TODO: uncomment
    }

    // TODO: split for room parts and sync only room that is needed
    suspend fun fullSyncRooms(authorizedUserId: UUID) {
        val roomVersions = versionsRepository.getRoomVersions()
        val messagesVersion = versionsRepository.getMessagesVersions().map { it.idRoom }
        val (remoteRooms, unavailableRooms) = roomSyncRepository.getRoomsFromVersionRemote(
            roomVersions
        )
        val localRoomMembers = roomSyncRepository.getRoomMembers(remoteRooms.map { it.idRoom })
        val membersDataToDeleteIds = localRoomMembers.map { it.idUser } - remoteRooms
            .flatMap { room -> room.members.map { it.idUser } }
            .toSet()

        (membersDataToDeleteIds + roomSyncRepository.getRoomMembers(unavailableRooms).map { it.idUser }).forEach {
            finishedSpendingSyncRepository.deleteByIdUser(it)
            shoppingListSyncRepository.deleteByIdUser(it)
            categoriesRepository.deleteByIdUser(it)
            userRepository.clearVersions(it)
        }

        roomSyncRepository.deleteRooms(unavailableRooms)
        roomSyncRepository.upsertRooms(remoteRooms)

        versionsRepository.setRoomVersions(remoteRooms.map { RoomVersion(it.idRoom, it.version) })
        val newMessageVersions = remoteRooms.filter { it.idRoom !in messagesVersion }.map {
            MessagesVersion(it.idRoom, -1)
        }
        if (newMessageVersions.isNotEmpty()) {
            // TODO: After part split left it only for create_event or join_event
            versionsRepository.setMessagesVersions(newMessageVersions)
            fullSyncMessages(authorizedUserId, newMessageVersions)
        }
    }

    suspend fun syncMessages(authorizedUserId: UUID, newMessages: List<NewMessagesResponse>) {
        val messagesGroupedByIdRoom = newMessages.groupBy { it.idRoom }
        for ((idRoom, messages) in messagesGroupedByIdRoom) {
            val sortedMessagesByVersion = messages.sortedBy { it.version }
            val currentMessageVersion = versionsRepository.getMessagesVersion(idRoom)
            if (currentMessageVersion.version + 1 != sortedMessagesByVersion[0].version) {
                fullSyncMessages(authorizedUserId)
                break
            }
            messageSyncRepository.upsertMessages(
                sortedMessagesByVersion.map {
                    RoomMessage(
                        idUser = it.idUser,
                        isPending = false,
                        idRoom = it.idRoom,
                        idMessage = it.idMessage,
                        idShoppingList = it.idShoppingList,
                        content = it.content,
                        createdAt = it.createdAt,
                        isRead = authorizedUserId == it.idUser,
                        version = it.version,
                    )
                }
            )
            versionsRepository.setMessagesVersion(
                MessagesVersion(
                    idRoom,
                    sortedMessagesByVersion.last().version
                )
            )
        }
    }

    suspend fun fullSyncNames(names: List<UserIdValue>) {
        val usersById = userRepository.getUsers().associateBy { it.idUser }
        userStoreRepository.getVisibleNames(GetVisibleNamesRequest(names))
            .let { users ->
                userRepository.saveUsers(
                    users.map {
                        User(
                            idUser = it.idUser,
                            username = it.visibleName ?: "",
                            version = usersById[it.idUser]?.version ?: -1,
                            spendingCategoryVersion = usersById[it.idUser]?.spendingCategoryVersion
                                ?: -1,
                            finishedSpendingVersion = usersById[it.idUser]?.finishedSpendingVersion
                                ?: -1,
                            shoppingListVersion = usersById[it.idUser]?.shoppingListVersion
                                ?: -1,
                        )
                    }
                )
            }
    }

    suspend fun fullSyncCategories(authorizedUserId: UUID) {
        val changedCategories = categoriesRepository.getChangedCategories()
        if (changedCategories.isNotEmpty())
            userStoreRepository.changeCategories(changedCategories)
        retrieveNewCategories(authorizedUserId)
    }

    suspend fun retrieveNewCategories(authorizedUserId: UUID) {
        val usersBy = userRepository.getUsers()
        val response = userStoreRepository.syncCategories(usersBy.map {
            CategoryVersionDto(
                it.idUser,
                it.spendingCategoryVersion,
            )
        })
        val categoriesToDelete =
            response.userCategories.flatMap { user -> user.categories }.filter { it.isDeleted }
        categoriesRepository.deleteSpendingCategoriesBy(categoriesToDelete.map { it.idCategory })
        categoriesRepository.upsertSpendingCategories(
            response.userCategories.flatMap { user ->
                user.categories.filter { !it.isDeleted }.map {
                    SpendingCategory(
                        name = it.name,
                        idParent = it.idParent,
                        idUser = if (authorizedUserId == user.idUser) null else user.idUser,
                        idCategory = it.idCategory,
                        version = it.version,
                        isDeleted = it.isDeleted,
                    )
                }
            }
        )
        categoriesRepository.setCategoryVersions(response.userCategories.map {
            CategoryVersionDto(
                it.idUser,
                it.categoryVersion
            )
        })
    }


    private suspend fun fullSyncMessages(
        authorizedUserId: UUID,
        messageVersionsFilter: List<MessagesVersion>? = null,
    ) {
        val messageVersions = messageVersionsFilter ?: versionsRepository.getMessagesVersions()
        val response = messageSyncRepository.getMessagesFromVersionRemote(messageVersions)
        roomSyncRepository.deleteRooms(response.unavailableRooms)
        val pendingMessages = messageSyncRepository.getPendingMessages()
        if (pendingMessages.isNotEmpty()) {
            try {
                messageSenderRepository.sendMessages(
                    SendMessageRequest(
                        messages = pendingMessages.map {
                            SingleMessageDto(
                                idMessage = it.idMessage,
                                idRoom = it.idRoom,
                                content = it.content,
                                idShoppingList = it.idShoppingList,
                            )
                        }
                    )
                )
            } catch (_: Exception) {
            }
        }

        val messagesByIdRoom = response.messages.groupBy { it.idRoom }
        messageSyncRepository.upsertMessages(
            response.messages.map {
                RoomMessage(
                    idUser = it.idUser,
                    isPending = false,
                    idRoom = it.idRoom,
                    idMessage = it.idMessage,
                    idShoppingList = it.idShoppingList,
                    content = it.content,
                    createdAt = it.createdAt,
                    isRead = it.idUser == authorizedUserId,
                    version = it.version,
                )
            }
        )
        versionsRepository.setMessagesVersions(
            messagesByIdRoom.map { entry ->
                MessagesVersion(
                    idRoom = entry.key,
                    version = entry.value.maxOf { it.version },
                )
            }
        )
    }

    suspend fun fullSyncShoppingLists(authorizedUserId: UUID) {
        val forSyncShoppingListByIdUser = shoppingListSyncRepository
            .getNotSynchronizedShoppingLists()
        if (forSyncShoppingListByIdUser.isNotEmpty()) {
            shoppingListStoreRepository.changeShoppingLists(forSyncShoppingListByIdUser.map {
                it.toRemote(emptyUUID)
            })
        }
        retrieveNewShoppingLists(authorizedUserId)
    }

    suspend fun retrieveNewShoppingLists(authorizedUserId: UUID) {
        val versions = roomSyncRepository.getRoomMembers()
            .map {
                ShoppingListVersion(
                    it.idUser,
                    it.shoppingListVersion
                )
            }
            .let { versions ->
                if (versions.find { it.idUser == authorizedUserId } == null) {
                    val authorizedUser = userRepository.getUserById(authorizedUserId)
                        ?: throw UserNotFoundException(authorizedUserId)
                    versions + ShoppingListVersion(
                        authorizedUser.idUser,
                        authorizedUser.shoppingListVersion,
                    )
                } else {
                    versions
                }
            }

        // TODO: Add unavailable users ids to response
        val responses = shoppingListStoreRepository
            .synchronizeShoppingLists(versions)

        responses.forEach { response ->
            response.updates.forEach {
                shoppingListSyncRepository.upsertShoppingList(it.toDto(authorizedUserId))
            }

            shoppingListSyncRepository.setShoppingListVersion(
                ShoppingListVersion(
                    response.idUser,
                    response.actualVersion
                )
            )
        }
        shoppingListSyncRepository.deleteMarkedShoppingListAndSynchronized()
    }

    suspend fun fullSyncFinishedSpendings(authorizedUserId: UUID) {
        val changedSpendings = finishedSpendingSyncRepository.getChangedFinishedSpendings()
        if (changedSpendings.isNotEmpty()) {
            finishedSpendingStoreRepository.changeFinishedSpendings(
                changedSpendings.toRemote(
                    authorizedUserId
                )
            )
        }
        retrieveNewFinishedSpendings(authorizedUserId)
    }

    suspend fun retrieveNewFinishedSpendings(authorizedUserId: UUID) {
        val idsRoomWithReadRole = roomSyncRepository.getRoomsWithAuthority(
            authorizedUserId,
            Authority.READ_USERS_DATA,
        )
        val usersToSynchronized = roomSyncRepository.getRoomMembers(idsRoomWithReadRole)
            .map {
                FinishedSpendingVersion(
                    it.idUser,
                    it.finishedSpendingVersion
                )
            }
            .let { versions ->
                if (versions.find { it.idUser == authorizedUserId } == null) {
                    val authorizedUser = userRepository.getUserById(authorizedUserId)
                        ?: throw UserNotFoundException(authorizedUserId)
                    versions + FinishedSpendingVersion(
                        authorizedUser.idUser,
                        authorizedUser.finishedSpendingVersion,
                    )
                } else {
                    versions
                }
            }
        val response = finishedSpendingStoreRepository.synchronizeFinishedSpendings(
            usersToSynchronized
        )
        if (response.isEmpty()) return

        finishedSpendingSyncRepository.upsertFinishedSpendingWithRecords(
            response.flatMap { it.updates.toServiceDto(authorizedUserId) }
        )
        finishedSpendingSyncRepository.setFinishedSpendingVersions(
            response.map { FinishedSpendingVersion(it.idUser, it.actualVersion) }
        )
        finishedSpendingSyncRepository.deleteMarkedAndSynchronizedFinishedSpendings()
    }
}


private fun List<RemoteFinishedSpendingDto>.toServiceDto(idUser: UUID) = map { finishedSpending ->
    SyncFinishedSpendingWithRecordsDto(
        idSpendingSummary = finishedSpending.idSpendingSummary,
        title = finishedSpending.name,
        purchaseDate = finishedSpending.purchaseDate,
        isDeleted = finishedSpending.isDeleted,
        idUser = if (finishedSpending.idUser == idUser) null else finishedSpending.idUser,
        version = finishedSpending.version,
        currencyValue = finishedSpending.currency,
        spendingRecords = finishedSpending.spendingRecords.map {
            SyncSpendingRecordDto(
                name = it.name,
                price = it.price,
                idCategory = it.idCategory,
                idSpendingRecord = it.idSpendingRecordData,
                idSpendingSummary = finishedSpending.idSpendingSummary,
            )
        },
    )
}


private fun List<FinishedSpendingWithRecordsDto>.toRemote(idUser: UUID): List<RemoteFinishedSpendingDto> =
    map {
        RemoteFinishedSpendingDto(
            idSpendingSummary = it.idSpendingSummary,
            idReceipt = null,
            purchaseDate = it.purchaseDate,
            currency = it.currencyValue,
            version = -1,
            idUser = idUser,
            isDeleted = it.isDeleted,
            name = it.title,
            spendingRecords = it.spendingRecords.map { spendingRecord ->
                RemoteSpendingRecordDto(
                    idSpendingRecordData = spendingRecord.idSpendingRecord,
                    idCategory = spendingRecord.idCategory,
                    name = spendingRecord.name,
                    price = spendingRecord.price,
                )
            }
        )
    }

private fun ShoppingListDto.toRemote(idUser: UUID) = RemoteShoppingListDto(
    idShoppingList = idShoppingList,
    shoppingItems = shoppingItems.map {
        RemoteShoppingItemDto(
            idShoppingItem = it.idSpendingRecordData,
            amount = it.amount,
            idSpendingRecordData = it.idSpendingRecordData,
            name = it.name,
            idCategory = it.idSpendingCategory,
        )
    },
    version = -1,
    idUser = idUser,
    isDeleted = isDeleted,
    name = name,
    color = color,
    isFinished = isFinished,
)

private fun RemoteShoppingListDto.toDto(authorizedUserId: UUID) = ShoppingListDto(
    name = name,
    color = color,
    idShoppingList = idShoppingList,
    shoppingItems = shoppingItems.map {
        ShoppingItemDto(
            idSpendingRecordData = it.idSpendingRecordData,
            name = it.name,
            idSpendingCategory = it.idCategory,
            amount = it.amount,
        )
    },
    isFinished = isFinished,
    isDeleted = isDeleted,
    version = version,
    idUser = if (idUser == authorizedUserId) null else idUser,
)

class UserNotFoundException(idUser: UUID) : Exception("User with id '$idUser' not found")
