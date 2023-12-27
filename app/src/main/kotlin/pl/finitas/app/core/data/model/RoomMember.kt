package pl.finitas.app.core.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import java.util.UUID

@Entity(
    foreignKeys = [
        ForeignKey(
            onDelete = ForeignKey.CASCADE,
            entity = User::class,
            parentColumns = ["idUser"],
            childColumns = ["idUser"]
        ),
        ForeignKey(
            onDelete = ForeignKey.CASCADE,
            entity = Room::class,
            parentColumns = ["idRoom"],
            childColumns = ["idRoom"]
        ),
        ForeignKey(
            onDelete = ForeignKey.SET_NULL,
            entity = RoomRole::class,
            parentColumns = ["idRole"],
            childColumns = ["idRole"]
        ),
    ],
    primaryKeys = ["idUser", "idRoom"],
)
data class RoomMember(
    val idUser: UUID,
    @ColumnInfo(index = true)
    val idRoom: UUID,
    @ColumnInfo(index = true)
    val idRole: UUID?,
    val isActive: Boolean,
)
