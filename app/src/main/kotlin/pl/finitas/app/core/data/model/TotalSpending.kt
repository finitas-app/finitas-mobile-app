package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TotalSpending(
    val time: Int?,
    val name: String,
    val idUser: Int?,
    @PrimaryKey val idTotalSpending: Int,
)
