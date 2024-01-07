package pl.finitas.app.room_feature.presentation.room_settings.roles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.data.model.Authority
import pl.finitas.app.core.presentation.components.constructors.ConstructorInput
import pl.finitas.app.core.presentation.components.constructors.InputError
import pl.finitas.app.core.presentation.components.constructors.SwitchComponent
import pl.finitas.app.core.presentation.components.dialog.ConstructorBoxDialog
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.room_feature.presentation.room_settings.RoomSettingsViewModel

@Composable
fun UpsertRoleDialog(
    viewModel: RoomSettingsViewModel,
) {
    ConstructorBoxDialog(
        isOpen = viewModel.isRoomRoleDialogOpen,
        onSave = { viewModel.upsertRole() },
        onClose = viewModel::closeRoleDialog
    ) {
        Fonts.heading2.Text(text = "Role name", modifier = Modifier.padding(bottom = 10.dp))
        ConstructorInput(
            value = viewModel.upsertRoleState.name,
            onValueChange = viewModel::setRoleName,
            modifier = Modifier.fillMaxWidth()
        )
        InputError(errors = viewModel.errors["roleTitle"], Modifier.padding(top = 10.dp))
        Fonts.heading2.Text(text = "Permissions", modifier = Modifier.padding(vertical = 10.dp))
        Authority.entries.forEach { authority ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Fonts.regular.Text(text = authority.name.replace('_', ' '), modifier = Modifier.padding(start = 8.dp, bottom = 10.dp, top = 10.dp))
                SwitchComponent(
                    isActive = authority in viewModel.upsertRoleState.authorities,
                    onChangeState = {
                        if (it) {
                            viewModel.addAuthority(authority)
                        } else {
                            viewModel.removeAuthority(authority)
                        }
                    },
                    height = 20.dp
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.White.copy(.1f))
            )
        }
        InputError(errors = viewModel.errors["roleSummary"], Modifier.padding(top = 10.dp))
    }
}
