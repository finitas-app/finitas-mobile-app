package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class SpendingCategory(
    val name: String,
    val idParent: UUID?,
    val createdAt: Long,
    val idUser: UUID? = null,
    @PrimaryKey val idCategory: UUID,
)
