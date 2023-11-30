package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MessageLine(
    val content: String,
    val dateAndTime: Int,
    val idFamily: Int?,
    val idFamilyMember: Int?,
    @PrimaryKey val idMessageLine: Int? = null,
)
