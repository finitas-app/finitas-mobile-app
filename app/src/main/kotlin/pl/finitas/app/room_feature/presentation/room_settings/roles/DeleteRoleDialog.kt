package pl.finitas.app.room_feature.presentation.room_settings.roles

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.dialog.ConstructorBoxDialog
import pl.finitas.app.core.presentation.components.utils.text.Fonts

@Composable
fun DeleteRoleDialog(
    roleTitle: String,
    isOpen: Boolean,
    onConfirm: () -> Unit,
    onClose: () -> Unit,
) {
    ConstructorBoxDialog(
        isOpen = isOpen,
        onSave = onConfirm,
        onClose = onClose,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Fonts.heading2.Text(text = roleTitle, modifier = Modifier.padding(bottom = 24.dp))
            Fonts.regular.Text(
                text = "You really want to remove this role. All users who own it will lose it.",
                textAlign = TextAlign.Center
            )
        }
    }
}
