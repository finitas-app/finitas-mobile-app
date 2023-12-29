package pl.finitas.app.room_feature.presentation.messanger.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.data.model.ShoppingList
import pl.finitas.app.core.presentation.components.dialog.ConstructorBoxDialog
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import java.util.UUID

@Composable
fun SendObjectDialog(
    shoppingListsPreview: List<ShoppingList>,
    isOpen: Boolean,
    onClose: () -> Unit,
    onSave: (UUID) -> Unit,
) {
    var selected by remember { mutableStateOf<UUID?>(null) }

    ConstructorBoxDialog(
        isOpen = isOpen,
        onSave = {
            if (selected != null) {
                onSave(selected!!)
            }
        },
        onClose = onClose,
    ) {
        Fonts.heading1.Text(
            text = "Shopping lists",
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 20.dp),
        )
        shoppingListsPreview.forEach { shoppingList ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selected = if (selected != shoppingList.idShoppingList)
                            shoppingList.idShoppingList
                        else
                            null
                    }
            ) {
                Fonts.regular.Text(
                    text = shoppingList.name,
                    modifier = Modifier.padding(start = 8.dp, bottom = 10.dp, top = 10.dp)
                )
                if (shoppingList.idShoppingList == selected) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "Check",
                        tint = Color.White
                    )
                } else {
                    Box(Modifier)
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.White.copy(.1f))
            )
        }
    }
}
