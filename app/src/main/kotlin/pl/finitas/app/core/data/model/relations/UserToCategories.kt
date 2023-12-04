package pl.finitas.app.core.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.finitas.app.core.data.model.Category
import pl.finitas.app.core.data.model.User

data class UserToCategories(
    @Embedded
    val user: User,
    @Relation(
        parentColumn = "idUser",
        entityColumn = "idUser"
    )
    val categories: List<Category>,
)
