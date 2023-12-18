package pl.finitas.app.room_feature.presentation.messanger

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel
import pl.finitas.app.core.presentation.components.ClickableIcon
import pl.finitas.app.core.presentation.components.background.SecondaryBackground
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.navigation.NavPaths
import pl.finitas.app.room_feature.domain.IncomingTextMessage
import pl.finitas.app.room_feature.domain.OutgoingTextMessage
import pl.finitas.app.room_feature.presentation.messanger.components.IncomingMessage
import pl.finitas.app.room_feature.presentation.messanger.components.MessengerInput
import pl.finitas.app.room_feature.presentation.messanger.components.OutgoingMessage

@Composable
fun MessengerScreen(navController: NavHostController) {
    val viewModel: MessengerViewModel = koinViewModel()
    val messages by viewModel.messages.collectAsState(initial = listOf())

    SecondaryBackground {
        Column {
            MessengerHeader(
                roomTitle = viewModel.roomTitle,
                onBackClick = { navController.navigate(NavPaths.RoomsScreen.route) },
            )
            LazyColumn(
                reverseLayout = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, bottom = 65.dp)
                ,
            ) {
                messages.forEach { message ->
                    item {
                        when (message) {
                            is IncomingTextMessage -> IncomingMessage(message)
                            is OutgoingTextMessage -> OutgoingMessage(
                                outgoingTextMessage = message,
                            )
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
            }
        }
        MessengerInput(
            onSendMessage = viewModel::sendTextMessage,
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 10.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun MessengerHeader(
    roomTitle: String,
    onBackClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ClickableIcon(
                imageVector = Icons.Rounded.KeyboardArrowLeft,
                onClick = onBackClick,
            )
            Fonts.heading1.Text(text = roomTitle)
        }
    }
}
