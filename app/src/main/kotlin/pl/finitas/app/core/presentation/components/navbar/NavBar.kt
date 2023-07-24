package pl.finitas.app.core.presentation.components.navbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.finitas.app.R
import pl.finitas.app.core.presentation.components.background.PrimaryBackground

@Composable
fun NavBar(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
    ) {

        Spacer(Modifier.weight(.05f))
        NavBarIcon(
            imageVector = Icons.Rounded.Person,
            modifier = Modifier.padding(5.dp),
        )
        NavBarIcon(
            imageVector = Icons.Rounded.List,
            modifier = Modifier
                .padding(3.dp),
        )
        NavBarIcon(
            imageVector = Icons.Rounded.Home,
            modifier = Modifier
                .padding(5.dp),
        )
        NavBarIcon(
            painterResource(id = R.drawable.ic_shopping_list),
            modifier = Modifier
                .padding(11.dp),
        )
        NavBarIcon(
            painterResource(id = R.drawable.ic_send_icon),
            modifier = Modifier
                .padding(8.dp),
        )
        Spacer(Modifier.weight(.05f))
    }
}

@Composable
private fun RowScope.NavBarIcon(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .weight(.8f)
            .then(modifier)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = "",
            modifier = modifier.then(
                Modifier
                    .fillMaxSize()
            ),
            tint = Color.White
        )
    }
}

@Composable
private fun RowScope.NavBarIcon(
    painter: Painter,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .weight(.8f)
    ) {
        Icon(
            painter = painter,
            contentDescription = "",
            modifier = modifier.then(
                Modifier
                    .fillMaxSize()
            ),
            tint = Color.White
        )
    }
}

@Preview
@Composable
fun test() {
    PrimaryBackground {

        NavBar(modifier = Modifier.align(Alignment.BottomCenter))
    }
}
