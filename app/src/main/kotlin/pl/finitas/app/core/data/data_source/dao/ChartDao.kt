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
        ca.name as 'categoryName',
        ca.idCategory as 'idCategory',
        fs.purchaseDate as 'purchaseDate',
        sr.price as 'price'
        
        FROM Chart ch
        JOIN ChartToCategoryRef ctc on ch.idChart = ctc.idChart
        JOIN SpendingCategory ca on ctc.idCategory = ca.idCategory
        JOIN SpendingRecordData srd on ca.idCategory = srd.idCategory
        JOIN SpendingRecord sr on sr.idSpendingRecordData = srd.idSpendingRecordData
        JOIN FinishedSpending fs on fs.idSpendingSummary = sr.idSpendingSummary
        
    """
    )
    fun getChartsWithCategoriesFlatFlow(): Flow<List<ChartWithCategoryFlat>>

    @Query("SELECT * FROM Chart")
    suspend fun getAllCharts(): List<Chart>

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

    @Transaction
    suspend fun deleteChartWithCategoryRefs(relation: ChartToCategoryRefs) {
        deleteCategoryRefsByChart(relation.chart.idChart)
        deleteChart(relation.chart)
    }
}

data class ChartWithCategoryFlat(
    val idChart: UUID,
    val startDate: LocalDateTime?,
    val endDate: LocalDateTime?,
    val categoryName: String,
    val idCategory: UUID,
    val purchaseDate: LocalDateTime,
    val price: BigDecimal,
    val chartType: Int,
)