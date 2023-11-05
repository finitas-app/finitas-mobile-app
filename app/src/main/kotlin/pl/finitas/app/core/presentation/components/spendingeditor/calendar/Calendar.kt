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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.window.Dialog
import pl.finitas.app.core.presentation.components.utils.colors.Colors
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.util.LinkedList

@Composable
fun Calendar(date: LocalDate, setDate: (LocalDate) -> Unit, modifier: Modifier = Modifier) {
    var dateForCalendarShow by remember {
        mutableStateOf(date)
    }
    Column(
        modifier = modifier
            .background(Colors.backgroundDark, RoundedCornerShape(8.dp))
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row (horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
            IconButton(onClick = { dateForCalendarShow = dateForCalendarShow.minusMonths(1) }) {
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowLeft,
                    contentDescription = "Left",
                    tint = calendarTextColor,
                )
            }
            YearMonthEditor(
                date = dateForCalendarShow,
                onDateChange = { dateForCalendarShow = it },
            )
            IconButton(onClick = { dateForCalendarShow = dateForCalendarShow.plusMonths(1) }) {
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowRight,
                    contentDescription = "Left",
                    tint = calendarTextColor,
                )
            }
        }
        DaySelectBoard(
            year = dateForCalendarShow.year,
            month = dateForCalendarShow.month,
            selectedDay = date,
            onDaySelect = setDate,
        )
    }
}

@Composable
fun YearMonthEditor(
    date: LocalDate,
    onDateChange: (LocalDate) -> Unit,
) {
    var isDialogOpen by remember { mutableStateOf(false) }

    val openDialog = { isDialogOpen = true }
    val closeDialog = { isDialogOpen = false }


    Box(Modifier) {
        Fonts.heading2.Text(
            text = "${date.month.toString().take(3).firstUpper()} ${date.year}",
            color = calendarTextColor,
            modifier = Modifier
                .align(Alignment.Center)
                .clickable { openDialog() },
        )
    }

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
                    .background(Colors.backgroundLight, shape = RoundedCornerShape(10.dp))
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

@Composable
fun DaySelectBoard(year: Int, month: Month, selectedDay: LocalDate, onDaySelect: (LocalDate) -> Unit) {
    val firstDay = LocalDate.of(year, month, 1)
    val weeks = prepareCalendarData(firstDay)

    fun colorOfDayText(dayOfWeek: Int) = if (dayOfWeek >= 5) Color(0xFFFF3B30) else calendarTextColor

    Row {
        DayOfWeek.values().forEachIndexed { index, day ->
            Box(modifier = Modifier
                .padding(vertical = 1.dp)
                .size(35.dp)) {
                Fonts.regular.Text(
                    text = "$day".take(2).firstUpper(),
                    color = colorOfDayText(index),
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }
    }
    Box(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .width(250.dp)
            .height(1.dp)
            .background(Color(0xFF757575))
    )

    Row {
        for (dayOfWeek in 0..6) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                for (day in weeks.map { it[dayOfWeek] }) {
                    val color = if (selectedDay == day) Color(0xFF4355FA) else Color.Transparent
                    Box(
                        modifier = Modifier
                            .padding(vertical = 1.dp)
                            .size(35.dp)
                            .clip(CircleShape)
                            .background(color)
                            .clickable { if (day != null) onDaySelect(day) }
                    ) {
                        if (day != null) {
                            Fonts.regular.Text(
                                text = "${day.dayOfMonth}",
                                color = colorOfDayText(dayOfWeek),
                                modifier = Modifier
                                    .align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
}

fun prepareCalendarData(firstDay: LocalDate): List<List<LocalDate?>> {
    val days = (1..firstDay.lengthOfMonth()).map {
        LocalDate.of(firstDay.year, firstDay.month, it)
    }

    val weeks = LinkedList<LinkedList<LocalDate?>>()
    weeks += LinkedList<LocalDate?>()

    days.forEach { day ->
        weeks.last += day
        if (day.dayOfWeek == DayOfWeek.SUNDAY) weeks += LinkedList<LocalDate?>()
    }
    if (weeks.last.isEmpty()) weeks.removeLast()

    weeks.first.let { week ->
        repeat(7 - week.size) {
            week.addFirst(null)
        }
    }
    weeks.last.let { week ->
        repeat(7 - week.size) {
            week.addLast(null)
        }
    }
    return weeks
}

private val calendarTextColor = Color(0xFFE0E0E0)

private fun String.firstUpper() = first().uppercase() + drop(1).lowercase()

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
