package pl.finitas.app.core.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

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
data class RoomRole(
    val name: String,
    @ColumnInfo(index = true)
    val idRoom: UUID,
    val authorities: Set<Authority>,
    @PrimaryKey val idRole: UUID,
)

enum class Authority {
    READ_USERS_DATA,
    MODIFY_USERS_DATA,
    MODIFY_ROOM,
}
