package pl.finitas.app.core.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.finitas.app.core.data.model.FamilyMember
import pl.finitas.app.core.data.model.MessageLine

data class FamilyMemberToMessageLines(
    @Embedded
    val familyMember: FamilyMember,
    @Relation(
        parentColumn = "idFamilyMember",
        entityColumn = "idFamilyMember"
    )
    val messageLines: List<MessageLine>,
)
