package pl.finitas.app.shopping_lists_feature.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import pl.finitas.app.core.data.model.SpendingCategory
import pl.finitas.app.core.domain.dto.store.DeleteShoppingListRequest
import pl.finitas.app.core.domain.dto.store.RemoteShoppingItemDto
import pl.finitas.app.core.domain.dto.store.RemoteShoppingListDto
import pl.finitas.app.core.domain.repository.ProfileRepository
import pl.finitas.app.core.domain.repository.ShoppingListStoreRepository
import pl.finitas.app.core.domain.repository.SpendingCategoryNotFoundException
import pl.finitas.app.core.domain.repository.SpendingCategoryRepository
import pl.finitas.app.shopping_lists_feature.presentation.write_shopping_list.ShoppingListState
import java.util.UUID


class ShoppingListService(
    private val shoppingListRepository: ShoppingListRepository,
    private val spendingCategoryRepository: SpendingCategoryRepository,
    private val shoppingListStoreRepository: ShoppingListStoreRepository,
    private val profileRepository: ProfileRepository,
) {
    fun getShoppingLists(): Flow<List<ShoppingListView>> =
        shoppingListRepository
            .getShoppingLists()
            .combine(
                spendingCategoryRepository
                    .getAllUsersSpendingCategories()
            ) { shoppingLists: List<ShoppingListDto>, categories ->
                val categoriesById = categories.associateBy { it.idCategory }

                shoppingLists
                    .map { shoppingList ->
                        val (
                            name,
                            color,
                            idShoppingList,
                            shoppingItems,
                            isFinished,
                        ) = shoppingList
                        val itemsById = shoppingItems.groupBy { it.idSpendingCategory }
                        ShoppingListView(
                            name = name,
                            color = color,
                            idShoppingList = idShoppingList,
                            isFinished = isFinished,
                            elements = itemsById.map { (idSpendingCategory, items) ->
                                ShoppingItemCategoryView(
                                    name = categoriesById[idSpendingCategory]?.name
                                        ?: throw SpendingCategoryNotFoundException(
                                            idSpendingCategory
                                        ),
                                    idSpendingCategory = idSpendingCategory,
                                    elements = items.map {
                                        ShoppingItemView(
                                            name = it.name,
                                            amount = it.amount,
                                            idSpendingCategory = it.idSpendingCategory,
                                            idShoppingItem = it.idSpendingRecordData,
                                        )
                                    }
                                )
                            }
                        ).normalizeShoppingListView(categoriesById)
                    }
            }

    suspend fun getShoppingListBy(
        idShoppingList: UUID,
        categories: List<ShoppingItemCategoryView>,
    ): ShoppingListState {
        val shoppingList = shoppingListRepository.getShoppingListBy(idShoppingList)
        val shoppingItemsByCategory = shoppingList.shoppingItems.groupBy { it.idSpendingCategory }

        return ShoppingListState(
            title = shoppingList.name,
            idShoppingList = shoppingList.idShoppingList,
            idUser = shoppingList.idUser,
            color = shoppingList.color,
            categories = categories.map { category ->
                category.copy(
                    elements = shoppingItemsByCategory[category.idSpendingCategory]
                        ?.map {
                            ShoppingItemView(
                                name = it.name,
                                amount = it.amount,
                                idShoppingItem = it.idSpendingRecordData,
                                idSpendingCategory = it.idSpendingCategory
                            )
                        } ?: listOf()
                )
            }
        )
    }

    suspend fun upsertShoppingList(shoppingListState: ShoppingListState) {
        if (shoppingListState.idUser != null) throw InvalidShoppingListState(
            shoppingListState,
            "Is not possible to upsert shopping list of not current user."
        )
        val generatedUUID = shoppingListState.idShoppingList ?: UUID.randomUUID()
        val dto = ShoppingListDto(
            name = shoppingListState.title,
            color = shoppingListState.color,
            idShoppingList = generatedUUID,
            isFinished = false,
            isDeleted = false,
            version = null,
            shoppingItems = shoppingListState.categories.flatMap { shoppingItemCategory ->
                shoppingItemCategory.elements.map { shoppingItem ->
                    if (shoppingItem !is ShoppingItemView) throw InvalidShoppingListState(
                        shoppingListState
                    )

                    ShoppingItemDto(
                        idSpendingRecordData = shoppingItem.idShoppingItem,
                        name = shoppingItem.name,
                        amount = shoppingItem.amount,
                        idSpendingCategory = shoppingItemCategory.idSpendingCategory,
                    )
                }
            }
        )

        shoppingListRepository.upsertShoppingList(dto)
        val currentUser = profileRepository.getAuthorizedUserId().first() ?: return
        try {
            shoppingListStoreRepository.changeShoppingLists(listOf(dto.toRemote(currentUser)))
        } catch (_: Exception) {

        }
    }

    suspend fun deleteShoppingListBy(idShoppingList: UUID) {
        val currentUser = profileRepository.getAuthorizedUserId().first()
        val shoppingList = shoppingListRepository.getShoppingListVersionBy(idShoppingList)
        if (
            shoppingList.version == null
        ) {
            shoppingListRepository.deleteShoppingListBy(idShoppingList)
            return
        }

        if (shoppingList.idUser == null) {
            shoppingListRepository.markAsDeleted(idShoppingList)
            if (currentUser != null) {
                try {
                    shoppingListStoreRepository.deleteShoppingList(
                        DeleteShoppingListRequest(
                            idShoppingList,
                            currentUser,
                        )
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return
        }
        //TODO: maybe disable for all not user data and just remove from database
        shoppingListStoreRepository.deleteShoppingList(
            DeleteShoppingListRequest(
                idShoppingList,
                shoppingList.idUser,
            )
        )
    }
}

// TODO: refactoring
private fun ShoppingListView.normalizeShoppingListView(categoryById: Map<UUID, SpendingCategory>): ShoppingListView {
    val recordsByCategoryId = elements.associate { element ->
        (element as ShoppingItemCategoryView).let { it.idSpendingCategory to it.elements }
    }.toMutableMap()
    val result = mutableListOf<ShoppingListElement>()
    val previousSpendingElements = mutableMapOf<UUID, ShoppingItemCategoryView>()

    outer@ while (recordsByCategoryId.isNotEmpty()) {
        val (idSpendingCategory, items) = recordsByCategoryId.entries.first()
        var currentCategory =
            categoryById[idSpendingCategory] ?: throw SpendingCategoryNotFoundException(
                idSpendingCategory
            )
        var currentShoppingElement = ShoppingItemCategoryView(
            currentCategory.name,
            items,
            idSpendingCategory,
        )
        previousSpendingElements[idSpendingCategory] = currentShoppingElement
        val possiblePrevious =
            verifyPrevious(previousSpendingElements, currentCategory.idParent)
        if (possiblePrevious != null) {
            possiblePrevious += currentShoppingElement
            recordsByCategoryId.remove(idSpendingCategory)
            continue
        }

        while (currentCategory.idParent != null) {
            currentCategory =
                categoryById[currentCategory.idParent] ?: throw SpendingCategoryNotFoundException(
                    currentCategory.idParent!!
                )
            val categoryId = currentCategory.idCategory
            val newContainers: MutableList<ShoppingListElement> =
                mutableListOf(currentShoppingElement)
            if (currentCategory.idCategory in recordsByCategoryId) {
                newContainers.addAll(recordsByCategoryId[categoryId]!!)
                recordsByCategoryId.remove(categoryId)
            }
            currentShoppingElement = ShoppingItemCategoryView(
                currentCategory.name,
                newContainers,
                categoryId,
            )
            previousSpendingElements[idSpendingCategory] = currentShoppingElement

            val possiblePreviousInner =
                verifyPrevious(previousSpendingElements, currentCategory.idParent)
            if (possiblePreviousInner != null) {
                possiblePreviousInner += currentShoppingElement
                continue@outer
            }
        }
        result += currentShoppingElement
        recordsByCategoryId.remove(idSpendingCategory)
    }

    return copy(elements = result)
}

private fun verifyPrevious(
    previous: Map<UUID, ShoppingItemCategoryView>,
    categoryId: UUID?,
): MutableList<ShoppingListElement>? {
    return previous[categoryId]?.let { it.elements as? MutableList<ShoppingListElement> }
}

private fun ShoppingListDto.toRemote(idUser: UUID): RemoteShoppingListDto = RemoteShoppingListDto(
    idShoppingList = idShoppingList,
    name = name,
    color = color,
    //TODO: to remove
    version = 0,
    idUser = idUser,
    isDeleted = isDeleted,
    isFinished = isFinished,
    shoppingItems = shoppingItems.map {
        RemoteShoppingItemDto(
            idShoppingItem = it.idSpendingRecordData,
            amount = it.amount,
            idSpendingRecordData = it.idSpendingRecordData,
            name = it.name,
            idCategory = it.idSpendingCategory,
        )
    }
)


class InvalidShoppingListState(
    shoppingListState: ShoppingListState,
    message: String = "Total spending state is invalid: $shoppingListState",
) :
    Exception(message)
