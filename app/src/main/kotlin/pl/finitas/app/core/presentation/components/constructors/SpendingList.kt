package pl.finitas.app.core.presentation.components.constructors

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.manage_spendings_feature.domain.services.SpendingContainer
import pl.finitas.app.manage_spendings_feature.domain.services.SpendingElement
import pl.finitas.app.manage_spendings_feature.domain.services.SpendingRecordView
import pl.finitas.app.manage_spendings_feature.domain.services.TotalSpendingView

private val borderColor = Color.White.copy(alpha = .1f)

@Composable
fun SpendingList(
    spendingElements: List<SpendingElement>,
    modifier: Modifier = Modifier,
    itemExtras: @Composable RowScope.(SpendingElement) -> Unit = {},
) {
    Column(
        modifier
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))

    ) {
        SpendingListRecursive(spendingElements, itemExtras)
    }
}

@Composable
private fun SpendingListRecursive(
    spendingElements: List<SpendingElement>,
    itemExtras: @Composable RowScope.(SpendingElement) -> Unit,
    depth: Int = 0,
) {

    Column(
        Modifier
            .fillMaxWidth()
    ) {
        spendingElements.forEachIndexed { index, spendingElement ->
            if (depth != 0 || index != 0) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(borderColor)
                )
            }
            SpendingElementComponent(spendingElement, itemExtras, depth)
        }
    }
}

@Composable
private fun SpendingElementComponent(
    spendingElement: SpendingElement,
    itemExtras: @Composable RowScope.(SpendingElement) -> Unit,
    depth: Int,
) {
    Row(
        modifier = Modifier
            .height(intrinsicSize = IntrinsicSize.Min)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            when (spendingElement) {
                is SpendingRecordView -> SpendingRecordBody(spendingElement, itemExtras, depth)
                is SpendingContainer -> SpendingCategoryBody(
                    spendingContainer = spendingElement,
                    itemExtras = itemExtras,
                    depth = depth,
                )

                is TotalSpendingView -> TotalSpendingBody(
                    totalSpendingView = spendingElement,
                    itemExtras = itemExtras,
                    depth = depth,
                )
            }
        }
    }
}

@Composable
private fun TotalSpendingBody(
    totalSpendingView: TotalSpendingView,
    itemExtras: @Composable RowScope.(SpendingElement) -> Unit,
    depth: Int,
) {
    SpendingCategoryBody(
        SpendingContainer(
            totalSpendingView.name,
            // TODO: remove idCategory and refactor
            0,
            totalSpendingView.spendingElements
        ),
        itemExtras,
        depth,
    )
}

@Composable
private fun SpendingCategoryBody(
    spendingContainer: SpendingContainer,
    itemExtras: @Composable RowScope.(SpendingElement) -> Unit,
    depth: Int,
) {
    var isOpenNestedCategories by remember { mutableStateOf(false) }
    fun hasNested() = spendingContainer.spendingElements.isNotEmpty()

    Row(
        modifier = Modifier
            .padding(top = 15.dp, bottom = 15.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        RenderStarter(spendingElement = spendingContainer, depth = depth)
        Row {
            if (hasNested())
                NestedToggled(
                    isOpened = isOpenNestedCategories,
                    onClick = { isOpenNestedCategories = !isOpenNestedCategories },
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 20.dp)
                )
            itemExtras(spendingContainer)
        }
    }
    if (hasNested() && isOpenNestedCategories)
        SpendingListRecursive(
            spendingContainer.spendingElements,
            itemExtras,
            depth + 1
        )
}

@Composable
private fun RowScope.NestedToggled(
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
private fun SpendingRecordBody(
    spendingRecordView: SpendingRecordView,
    itemExtras: @Composable RowScope.(SpendingRecordView) -> Unit,
    depth: Int,
) {
    Row(
        modifier = Modifier
            .padding(top = 15.dp, bottom = 15.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        RenderStarter(spendingElement = spendingRecordView, depth = depth)
        itemExtras(spendingRecordView)
    }
}

@Composable
fun RenderStarter(spendingElement: SpendingElement, depth: Int) {
    Row(
        Modifier
            .padding(start = 20.dp)
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
            text = spendingElement.name,
            Modifier
                .align(Alignment.CenterVertically)
        )
    }
}
