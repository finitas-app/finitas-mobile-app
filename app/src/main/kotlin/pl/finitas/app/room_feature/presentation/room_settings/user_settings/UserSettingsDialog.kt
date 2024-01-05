package pl.finitas.app.room_feature.presentation.room_settings.user_settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.RemoveRedEye
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
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
import pl.finitas.app.core.presentation.components.constructors.DeleteIcon
import pl.finitas.app.core.presentation.components.dialog.ConstructorBoxDialog
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.navigation.NavPaths
import pl.finitas.app.room_feature.domain.RoomMemberView
import pl.finitas.app.room_feature.domain.RoomRoleView
import pl.finitas.app.room_feature.presentation.room_settings.RoomSettingsViewModel

@Composable
fun UserSettingsDialog(
    roomMember: RoomMemberView?,
    roles: List<RoomRoleView>,
    viewModel: RoomSettingsViewModel,
    navController: NavController,
    hasModifyRoomAuthority: Boolean,
    hasReadUserDataAuthority: Boolean,
) {
    var selectedRole by remember(roomMember) { mutableStateOf(roomMember?.roomRole?.idRole) }
    var isRoleSelectorOpen by remember { mutableStateOf(false) }
    ConstructorBoxDialog(
        isOpen = viewModel.selectedUser != null && !isRoleSelectorOpen,
        onSave = {},
        onClose = viewModel::closeUserDialog,
        isActiveNavBar = false,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 10.dp)
        ) {
            Fonts.heading1.Text(text = roomMember?.username ?: "")
            if (hasReadUserDataAuthority && roomMember != null) {
                ClickableIcon(
                    imageVector = Icons.Rounded.RemoveRedEye,
                    onClick = {
                        navController.navigate(NavPaths.HomeScreen.route + "?idsUser=${roomMember.idUser}&idRoom=${viewModel.idRoom}")
                        viewModel.closeUserDialog()
                    }
                )
            }
        }
        Fonts.heading2.Text(text = "Role")
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(13.dp)
        ) {
            Fonts.regular.Text(text = roomMember?.roomRole?.name ?: "none")
            if (hasModifyRoomAuthority) {
                ClickableIcon(
                    painter = painterResource(id = R.drawable.ic_edit_icon),
                    onClick = { isRoleSelectorOpen = true },
                    iconSize = 20.dp
                )
            }
        }
        if (hasModifyRoomAuthority) {
            DeleteIcon(
                label = "Remove from the room",
                onDeleteClick = { viewModel.removeSelectedUserFromRoom() },
                modifier = Modifier.padding(bottom = 13.dp)
            )
        }
    }
    ConstructorBoxDialog(
        isOpen = viewModel.selectedUser != null && isRoleSelectorOpen,
        onSave = {
            viewModel.assignRoleToSelectedUser(
                idRole = selectedRole,
                onSuccess = {
                    isRoleSelectorOpen = false
                    selectedRole = null
                    viewModel.closeUserDialog()
                }
            )
        },
        onClose = {
            isRoleSelectorOpen = false
            selectedRole = roomMember?.roomRole?.idRole
        },
        onDismiss = {
            isRoleSelectorOpen = false
            selectedRole = roomMember?.roomRole?.idRole
            viewModel.closeUserDialog()
        }
    ) {
        Fonts.heading1.Text(
            text = "Select role",
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        roles.forEach { role ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selectedRole = if (selectedRole != role.idRole)
                            role.idRole
                        else
                            null
                    }
            ) {
                Fonts.regular.Text(
                    text = role.name,
                    modifier = Modifier.padding(start = 8.dp, bottom = 10.dp, top = 10.dp)
                )
                if (role.idRole == selectedRole) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "Check",
                        tint = Color.White
                    )
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
