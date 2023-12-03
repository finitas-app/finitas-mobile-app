package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Blob

@Entity
data class Receipt(
    val photo: Blob?,
    val idTotalSpending: Int?,
    @PrimaryKey val idReceipt: Int,
)
