package pl.finitas.app.sync_feature.data.data_source

import pl.finitas.app.core.data.data_source.dao.ShoppingListDao
import pl.finitas.app.core.data.model.ShoppingItem
import pl.finitas.app.core.data.model.ShoppingList
import pl.finitas.app.core.data.model.ShoppingListVersion
import pl.finitas.app.core.data.model.SpendingRecordData
import pl.finitas.app.core.data.model.relations.SpendingRecordDataToShoppingItem
import pl.finitas.app.shopping_lists_feature.domain.ShoppingItemDto
import pl.finitas.app.shopping_lists_feature.domain.ShoppingListDto
import pl.finitas.app.sync_feature.domain.repository.ShoppingListSyncRepository
import java.util.UUID

class ShoppingListSyncRepositoryImpl(
    private val shoppingListDao: ShoppingListDao,
) : ShoppingListSyncRepository {
    override suspend fun getNotSynchronizedShoppingLists(): List<ShoppingListDto> {
        return shoppingListDao
            .getNotSynchronizedShoppingListsFlat()
            .groupBy {
                TempShoppingList(
                    it.name,
                    it.color,
                    it.idShoppingList,
                    it.isFinished,
                    it.isDeleted,
                    it.version,
                )
            }
            .map { (shoppingList, shoppingItems) ->
                ShoppingListDto(
                    name = shoppingList.name,
                    color = shoppingList.color,
                    idShoppingList = shoppingList.idShoppingList,
                    idUser = null,
                    isFinished = shoppingList.isFinished,
                    isDeleted = shoppingList.isDeleted,
                    version = shoppingList.version,
                    shoppingItems = shoppingItems.map {
                        ShoppingItemDto(
                            name = it.itemName,
                            amount = it.amount,
                            idSpendingRecordData = it.idSpendingRecordData,
                            idSpendingCategory = it.idSpendingCategory,
                        )
                    }
                )
            }

    }

    override suspend fun getShoppingListVersions(): List<ShoppingListVersion> {
        return shoppingListDao.getShoppingListVersions()
    }

    override suspend fun setShoppingListVersion(actualShoppingListVersion: ShoppingListVersion) {
        shoppingListDao.setShoppingListVersion(actualShoppingListVersion)
    }

    override suspend fun createShoppingListVersionIfNotPresent(idUser: UUID): ShoppingListVersion {
        return shoppingListDao.createShoppingListVersionIfNotPresent(idUser)
    }

    override suspend fun upsertShoppingList(shoppingListDto: ShoppingListDto) {
        shoppingListDao.upsertShoppingListWithItems(
            shoppingList = ShoppingList(
                idUser = shoppingListDto.idUser,
                color = shoppingListDto.color,
                idShoppingList = shoppingListDto.idShoppingList,
                name = shoppingListDto.name,
                isFinished = false,
                isDeleted = shoppingListDto.isDeleted,
                version = shoppingListDto.version,
            ),
            shoppingItems = shoppingListDto.shoppingItems.map {
                SpendingRecordDataToShoppingItem(
                    spendingRecordData = SpendingRecordData(
                        name = it.name,
                        idSpendingRecordData = it.idSpendingRecordData,
                        idCategory = it.idSpendingCategory,
                    ),
                    shoppingItem = ShoppingItem(
                        idShoppingList = shoppingListDto.idShoppingList,
                        idSpendingRecordData = it.idSpendingRecordData,
                        amount = it.amount,
                    )
                )
            },
        )
    }


    override suspend fun deleteMarkedShoppingListAndSynchronized() {
        shoppingListDao.deleteMarkedShoppingListAndSynchronized()
    }
}


private data class TempShoppingList(
    val name: String,
    val color: Int,
    val idShoppingList: UUID,
    val isFinished: Boolean,
    val isDeleted: Boolean,
    val version: Int?,
)
