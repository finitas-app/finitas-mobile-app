package pl.finitas.app.core.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    foreignKeys = [
        ForeignKey(
            onDelete = ForeignKey.CASCADE,
            entity = SpendingCategory::class,
            parentColumns = ["idCategory"],
            childColumns = ["idCategory"]
        ),
    ],
)
data class SpendingRecordData(
    val name: String,
    @ColumnInfo(index = true)
    val idCategory: UUID,
    @PrimaryKey val idSpendingRecordData: UUID,
)
