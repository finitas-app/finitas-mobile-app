package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SpendingSummary(
    val createdAt: Int,
    val name: String,
    val idUser: String?,
    @PrimaryKey val idSpendingSummary: String? = null,
)
