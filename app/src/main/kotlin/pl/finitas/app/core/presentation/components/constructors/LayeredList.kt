package pl.finitas.app.core.presentation.components.constructors

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import pl.finitas.app.core.presentation.components.utils.trimOnOverflow

private val borderColor = Color.White.copy(alpha = .1f)

/**
 * Specify the type of the generalized class in LayeredList explicitly to the origin interface,
 * since the implementation uses an unverifiable cast.
 */
@Composable
fun <T : Nameable> LayeredList(
    nameableCollection: List<T>,
    modifier: Modifier = Modifier,
    maxTextLength: Int = 14,
    onNameClick: (T) -> Unit = {},
    itemExtras: @Composable RowScope.(T) -> Unit = {},
) {
    Column(
        modifier
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
    ) {
        LayeredListRecursive(
            nameableCollections = nameableCollection,
            onNameClick = onNameClick,
            itemExtras = itemExtras,
            maxTextLength = maxTextLength,
        )
    }
}

@Composable
private fun <T : Nameable> LayeredListRecursive(
    nameableCollections: List<T>,
    onNameClick: (T) -> Unit = {},
    itemExtras: @Composable RowScope.(T) -> Unit,
    depth: Int = 0,
    maxTextLength: Int,
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
            NameableComponent(
                nameable = nameableCollection,
                onNameClick = onNameClick,
                itemExtras = itemExtras,
                depth = depth,
                maxTextLength = maxTextLength,
            )
        }
    }
}

@Composable
private fun <T : Nameable> NameableComponent(
    nameable: T,
    onNameClick: (T) -> Unit = {},
    itemExtras: @Composable RowScope.(T) -> Unit,
    depth: Int,
    maxTextLength: Int,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (nameable is NameableCollection<*>) {
            NameableCollectionBody(
                nameableCollection = nameable as NameableCollection<T>,
                onNameClick = onNameClick,
                itemExtras = itemExtras,
                depth = depth,
                maxTextLength = maxTextLength
            )
        } else {
            NameableBody(
                nameable = nameable,
                onNameClick = onNameClick,
                itemExtras = itemExtras,
                depth = depth,
                maxTextLength = maxTextLength,
            )
        }
    }
}

@Composable
private fun <T : Nameable> ColumnScope.NameableCollectionBody(
    nameableCollection: NameableCollection<T>,
    onNameClick: (T) -> Unit,
    itemExtras: @Composable RowScope.(T) -> Unit,
    depth: Int,
    maxTextLength: Int,
) {
    var isOpenNestedCategories by remember { mutableStateOf(false) }
    fun hasNested() = nameableCollection.elements.isNotEmpty()

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RenderStarter(nameable = nameableCollection as T, onNameClick = onNameClick, depth = depth, maxTextLength = maxTextLength)
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
                nameableCollections = nameableCollection.elements,
                onNameClick = onNameClick,
                itemExtras = itemExtras,
                depth = depth + 1,
                maxTextLength = maxTextLength,
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
private fun <T : Nameable> NameableBody(
    nameable: T,
    onNameClick: (T) -> Unit = {},
    itemExtras: @Composable RowScope.(T) -> Unit,
    depth: Int,
    maxTextLength: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RenderStarter(
            nameable = nameable,
            onNameClick = onNameClick,
            depth = depth,
            maxTextLength = maxTextLength,
        )
        itemExtras(nameable)
    }
}

@Composable
fun <T : Nameable> RenderStarter(
    nameable: T,
    onNameClick: (T) -> Unit,
    depth: Int,
    maxTextLength: Int,
) {
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
        val interactionSource = remember { MutableInteractionSource() }
        Fonts.regular.Text(
            text = nameable.name.trimOnOverflow(maxTextLength - depth * 3),
            Modifier
                .align(Alignment.CenterVertically)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = { onNameClick(nameable) },
                )
        )
    }
}
