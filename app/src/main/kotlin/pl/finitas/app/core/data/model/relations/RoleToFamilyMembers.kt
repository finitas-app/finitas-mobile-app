package pl.finitas.app.core.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.finitas.app.core.data.model.FamilyMember
import pl.finitas.app.core.data.model.Role

data class RoleToFamilyMembers(
    @Embedded
    val role: Role,
    @Relation(
        parentColumn = "idRole",
        entityColumn = "idRole"
    )
    val familyMembers: List<FamilyMember>,
)
