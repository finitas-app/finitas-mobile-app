package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RegularSpending(
    val cron: String,
    val idUser: String?,
    @PrimaryKey val idRegularSpending: String? = null
)
