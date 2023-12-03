package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Category(
    val name: String,
    val idParent: Int?,
    val createdAt: LocalDateTime,
    @PrimaryKey val idCategory: Int,
    val idUser: Int? = null,
)
