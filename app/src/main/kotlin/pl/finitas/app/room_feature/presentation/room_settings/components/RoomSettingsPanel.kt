package pl.finitas.app.room_feature.presentation.room_settings.components

import android.content.Intent
import android.widget.Toast
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
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import pl.finitas.app.R
import pl.finitas.app.core.data.model.Authority
import pl.finitas.app.core.presentation.components.ClickableIcon
import pl.finitas.app.core.presentation.components.constructors.InputError
import pl.finitas.app.core.presentation.components.utils.colors.Colors
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.core.presentation.components.utils.trimOnOverflow
import pl.finitas.app.navigation.NavPaths
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

    val room by viewModel.room.collectAsState(RoomWithAdditionalInfoView.empty)
    if (room == null) {
        navController.navigate(NavPaths.RoomsScreen.route)
        return
    }

    var roleToDelete by remember { mutableStateOf<RoomRoleView?>(null) }
    val hasModifyRoomAuthority by viewModel.hasModifyRoomAuthority.collectAsState(initial = false)
    val hasReadUserDataAuthority by viewModel.hasReadUsersDataAuthority.collectAsState(initial = false)
    val context = LocalContext.current

    LaunchedEffect(hasModifyRoomAuthority, hasReadUserDataAuthority) {
        if (!hasModifyRoomAuthority && !hasReadUserDataAuthority) {
            roleToDelete = null
        }
    }

    LaunchedEffect(roleToDelete, room) {
        if (
            roleToDelete != null &&
            (room!!.roomRoles - roleToDelete!!).flatMap { it.authorities }.none { it == Authority.MODIFY_ROOM }
        ) {
            roleToDelete = null
        }
    }

    val invitationLink = remember(room) { "finitas.pl/${room!!.invitationLinkUUID}" }
    Column {
        RoomHeader(
            title = room!!.title,
            onTitleChangeClick = { viewModel.openChangeRoomNameDialog(room!!.title) },
            hasModifyRoomAuthority = hasModifyRoomAuthority,
            onBackClick = { navController.popBackStack() }
        )
        RoomLink(
            invitationLink = invitationLink,
            errors = viewModel.errors["regenerateLink"],
            onRefreshClick = viewModel::regenerateLink,
            hasModifyRoomAuthority = hasModifyRoomAuthority,
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 20.dp)
        )
        RoomRoles(
            roles = room!!.roomRoles,
            onAddRole = { viewModel.openRoleDialog() },
            onElementClick = {
                if (hasModifyRoomAuthority) {
                    viewModel.openRoleDialog(it.toState())
                }
            },
            onDeleteRole = { role ->
                if (
                    (room!!.roomRoles - role)
                        .flatMap { it.authorities }
                        .none { it == Authority.MODIFY_ROOM }
                ) {
                    roleToDelete = null
                    Toast.makeText(
                        context,
                        "You cannot delete the last role with the MODIFY ROOM right.",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    roleToDelete = role
                }
            },
            hasModifyRoomAuthority = hasModifyRoomAuthority,
            modifier = Modifier
                .padding(top = 24.dp, start = 20.dp, end = 20.dp),
        )
        RoomMembers(
            roomMembers = room!!.roomMembers,
            onUserClick = { viewModel.selectUser(it.idUser) },
            hasModifyRoomAuthority = hasModifyRoomAuthority,
            hasReadUserDataAuthority = hasReadUserDataAuthority,
            modifier = Modifier
                .padding(top = 24.dp, start = 20.dp, end = 20.dp)
        )
    }

    UpsertRoleDialog(viewModel)
    DeleteRoleDialog(
        roleTitle = roleToDelete?.name ?: "",
        isOpen = roleToDelete != null,
        errors = viewModel.errors["deleteRole"],
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
        roomMember = room!!.roomMembers.find { it.idUser == viewModel.selectedUser },
        roles = room!!.roomRoles,
        navController = navController,
        hasModifyRoomAuthority = hasModifyRoomAuthority,
        hasReadUserDataAuthority = hasReadUserDataAuthority,
        viewModel = viewModel,
    )
    ChangeRoomNameDialog(
        isDialogOpen = viewModel.isChangeRoomNameDialogOpen,
        errors = viewModel.errors["roomName"],
        newNameValue = viewModel.newRoomName,
        onNameChange = viewModel::setNewRoomNameValue,
        onClose = viewModel::closeChangeRoomNameDialog,
        onSave = viewModel::changeRoomName
    )
}

@Composable
private fun RoomHeader(
    title: String,
    onTitleChangeClick: () -> Unit,
    hasModifyRoomAuthority: Boolean,
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
        if (hasModifyRoomAuthority) {
            ClickableIcon(
                painter = painterResource(id = R.drawable.ic_edit_icon),
                onClick = onTitleChangeClick,
                iconSize = 23.dp
            )
        }
    }
}

@Composable
private fun RoomLink(
    invitationLink: String,
    errors: List<String>?,
    onRefreshClick: () -> Unit,
    hasModifyRoomAuthority: Boolean,
    modifier: Modifier = Modifier,
) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, invitationLink)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "bob")
    val context = LocalContext.current
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
                Fonts.regular.Text(text = invitationLink.trimOnOverflow(19))
            }
            if (hasModifyRoomAuthority) {
                ClickableIcon(
                    imageVector = Icons.Rounded.Refresh,
                    onClick = onRefreshClick,
                )
            }
            ClickableIcon(
                imageVector = Icons.Rounded.Share,
                onClick = { ContextCompat.startActivity(context, shareIntent, null) },
            )
        }
        InputError(errors = errors, Modifier.padding(top = 10.dp))
    }
}

@Composable
private fun RoomRoles(
    roles: List<RoomRoleView>,
    onAddRole: () -> Unit,
    onElementClick: (RoomRoleView) -> Unit,
    onDeleteRole: (RoomRoleView) -> Unit,
    hasModifyRoomAuthority: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Fonts.heading1.Text(text = "Roles")
            if (hasModifyRoomAuthority) {
                ClickableIcon(
                    imageVector = Icons.Rounded.AddCircleOutline,
                    onClick = onAddRole,
                )
            }
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
                        .padding(horizontal = 8.dp, vertical = 12.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                        ) { onElementClick(role) }
                )
                if (hasModifyRoomAuthority) {
                    ClickableIcon(
                        imageVector = Icons.Rounded.Delete,
                        onClick = { onDeleteRole(role) })
                } else {
                    Box(Modifier)
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

@Composable
private fun RoomMembers(
    roomMembers: List<RoomMemberView>,
    onUserClick: (RoomMemberView) -> Unit,
    hasModifyRoomAuthority: Boolean,
    hasReadUserDataAuthority: Boolean,
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
                    .clickable {
                        if (hasModifyRoomAuthority || hasReadUserDataAuthority)
                            onUserClick(roomMember)
                    }
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
