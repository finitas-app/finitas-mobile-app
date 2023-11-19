package pl.finitas.app.core.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pl.finitas.app.manage_spendings_feature.data.data_source.SpendingCategoryDao
import pl.finitas.app.manage_spendings_feature.data.data_source.TotalSpendingDao
import pl.finitas.app.manage_spendings_feature.domain.model.SpendingCategory
import pl.finitas.app.manage_spendings_feature.domain.model.SpendingRecord
import pl.finitas.app.manage_spendings_feature.domain.model.TotalSpending

@Database(
    entities = [
        TotalSpending::class,
        SpendingRecord::class,
        SpendingCategory::class,
    ],
    version = 1,
)
@TypeConverters(
    LocalDateTimeConverter::class,
    BigDecimalConverter::class,
)
abstract class FinitasDatabase : RoomDatabase() {

    abstract val totalSpendingDao: TotalSpendingDao
    abstract val spendingCategoryDao: SpendingCategoryDao

    companion object {
        const val databaseName = "finitas_db"
    }
}