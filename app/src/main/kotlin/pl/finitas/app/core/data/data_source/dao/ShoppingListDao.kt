package pl.finitas.app.core.data.data_source.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.data.model.ShoppingItem
import pl.finitas.app.core.data.model.ShoppingList
import pl.finitas.app.core.data.model.SpendingRecordData
import pl.finitas.app.core.data.model.relations.SpendingRecordDataToShoppingItem
import java.util.UUID

@Dao
interface ShoppingListDao {


    @Transaction
    @Query("""
        SELECT 
            sl.idShoppingList as 'idShoppingList',
            sl.name as 'name',
            sl.color as 'color',
            sl.isFinished as 'isFinished',
            sl.isDeleted as 'isDeleted',
            sl.version as 'version',
            sl.idUser as 'idUser',
            si.idSpendingRecordData as 'idSpendingRecordData',
            si.amount as 'amount',
            srd.name as 'itemName',
            srd.idCategory as 'idSpendingCategory'
        FROM ShoppingList sl
        JOIN ShoppingItem si on sl.idShoppingList = si.idShoppingList
        JOIN SpendingRecordData srd on si.idSpendingRecordData = srd.idSpendingRecordData
        LEFT JOIN RoomMessage rm on sl.idShoppingList = rm.idShoppingList
        WHERE (sl.idUser is null OR rm.idMessage is not null) AND sl.isDeleted = 0
    """)
    fun getShoppingListWithItemsFlatFlow(): Flow<List<ShoppingListItemFlat>>

    @Transaction
    @Query("""
        SELECT 
            sl.idShoppingList as 'idShoppingList',
            sl.name as 'name',
            sl.color as 'color',
            sl.isFinished as 'isFinished',
            sl.isDeleted as 'isDeleted',
            sl.version as 'version',
            sl.idUser as 'idUser',
            si.idSpendingRecordData as 'idSpendingRecordData',
            si.amount as 'amount',
            srd.name as 'itemName',
            srd.idCategory as 'idSpendingCategory'
        FROM ShoppingList sl
        JOIN ShoppingItem si on sl.idShoppingList = si.idShoppingList
        JOIN SpendingRecordData srd on si.idSpendingRecordData = srd.idSpendingRecordData
        WHERE version is null and idUser is null
    """)
    fun getNotSynchronizedShoppingListsFlat(): List<ShoppingListItemFlat>

    @Transaction
    @Query("""
        SELECT 
            sl.idShoppingList as 'idShoppingList',
            sl.name as 'name',
            sl.color as 'color',
            sl.isFinished as 'isFinished',
            sl.isDeleted as 'isDeleted',
            sl.version as 'version',
            sl.idUser as 'idUser',
            si.idSpendingRecordData as 'idSpendingRecordData',
            si.amount as 'amount',
            srd.name as 'itemName',
            srd.idCategory as 'idSpendingCategory'
        FROM ShoppingList sl
        JOIN ShoppingItem si on sl.idShoppingList = si.idShoppingList
        JOIN SpendingRecordData srd on si.idSpendingRecordData = srd.idSpendingRecordData
        WHERE sl.idShoppingList = :idShoppingList
    """)
    suspend fun getShoppingLisWithItemsFlatBy(idShoppingList: UUID): List<ShoppingListItemFlat>

    @Insert
    suspend fun insertShoppingList(shoppingList: ShoppingList): Long

    @Update
    suspend fun updateShoppingList(shoppingList: ShoppingList)

    @Upsert
    suspend fun upsertShoppingList(shoppingList: ShoppingList)

    @Upsert
    suspend fun upsertSpendingRecordsData(spendingRecordsData: List<SpendingRecordData>)

    @Upsert
    suspend fun upsertShoppingItems(shoppingItems: List<ShoppingItem>)

    @Transaction
    suspend fun upsertShoppingListWithItems(shoppingList: ShoppingList, shoppingItems: List<SpendingRecordDataToShoppingItem>) {
        deleteShoppingListItemsByWithData(shoppingList.idShoppingList)
        upsertShoppingList(shoppingList)
        upsertSpendingRecordsData(shoppingItems.map { it.spendingRecordData })
        upsertShoppingItems(shoppingItems.map { it.shoppingItem })
    }

    @Query("DELETE FROM ShoppingList WHERE idShoppingList = :idShoppingList;")
    suspend fun deleteShoppingListBy(idShoppingList: UUID)

    @Query("DELETE FROM ShoppingItem WHERE idShoppingList = :idShoppingList;")
    suspend fun deleteShoppingItemsByIdShoppingList(idShoppingList: UUID)

    @Query("DELETE FROM SpendingRecordData WHERE idSpendingRecordData in (:ids);")
    suspend fun deleteSpendingRecordsDataBy(ids: List<UUID>)

    @Query("SELECT idSpendingRecordData FROM ShoppingItem WHERE idShoppingList = :idShoppingList")
    suspend fun getShoppingItemsByIdShoppingList(idShoppingList: UUID): List<SpendingRecordDataId>

    suspend fun deleteShoppingListItemsByWithData(idShoppingList: UUID) {
        val spendingRecordDataIds = getShoppingItemsByIdShoppingList(idShoppingList)
        deleteShoppingItemsByIdShoppingList(idShoppingList)
        deleteSpendingRecordsDataBy(spendingRecordDataIds.map { it.idSpendingRecordData })
    }

    @Query("UPDATE ShoppingList SET isDeleted = 1, version = null WHERE idShoppingList = :idShoppingList")
    suspend fun markAsDeleted(idShoppingList: UUID)

    @Transaction
    suspend fun deleteShoppingListWithItemsBy(idShoppingList: UUID) {
        deleteShoppingListItemsByWithData(idShoppingList)
        deleteShoppingListBy(idShoppingList)
    }

    @Query("SELECT * FROM ShoppingList WHERE idUser is null")
    fun  getCurrentUserShoppingLists(): Flow<List<ShoppingList>>

    @Query("SELECT * FROM ShoppingList WHERE idShoppingList in (:idsShoppingList)")
    fun  getShoppingListsBy(idsShoppingList: List<UUID>): Flow<List<ShoppingList>>

    @Query("SELECT * FROM ShoppingList WHERE idShoppingList = :idShoppingList")
    suspend fun getShoppingListBy(idShoppingList: UUID): ShoppingList

    @Query("SELECT idShoppingList FROM ShoppingList WHERE version is not null and isDeleted = 1")
    suspend fun markedShoppingListWithVersion(): List<ShoppingListId>

    @Transaction
    suspend fun deleteMarkedShoppingListAndSynchronized() {
        val temp = markedShoppingListWithVersion()
        temp.forEach {
            deleteShoppingListWithItemsBy(it.idShoppingList)
        }
    }

    @Query("SELECT * FROM ShoppingList WHERE idUser = :idUser")
    suspend fun findByIdUser(idUser: UUID): List<ShoppingList>

    @Transaction
    suspend fun deleteByIdUser(idUser: UUID) {
        findByIdUser(idUser).forEach {
            deleteShoppingListWithItemsBy(it.idShoppingList)
        }
    }

    @Query("SELECT * FROM ShoppingList WHERE idUser is not null")
    suspend fun findAllForeignShoppingLists(): List<ShoppingList>

    @Transaction
    suspend fun deleteAllForeignShoppingLists() {
        findAllForeignShoppingLists().forEach {
            deleteShoppingListWithItemsBy(it.idShoppingList)
        }
    }
}

data class SpendingRecordDataId(
    val idSpendingRecordData: UUID,
)

data class ShoppingListItemFlat(
    val idShoppingList: UUID,
    val name: String,
    val color: Int,
    val isFinished: Boolean,
    val isDeleted: Boolean,
    val version: Int?,
    val idSpendingRecordData: UUID,
    val amount: Int,
    val itemName: String,
    val idSpendingCategory: UUID,
    val idUser: UUID?,
)

data class ShoppingListVersionDto(
    val idUser: UUID?,
    val version: Int?,
)

data class ShoppingListId(
    val idShoppingList: UUID,
)
