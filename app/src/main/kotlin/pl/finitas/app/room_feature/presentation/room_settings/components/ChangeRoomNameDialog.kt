package pl.finitas.app.room_feature.presentation.room_settings.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.constructors.ConstructorInput
import pl.finitas.app.core.presentation.components.constructors.InputError
import pl.finitas.app.core.presentation.components.dialog.ConstructorBoxDialog
import pl.finitas.app.core.presentation.components.utils.text.Fonts

@Composable
fun ChangeRoomNameDialog(
    isDialogOpen: Boolean,
    errors: List<String>?,
    newNameValue: String,
    onNameChange: (String) -> Unit,
    onClose: () -> Unit,
    onSave: () -> Unit,
) {
    ConstructorBoxDialog(
        isOpen = isDialogOpen,
        onSave = onSave,
        onClose = onClose,
    ) {
        Fonts.heading1.Text(
            text = "New room name",
            modifier = Modifier
                .padding(bottom = 10.dp)
        )
        ConstructorInput(
            value = newNameValue,
            onValueChange = onNameChange,
            modifier = Modifier
                .fillMaxWidth()
        )
        InputError(errors = errors, Modifier.padding(top = 10.dp))
    }
}
