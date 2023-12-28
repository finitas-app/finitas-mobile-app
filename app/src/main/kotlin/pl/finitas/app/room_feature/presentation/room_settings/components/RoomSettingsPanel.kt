package pl.finitas.app.room_feature.presentation.room_settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pl.finitas.app.R
import pl.finitas.app.core.presentation.components.ClickableIcon
import pl.finitas.app.core.presentation.components.utils.colors.Colors
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.room_feature.domain.RoomMemberView
import pl.finitas.app.room_feature.domain.RoomRoleView
import pl.finitas.app.room_feature.domain.RoomWithAdditionalInfoView
import pl.finitas.app.room_feature.presentation.room_settings.RoomSettingsViewModel
import pl.finitas.app.room_feature.presentation.room_settings.roles.DeleteRoleDialog
import pl.finitas.app.room_feature.presentation.room_settings.roles.UpsertRoleDialog
import pl.finitas.app.room_feature.presentation.room_settings.user_settings.UserSettingsDialog

@Composable
fun RoomSettingsPanel(
    navController: NavController,
    viewModel: RoomSettingsViewModel,
) {
    var roleToDelete by remember { mutableStateOf<RoomRoleView?>(null) }

    val room by viewModel.room.collectAsState(RoomWithAdditionalInfoView.empty)
    Column {
        RoomHeader(
            title = room.title,
            onTitleChangeClick = { /*TODO*/ },
            onBackClick = { navController.popBackStack() }
        )
        RoomLink(
            invitationLink = "finitas.pl/${room.invitationLinkUUID.toString().take(13)}...",
            onCopyClick = {},
            onShareClick = {},
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 20.dp)
        )
        RoomRoles(
            roles = room.roomRoles,
            onAddRole = { viewModel.openRoleDialog() },
            onElementClick = { viewModel.openRoleDialog(it.toState()) },
            onDeleteRole = { roleToDelete = it },
            modifier = Modifier
                .padding(top = 24.dp, start = 20.dp, end = 20.dp),
        )
        RoomMembers(
            roomMembers = room.roomMembers,
            onUserClick = { viewModel.selectUser(it.idUser) },
            modifier = Modifier
                .padding(top = 24.dp, start = 20.dp, end = 20.dp)
        )
    }

    UpsertRoleDialog(viewModel)
    DeleteRoleDialog(
        roleTitle = roleToDelete?.name ?: "",
        isOpen = roleToDelete != null,
        onConfirm = {
            viewModel.deleteRole(
                idRole = roleToDelete?.idRole!!,
                onSuccess = { roleToDelete = null },
            )
        },
        onClose = {
            roleToDelete = null
        }
    )
    UserSettingsDialog(
        roomMember = room.roomMembers.find { it.idUser == viewModel.selectedUser },
        roles = room.roomRoles,
        viewModel = viewModel,
    )
}

@Composable
private fun RoomHeader(
    title: String,
    onTitleChangeClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ClickableIcon(
            imageVector = Icons.Rounded.ArrowBackIos,
            onClick = onBackClick,
        )
        Fonts.heading1.Text(text = title)
        ClickableIcon(
            painter = painterResource(id = R.drawable.ic_edit_icon),
            onClick = onTitleChangeClick,
            iconSize = 23.dp
        )
    }
}

@Composable
private fun RoomLink(
    invitationLink: String,
    onCopyClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Fonts.heading1.Text(text = "Link")
        Row(
            Modifier.padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(.75f)
                    .background(
                        Colors.messageInputColor,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Fonts.regular.Text(text = invitationLink)
            }
            ClickableIcon(
                imageVector = Icons.Rounded.ContentCopy,
                onClick = onCopyClick,
            )
            ClickableIcon(
                imageVector = Icons.Rounded.Share,
                onClick = onShareClick,
            )
        }
    }
}

@Composable
private fun RoomRoles(
    roles: List<RoomRoleView>,
    onAddRole: () -> Unit,
    onElementClick: (RoomRoleView) -> Unit,
    onDeleteRole: (RoomRoleView) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Fonts.heading1.Text(text = "Roles")
            ClickableIcon(
                imageVector = Icons.Rounded.AddCircleOutline,
                onClick = onAddRole,
            )
        }
        roles.forEach { role ->
            val interactionSource = remember {
                MutableInteractionSource()
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Fonts.regular.Text(
                    text = role.name,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                        ) { onElementClick(role) }
                )
                ClickableIcon(imageVector = Icons.Rounded.Delete, onClick = { onDeleteRole(role) })
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.White.copy(.1f))
            )
        }
    }
}

@Composable
private fun RoomMembers(
    roomMembers: List<RoomMemberView>,
    onUserClick: (RoomMemberView) -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
    ) {
        Fonts.heading1.Text(text = "Users")
        roomMembers.forEach { roomMember ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .clickable { onUserClick(roomMember) }
            ) {
                Fonts.regular.Text(
                    text = roomMember.username,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(top = 6.dp, end = 10.dp)
                ) {
                    val roleName = roomMember.roomRole?.name
                    if (roleName != null)
                        Fonts.graphMini.Text(text = roleName, color = Colors.backgroundLight)
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.White.copy(.1f))
            )
        }
    }
}
