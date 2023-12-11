package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class DelayedRequest(
    val request: String,
    val body: String,
    val idRequestMethod: UUID,
    @PrimaryKey val idDelayedRequest: UUID? = null,
)
