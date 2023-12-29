package pl.finitas.app.room_feature.data.data_sorce

import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.data.data_source.dao.ShoppingListDao
import pl.finitas.app.core.data.model.ShoppingList
import pl.finitas.app.room_feature.domain.repository.RoomShoppingListRepository
import java.util.UUID

class RoomShoppingListRepositoryImpl(
    private val shoppingListDao: ShoppingListDao,
) : RoomShoppingListRepository {

    override fun getShoppingListsBy(idsShoppingList: List<UUID>): Flow<List<ShoppingList>> {
        return shoppingListDao.getShoppingListsBy(idsShoppingList)
    }

    override fun getShoppingListsPreview(): Flow<List<ShoppingList>> {
        return shoppingListDao.getCurrentUserShoppingLists()
    }
}

private data class TempShoppingList(
    val name: String,
    val color: Int,
    val idShoppingList: UUID,
    val isFinished: Boolean,
)