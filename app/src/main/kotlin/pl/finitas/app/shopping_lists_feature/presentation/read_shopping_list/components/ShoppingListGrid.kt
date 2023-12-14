package pl.finitas.app.shopping_lists_feature.presentation.read_shopping_list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.ClickableIcon
import pl.finitas.app.core.presentation.components.constructors.LayeredList
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.shopping_lists_feature.domain.ShoppingItemView
import pl.finitas.app.shopping_lists_feature.domain.ShoppingListView
import java.util.UUID

private val colors = listOf(
    Color(0xFF76A470),
    Color(0xFF299182),
    Color(0xFFF85784),
    Color(0xFFFBA776),
    Color(0xFFE1BC6A),
    Color(0xFF635B7D),
)

@Composable
fun ShoppingListGrid(
    shoppingListElements: List<ShoppingListView>,
    selected: UUID?,
    onElementClick: (ShoppingListView) -> Unit,
    onEditClick: (ShoppingListView) -> Unit,
    onDeleteClick: (ShoppingListView) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (
        firstPart,
        selectedElement,
        lastPart,
    ) = prepareElementsToView(shoppingListElements, selected)

    Column(
        modifier
            .verticalScroll(rememberScrollState())
            .padding(bottom = 100.dp)
    ) {
        firstPart.chunked(2).forEach { elements ->
            ShoppingListRow(
                firstElement = elements[0],
                secondElement = elements.getOrNull(1),
                onElementClick = onElementClick,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick,
            )
        }
        if (selectedElement != null)
            ShoppingListBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp),
                color = Color(selectedElement.color),
            ) {
                BigShoppingListBody(
                    shoppingList = selectedElement,
                    onElementClick = onElementClick,
                    onEditClick = onEditClick,
                    onDeleteClick = onDeleteClick
                )
            }
        lastPart.chunked(2).forEach { elements ->
            ShoppingListRow(
                firstElement = elements[0],
                secondElement = elements.getOrNull(1),
                onElementClick = onElementClick,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick,
            )
        }
    }
}

@Composable
private fun ShoppingListRow(
    firstElement: ShoppingListView,
    secondElement: ShoppingListView?,
    onElementClick: (ShoppingListView) -> Unit,
    onEditClick: (ShoppingListView) -> Unit,
    onDeleteClick: (ShoppingListView) -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(bottom = 14.dp)
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        ShoppingListBox(
            color = Color(firstElement.color),
            modifier = Modifier
                .weight(.4f)
                .aspectRatio(1f)
                .clickable(
                    indication = null,
                    interactionSource = interactionSource,
                ) { onElementClick(firstElement) }
        ) {
            SmallShoppingListBody(
                shoppingList = firstElement,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick,
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        if (secondElement != null) {
            val secondInteractionSourse = remember { MutableInteractionSource() }
            ShoppingListBox(
                color = Color(secondElement.color),
                modifier = Modifier
                    .weight(.4f)
                    .aspectRatio(1f)
                    .clickable(
                        interactionSource = secondInteractionSourse,
                        indication = null,
                    ) { onElementClick(secondElement) }
            ) {
                SmallShoppingListBody(
                    shoppingList = secondElement,
                    onEditClick = onEditClick,
                    onDeleteClick = onDeleteClick,
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .weight(.4f)
                    .aspectRatio(1f)
            )
        }
    }
}

@Composable
private fun BoxScope.SmallShoppingListBody(
    shoppingList: ShoppingListView,
    onEditClick: (ShoppingListView) -> Unit,
    onDeleteClick: (ShoppingListView) -> Unit,
) {
    Fonts.regular.Text(
        text = shoppingList.name,
        modifier = Modifier
            .padding(bottom = 20.dp)
            .align(Alignment.Center),
    )

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth(),
    ) {
        ClickableIcon(
            imageVector = Icons.Rounded.Edit,
            onClick = { onEditClick(shoppingList) }
        )
        ClickableIcon(
            imageVector = Icons.Rounded.Delete,
            onClick = { onDeleteClick(shoppingList) }
        )
    }
}

@Composable
private fun BigShoppingListBody(
    shoppingList: ShoppingListView,
    onElementClick: (ShoppingListView) -> Unit,
    onEditClick: (ShoppingListView) -> Unit,
    onDeleteClick: (ShoppingListView) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        Fonts.regular.Text(
            text = shoppingList.name,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 24.dp, bottom = 24.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                ) { onElementClick(shoppingList) },
        )
        LayeredList(
            nameableCollection = shoppingList.elements,
            modifier = Modifier
                .padding(horizontal = 20.dp)
        ) { shoppingElement ->
            if (shoppingElement is ShoppingItemView)
                Fonts.regular.Text(
                    text = "${shoppingElement.amount}",
                    modifier = Modifier
                        .padding(end = 16.dp)
                )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
        ) {
            ClickableIcon(
                imageVector = Icons.Rounded.Edit,
                onClick = { onEditClick(shoppingList) }
            )
            ClickableIcon(
                imageVector = Icons.Rounded.Delete,
                onClick = { onDeleteClick(shoppingList) }
            )
        }
    }
}

private fun prepareElementsToView(
    shoppingListElements: List<ShoppingListView>,
    selected: UUID?,
): Triple<List<ShoppingListView>, ShoppingListView?, List<ShoppingListView>> {
    if (selected == null) {
        return Triple(shoppingListElements, null, listOf())
    }
    val index = shoppingListElements.indexOfFirst { it.idShoppingList == selected }
    val selectedElement = shoppingListElements[index]

    val firstPart = shoppingListElements.subList(0, index).toMutableList()
    val lastPart =
        shoppingListElements.subList(index + 1, shoppingListElements.size).toMutableList()

    if (firstPart.size % 2 != 0) {
        lastPart.add(0, firstPart.removeLast())
    }

    return Triple(firstPart, selectedElement, lastPart)
}
