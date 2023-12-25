package pl.finitas.app.core.presentation.components.navbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pl.finitas.app.R
import pl.finitas.app.core.presentation.components.ClickableIcon
import pl.finitas.app.core.presentation.components.background.PrimaryBackground
import pl.finitas.app.navigation.NavPaths

@Composable
fun BoxScope.NavBar(
    navController: NavController,
    backgroundColor: Color = Color(16, 16, 16),
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(140.dp)
            .align(Alignment.BottomCenter)
            .background(
                Brush.verticalGradient(
                    listOf(Color.Transparent, backgroundColor),
                    endY = with(LocalDensity.current) { 90.dp.toPx() }
                )
            )
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(100.dp),
        ) {

            Spacer(Modifier.weight(.05f))
            NavBarIcon(
                imageVector = Icons.Rounded.Person,
                onClick = {
                    navController.navigate(NavPaths.ProfileScreen.route)
                },
            )
            NavBarIcon(
                imageVector = Icons.Rounded.List,
                onClick = {
                    navController.navigate(NavPaths.AdditionalElementsScreen.route)
                },
            )
            NavBarIcon(
                imageVector = Icons.Rounded.Home,
                onClick = {
                    navController.navigate(NavPaths.HomeScreen.route)
                },
            )
            NavBarIcon(
                painterResource(id = R.drawable.ic_shopping_list),
                onClick = {
                    navController.navigate(NavPaths.ShoppingLists.route)
                },
            )
            NavBarIcon(
                painterResource(id = R.drawable.ic_send_icon),
                onClick = {
                    navController.navigate(NavPaths.RoomsScreen.route)
                },
            )
            Spacer(Modifier.weight(.05f))
        }
    }

}

@Composable
private fun RowScope.NavBarIcon(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    ClickableIcon(
        imageVector = imageVector,
        contentDescription = "",
        onClick = onClick,
        modifier = modifier
            .fillMaxSize()
            .weight(.8f),
        iconSize = 40.dp
    )
}

@Composable
private fun RowScope.NavBarIcon(
    painter: Painter,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    ClickableIcon(
        painter = painter,
        contentDescription = "",
        onClick = onClick,
        modifier = modifier
            .fillMaxSize()
            .weight(.8f),
        iconSize = 40.dp
    )
}

@Preview
@Composable
fun test() {
    PrimaryBackground {

    }
}
