package pl.finitas.app.manage_spendings_feature.presentation.utils

import androidx.lifecycle.SavedStateHandle
import pl.finitas.app.room_feature.presentation.messanger.IdRoomNotProvidedException
import java.util.UUID

fun SavedStateHandle.extractIdRoomAndIdsUsers(): Pair<UUID?, List<UUID>> {
    val idsUserRaw = get<String>("idsUser")
    val idsUser = if (idsUserRaw.isNullOrBlank()) listOf()
    else idsUserRaw.split(",").map { UUID.fromString(it)!! }
    val idRoom = get<String>("idRoom")
        ?.let {
            if (it.isNotBlank()) {
                UUID.fromString(it)
            } else {
                null
            }
        }

    if (idRoom == null && idsUser.isNotEmpty()) {
        throw IdRoomNotProvidedException()
    }

    return idRoom to idsUser
}