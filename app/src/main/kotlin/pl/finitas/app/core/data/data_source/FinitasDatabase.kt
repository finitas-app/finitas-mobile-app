package pl.finitas.app.core.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pl.finitas.app.core.data.data_source.dao.FinishedSpendingDao
import pl.finitas.app.core.data.data_source.dao.ReceiptDao
import pl.finitas.app.core.data.data_source.dao.RegularSpendingDao
import pl.finitas.app.core.data.data_source.dao.ShoppingItemDao
import pl.finitas.app.core.data.data_source.dao.ShoppingListDao
import pl.finitas.app.core.data.data_source.dao.SpendingCategoryDao
import pl.finitas.app.core.data.data_source.dao.SpendingRecordDao
import pl.finitas.app.core.data.data_source.dao.SpendingRecordDataDao
import pl.finitas.app.core.data.data_source.dao.SpendingSummaryDao
import pl.finitas.app.core.data.model.FinishedSpending
import pl.finitas.app.core.data.model.Receipt
import pl.finitas.app.core.data.model.RegularSpending
import pl.finitas.app.core.data.model.Room
import pl.finitas.app.core.data.model.RoomMember
import pl.finitas.app.core.data.model.RoomMessage
import pl.finitas.app.core.data.model.RoomRole
import pl.finitas.app.core.data.model.ShoppingItem
import pl.finitas.app.core.data.model.ShoppingList
import pl.finitas.app.core.data.model.SpendingCategory
import pl.finitas.app.core.data.model.SpendingRecord
import pl.finitas.app.core.data.model.SpendingRecordData
import pl.finitas.app.core.data.model.SpendingSummary
import pl.finitas.app.core.data.model.User

@Database(
    entities = [
        User::class,
        SpendingCategory::class,
        SpendingRecordData::class,
        SpendingSummary::class,
        RegularSpending::class,
        SpendingRecord::class,
        Receipt::class,
        FinishedSpending::class,
        ShoppingList::class,
        ShoppingItem::class,
        Room::class,
        RoomRole::class,
        RoomMember::class,
        RoomMessage::class,
    ],
    version = 1,
)
@TypeConverters(
    LocalDateTimeConverter::class,
    BigDecimalConverter::class,
    UUIDConverter::class,
)
abstract class FinitasDatabase : RoomDatabase() {

    abstract val finishedSpendingDao: FinishedSpendingDao
    abstract val receiptDao: ReceiptDao
    abstract val regularSpendingDao: RegularSpendingDao
    abstract val shoppingItemDao: ShoppingItemDao
    abstract val shoppingListDao: ShoppingListDao
    abstract val spendingCategoryDao: SpendingCategoryDao
    abstract val spendingRecordDao: SpendingRecordDao
    abstract val spendingRecordDataDao: SpendingRecordDataDao
    abstract val spendingSummaryDao: SpendingSummaryDao

    companion object {
        const val databaseName = "finitas_db"
    }
}