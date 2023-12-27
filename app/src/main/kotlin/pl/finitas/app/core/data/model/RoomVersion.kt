package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import pl.finitas.app.core.domain.dto.SerializableUUID

@Entity(
    foreignKeys = [
        ForeignKey(
            onDelete = ForeignKey.CASCADE,
            entity = Room::class,
            parentColumns = ["idRoom"],
            childColumns = ["idRoom"]
        ),
    ],
)
@Serializable
data class RoomVersion(
    @PrimaryKey val idRoom: SerializableUUID,
    val version: Int,
)