package pl.finitas.app.core.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import pl.finitas.app.core.data.model.Chart
import pl.finitas.app.core.data.model.ChartToCategoryRef
import pl.finitas.app.core.data.model.relations.ChartToCategoryRefs
import pl.finitas.app.profile_feature.presentation.CurrencyValue
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Dao
interface ChartDao {

    @Transaction
    @Query(
        """
        SELECT 
        ch.idChart as 'idChart',
        ch.chartType as 'chartType',
        ch.startDate as 'startDate',
        ch.endDate as 'endDate',
        ch.currencyValue as 'currencyValue',
        ch.idTargetUser as 'idTargetUser',
        ch.idRoom as 'idRoom',
        ca.name as 'categoryName',
        ca.idCategory as 'idCategory',
        fs.purchaseDate as 'purchaseDate',
        sr.price as 'price'
        
        FROM Chart ch
        JOIN ChartToCategoryRef ctc on ch.idChart = ctc.idChart
        JOIN SpendingCategory ca on ctc.idCategory = ca.idCategory
        LEFT JOIN SpendingRecordData srd on ca.idCategory = srd.idCategory
        LEFT JOIN SpendingRecord sr on sr.idSpendingRecordData = srd.idSpendingRecordData
        LEFT JOIN SpendingSummary ss on sr.idSpendingSummary = ss.idSpendingSummary and ss.currencyValue = ch.currencyValue
        LEFT JOIN FinishedSpending fs on fs.idSpendingSummary = ss.idSpendingSummary
        WHERE ch.idRoom is null and ch.idTargetUser is null
    """
    )
    fun getChartsWithCategoriesFlatFlow(): Flow<List<ChartWithCategoryFlat>>
    @Transaction
    @Query(
        """
        SELECT 
        ch.idChart as 'idChart',
        ch.chartType as 'chartType',
        ch.startDate as 'startDate',
        ch.endDate as 'endDate',
        ch.currencyValue as 'currencyValue',
        ch.idTargetUser as 'idTargetUser',
        ch.idRoom as 'idRoom',
        ca.name as 'categoryName',
        ca.idCategory as 'idCategory',
        fs.purchaseDate as 'purchaseDate',
        sr.price as 'price'
        
        FROM Chart ch
        JOIN ChartToCategoryRef ctc on ch.idChart = ctc.idChart
        JOIN SpendingCategory ca on ctc.idCategory = ca.idCategory
        LEFT JOIN SpendingRecordData srd on ca.idCategory = srd.idCategory
        LEFT JOIN SpendingRecord sr on sr.idSpendingRecordData = srd.idSpendingRecordData
        LEFT JOIN SpendingSummary ss on sr.idSpendingSummary = ss.idSpendingSummary and ss.currencyValue = ch.currencyValue
        LEFT JOIN FinishedSpending fs on fs.idSpendingSummary = ss.idSpendingSummary
        WHERE ch.idRoom = :idRoom
    """
    )
    fun getChartsWithCategoriesFlatByIdRoomFlow(idRoom: UUID): Flow<List<ChartWithCategoryFlat>>
    @Transaction
    @Query(
        """
        SELECT 
        ch.idChart as 'idChart',
        ch.chartType as 'chartType',
        ch.startDate as 'startDate',
        ch.endDate as 'endDate',
        ch.currencyValue as 'currencyValue',
        ch.idTargetUser as 'idTargetUser',
        ch.idRoom as 'idRoom',
        ca.name as 'categoryName',
        ca.idCategory as 'idCategory',
        fs.purchaseDate as 'purchaseDate',
        sr.price as 'price'
        
        FROM Chart ch
        JOIN ChartToCategoryRef ctc on ch.idChart = ctc.idChart
        JOIN SpendingCategory ca on ctc.idCategory = ca.idCategory
        LEFT JOIN SpendingRecordData srd on ca.idCategory = srd.idCategory
        LEFT JOIN SpendingRecord sr on sr.idSpendingRecordData = srd.idSpendingRecordData
        LEFT JOIN SpendingSummary ss on sr.idSpendingSummary = ss.idSpendingSummary and ss.currencyValue = ch.currencyValue
        LEFT JOIN FinishedSpending fs on fs.idSpendingSummary = ss.idSpendingSummary
        WHERE ch.idTargetUser = :idTargetUser
    """
    )
    fun getChartsWithCategoriesFlatByIdTargetUserFlow(idTargetUser: UUID?): Flow<List<ChartWithCategoryFlat>>



    @Query("DELETE FROM ChartToCategoryRef WHERE idChart = :idChart")
    suspend fun deleteCategoryRefsByChart(idChart: UUID)

    @Delete
    suspend fun deleteChart(chart: Chart)

    @Upsert
    suspend fun upsertChart(chart: Chart)

    @Upsert
    suspend fun upsertRefs(chartToCategoryRefs: List<ChartToCategoryRef>)

    @Transaction
    suspend fun upsertChartWithCategories(relation: ChartToCategoryRefs) {
        deleteCategoryRefsByChart(relation.chart.idChart)
        upsertChart(relation.chart)
        upsertRefs(relation.categoryRefs)
    }
}

data class ChartWithCategoryFlat(
    val idChart: UUID,
    val startDate: LocalDateTime?,
    val endDate: LocalDateTime?,
    val currencyValue: CurrencyValue,
    val chartType: Int,
    val idTargetUser: UUID?,
    val idRoom: UUID?,
    val categoryName: String,
    val idCategory: UUID,
    val purchaseDate: LocalDateTime?,
    val price: BigDecimal?,
)