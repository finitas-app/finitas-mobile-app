package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import pl.finitas.app.core.domain.dto.SerializableUUID

@Entity
@Serializable
data class ShoppingListVersion(
    @PrimaryKey val idUser: SerializableUUID,
    val version: Int,
)
