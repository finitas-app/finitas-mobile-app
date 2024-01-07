package pl.finitas.app.room_feature.presentation.rooms.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SwitchLeft
import androidx.compose.material.icons.rounded.SwitchRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.ClickableIcon
import pl.finitas.app.core.presentation.components.constructors.ConstructorInput
import pl.finitas.app.core.presentation.components.constructors.InputError
import pl.finitas.app.core.presentation.components.dialog.ConstructorBoxDialog
import pl.finitas.app.core.presentation.components.utils.colors.Colors
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.room_feature.presentation.rooms.AddRoomOption
import pl.finitas.app.room_feature.presentation.rooms.AddRoomState

@Composable
fun AddRoomDialog(
    isDialogOpen: Boolean,
    errors: List<String>?,
    addRoomState: AddRoomState,
    onTitleChange: (String) -> Unit,
    onInvitationLinkChange: (String) -> Unit,
    onAddRoomOptionChange: (AddRoomOption) -> Unit,
    onClose: () -> Unit,
    onSaveRoom: () -> Unit,
) {

    ConstructorBoxDialog(
        isOpen = isDialogOpen,
        onSave = {
            onSaveRoom()
        },
        onClose = { onClose() }
    ) {
        when (addRoomState.addRoomOption) {
            AddRoomOption.Join -> {
                JoinRoom(
                    invitationLink = addRoomState.invitationLink,
                    onEdit = onInvitationLinkChange,
                    onSwitchMethod = {
                        onAddRoomOptionChange(AddRoomOption.Create)
                    }
                )
            }

            AddRoomOption.Create -> {
                CreateRoom(
                    title = addRoomState.title,
                    onEdit = onTitleChange,
                    onSwitchMethod = {
                        onAddRoomOptionChange(AddRoomOption.Join)
                    }
                )
            }
        }
        InputError(errors = errors, Modifier.padding(top = 10.dp))
    }
}

@Composable
private fun JoinRoom(
    invitationLink: String,
    onEdit: (String) -> Unit,
    onSwitchMethod: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Fonts.heading1.Text(text = "Join room")
        ClickableIcon(
            imageVector = Icons.Rounded.SwitchLeft,
            onClick = onSwitchMethod,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
    Fonts.regular.Text(
        text = "Link", modifier = Modifier.padding(top = 20.dp)
    )
    ConstructorInput(
        value = invitationLink,
        onValueChange = onEdit,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
    )
}

@Composable
private fun CreateRoom(
    title: String,
    onEdit: (String) -> Unit,
    onSwitchMethod: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Fonts.heading1.Text(text = "Create room")
        ClickableIcon(
            imageVector = Icons.Rounded.SwitchRight,
            onClick = onSwitchMethod,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
    Fonts.regular.Text(
        text = "Title", modifier = Modifier.padding(top = 20.dp)
    )
    ConstructorInput(
        value = title,
        onValueChange = onEdit,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
    )
}

@Composable
private fun SwitchAddRoomButton(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(20)
    Box(
        modifier = modifier
            .background(Colors.activationColor, shape)
            .clip(shape)
            .clickable(onClick = onClick)
    ) {
        Fonts.heading2.Text(
            text = title,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 10.dp, vertical = 2.dp)
        )
    }
}
