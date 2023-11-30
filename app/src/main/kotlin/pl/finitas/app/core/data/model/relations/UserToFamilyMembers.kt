package pl.finitas.app.core.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.finitas.app.core.data.model.FamilyMember
import pl.finitas.app.core.data.model.User

data class UserToFamilyMembers(
    @Embedded
    val user: User,
    @Relation(
        parentColumn = "idUser",
        entityColumn = "idUser"
    )
    val familyMembers: List<FamilyMember>,
)
