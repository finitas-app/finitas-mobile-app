package pl.finitas.app.core.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.data.model.FinishedSpending
import pl.finitas.app.core.data.model.SpendingRecord
import pl.finitas.app.core.data.model.SpendingRecordData
import pl.finitas.app.core.data.model.SpendingSummary
import pl.finitas.app.core.data.model.relations.SpendingRecordDataToSpendingRecord
import pl.finitas.app.core.data.model.relations.SpendingSummaryToFinishedSpending
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Dao
interface FinishedSpendingDao {
    @Transaction
    @Query(
        """
        SELECT 
        fs.idSpendingSummary as 'idSpendingSummary',
        ss.name as 'title',
        fs.purchaseDate as 'purchaseDate',
        fs.isDeleted as 'isDeleted',
        srd.name as 'spendingRecordName',
        sr.price as 'price',
        srd.idCategory as 'idCategory',
        srd.idSpendingRecordData as 'idSpendingRecord'
        FROM FinishedSpending fs
        JOIN SpendingSummary ss on fs.idSpendingSummary = ss.idSpendingSummary
        JOIN SpendingRecord sr on ss.idSpendingSummary == sr.idSpendingSummary
        JOIN SpendingRecordData srd on sr.idSpendingRecordData = srd.idSpendingRecordData
        WHERE idUser is null
    """
    )
    fun getFinishedSpendingsWithRecordFlat(): Flow<List<FinishedSpendingWithRecordFlat>>

    @Transaction
    @Query(
        """
        SELECT 
        fs.idSpendingSummary as 'idSpendingSummary',
        ss.name as 'title',
        fs.purchaseDate as 'purchaseDate',
        fs.isDeleted as 'isDeleted',
        srd.name as 'spendingRecordName',
        sr.price as 'price',
        srd.idCategory as 'idCategory',
        srd.idSpendingRecordData as 'idSpendingRecord'
        FROM FinishedSpending fs
        JOIN SpendingSummary ss on fs.idSpendingSummary = ss.idSpendingSummary
        JOIN SpendingRecord sr on ss.idSpendingSummary == sr.idSpendingSummary
        JOIN SpendingRecordData srd on sr.idSpendingRecordData = srd.idSpendingRecordData
        WHERE fs.idUser is null and fs.version is null
    """
    )
    suspend fun getChangedFinishedSpendings(): List<FinishedSpendingWithRecordFlat>

    @Upsert
    suspend fun upsertFinishedSpending(finishedSpending: FinishedSpending)

    @Upsert
    suspend fun upsertSpendingSummary(spendingSummary: SpendingSummary)

    @Upsert
    suspend fun upsertSpendingRecordsData(spendingRecordsData: List<SpendingRecordData>)

    @Upsert
    suspend fun upsertSpendingRecords(spendingRecords: List<SpendingRecord>)

    @Transaction
    suspend fun upsertFinishedSpendingWithRecords(
        spendingSummaryToFinishedSpending: SpendingSummaryToFinishedSpending,
        spendingRecords: List<SpendingRecordDataToSpendingRecord>,
    ) {
        upsertSpendingSummary(spendingSummaryToFinishedSpending.spendingSummary)
        upsertFinishedSpending(spendingSummaryToFinishedSpending.finishedSpending)
        upsertSpendingRecordsData(spendingRecords.map { it.spendingRecordData })
        upsertSpendingRecords(spendingRecords.map { it.spendingRecord })
    }

    @Transaction
    suspend fun deleteWithRecords(idFinishedSpending: UUID) {
        deleteSpendingRecordsData(
            getSpendingRecordsDataBy(idSpendingSummary = idFinishedSpending)
        )
        deleteById(idFinishedSpending)
    }

    @Query(
        """
        SELECT srd.* 
        FROM SpendingRecordData srd
        JOIN SpendingRecord sr ON srd.idSpendingRecordData = sr.idSpendingRecordData
        WHERE sr.idSpendingSummary = :idSpendingSummary
    """
    )
    suspend fun getSpendingRecordsDataBy(idSpendingSummary: UUID): List<SpendingRecordData>

    @Delete
    suspend fun deleteSpendingRecordsData(spendingRecords: List<SpendingRecordData>)

    @Query(
        """
        DELETE FROM SpendingSummary WHERE idSpendingSummary = :idFinishedSpending 
    """
    )
    suspend fun deleteById(idFinishedSpending: UUID)

    @Query("SELECT idSpendingSummary FROM FinishedSpending WHERE version is not null and isDeleted = 1")
    suspend fun markedShoppingListWithVersion(): List<IdFinishedSpending>

    @Transaction
    suspend fun deleteMarkedFinishedSpendingsAndSynchronized() {
        val temp = markedShoppingListWithVersion()
        temp.forEach {
            deleteWithRecords(it.idSpendingSummary)
        }
    }
}

data class FinishedSpendingWithRecordFlat(
    val idSpendingSummary: UUID,
    val title: String,
    val purchaseDate: LocalDateTime,
    val isDeleted: Boolean,
    val spendingRecordName: String,
    val price: BigDecimal,
    val idCategory: UUID,
    val idSpendingRecord: UUID? = null,
)

data class IdFinishedSpending(
    val idSpendingSummary: UUID,
)
