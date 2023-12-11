package pl.finitas.app.core.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import pl.finitas.app.core.data.model.SpendingCategory

data class ParentCategoryToChildCategories(
    @Embedded
    val parentCategory: SpendingCategory,
    @Relation(
        parentColumn = "idParent",
        entityColumn = "idCategory"
    )
    val childCategories: List<SpendingCategory>,
)