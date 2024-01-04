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
import pl.finitas.app.core.data.model.ShoppingListVersion
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
            si.idSpendingRecordData as 'idSpendingRecordData',
            si.amount as 'amount',
            srd.name as 'itemName',
            srd.idCategory as 'idSpendingCategory'
        FROM ShoppingList sl
        JOIN ShoppingItem si on sl.idShoppingList = si.idShoppingList
        JOIN SpendingRecordData srd on si.idSpendingRecordData = srd.idSpendingRecordData
        WHERE sl.idUser is null and sl.idShoppingList = :idShoppingList
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

    @Query("UPDATE ShoppingList SET isDeleted = 1 WHERE idShoppingList = :idShoppingList")
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

    @Query("SELECT idUser, version FROM ShoppingList WHERE idShoppingList = :idShoppingList")
    suspend fun getShoppingListVersionBy(idShoppingList: UUID): ShoppingListVersionDto

    @Query("SELECT idUser, version FROM ShoppingListVersion WHERE idUser = :idUser")
    suspend fun getShoppingListVersionByIdUser(idUser: UUID): ShoppingListVersion?

    @Query("SELECT idUser, version FROM ShoppingListVersion")
    suspend fun getShoppingListVersions(): List<ShoppingListVersion>

    @Upsert
    suspend fun setShoppingListVersion(shoppingListVersion: ShoppingListVersion)

    @Query("SELECT idShoppingList FROM ShoppingList WHERE version is not null and isDeleted = 1")
    suspend fun markedShoppingListWithVersion(): List<ShoppingListId>

    @Transaction
    suspend fun deleteMarkedShoppingListAndSynchronized() {
        val temp = markedShoppingListWithVersion()
        temp.forEach {
            deleteShoppingListWithItemsBy(it.idShoppingList)
        }
    }

    @Transaction
    suspend fun createShoppingListVersionIfNotPresent(idUser: UUID): ShoppingListVersion {
        return getShoppingListVersionByIdUser(idUser) ?: run {
            val newVersion = ShoppingListVersion(idUser, 0)
            setShoppingListVersion(newVersion)
            newVersion
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
)

data class ShoppingListVersionDto(
    val idUser: UUID?,
    val version: Int?,
)

data class ShoppingListId(
    val idShoppingList: UUID,
)
