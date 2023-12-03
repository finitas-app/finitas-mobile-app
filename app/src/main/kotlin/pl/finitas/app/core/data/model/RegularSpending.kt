package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RegularSpending(
    val dayOfMonth: Int,
    val time: Int,
    val idSpendingUnit: Int?,
    val idUser: Int?,
    @PrimaryKey val idRegularSpending: Int,
)
