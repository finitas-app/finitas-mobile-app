package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Family(
    @PrimaryKey val idFamily: Int,
)
