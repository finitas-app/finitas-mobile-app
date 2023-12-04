package pl.finitas.app.core.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import pl.finitas.app.core.data.model.ShoppingList

@Dao
interface ShoppingListDao {

    @Insert
    suspend fun insertShoppingList(shoppingList: ShoppingList): Long

    @Update
    suspend fun updateShoppingList(shoppingList: ShoppingList)

    @Delete
    suspend fun deleteShoppingList(shoppingList: ShoppingList)
}