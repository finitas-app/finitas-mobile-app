package pl.finitas.app.core.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.data.model.RegularSpending
import pl.finitas.app.core.data.model.SpendingRecord
import pl.finitas.app.core.data.model.SpendingRecordData
import pl.finitas.app.core.data.model.SpendingSummary
import pl.finitas.app.core.data.model.relations.SpendingRecordDataToSpendingRecord
import pl.finitas.app.core.data.model.relations.SpendingSummaryToRegularSpending
import pl.finitas.app.profile_feature.presentation.CurrencyValue
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Dao
interface RegularSpendingDao {
    @Transaction
    @Query(
        """
        SELECT 
        rs.idSpendingSummary as 'idSpendingSummary',
        rs.actualizationPeriod as 'actualizationPeriod',
        rs.lastActualizationDate as 'lastActualizationDate',
        rs.periodUnit as 'periodUnit',
        ss.name as 'title',
        sr.price as 'price',
        ss.currencyValue as 'currencyValue',
        srd.name as 'spendingRecordName',
        srd.idCategory as 'idCategory',
        srd.idSpendingRecordData as 'idSpendingRecord'
        FROM RegularSpending rs
        JOIN SpendingSummary ss on rs.idSpendingSummary = ss.idSpendingSummary
        JOIN SpendingRecord sr on ss.idSpendingSummary = sr.idSpendingSummary
        JOIN SpendingRecordData srd on sr.idSpendingRecordData = srd.idSpendingRecordData
    """
    )
    fun getRegularSpendingsWithRecordFlatFlow(): Flow<List<RegularSpendingWithRecordFlat>>

    @Transaction
    @Query(
        """
        SELECT 
        rs.idSpendingSummary as 'idSpendingSummary',
        rs.actualizationPeriod as 'actualizationPeriod',
        rs.lastActualizationDate as 'lastActualizationDate',
        rs.periodUnit as 'periodUnit',
        ss.name as 'title',
        sr.price as 'price',
        ss.currencyValue as 'currencyValue',
        srd.name as 'spendingRecordName',
        srd.idCategory as 'idCategory',
        srd.idSpendingRecordData as 'idSpendingRecord'
        FROM RegularSpending rs
        JOIN SpendingSummary ss on rs.idSpendingSummary = ss.idSpendingSummary
        JOIN SpendingRecord sr on ss.idSpendingSummary = sr.idSpendingSummary
        JOIN SpendingRecordData srd on sr.idSpendingRecordData = srd.idSpendingRecordData
    """
    )
    suspend fun getRegularSpendingsWithRecordFlat(): List<RegularSpendingWithRecordFlat>

    @Query("SELECT * FROM RegularSpending WHERE idSpendingSummary = :idSpendingSummary")
    suspend fun findRegularSpendingBy(idSpendingSummary: UUID): RegularSpending?

    @Query("DELETE FROM SpendingRecord WHERE idSpendingSummary = :idSpendingSummary")
    suspend fun deleteSpendingRecordsBySpendingSummary(idSpendingSummary: UUID)

    @Insert
    suspend fun insertRegularSpending(regularSpending: RegularSpending): Long

    @Update
    suspend fun updateRegularSpending(regularSpending: RegularSpending)

    @Delete
    suspend fun deleteRegularSpending(regularSpending: RegularSpending)

    @Delete
    suspend fun deleteSpendingSummary(spendingSummary: SpendingSummary)

    @Delete
    suspend fun deleteSpendingRecordsData(spendingRecordsData: List<SpendingRecordData>)

    @Delete
    suspend fun deleteSpendingRecords(spendingRecords: List<SpendingRecord>)

    @Transaction
    suspend fun deleteRegularSpendingWithRecords(
        spendingSummaryToRegularSpending: SpendingSummaryToRegularSpending,
        spendingRecords: List<SpendingRecordDataToSpendingRecord>,
    ) {
        deleteSpendingRecordsData(spendingRecords.map { it.spendingRecordData })
        deleteSpendingSummary(spendingSummaryToRegularSpending.spendingSummary)
        deleteRegularSpending(spendingSummaryToRegularSpending.regularSpending)
        deleteSpendingRecords(spendingRecords.map { it.spendingRecord })
    }

    @Upsert
    suspend fun upsertRegularSpending(regularSpending: RegularSpending)

    @Upsert
    suspend fun upsertSpendingSummary(spendingSummary: SpendingSummary)

    @Upsert
    suspend fun upsertSpendingRecordsData(spendingRecordsData: List<SpendingRecordData>)

    @Upsert
    suspend fun upsertSpendingRecords(spendingRecords: List<SpendingRecord>)

    @Transaction
    suspend fun upsertRegularSpendingWithRecords(
        spendingSummaryToRegularSpending: SpendingSummaryToRegularSpending,
        spendingRecords: List<SpendingRecordDataToSpendingRecord>,
    ) {
        deleteSpendingRecordsBySpendingSummary(
            spendingSummaryToRegularSpending.spendingSummary.idSpendingSummary
        )
        upsertSpendingSummary(spendingSummaryToRegularSpending.spendingSummary)
        upsertRegularSpending(spendingSummaryToRegularSpending.regularSpending)
        upsertSpendingRecordsData(spendingRecords.map { it.spendingRecordData })
        upsertSpendingRecords(spendingRecords.map { it.spendingRecord })
    }
}

data class RegularSpendingWithRecordFlat(
    val idSpendingSummary: UUID,
    val title: String,
    val price: BigDecimal,
    val currencyValue: CurrencyValue,
    val spendingRecordName: String,
    val idCategory: UUID,
    val idSpendingRecord: UUID,
    val periodUnit: Int,
    val actualizationPeriod: Int,
    val lastActualizationDate: LocalDateTime,
)