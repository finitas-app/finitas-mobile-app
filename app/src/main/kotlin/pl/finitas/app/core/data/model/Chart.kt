package pl.finitas.app.core.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.UUID

@Entity(
    foreignKeys = [
        ForeignKey(
            onDelete = ForeignKey.CASCADE,
            entity = Room::class,
            parentColumns = ["idRoom"],
            childColumns = ["idRoom"],
        ),
        ForeignKey(
            onDelete = ForeignKey.CASCADE,
            entity = User::class,
            parentColumns = ["idUser"],
            childColumns = ["idTargetUser"],
        ),
    ]
)
data class Chart(
    @PrimaryKey val idChart: UUID,
    val startDate: LocalDateTime?,
    val endDate: LocalDateTime?,
    val chartType: Int,
    @ColumnInfo(index = true)
    val idTargetUser: UUID?,
    @ColumnInfo(index = true)
    val idRoom: UUID?,
)

@Entity(
    primaryKeys = ["idChart", "idCategory"],
    foreignKeys = [
        ForeignKey(
            onDelete = ForeignKey.CASCADE,
            entity = Chart::class,
            parentColumns = ["idChart"],
            childColumns = ["idChart"],
        ),
        ForeignKey(
            onDelete = ForeignKey.CASCADE,
            entity = SpendingCategory::class,
            parentColumns = ["idCategory"],
            childColumns = ["idCategory"],
        ),
    ]
)
data class ChartToCategoryRef(
    @ColumnInfo(index = true)
    val idChart: UUID,
    @ColumnInfo(index = true)
    val idCategory: UUID,
)
