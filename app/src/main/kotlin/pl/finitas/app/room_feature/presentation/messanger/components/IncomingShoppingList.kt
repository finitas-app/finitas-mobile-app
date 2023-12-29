package pl.finitas.app.room_feature.presentation.messanger.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Assignment
import androidx.compose.material.icons.rounded.AssignmentLate
import androidx.compose.material.icons.rounded.AssignmentTurnedIn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.data.model.ShoppingList
import pl.finitas.app.core.presentation.components.utils.colors.Colors
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.room_feature.domain.IncomingShoppingListMessage
import java.time.format.DateTimeFormatter

private val patternOfMessageTime = DateTimeFormatter.ofPattern("HH:mm")

@Composable
fun IncomingShoppingList(
    incomingShoppingListMessage: IncomingShoppingListMessage,
    shoppingList: ShoppingList?,
    modifier: Modifier = Modifier,
) {
    val (
        time,
        idMessage,
        isRead,
        sender,
        idShoppingList,
    ) = incomingShoppingListMessage
    Box(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = modifier
                .padding(end = 30.dp)
                .background(
                    color = Colors.incomingMessageBackground,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomEnd = 16.dp,
                        bottomStart = 6.dp,
                    ),
                )
                .padding(start = 13.dp, top = 13.dp, end = 9.dp, bottom = 9.dp)
                .align(Alignment.CenterStart)
        ) {
            Column {
                sender?.let {
                    Fonts.chatMessagesHeader.Text(
                        text = sender,
                        color = Color(0xFFC7F83C),
                    )
                    Spacer(modifier = Modifier.height(7.dp))
                }
                Row(Modifier.height(IntrinsicSize.Min)) {
                    Icon(
                        imageVector = if (shoppingList == null) {
                            Icons.Rounded.AssignmentLate // TODO: Loading instead of Icon
                        } else if (shoppingList.isFinished) {
                            Icons.Rounded.AssignmentTurnedIn
                        } else {
                            Icons.Rounded.Assignment
                        },
                        contentDescription = "Assignment",
                        modifier = Modifier
                            .aspectRatio(1f)
                            .fillMaxHeight()
                    )
                    Column {
                        Spacer(modifier = Modifier.height(14.dp))
                        Fonts.chatListHeader.Text(
                            text = shoppingList?.name
                                ?: "Loading...", // TODO: add loading but not elvis
                            color = Color.White,
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Fonts.chatMessageTime.Text(
                            text = time.format(patternOfMessageTime),
                            modifier = Modifier.align(Alignment.End),
                            color = Colors.backgroundLight,
                        )
                    }
                }
            }
        }
    }
}