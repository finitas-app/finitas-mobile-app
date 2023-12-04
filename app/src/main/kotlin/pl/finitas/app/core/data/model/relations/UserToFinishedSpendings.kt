package pl.finitas.app.core.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.finitas.app.core.data.model.FinishedSpending
import pl.finitas.app.core.data.model.User

data class UserToFinishedSpendings(
    @Embedded
    val user: User,
    @Relation(
        parentColumn = "idUser",
        entityColumn = "idUser"
    )
    val finishedSpendings: List<FinishedSpending>,
)
