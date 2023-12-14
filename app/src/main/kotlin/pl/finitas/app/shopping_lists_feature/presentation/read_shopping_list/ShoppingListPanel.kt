package pl.finitas.app.shopping_lists_feature.presentation.read_shopping_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.ClickableIcon
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.shopping_lists_feature.presentation.read_shopping_list.components.ShoppingListGrid
import java.util.UUID

@Composable
fun ShoppingListPanel(
    viewModel: ShoppingListViewModel,
    onUpsertShoppingListClick: (UUID?) -> Unit,
) {
    val shoppingLists by viewModel.shoppingLists.collectAsState(listOf())
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 64.dp, start = 30.dp, bottom = 10.dp, end = 30.dp),
        ) {
            Fonts.heading1.Text(
                text = "Shopping list",
                modifier = Modifier
                    .padding(start = 6.dp)
            )
            ClickableIcon(
                imageVector = Icons.Rounded.AddCircle,
                onClick = { onUpsertShoppingListClick(null) },
                Modifier.padding(top = 4.dp)
            )
        }
        ShoppingListGrid(
            shoppingListElements = shoppingLists,
            selected = viewModel.selected,
            onElementClick = {
                viewModel.selectElement(it.idShoppingList)
            },
            modifier = Modifier
                .padding(horizontal = 30.dp),
            onEditClick = { onUpsertShoppingListClick(it.idShoppingList) },
            onDeleteClick = { viewModel.onDeleteShoppingList(it.idShoppingList) },
        )
    }
}
