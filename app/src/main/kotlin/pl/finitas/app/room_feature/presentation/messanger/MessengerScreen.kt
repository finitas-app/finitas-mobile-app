package pl.finitas.app.room_feature.presentation.messanger

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel
import pl.finitas.app.core.data.model.Authority
import pl.finitas.app.core.domain.emptyUUID
import pl.finitas.app.core.presentation.components.ClickableIcon
import pl.finitas.app.core.presentation.components.background.SecondaryBackground
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.navigation.NavPaths
import pl.finitas.app.room_feature.domain.IncomingShoppingListMessage
import pl.finitas.app.room_feature.domain.IncomingTextMessage
import pl.finitas.app.room_feature.domain.OutgoingShoppingListMessage
import pl.finitas.app.room_feature.domain.OutgoingTextMessage
import pl.finitas.app.room_feature.presentation.messanger.components.IncomingShoppingList
import pl.finitas.app.room_feature.presentation.messanger.components.IncomingTextMessageComponent
import pl.finitas.app.room_feature.presentation.messanger.components.MessengerInput
import pl.finitas.app.room_feature.presentation.messanger.components.OutgoingShoppingList
import pl.finitas.app.room_feature.presentation.messanger.components.OutgoingTextMessageComponent
import pl.finitas.app.room_feature.presentation.messanger.components.SendObjectDialog

@Composable
fun MessengerScreen(navController: NavHostController) {
    val viewModel: MessengerViewModel = koinViewModel()
    val idRoom = viewModel.idRoom
    if (idRoom == null) {
        navController.navigate(NavPaths.RoomsScreen.route)
    }
    val currentUserAuthorities by viewModel.currentUserAuthorities.collectAsState(initial = setOf())
    val roomTitle by viewModel.roomTitle.collectAsState(initial = "")
    val messages by viewModel.messages.collectAsState(initial = listOf())
    val idUser by viewModel.authorizedUserId.collectAsState(emptyUUID)
    val shoppingLists by viewModel.shoppingLists.collectAsState(mapOf())
    val shoppingListsPreview by viewModel.shoppingListsPreview.collectAsState(listOf())

    if (idUser == null) {
        navController.navigate(NavPaths.AuthScreen.route)
        return
    }
    SecondaryBackground {
        Column {
            MessengerHeader(
                roomTitle = roomTitle,
                onBackClick = { navController.navigate(NavPaths.RoomsScreen.route) },
                onSettingsClick = { navController.navigate(NavPaths.RoomsSettingsScreen.route + "?idRoom=${viewModel.idRoom}") },
                onShowRoomSpendingsClick = { viewModel.onShowRoomSpendingsClick(navController) },
                hasReadDataAuthority = Authority.READ_USERS_DATA in currentUserAuthorities,
            )
            LazyColumn(
                reverseLayout = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, bottom = 65.dp),
            ) {
                messages.forEach { message ->
                    item {
                        when (message) {
                            is IncomingTextMessage -> IncomingTextMessageComponent(message)
                            is OutgoingTextMessage -> OutgoingTextMessageComponent(message)
                            is IncomingShoppingListMessage -> IncomingShoppingList(
                                incomingShoppingListMessage = message,
                                shoppingList = shoppingLists[message.idShoppingList],
                            )
                            is OutgoingShoppingListMessage -> OutgoingShoppingList(
                                outgoingShoppingListMessage = message,
                                shoppingList = shoppingLists[message.idShoppingList],
                            )
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
            }
        }
        MessengerInput(
            onSendMessage = viewModel::sendTextMessage,
            onPinObject = viewModel::openSendObjectDialog,
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 10.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )
        SendObjectDialog(
            shoppingListsPreview = shoppingListsPreview,
            isOpen = viewModel.isSendObjectDialogOpen,
            onClose = viewModel::closeSendObjectDialog,
            onSave = viewModel::sendShoppingList,
        )
    }
}

@Composable
private fun MessengerHeader(
    roomTitle: String,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onShowRoomSpendingsClick: () -> Unit,
    hasReadDataAuthority: Boolean,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ClickableIcon(
                imageVector = Icons.Rounded.ArrowBackIos,
                onClick = onBackClick,
            )
            Fonts.heading1.Text(text = roomTitle)
        }
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (hasReadDataAuthority) {
                ClickableIcon(
                    imageVector = Icons.Rounded.BarChart,
                    onClick = onShowRoomSpendingsClick,
                )
            }
            ClickableIcon(
                imageVector = Icons.Rounded.Settings,
                onClick = onSettingsClick,
            )
        }
    }
}
