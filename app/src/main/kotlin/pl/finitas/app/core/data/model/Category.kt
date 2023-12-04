package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Category(
    val name: String,
    val idParent: String?,
    val createdAt: Long,
    val idUser: String? = null,
    @PrimaryKey val idCategory: String? = null,
)
