package pl.finitas.app.room_feature.presentation.room_settings.roles

import pl.finitas.app.core.data.model.Authority
import java.util.UUID

data class UpsertRoleState(
    val name: String,
    val authorities: Set<Authority>,
    val idRole: UUID? = null,
) {
    companion object {
        val empty get() = UpsertRoleState("", setOf())
    }
}