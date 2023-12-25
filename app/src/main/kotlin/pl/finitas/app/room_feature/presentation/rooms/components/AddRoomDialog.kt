package pl.finitas.app.room_feature.presentation.rooms.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.finitas.app.core.presentation.components.constructors.ConstructorInput
import pl.finitas.app.core.presentation.components.dialog.ConstructorBoxDialog
import pl.finitas.app.core.presentation.components.utils.text.Fonts
import pl.finitas.app.room_feature.presentation.rooms.AddRoomState

@Composable
fun AddRoomDialog(
    isDialogOpen: Boolean,
    onClose: () -> Unit,
    onSaveRoom: (AddRoomState) -> Unit,
) {
    var title by remember { mutableStateOf("") }

    ConstructorBoxDialog(
        isOpen = isDialogOpen,
        onSave = { onSaveRoom(AddRoomState(title)) },
        onClose = { onClose() }
    ) {
        Row(
            modifier = Modifier
        ) {
            Fonts.heading1.Text(text = "Add room")
        }
        Fonts.regular.Text(
            text = "Title", modifier = Modifier.padding(top = 20.dp)
        )
        ConstructorInput(
            value = title,
            onValueChange = { title = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
        )
    }
}
