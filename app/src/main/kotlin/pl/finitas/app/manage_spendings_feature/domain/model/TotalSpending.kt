package pl.finitas.app.manage_spendings_feature.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class TotalSpending(
    val title: String,
    val time: LocalDateTime,
    val idUser: Int? = null,
    @PrimaryKey val idTotalSpending: Int? = null,
)
