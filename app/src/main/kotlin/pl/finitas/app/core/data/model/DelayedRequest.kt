package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DelayedRequest(
    val request: String,
    val body: String,
    val idRequestMethod: String,
    @PrimaryKey val idDelayedRequest: String? = null,
)
