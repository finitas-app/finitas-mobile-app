package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Role(
    val name: String,
    val idRoom: String,
    @PrimaryKey val idRole: String? = null,
)
