package pl.finitas.app.shopping_lists_feature.data.data_sourse

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.finitas.app.core.data.data_source.dao.ShoppingListDao
import pl.finitas.app.core.data.model.ShoppingItem
import pl.finitas.app.core.data.model.ShoppingList
import pl.finitas.app.core.data.model.SpendingRecordData
import pl.finitas.app.core.data.model.relations.SpendingRecordDataToShoppingItem
import pl.finitas.app.shopping_lists_feature.domain.ShoppingItemDto
import pl.finitas.app.shopping_lists_feature.domain.ShoppingListDto
import pl.finitas.app.shopping_lists_feature.domain.ShoppingListRepository
import java.util.UUID

class ShoppingListRepositoryImpl(
    private val shoppingListDao: ShoppingListDao,
) : ShoppingListRepository {
    override fun getShoppingLists(): Flow<List<ShoppingListDto>> =
        shoppingListDao.getShoppingListWithItemsFlat().map { items ->
            items
                .groupBy {
                    TempShoppingList(
                        it.name,
                        it.color,
                        it.idShoppingList,
                        it.isFinished,
                    )
                }
                .map { (shoppingList, shoppingItems) ->
                    ShoppingListDto(
                        name = shoppingList.name,
                        color = shoppingList.color,
                        idShoppingList = shoppingList.idShoppingList,
                        idUser = null,
                        isFinished = shoppingList.isFinished,
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

    override suspend fun getShoppingListBy(idShoppingList: UUID): ShoppingListDto {
        val shoppingListFlat = shoppingListDao.getShoppingLisWithItemsFlatBy(idShoppingList)
        if (shoppingListFlat.isEmpty()) throw ShoppingListNotFoundException(idShoppingList)

        return ShoppingListDto(
            name = shoppingListFlat[0].name,
            color = shoppingListFlat[0].color,
            idShoppingList = shoppingListFlat[0].idShoppingList,
            idUser = null,
            isFinished = shoppingListFlat[0].isFinished,
            shoppingItems = shoppingListFlat.map {
                ShoppingItemDto(
                    name = it.itemName,
                    amount = it.amount,
                    idSpendingRecordData = it.idSpendingRecordData,
                    idSpendingCategory = it.idSpendingCategory,
                )
            }
        )
    }

    override suspend fun upsertShoppingList(shoppingListDto: ShoppingListDto) {
        shoppingListDao.upsertShoppingListWithItems(
            shoppingList = ShoppingList(
                idUser = shoppingListDto.idUser,
                color = shoppingListDto.color,
                idShoppingList = shoppingListDto.idShoppingList,
                name = shoppingListDto.name,
                isFinished = false,
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

    override suspend fun deleteShoppingListBy(idShoppingList: UUID) {
        shoppingListDao.deleteShoppingListWithItemsBy(idShoppingList)
    }
}

private data class TempShoppingList(
    val name: String,
    val color: Int,
    val idShoppingList: UUID,
    val isFinished: Boolean,
)

class ShoppingListNotFoundException(idShoppingList: UUID): Exception("Shopping list '$idShoppingList' not found.")