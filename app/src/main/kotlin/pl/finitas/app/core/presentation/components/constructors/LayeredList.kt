package pl.finitas.app.core.presentation.components.constructors

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.domain.Nameable
import pl.finitas.app.core.domain.NameableCollection
import pl.finitas.app.core.presentation.components.utils.text.Fonts

private val borderColor = Color.White.copy(alpha = .1f)

/**
 * Specify the type of the generalized class in LayeredList explicitly to the origin interface,
 * since the implementation uses an unverifiable cast.
 */
@Composable
fun <T : Nameable> LayeredList(
    nameableCollection: List<T>,
    modifier: Modifier = Modifier,
    itemExtras: @Composable RowScope.(T) -> Unit = {},
) {
    Column(
        modifier
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
    ) {
        LayeredListRecursive(
            nameableCollection,
            itemExtras,
        )
    }
}

@Composable
private fun <T> LayeredListRecursive(
    nameableCollections: List<Nameable>,
    itemExtras: @Composable RowScope.(T) -> Unit,
    depth: Int = 0,
) {

    Column(
        Modifier
            .fillMaxWidth()
    ) {
        nameableCollections.forEachIndexed { index, nameableCollection ->
            if (depth != 0 || index != 0) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(borderColor)
                )
            }
            NameableComponent(nameableCollection, itemExtras, depth)
        }
    }
}

@Composable
private fun <T> NameableComponent(
    nameable: Nameable,
    itemExtras: @Composable RowScope.(T) -> Unit,
    depth: Int,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (nameable is NameableCollection) {
            NameableCollectionBody(
                nameableCollection = nameable,
                itemExtras,
                depth = depth,
            )
        } else {
            NameableBody(nameable, itemExtras, depth)
        }
    }
}

@Composable
private fun <T> ColumnScope.NameableCollectionBody(
    nameableCollection: NameableCollection,
    itemExtras: @Composable RowScope.(T) -> Unit,
    depth: Int,
) {
    var isOpenNestedCategories by remember { mutableStateOf(false) }
    fun hasNested() = nameableCollection.elements.isNotEmpty()

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RenderStarter(nameable = nameableCollection, depth = depth)
        Row {
            if (hasNested())
                NestedSwitch(
                    isOpened = isOpenNestedCategories,
                    onClick = { isOpenNestedCategories = !isOpenNestedCategories },
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 16.dp)
                )
            itemExtras(nameableCollection as T)
        }
    }
    if (hasNested()) {
        AnimatedVisibility(
            visible = isOpenNestedCategories,
        ) {
            LayeredListRecursive(
                nameableCollection.elements,
                itemExtras,
                depth + 1
            )
        }
    }
}

@Composable
private fun NestedSwitch(
    isOpened: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier
            .clip(CircleShape)
            .clickable(onClick = onClick)
    ) {
        if (isOpened)
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowDown,
                contentDescription = "Down",
                tint = Color.White,
            )
        else
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowRight,
                contentDescription = "Right",
                tint = Color.White,
            )
    }
}

@Composable
private fun <T> NameableBody(
    nameable: Nameable,
    itemExtras: @Composable RowScope.(T) -> Unit,
    depth: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RenderStarter(nameable = nameable, depth = depth)
        itemExtras(nameable as T)
    }
}

@Composable
fun RenderStarter(nameable: Nameable, depth: Int) {
    Row(
        Modifier
            .padding(top = 16.dp, bottom = 16.dp, start = 16.dp)
    ) {
        if (depth > 0)
            Box(
                modifier = Modifier
                    .let {
                        if (depth == 1) {
                            it.padding(end = 10.dp)
                        } else {
                            it.padding(end = 15.dp)
                        }
                    }
                    .width((15 * depth - 10).dp)
                    .align(Alignment.CenterVertically)
                    .height(1.dp)
                    .background(Color.White.copy(0.1f * depth))
            )
        Fonts.regular.Text(
            text =
            if (nameable.name.length > 20)
                "${nameable.name.substring(0, 20)}..."
            else nameable.name,
            Modifier
                .align(Alignment.CenterVertically)
        )
    }
}
