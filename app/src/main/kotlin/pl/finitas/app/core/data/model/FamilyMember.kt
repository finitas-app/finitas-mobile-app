package pl.finitas.app.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FamilyMember(
    val idFamily: Int?,
    val idRole: Int?,
    val idUser: Int?,
    @PrimaryKey val idFamilyMember: Int? = null,
)
