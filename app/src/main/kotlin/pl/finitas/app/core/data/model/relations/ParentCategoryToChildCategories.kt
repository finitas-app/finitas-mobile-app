package pl.finitas.app.core.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.finitas.app.core.data.model.Category

data class ParentCategoryToChildCategories(
    @Embedded
    val parentCategory: Category,
    @Relation(
        parentColumn = "idParent",
        entityColumn = "idCategory"
    )
    val childCategories: List<Category>,
)