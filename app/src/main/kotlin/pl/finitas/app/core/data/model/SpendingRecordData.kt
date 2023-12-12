package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class SpendingRecordData(
    val name: String,
    val idCategory: UUID,
    @PrimaryKey val idSpendingRecordData: UUID,
)
