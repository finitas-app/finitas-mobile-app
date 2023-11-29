package pl.finitas.app.manage_spendings_feature.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class SpendingCategory(
    val name: String,
    val idParent: Int?,
    val createdAt: LocalDateTime,
    @PrimaryKey val idCategory: Int? = null,
    val idUser: Int? = null,
)
