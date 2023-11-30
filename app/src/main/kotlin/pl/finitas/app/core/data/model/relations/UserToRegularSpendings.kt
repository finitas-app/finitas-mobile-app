package pl.finitas.app.core.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.finitas.app.core.data.model.RegularSpending
import pl.finitas.app.core.data.model.User

data class UserToRegularSpendings(
    @Embedded
    val user: User,
    @Relation(
        parentColumn = "idUser",
        entityColumn = "idUser"
    )
    val regularSpendings: List<RegularSpending>,
)
