package pl.finitas.app.core.data.model

import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import pl.finitas.app.core.domain.dto.SerializableUUID

@Serializable
data class ShoppingListVersion(
    @PrimaryKey val idUser: SerializableUUID,
    val version: Int,
)
