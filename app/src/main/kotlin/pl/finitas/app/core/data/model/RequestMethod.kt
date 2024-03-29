package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class RequestMethod(
    val name: String,
    @PrimaryKey val idRequestMethod: UUID? = null,
)
