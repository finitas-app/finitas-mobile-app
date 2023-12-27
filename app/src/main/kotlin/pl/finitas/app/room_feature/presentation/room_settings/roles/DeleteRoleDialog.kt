package pl.finitas.app.room_feature.presentation.room_settings.roles

import androidx.compose.runtime.Composable
import pl.finitas.app.core.presentation.components.dialog.ConstructorBoxDialog

@Composable
fun DeleteRoleDialog(
    isOpen: Boolean,
    onConfirm: () -> Unit,
    onClose: () -> Unit,
) {
    ConstructorBoxDialog(
        isOpen = isOpen,
        onSave = onConfirm,
        onClose = onClose,
    ) {

    }
}
