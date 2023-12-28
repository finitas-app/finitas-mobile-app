package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.UUID

@Entity
data class Chart (
    @PrimaryKey val idChart: UUID,
    val startDate: LocalDateTime?,
    val endDate: LocalDateTime?,
    val chartType: Int,
)

@Entity(primaryKeys = ["idChart", "idCategory"])
data class ChartToCategoryRef(
    val idChart: UUID,
    val idCategory: UUID,
)
