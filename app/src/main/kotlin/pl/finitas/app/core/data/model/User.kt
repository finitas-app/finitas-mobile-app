package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    val visibleName: String,
    val username: String,
    val password: String,
    @PrimaryKey val idUser: Int? = null,
)
