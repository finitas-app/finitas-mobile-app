package pl.finitas.app.profile_feature.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.constructors.borderShape
import pl.finitas.app.core.presentation.components.constructors.borderStroke
import pl.finitas.app.core.presentation.components.dialog.NestedDialog
import pl.finitas.app.core.presentation.components.utils.colors.Colors
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import java.time.LocalTime

@Composable
fun TimeInput(
    time: LocalTime,
    onChange: (LocalTime) -> Unit,
    enabled: Boolean = true,
) {
    var isDialogOpen by remember { mutableStateOf(false) }

    val openDialog = {
        isDialogOpen = true
    }
    val closeDialog = { isDialogOpen = false }


    Box(
        modifier = Modifier
            .height(35.dp)
            .padding(start = 60.dp)
            .fillMaxWidth()
            .background(Colors.backgroundDark, borderShape)
            .border(
                border = borderStroke,
                shape = borderShape,
            ),
    ) {
        val getTimeString = {
            val hour = if (time.hour < 10) "0${time.hour}" else "${time.hour}"
            val minute = if (time.minute < 10) "0${time.minute}" else "${time.minute}"
            "$hour:$minute"
        }
        Fonts.regular.Text(
            text = if (enabled) getTimeString() else "",
            color = Color.Gray,
            modifier = Modifier
                .align(Alignment.Center)
                .clickable { if (enabled) openDialog() },
        )
    }

    NestedDialog(
        isOpen = isDialogOpen,
        onDismiss = closeDialog,
    ) {
        val interactionSource = remember { MutableInteractionSource() }

        Box(modifier = Modifier
            .padding(bottom = 30.dp)
            .background(Colors.backgroundLight, shape = RoundedCornerShape(10.dp))
            .align(Alignment.BottomCenter)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {}
        ) {
            TimeEditor(
                time = time,
                onSave = onChange,
                onCancel = closeDialog,
            )
        }
    }
}

private val minutes = (0..59).toList()
private val hours = (0..23).toList()

@Composable
private fun TimeEditor(
    time: LocalTime,
    onSave: (LocalTime) -> Unit,
    onCancel: () -> Unit,
) {

    val minutesListState = rememberLazyListState(minutes.size * 10000 + time.minute - 1)
    val hoursListState = rememberLazyListState(hours.size * 10000 + time.hour - 1)

    val positionOfMinute by remember { derivedStateOf { ItemPositions(minutesListState) } }
    val positionOfHour by remember { derivedStateOf { ItemPositions(hoursListState) } }

    LaunchedEffect(positionOfMinute) {
        val (_, offset, isScrolling) = positionOfMinute

        if (!isScrolling && offset != 0) {
            minutesListState.animateScrollToItem(positionOfMinute.getClosestIndex, 0)
        }
    }

    LaunchedEffect(positionOfHour) {
        val (_, offset, isScrolling) = positionOfHour
        if (!isScrolling && offset != 0) {
            hoursListState.animateScrollToItem(positionOfHour.getClosestIndex, 0)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp, bottom = 20.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp)
                .height(110.dp)
        ) {
            LazyColumn(
                state = hoursListState,
                modifier = Modifier
            ) {
                this.items(Int.MAX_VALUE) { index ->
                    val hour = hours[index % hours.size]
                    val color by animateColorAsState(
                        targetValue = if (positionOfHour.isSelected(index)) Color.White else Color.Gray,
                        label = "",
                    )

                    val hourString = if (hour < 10) "0$hour" else "$hour"
                    Fonts.heading1.Text(text = hourString, color = color)
                }
            }
            LazyColumn(
                state = minutesListState,
                modifier = Modifier
            ) {
                this.items(Int.MAX_VALUE) { index ->
                    val minute = minutes[index % minutes.size]
                    val color by animateColorAsState(
                        targetValue = if (positionOfMinute.isSelected(index)) Color.White else Color.Gray,
                        label = "",
                    )

                    val minuteString = if (minute < 10) "0$minute" else "$minute"
                    Fonts.heading1.Text(text = minuteString, color = color)
                }
            }
        }
        Navigation(
            onSave = {
                onSave(
                    LocalTime.of(
                        (positionOfHour.index + 1) % hours.size,
                        (positionOfMinute.index + 1) % minutes.size
                    )
                )
            },
            onCancel = onCancel,
        )
    }
}

@Composable
private fun Navigation(
    onSave: () -> Unit,
    onCancel: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Fonts.heading2.Text(
            text = "Cancel",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxWidth(.4f)
                .clip(RoundedCornerShape(100))
                .clickable { onCancel() }
        )
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(1.dp, 30.dp)
                .background(Color.Gray)
        )
        Fonts.heading2.Text(
            text = "Save",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxWidth(.4f)
                .clip(RoundedCornerShape(100))
                .clickable {
                    onSave()
                    onCancel()
                },
        )
    }
}

private data class ItemPositions(
    val index: Int,
    val offset: Int,
    val isScrolling: Boolean,
) {
    val getClosestIndex get() = if (offset < offsetThreshold) this.index else this.index + 1

    constructor(listState: LazyListState) : this(
        listState.firstVisibleItemIndex,
        listState.firstVisibleItemScrollOffset,
        listState.isScrollInProgress,
    )

    fun isSelected(index: Int) = index == getClosestIndex + 1

    private companion object {
        const val offsetThreshold = 50
    }
}
