package pl.finitas.app.shopping_lists_feature.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pl.finitas.app.core.presentation.components.background.PrimaryBackground
import pl.finitas.app.core.presentation.components.navbar.NavBar
import pl.finitas.app.shopping_lists_feature.presentation.components.ShoppingListComponent


@Composable
fun ShoppingListsScreen(
    navController: NavController,
) {
    PrimaryBackground {
        Column {
            ShoppingListComponent(modifier = Modifier
                .padding(top = 200.dp, start = 20.dp)
                .size(200.dp))
            ShoppingListComponent(modifier = Modifier
                .padding(top = 20.dp, start = 20.dp)
                .size(200.dp),
                color = Color(0xFF299182)
            )
        }
        NavBar(navController = navController)
    }
}
