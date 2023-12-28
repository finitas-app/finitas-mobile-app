package pl.finitas.app.core.data.model.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import pl.finitas.app.core.data.model.Chart
import pl.finitas.app.core.data.model.ChartToCategoryRef
import pl.finitas.app.core.data.model.SpendingCategory
import pl.finitas.app.core.data.model.SpendingRecordData

data class ChartToCategoryRefs(
    @Embedded val chart: Chart,
    @Relation(
        parentColumn = "idChart",
        entityColumn = "idCategory",
    )
    val categoryRefs: List<ChartToCategoryRef>
)

data class ChartToCategories (
    @Embedded val chart: Chart,
    @Relation(
        parentColumn = "idChart",
        entityColumn = "idCategory",
        associateBy = Junction(ChartToCategoryRef::class)
    )
    val categories: List<SpendingCategory>
)

data class CategoryToCharts (
    @Embedded val category: SpendingCategory,
    @Relation(
        parentColumn = "idCategory",
        entityColumn = "idChart",
        associateBy = Junction(ChartToCategoryRef::class)
    )
    val charts: List<Chart>
)