package pl.finitas.app.manage_spendings_feature.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SpendingCategory(
    val name: String,
    val idParent: Int?,
    @PrimaryKey val idCategory: Int? = null,
    val idUser: Int? = null,
)
