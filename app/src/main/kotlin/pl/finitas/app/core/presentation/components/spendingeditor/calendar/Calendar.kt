package pl.finitas.app.core.presentation.components.spendingeditor.calendar

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import pl.finitas.app.R
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.util.LinkedList

@Composable
fun Calendar() {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF213138))
    ) {
        var date by remember {
            mutableStateOf(LocalDate.of(2003, 12, 2))
        }
        var dateForCalendarShow by remember {
            mutableStateOf(LocalDate.of(2003, 12, 2))
        }
        Text(text = "$date")
        Column(modifier = Modifier.align(Alignment.Center)) {
            YearMonthEditor(
                date = dateForCalendarShow,
                onDateChange = { dateForCalendarShow = it },
            )
            DaySelectBoard(year = dateForCalendarShow.year, month = dateForCalendarShow.month, onDaySelect = { day ->
                date = day
            })
        }
    }
}

@Composable
fun DaySelectBoard(year: Int, month: Month, onDaySelect: (LocalDate) -> Unit) {
    val firstDay = LocalDate.of(year, month, 1)
    val weeks = prepareCalendarMonthData(firstDay)


    var currentDay = 0
    Row {
        for (dayOfWeek in 0..6) {
            Column {
                for (day in weeks.map { it[dayOfWeek] }) {
                    if (day == null) {
                        Text(text = "x", Modifier.padding(end = 10.dp))
                    } else {
                        Text(text = "${day.dayOfMonth}", Modifier.padding(5.dp).clip(CircleShape).clickable { onDaySelect(day) })
                    }
                }
            }
        }
    }
}

@Composable
fun YearMonthEditor(
    date: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isDialogOpen by remember { mutableStateOf(false) }

    val openDialog = { isDialogOpen = true }
    val closeDialog = { isDialogOpen = false }


    Row {
        Text(text = "${date.month} ${date.year}", modifier = modifier
            .background(Color.White)
            .clickable { openDialog() })
    }

    val background = colorResource(id = R.color.background_dark)

    if (isDialogOpen)
        Dialog(onDismissRequest = closeDialog) {
            val interactionSourceOuter = remember { MutableInteractionSource() }
            Box(modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = interactionSourceOuter,
                    indication = null
                ) { closeDialog() }) {
                val interactionSource = remember { MutableInteractionSource() }

                Box(modifier = Modifier
                    .padding(bottom = 30.dp)
                    .background(background, shape = RoundedCornerShape(10.dp))
                    .align(Alignment.BottomCenter)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {}
                ) {
                    YearMonthEditor(
                        date = date,
                        onSave = onDateChange,
                        onCancel = closeDialog,
                    )
                }
            }
        }
}


val months = Month.values().map { it.toString().take(3) }
val years = (2000..LocalDate.now().year + 1).toList()

@Composable
fun BoxScope.YearMonthEditor(
    date: LocalDate,
    onSave: (LocalDate) -> Unit,
    onCancel: () -> Unit,
) {

    val monthsListState = rememberLazyListState(months.size * 10000 + date.month.ordinal - 1)
    val yearsListState = rememberLazyListState(years.size * 10000 + years.indexOf(date.year) - 1)


    val positionOfMonth by remember { derivedStateOf { ItemPositions(monthsListState) } }
    val positionOfYear by remember { derivedStateOf { ItemPositions(yearsListState) } }

    LaunchedEffect(positionOfMonth) {
        val (_, offset, isScrolling) = positionOfMonth

        if (!isScrolling && offset != 0) {
            monthsListState.animateScrollToItem(positionOfMonth.getClosestIndex, 0)
        }
    }

    LaunchedEffect(positionOfYear) {
        val (_, offset, isScrolling) = positionOfYear
        println(positionOfMonth.index)
        if (!isScrolling && offset != 0) {
            yearsListState.animateScrollToItem(positionOfYear.getClosestIndex, 0)
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
                state = monthsListState,
                modifier = Modifier
            ) {
                this.items(Int.MAX_VALUE) { index ->
                    val month = months[index % months.size]
                    val color by animateColorAsState(
                        targetValue = if (positionOfMonth.isSelected(index)) Color.White else Color.Gray,
                        label = "month_selector_color",
                    );

                    Fonts.heading1.Text(text = month, color = color)
                }
            }

            LazyColumn(
                state = yearsListState,
                modifier = Modifier
            ) {
                this.items(Int.MAX_VALUE) { index ->
                    val year = years[index % years.size]
                    val color by animateColorAsState(
                        targetValue = if (positionOfYear.isSelected(index)) Color.White else Color.Gray,
                        label = "month_selector_color",
                    );

                    Fonts.heading1.Text(text = "$year", color = color)
                }
            }
        }
        Navigation(
            onSave = {
                onSave(
                    LocalDate.of(
                        years[(positionOfYear.index + 1) % years.size],
                        (positionOfMonth.index + 1) % months.size + 1,
                        1,
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

fun prepareCalendarMonthData(firstDay: LocalDate): List<List<LocalDate?>> {
    val days = (1..firstDay.lengthOfMonth()).map {
        LocalDate.of(firstDay.year, firstDay.month, it)
    }

    val list = LinkedList<LinkedList<LocalDate?>>()
    list += LinkedList<LocalDate?>()

    days.forEach { day ->
        list.last() += day
        if (day.dayOfWeek == DayOfWeek.SUNDAY) list += LinkedList<LocalDate?>()
    }

    list.first.let { week ->
        repeat(7 - week.size) {
            week.addFirst(null)
        }
    }
    list.last.let { week ->
        repeat(7 - week.size) {
            week.addLast(null)
        }
    }
    return list
}


data class ItemPositions(
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
