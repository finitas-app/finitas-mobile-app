package pl.finitas.app.core.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.finitas.app.core.data.model.SpendingSummary
import pl.finitas.app.core.data.model.User

data class UserToSpendingSummaries(
    @Embedded
    val user: User,
    @Relation(
        parentColumn = "idUser",
        entityColumn = "idUser"
    )
    val spendingSummaries: List<SpendingSummary>,
)
