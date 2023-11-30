package pl.finitas.app.core.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.finitas.app.core.data.model.Family
import pl.finitas.app.core.data.model.FamilyMember
import pl.finitas.app.core.data.model.RegularSpending
import pl.finitas.app.core.data.model.TotalSpending
import pl.finitas.app.core.data.model.User

data class FamilyToFamilyMembers(
    @Embedded
    val family: Family,
    @Relation(
        parentColumn = "idFamily",
        entityColumn = "idFamily"
    )
    val familyMembers: List<FamilyMember>,
)
