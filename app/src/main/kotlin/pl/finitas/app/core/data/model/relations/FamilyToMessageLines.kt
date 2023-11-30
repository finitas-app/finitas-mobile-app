package pl.finitas.app.core.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.finitas.app.core.data.model.Family
import pl.finitas.app.core.data.model.MessageLine

data class FamilyToMessageLines(
    @Embedded
    val family: Family,
    @Relation(
        parentColumn = "idFamily",
        entityColumn = "idFamily"
    )
    val messageLines: List<MessageLine>,
)
